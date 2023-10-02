package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;

import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.exception.TripPlanNotFoundException;
import com.kodilla.cheapflightsearch.mapper.SkyscannerMapper;
import com.kodilla.cheapflightsearch.mapper.TripPlanMapper;
import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
import com.kodilla.cheapflightsearch.repository.RouteRepository;
import com.kodilla.cheapflightsearch.repository.TripPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    private final SkyscannerService skyscannerService;
    private final SkyscannerMapper skyscannerMapper;
    private final TripPlanRepository tripPlanRepository;
    private final RouteRepository routeRepository;
    private final CalendarService calendarService;
    private final TripPlanMapper tripPlanMapper;
    private final AirportService airportService;

    public List<Itinerary> getItineraries() {
        return itineraryRepository.findAll();
    }

    public Itinerary getItinerary(Long id) throws ItineraryNotFoundException {
        return itineraryRepository.findById(id).orElseThrow(ItineraryNotFoundException::new);
    }

    public List<Itinerary> getItinerariesByUser(User user) {
        return itineraryRepository.findAll().stream()
                .filter(i -> i.getTripPlan().getUser().equals(user))
                .collect(Collectors.toList());
    }

    public Itinerary getItineraryByItineraryMark(String itineraryMark) throws ItineraryNotFoundException {
        return itineraryRepository.findAll().stream()
                .filter(i -> i.getItineraryMark().equals(itineraryMark))
                .findFirst()
                .orElseThrow(ItineraryNotFoundException::new);
    }

    public void deleteItinerary(Long id) throws ItineraryNotFoundException {
        if (itineraryRepository.existsById(id)) {
            Itinerary itineraryToDelete = itineraryRepository.findById(id).get();
            if (itineraryToDelete.getTripPlan() != null) {
                TripPlan tripPlanToUpdate = tripPlanRepository.findById(itineraryToDelete.getTripPlan().getTripPlanId()).get();
                tripPlanToUpdate.setItinerary(null);
                tripPlanRepository.save(tripPlanToUpdate);
            }
            itineraryRepository.deleteById(itineraryToDelete.getItineraryId());
        } else {
            throw new ItineraryNotFoundException();
        }
    }

    public Itinerary updateItinerary(Long id, Itinerary itinerary) throws ItineraryNotFoundException {
        if (itineraryRepository.findById(id).isPresent()) {
            return itineraryRepository.save(itinerary);
        } else {
            throw new ItineraryNotFoundException();
        }
    }

    public Itinerary createItinerary(Itinerary itinerary) {
        if (itinerary.getTripPlan() != null) {
            TripPlan tripPlanToUpdate = tripPlanRepository.findById(itinerary.getItineraryId()).get();
            tripPlanToUpdate.setItinerary(itinerary);
            tripPlanRepository.save(tripPlanToUpdate);
        }
        return itineraryRepository.save(itinerary);
    }

    public boolean checkIfItineraryExists(Itinerary itineraryToCheck) {
        return itineraryRepository.findAll().stream()
                .anyMatch(i -> i.getTripPlan().equals(itineraryToCheck.getTripPlan()));
    }

    public List<Itinerary> getPurchasedItineraries() {
        return itineraryRepository.findAll().stream()
                .filter(Itinerary::isPurchased)
                .collect(Collectors.toList());
    }

    public void createItineraryBasedOnTripPlan(TripPlan tripPlan) throws Exception {
        Itinerary searchingResult = skyscannerService.searchCreateGetItinerary(
                skyscannerMapper.mapTripPlanToFlightSearchDto(tripPlan)
        );
        searchingResult.setTripPlan(tripPlan);
        if (tripPlan.getItinerary() != null) {
            searchingResult.setItineraryId(tripPlan.getItinerary().getItineraryId());
        }
        Itinerary savedItinerary = itineraryRepository.save(searchingResult);
        if (tripPlan.getItinerary() == null) {
            tripPlan.setItinerary(savedItinerary);
            tripPlanRepository.save(tripPlan);
        }
    }

    public void switchItineraryPurchased(Itinerary itinerary) {
        itinerary.setPurchased(!itinerary.isPurchased());
        itineraryRepository.save(itinerary);
    }

    //TODO better to use method 'createTripPlansFromFavouriteRoutesAndHolidayPlans' and remove this
    public List<Itinerary> searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(List<HolidayPlan> holidayPlanList,
                                                                                      List<Route> routes,
                                                                                      int adults
    ) throws Exception {
        List<Itinerary> resultList = new ArrayList<>();
        for (Route route : routes) {
            for (HolidayPlan holidayPlan : holidayPlanList) {
                if (((route.getDaysOfWeek().contains(holidayPlan.getBeginDate().getDayOfWeek()))
                        && (route.getDaysOfWeek().contains(holidayPlan.getEndDate().getDayOfWeek())))
                        && (route.isFavourite())) {
                    Optional<Itinerary> searchResult = Optional.ofNullable(
                            itineraryRepository.save(
                                    skyscannerService.searchCreateGetItinerary(
                                            skyscannerMapper.mapTripPlanToFlightSearchDto(
                                                    tripPlanMapper.mapRouteAndHolidayPlanToTripPlan(
                                                            holidayPlan,
                                                            route, adults
                                                    )
                                            )
                                    )
                            )
                    );
                    if (searchResult.isPresent()) {
                        resultList.add(searchResult.get());
                    }
                }
            }
        }
        return resultList;
    }

    public List<Itinerary> searchForItinerariesFavouriteRoutesFewNextWeekends(int howManyWeekends,
                                                                              List<Route> routes,
                                                                              int adults) throws Exception {
        List<Itinerary> resultList = new ArrayList<>();
        List<HolidayPlan> temporaryHolidayPlanList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate localDateNextFriday = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        LocalDate localDateNextSunday = localDateNextFriday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        int j = 0;
        for (int i = 0; i < howManyWeekends; i++) {
            j = i * 7;
            temporaryHolidayPlanList.add(new HolidayPlan(localDateNextFriday.plus(j, ChronoUnit.DAYS), localDateNextSunday.plus(j, ChronoUnit.DAYS)));
        }
        for (Route route : routes) {
            for (HolidayPlan holidayPlan : temporaryHolidayPlanList) {
                if (((route.getDaysOfWeek().contains(holidayPlan.getBeginDate().getDayOfWeek()))
                        && (route.getDaysOfWeek().contains(holidayPlan.getEndDate().getDayOfWeek())))
                        && (route.isFavourite())) {
                    Optional<Itinerary> searchResult = Optional.ofNullable(
                            itineraryRepository.save(
                                    skyscannerService.searchCreateGetItinerary(
                                            skyscannerMapper.mapTripPlanToFlightSearchDto(
                                                    tripPlanMapper.mapRouteAndHolidayPlanToTripPlan(
                                                            holidayPlan,
                                                            route, adults
                                                    )
                                            )
                                    )
                            )
                    );
                    if (searchResult.isPresent()) {
                        resultList.add(searchResult.get());
                    }
                }
            }
        }
        return resultList;
    }

    public List<TripPlan> getTripPlans() {
        return tripPlanRepository.findAll();
    }

    public List<TripPlan> getTripPlansByUserId(Long userId) {
        return tripPlanRepository.findAll().stream()
                .filter(t -> t.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<TripPlan> getTripPlansByUser(User user) {
        return tripPlanRepository.findByUser(user);
    }

    public void deleteTripPlan(Long id) throws TripPlanNotFoundException {
        Optional<TripPlan> tripPlanToDelete = tripPlanRepository.findById(id);
        if (tripPlanToDelete.isPresent()) {
            itineraryRepository.findByTripPlan(tripPlanToDelete.get()).stream()
                    .forEach(i -> {
                        i.setTripPlan(null);
                        itineraryRepository.save(i);
                    });
            tripPlanRepository.deleteById(id);
        } else {
            throw new TripPlanNotFoundException();
        }
    }

    public TripPlan updateTripPlan(Long id, TripPlan tripPlan) throws TripPlanNotFoundException {
        if (tripPlanRepository.findById(id).isPresent()) {
            return tripPlanRepository.save(tripPlan);
        } else {
            throw new TripPlanNotFoundException();
        }
    }

    public TripPlan createTripPlan(TripPlan tripPlan) {
        return tripPlanRepository.save(tripPlan);
    }

    public List<TripPlan> createTripPlansFromFavouriteRoutesAndHolidayPlans(User currentUser, int adults) {
        List<Route> routes = routeRepository.findByUser(currentUser);
        List<HolidayPlan> holidayPlans = calendarService.getHolidayPlansByUser(currentUser); //TODO maybe is better to add relations to user from calendar side to use repository here;
        List<TripPlan> currentUserTripPlans = tripPlanRepository.findByUser(currentUser);
        List<TripPlan> tripPlans = new ArrayList<>();
        for (Route route : routes) {
            for (HolidayPlan holidayPlan : holidayPlans) {
                if (checkMatchingRoutesAndTripPlans(route, holidayPlan)) {
                    TripPlan newTripPlan = TripPlan.builder()
                            .originIata(route.getOrigin().getIataCode())
                            .destinationIata(route.getDestination().getIataCode())
                            .beginDate(holidayPlan.getBeginDate())
                            .endDate(holidayPlan.getEndDate())
                            .adults(adults)
                            .user(currentUser)
                            .build();
                    tripPlans.add(newTripPlan);
                }
            }
        }
        return tripPlans.stream()
                .filter(tp -> !currentUserTripPlans.contains(tp))
                .map(tp -> tripPlanRepository.save(tp))
                .collect(Collectors.toList());
    }

    private boolean checkMatchingRoutesAndTripPlans(Route route, HolidayPlan holidayPlan) {
        return ((route.getDaysOfWeek().contains(holidayPlan.getBeginDate().getDayOfWeek()))
                && (route.getDaysOfWeek().contains(holidayPlan.getEndDate().getDayOfWeek())))
                && (route.isFavourite());
    }

    public String getWeatherForTripPlanDestination(TripPlan tripPlan) {
        try {
            return airportService.getWeatherForAirport(airportService.getAirportByIata(tripPlan.getDestinationIata()));
        } catch (Exception e) {
            return "Not found";
        }
    }

    public String getCityForTripPlanDestination(TripPlan tripPlan) {
        try {
            return airportService.getAirportByIata(tripPlan.getDestinationIata()).getCity();
        } catch (Exception e) {
            return "Not found";
        }
    }
}

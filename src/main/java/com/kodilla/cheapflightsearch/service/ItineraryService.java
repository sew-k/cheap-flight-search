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
    private final TripPlanMapper tripPlanMapper;
    private final AirportService airportService;

    public List<Itinerary> getItineraries() {
        return itineraryRepository.findAll();
    }

    public Itinerary getItinerary(Long id) throws ItineraryNotFoundException {
        return itineraryRepository.findById(id).orElseThrow(ItineraryNotFoundException::new);
    }

    public List<Itinerary> getItinerariesByUser(User user) {
        return tripPlanRepository.findByUser(user).stream()
                .filter(t -> t.getItinerary() != null)
                .map(t -> itineraryRepository.findById(t.getItinerary().getItineraryId()))
                .filter(o -> o.isPresent())
                .map(o -> o.get())
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
            itineraryRepository.deleteById(id);
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
        return itineraryRepository.save(itinerary);
    }

    public List<Itinerary> getPurchasedItineraries() {
        return itineraryRepository.findAll().stream()
                .filter(Itinerary::isPurchased)
                .collect(Collectors.toList());
    }

    public Optional<Itinerary> searchForItineraryBasedOnTripPlan(TripPlan tripPlan) throws ItineraryNotFoundException {
        try {
            Optional<Itinerary> result = Optional.ofNullable(skyscannerService.searchCreateGetItinerary(skyscannerMapper.mapTripPlanToFlightSearchDto(tripPlan)));
            if (result.isPresent()) {
                tripPlanRepository.save(tripPlan);
                result.get().setTripPlan(tripPlan);
                itineraryRepository.save(result.get());
            }
            return result;
        } catch (Exception e) {
            throw new ItineraryNotFoundException();
        }
    }

    public void switchItineraryPurchased(Itinerary itinerary) {
        itinerary.setPurchased(!itinerary.isPurchased());
        itineraryRepository.save(itinerary);
    }

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
        if (tripPlanRepository.findById(id).isPresent()) {
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

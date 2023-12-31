package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;

import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
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
        return itineraryRepository.existsById(itineraryToCheck.getItineraryId());
    }

    public List<Itinerary> getPurchasedItineraries() {
        return itineraryRepository.findAll().stream()
                .filter(Itinerary::isPurchased)
                .collect(Collectors.toList());
    }

    public void createItineraryBasedOnTripPlan(TripPlan tripPlan) throws ItineraryNotFoundException {
        try {
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
        } catch (Exception e) {
            throw new ItineraryNotFoundException();
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
}

package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.exception.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;

import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.mapper.SkyscannerMapper;
import com.kodilla.cheapflightsearch.mapper.TripPlanMapper;
import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
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
    private final TripPlanMapper tripPlanMapper;
    private final RouteService routeService;

    public List<Itinerary> getItineraries() {
        return itineraryRepository.findAll();
    }

    public Itinerary getItinerary(Long id) throws ItineraryNotFoundException {
        return itineraryRepository.findById(id).orElseThrow(ItineraryNotFoundException::new);
    }

    public Itinerary getItineraryByItineraryMark(String itineraryMark) throws ItineraryNotFoundException {
        return itineraryRepository.findAll().stream()
                .filter(i -> i.getItineraryMark().equals(itineraryMark))
                .findFirst()
                .orElseThrow(ItineraryNotFoundException::new);
    }

    public void deleteItinerary(Long id) throws ItineraryNotFoundException {
        if (itineraryRepository.findById(id).isPresent()) {
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

    public Optional<Itinerary> searchForItineraryBasedOnTripPlan(TripPlan tripPlan) {
        try {
            return Optional.ofNullable(createItinerary(skyscannerService.searchCreateGetItinerary(skyscannerMapper.mapTripPlanToFlightSearchDto(tripPlan))));
        } catch (Exception e) {
            return null;
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
}

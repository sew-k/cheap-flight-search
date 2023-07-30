package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.controller.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;

import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.mapper.SkyscannerMapper;
import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final CalendarService calendarService;
    private final RouteService routeService;

    public List<Itinerary> getItineraries() {
        return itineraryRepository.findAll();
    }

    public Itinerary getItinerary(Long id) throws ItineraryNotFoundException {
        return itineraryRepository.findById(id).orElseThrow(ItineraryNotFoundException::new);
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

    public List<Itinerary> searchForItinerariesMatchingRoutesAndHolidayPlans(List<HolidayPlan> holidayPlanList,
                                                                             List<Route> routes,
                                                                             int adults
                                                                            ) throws Exception {
        List<Itinerary> resultList = new ArrayList<>();
        for (Route route : routes) {
            for (HolidayPlan holidayPlan : holidayPlanList) {
                if (route.getDaysOfWeek().contains(holidayPlan.getBeginDate().getDayOfWeek())) {
                    Optional<Itinerary> searchResult = Optional.ofNullable(
                            createItinerary(
                                    skyscannerService.searchCreateGetItinerary(
                                            skyscannerMapper.mapTripPlanToFlightSearchDto(
                                                    new TripPlan(
                                                            route.getOrigin().getIataCode(),
                                                            route.getDestination().getIataCode(),
                                                            holidayPlan.getBeginDate(),
                                                            holidayPlan.getEndDate(),
                                                            adults
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

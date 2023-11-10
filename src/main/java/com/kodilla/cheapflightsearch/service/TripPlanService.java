package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.TripPlanNotFoundException;
import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
import com.kodilla.cheapflightsearch.repository.RouteRepository;
import com.kodilla.cheapflightsearch.repository.TripPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripPlanService {
    private final TripPlanRepository tripPlanRepository;
    private final ItineraryRepository itineraryRepository;
    private final RouteRepository routeRepository;
    private final AirportService airportService;
    private final CalendarService calendarService;


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
        if (tripPlanRepository.existsById(id)) {
            return tripPlanRepository.save(tripPlan);
        } else {
            throw new TripPlanNotFoundException();
        }
    }

    public TripPlan createTripPlan(TripPlan tripPlan) {
        User user = tripPlan.getUser();
        TripPlan updatedTripPlan = tripPlan;
        if (!tripPlanRepository.findByUser(user).contains(tripPlan)) {
            updatedTripPlan = tripPlanRepository.save(tripPlan);
        }
        return updatedTripPlan;
    }

    public List<TripPlan> createTripPlansFromFavouriteRoutesAndHolidayPlans(User currentUser, int adults) {
        List<Route> routes = routeRepository.findByUser(currentUser);
        List<HolidayPlan> holidayPlans = calendarService.getHolidayPlansByUser(currentUser); //TODO maybe is better to add relations to user from calendar side to use repository here;
        List<TripPlan> currentUserTripPlans = tripPlanRepository.findByUser(currentUser);
        List<TripPlan> tripPlans = new ArrayList<>();
        for (Route route : routes) {
            for (HolidayPlan holidayPlan : holidayPlans) {
                if (checkMatchingRouteAndHolidayPlan(route, holidayPlan)) {
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

    private boolean checkMatchingRouteAndHolidayPlan(Route route, HolidayPlan holidayPlan) {
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

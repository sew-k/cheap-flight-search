package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import org.springframework.stereotype.Service;

@Service
public class TripPlanMapper {
    public TripPlan mapRouteAndHolidayPlanToTripPlan(HolidayPlan holidayPlan, Route route, int adults) {
        return new TripPlan(
                route.getOrigin().getIataCode(),
                route.getDestination().getIataCode(),
                holidayPlan.getBeginDate(),
                holidayPlan.getEndDate(),
                adults);
    }
}

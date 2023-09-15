package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TripPlanMapperTestSuite {
    @Autowired
    TripPlanMapper tripPlanMapper;

    @Test
    void testMapRouteAndHolidayPlanToTripPlan() {
        //Given
        Route route = new Route(
                1L,
                new Airport("Poland", "Warsaw", "WAW"),
                new Airport("Germany", "Berlin", "BER"),
                Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                true,
                new User()
                );
        HolidayPlan holidayPlan = new HolidayPlan(
                LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,10)
        );

        //When
        TripPlan resultTripPlan = tripPlanMapper.mapRouteAndHolidayPlanToTripPlan(holidayPlan, route, 3);

        //Then
        assertEquals(route.getOrigin().getIataCode(), resultTripPlan.getOriginIata());
        assertEquals(route.getDestination().getIataCode(), resultTripPlan.getDestinationIata());
        assertEquals(3, resultTripPlan.getAdults());
        assertEquals(holidayPlan.getBeginDate(), resultTripPlan.getBeginDate());
        assertEquals(holidayPlan.getEndDate(), resultTripPlan.getEndDate());
    }
}
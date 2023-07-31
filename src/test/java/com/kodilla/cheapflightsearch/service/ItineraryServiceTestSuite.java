package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.mapper.SkyscannerMapper;
import com.kodilla.cheapflightsearch.mapper.TripPlanMapper;
import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
import com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata.FlightSearchRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItineraryServiceTestSuite {

    @InjectMocks
    private ItineraryService itineraryService;
    @Mock
    private SkyscannerService skyscannerService;
    @Mock
    private SkyscannerMapper skyscannerMapper;
    @Mock
    private TripPlanMapper tripPlanMapper;
    @Mock
    private ItineraryRepository itineraryRepository;

    @Test
    void getItineraries() {
    }

    @Test
    void getItinerary() {
    }

    @Test
    void deleteItinerary() {
    }

    @Test
    void updateItinerary() {
    }

    @Test
    void createItinerary() {
    }

    @Test
    void getPurchasedItineraries() {
    }

    @Test
    void searchForItineraryBasedOnTripPlan() {
    }

    @Test
    void switchItineraryPurchased() {
    }

    @Test
    void searchForItinerariesMatchingRoutesAndHolidayPlans_DatesMatching() {
        //Given
        Route route = new Route(
                new Airport("Poland", "Warsaw", "WAW"),
                new Airport("Germany", "Cologne", "CGN"),
                Set.of(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY),
                true
        );
        HolidayPlan holidayPlan = new HolidayPlan(
                LocalDate.of(2023, 9, 22),
                LocalDate.of(2023, 9, 24)
        );
        TripPlan tripPlan = new TripPlan(
                route.getOrigin().getIataCode(),
                route.getDestination().getIataCode(),
                holidayPlan.getBeginDate(),
                holidayPlan.getEndDate(),
                1
        );
        FlightSearchRequestDto flightSearchRequestDto = new FlightSearchRequestDto(
                1,
                "WAW",
                "CGN",
                LocalDate.of(2023, 9, 22),
                LocalDate.of(2023, 9, 24)
        );
        List<Itinerary> resultList = new ArrayList<>();
        Itinerary itinerary = new Itinerary();
        try {
            when(skyscannerService.searchCreateGetItinerary(flightSearchRequestDto)).thenReturn(itinerary);
        } catch (Exception e) {

        }
        when(tripPlanMapper.mapRouteAndHolidayPlanToTripPlan(holidayPlan, route, 1)).thenReturn(tripPlan);
        when(skyscannerMapper.mapTripPlanToFlightSearchDto(tripPlan)).thenReturn(flightSearchRequestDto);
        when(itineraryRepository.save(itinerary)).thenReturn(itinerary);

        //When
        try {
            resultList = itineraryService.searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(
                    List.of(holidayPlan),
                    List.of(route),
                    1
            );
        } catch (Exception e) {

        }

        //Then
        try {
            verify(skyscannerService, atLeastOnce()).searchCreateGetItinerary(flightSearchRequestDto);
        } catch (Exception e) {

        }
        verify(tripPlanMapper, atLeastOnce()).mapRouteAndHolidayPlanToTripPlan(holidayPlan, route, 1);
        verify(skyscannerMapper, atLeastOnce()).mapTripPlanToFlightSearchDto(tripPlan);
        verify(itineraryRepository, atLeastOnce()).save(itinerary);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
    }
    @Test
    void searchForItinerariesMatchingRoutesAndHolidayPlans_DatesNotMatching() {
        //Given
        Route route = new Route(
                new Airport("Poland", "Warsaw", "WAW"),
                new Airport("Germany", "Cologne", "CGN"),
                Set.of(DayOfWeek.THURSDAY, DayOfWeek.SUNDAY, DayOfWeek.SATURDAY),
                true
        );
        HolidayPlan holidayPlan = new HolidayPlan(
                LocalDate.of(2023, 9, 22),
                LocalDate.of(2023, 9, 24)
        );
        TripPlan tripPlan = new TripPlan(
                route.getOrigin().getIataCode(),
                route.getDestination().getIataCode(),
                holidayPlan.getBeginDate(),
                holidayPlan.getEndDate(),
                1
        );
        FlightSearchRequestDto flightSearchRequestDto = new FlightSearchRequestDto(
                1,
                "WAW",
                "CGN",
                LocalDate.of(2023, 9, 22),
                LocalDate.of(2023, 9, 24)
        );
        List<Itinerary> resultList = new ArrayList<>();
        Itinerary itinerary = new Itinerary();

        //When
        try {
            resultList = itineraryService.searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(
                    List.of(holidayPlan),
                    List.of(route),
                    1
            );
        } catch (Exception e) {

        }

        //Then
        try {
            verify(skyscannerService, never()).searchCreateGetItinerary(flightSearchRequestDto);
        } catch (Exception e) {

        }
        verify(tripPlanMapper, never()).mapRouteAndHolidayPlanToTripPlan(holidayPlan, route, 1);
        verify(skyscannerMapper, never()).mapTripPlanToFlightSearchDto(tripPlan);
        verify(itineraryRepository, never()).save(itinerary);
        assertTrue(resultList.isEmpty());
        assertEquals(0, resultList.size());
    }
}
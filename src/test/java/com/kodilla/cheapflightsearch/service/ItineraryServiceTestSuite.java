package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.exception.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.mapper.SkyscannerMapper;
import com.kodilla.cheapflightsearch.mapper.TripPlanMapper;
import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
import com.kodilla.cheapflightsearch.repository.TripPlanRepository;
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
import java.util.Optional;
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
    @Mock
    private TripPlanRepository tripPlanRepository;

    @Test
    void getItineraries() {
        //When
        itineraryService.getItineraries();

        //Then
        verify(itineraryRepository, atLeastOnce()).findAll();
    }

    @Test
    void getItinerary() throws Exception {
        //Given
        Long id = 1L;
        when(itineraryRepository.findById(id)).thenReturn(Optional.of(new Itinerary()));

        //When
        itineraryService.getItinerary(id);

        //Then
        verify(itineraryRepository, atLeastOnce()).findById(id);
    }

    @Test
    void testDeleteItinerary() throws Exception {
        //Given
        Itinerary itinerary = new Itinerary(1L, "itinerary mark", 199.99, null, "link", false);
        Long id = itinerary.getItineraryId();
        when(itineraryRepository.existsById(id)).thenReturn(true);
        when(itineraryRepository.findById(id)).thenReturn(Optional.of(itinerary));

        //When
        itineraryService.deleteItinerary(id);

        //Then
        verify(itineraryRepository, atLeastOnce()).deleteById(id);
    }

    @Test
    void testUpdateItinerary() throws Exception {
        //Given
        Long id = 1L;
        Itinerary itinerary = new Itinerary();
        when(itineraryRepository.findById(id)).thenReturn(Optional.of(itinerary));

        //When
        itineraryService.updateItinerary(id, itinerary);

        //Then
        verify(itineraryRepository, atLeastOnce()).findById(id);
        verify(itineraryRepository, atLeastOnce()).save(itinerary);
    }

    @Test
    void testUpdateItinerary_notExisting() {
        //Given
        Long id = 1L;
        Itinerary itinerary = new Itinerary();
        when(itineraryRepository.findById(id)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(ItineraryNotFoundException.class, () -> itineraryService.updateItinerary(id, itinerary));
        verify(itineraryRepository, atLeastOnce()).findById(id);
        verify(itineraryRepository, never()).save(itinerary);
    }

    @Test
    void testCreateItinerary() {
        //Given
        Itinerary itinerary = new Itinerary("itinerary mark", 199.99, null, "link");

        //When
        itineraryService.createItinerary(itinerary);

        //Then
        verify(itineraryRepository, atLeastOnce()).save(itinerary);
    }

    @Test
    void testGetPurchasedItineraries() {
        //Given
        Itinerary itinerary1 = new Itinerary();
        itinerary1.setPurchased(true);
        Itinerary itinerary2 = new Itinerary();
        itinerary2.setPurchased(true);
        Itinerary itinerary3 = new Itinerary();
        List<Itinerary> itineraries = List.of(itinerary1, itinerary2, itinerary3);
        when(itineraryRepository.findAll()).thenReturn(itineraries);

        //When
        List<Itinerary> fetchedItineraries = itineraryService.getPurchasedItineraries();

        //Then
        verify(itineraryRepository, atLeastOnce()).findAll();
        assertFalse(fetchedItineraries.isEmpty());
        assertEquals(2, fetchedItineraries.size());
    }

    @Test
    void testCreateItineraryBasedOnTripPlan() throws Exception {
        //Given
        Itinerary foundItinerary = new Itinerary(
                "mark",
                999.99,
                "link"
        );
        TripPlan tripPlan = new TripPlan(
                "WAW12",
                "WMI21",
                LocalDate.of(2030, 01, 01),
                LocalDate.of(2030, 01, 02),
                1
        );
        FlightSearchRequestDto flightSearchRequestDto = new FlightSearchRequestDto(
                1,
                "WAW12",
                "WMI21",
                LocalDate.of(2030, 01, 01),
                LocalDate.of(2030, 01, 02)
        );
        when(skyscannerMapper.mapTripPlanToFlightSearchDto(tripPlan)).thenReturn(flightSearchRequestDto);
        when(skyscannerService.searchCreateGetItinerary(flightSearchRequestDto)).thenReturn(foundItinerary);

        //When
        itineraryService.createItineraryBasedOnTripPlan(tripPlan);

        //Then
        assertEquals(tripPlan.getOriginIata(), foundItinerary.getTripPlan().getOriginIata());
        assertEquals(tripPlan.getDestinationIata(), foundItinerary.getTripPlan().getDestinationIata());
        verify(itineraryRepository, atLeastOnce()).save(foundItinerary);
        verify(tripPlanRepository, atLeastOnce()).save(tripPlan);
    }

    @Test
    void testSwitchItineraryPurchased() {
        //Given
        Itinerary itinerary = new Itinerary("itinerary mark", 199.99, "purchase link");
        itineraryService.createItinerary(itinerary);
        Long id = itinerary.getItineraryId();

        //When
        itineraryService.switchItineraryPurchased(itinerary);

        //Then
        assertTrue(itinerary.isPurchased());

        //CleanUp
        try {
            itineraryService.deleteItinerary(id);
        } catch (Exception e) {

        }
    }

    @Test
    void testSearchForItinerariesMatchingRoutesAndHolidayPlans_DatesMatching() {
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
    void testSearchForItinerariesMatchingRoutesAndHolidayPlans_DatesNotMatching() {
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
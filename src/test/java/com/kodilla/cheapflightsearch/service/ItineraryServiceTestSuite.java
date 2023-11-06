package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
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
    void testGetItineraries() {
        //When
        itineraryService.getItineraries();

        //Then
        verify(itineraryRepository, atLeastOnce()).findAll();
    }

    @Test
    void testGetItinerary() throws Exception {
        //Given
        Long id = 1L;
        when(itineraryRepository.findById(id)).thenReturn(Optional.of(new Itinerary()));

        //When
        itineraryService.getItinerary(id);

        //Then
        verify(itineraryRepository, atLeastOnce()).findById(id);
    }

    @Test
    void testGetItinerary_notExisting() throws Exception {
        //Given
        Long id = 999L;

        //When & Then
        assertThrows(ItineraryNotFoundException.class, () -> itineraryService.getItinerary(id));
        verify(itineraryRepository, atLeastOnce()).findById(id);
    }

    @Test
    void testGetItinerariesByUser() {
        //Given
        User user1 = new User("username1", "userEmail1", "userPassword1");
        User user2 = new User("username2", "userEmail2", "userPassword2");
        TripPlan tripPlan1 = new TripPlan();
        TripPlan tripPlan2 = new TripPlan();
        TripPlan tripPlan3 = new TripPlan();
        tripPlan1.setUser(user1);
        tripPlan2.setUser(user1);
        tripPlan3.setUser(user2);
        Itinerary itinerary1 = new Itinerary();
        Itinerary itinerary2 = new Itinerary();
        Itinerary itinerary3 = new Itinerary();
        itinerary1.setTripPlan(tripPlan1);
        itinerary2.setTripPlan(tripPlan2);
        itinerary3.setTripPlan(tripPlan3);
        when(itineraryRepository.findAll()).thenReturn(List.of(itinerary1, itinerary2, itinerary3));

        //When
        List<Itinerary> resultList = itineraryService.getItinerariesByUser(user1);

        //Then
        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());
    }

    @Test
    void testGetItineraryByItineraryMark() throws Exception {
        //Given
        Itinerary itinerary4 = new Itinerary("mark4", 194.10, "link4");
        Itinerary itinerary5 = new Itinerary("mark5", 195.10, "link5");
        Itinerary itinerary6 = new Itinerary("mark6", 196.10, "link6");
        when(itineraryRepository.findAll()).thenReturn(List.of(itinerary4, itinerary5, itinerary6));

        //When
        Optional<Itinerary> fetchedItinerary = Optional.ofNullable(itineraryService.getItineraryByItineraryMark("mark5"));

        //Then
        assertTrue(fetchedItinerary.isPresent());
        assertEquals(itinerary5, fetchedItinerary.get());
    }

    @Test
    void testGetItineraryByItineraryMark_notExisting() throws Exception {
        //Given
        Itinerary itinerary4 = new Itinerary("mark4", 194.10, "link4");
        Itinerary itinerary5 = new Itinerary("mark5", 195.10, "link5");
        Itinerary itinerary6 = new Itinerary("mark6", 196.10, "link6");
        when(itineraryRepository.findAll()).thenReturn(List.of(itinerary4, itinerary5, itinerary6));

        //When & Then
        assertThrows(ItineraryNotFoundException.class, () -> itineraryService.getItineraryByItineraryMark("mark99"));
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
    void testDeleteItinerary_notExisting() throws Exception {
        //Given
        Long id = 999L;

        //When
        when(itineraryRepository.existsById(id)).thenReturn(false);

        //Then
        assertThrows(ItineraryNotFoundException.class, () -> itineraryService.deleteItinerary(id));
        verify(itineraryRepository, atLeastOnce()).existsById(id);
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
    void testSearchForItinerariesMatchingRoutesAndHolidayPlans_DatesMatching() throws Exception {
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
        Itinerary itinerary = new Itinerary();
        when(skyscannerService.searchCreateGetItinerary(flightSearchRequestDto)).thenReturn(itinerary);
        when(tripPlanMapper.mapRouteAndHolidayPlanToTripPlan(holidayPlan, route, 1)).thenReturn(tripPlan);
        when(skyscannerMapper.mapTripPlanToFlightSearchDto(tripPlan)).thenReturn(flightSearchRequestDto);
        when(itineraryRepository.save(itinerary)).thenReturn(itinerary);

        //When
        List<Itinerary> resultList = itineraryService.searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(
                List.of(holidayPlan),
                List.of(route),
                1
        );

        //Then
        verify(skyscannerService, atLeastOnce()).searchCreateGetItinerary(flightSearchRequestDto);
        verify(tripPlanMapper, atLeastOnce()).mapRouteAndHolidayPlanToTripPlan(holidayPlan, route, 1);
        verify(skyscannerMapper, atLeastOnce()).mapTripPlanToFlightSearchDto(tripPlan);
        verify(itineraryRepository, atLeastOnce()).save(itinerary);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
    }

    @Test
    void testSearchForItinerariesMatchingRoutesAndHolidayPlans_DatesNotMatching() throws Exception {
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
        Itinerary itinerary = new Itinerary();

        //When
        List<Itinerary> resultList = itineraryService.searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(
                List.of(holidayPlan),
                List.of(route),
                1
        );

        //Then
        verify(skyscannerService, never()).searchCreateGetItinerary(flightSearchRequestDto);
        verify(tripPlanMapper, never()).mapRouteAndHolidayPlanToTripPlan(holidayPlan, route, 1);
        verify(skyscannerMapper, never()).mapTripPlanToFlightSearchDto(tripPlan);
        verify(itineraryRepository, never()).save(itinerary);
        assertTrue(resultList.isEmpty());
        assertEquals(0, resultList.size());
    }
}
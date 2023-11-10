package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserRole;
import com.kodilla.cheapflightsearch.exception.AirportNotFoundException;
import com.kodilla.cheapflightsearch.exception.TripPlanNotFoundException;
import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
import com.kodilla.cheapflightsearch.repository.RouteRepository;
import com.kodilla.cheapflightsearch.repository.TripPlanRepository;
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
class TripPlanServiceTestSuite {
    @InjectMocks
    private TripPlanService tripPlanService;
    @Mock
    private TripPlanRepository tripPlanRepository;
    @Mock
    private ItineraryRepository itineraryRepository;
    @Mock
    private AirportService airportService;
    @Mock
    private CalendarService calendarService;
    @Mock
    private RouteRepository routeRepository;

    @Test
    void getTripPlans() {
        //Given
        TripPlan tripPlan1 = new TripPlan();
        TripPlan tripPlan2 = new TripPlan();
        when(tripPlanRepository.findAll()).thenReturn(List.of(tripPlan1, tripPlan2));

        //When
        List<TripPlan> fetchedList = tripPlanService.getTripPlans();

        //Then
        verify(tripPlanRepository, atLeastOnce()).findAll();
        assertFalse(fetchedList.isEmpty());
        assertEquals(2, fetchedList.size());
    }

    @Test
    void getTripPlansByUserId() {
        //Given
        TripPlan tripPlan3 = new TripPlan(
                "iataOrigin3",
                "iataDest3",
                LocalDate.of(2030, 03, 01),
                LocalDate.of(2030, 03, 02),
                1
        );
        TripPlan tripPlan4 = new TripPlan(
                "iataOrigin4",
                "iataDest4",
                LocalDate.of(2030, 04, 01),
                LocalDate.of(2030, 04, 02),
                1
        );
        TripPlan tripPlan5 = new TripPlan(
                "iataOrigin5",
                "iataDest5",
                LocalDate.of(2030, 05, 01),
                LocalDate.of(2030, 05, 02),
                1
        );
        User user1 = new User(
                1L,
                "username1",
                "userEmail1",
                UserRole.USER,
                "userPassword1",
                new Calendar()
        );
        User user2 = new User(
                2L,
                "username2",
                "userEmail2",
                UserRole.USER,
                "userPassword2",
                new Calendar()
        );
        tripPlan3.setUser(user1);
        tripPlan4.setUser(user1);
        tripPlan5.setUser(user2);
        when(tripPlanRepository.findAll()).thenReturn(List.of(tripPlan3, tripPlan4, tripPlan5));

        //When
        List<TripPlan> fetchedList = tripPlanService.getTripPlansByUserId(user1.getUserId());

        //Then
        verify(tripPlanRepository, atLeastOnce()).findAll();
        assertFalse(fetchedList.isEmpty());
        assertEquals(2, fetchedList.size());
        assertTrue(fetchedList.contains(tripPlan3));
        assertTrue(fetchedList.contains(tripPlan4));
    }

    @Test
    void getTripPlansByUser() {
        //Given
        User user3 = new User();
        when(tripPlanRepository.findByUser(user3)).thenReturn(new ArrayList<>());

        //When
        List<TripPlan> fetchedList = tripPlanService.getTripPlansByUser(user3);

        //Then
        verify(tripPlanRepository, atLeastOnce()).findByUser(user3);
        assertTrue(fetchedList.isEmpty());
    }

    @Test
    void deleteTripPlan() throws Exception {
        //Given
        Long id = 1L;
        TripPlan tripPlan = new TripPlan();
        when(tripPlanRepository.findById(id)).thenReturn(Optional.of(tripPlan));
        when(itineraryRepository.findByTripPlan(tripPlan)).thenReturn(new ArrayList<>());

        //When
        tripPlanService.deleteTripPlan(id);

        //Then
        verify(tripPlanRepository, atLeastOnce()).deleteById(id);
    }

    @Test
    void deleteTripPlan_notExisting() throws Exception {
        //Given
        Long id = 99L;
        when(tripPlanRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        //When & Then
        assertThrows(TripPlanNotFoundException.class, () -> tripPlanService.deleteTripPlan(id));
    }

    @Test
    void updateTripPlan() throws Exception {
        //Given
        Long id = 1L;
        TripPlan tripPlanToSave = new TripPlan();
        when(tripPlanRepository.existsById(id)).thenReturn(true);
        when(tripPlanRepository.save(tripPlanToSave)).thenReturn(tripPlanToSave);

        //When
        TripPlan savedTripPlan = tripPlanService.updateTripPlan(1L, tripPlanToSave);

        //Then
        assertDoesNotThrow(() -> tripPlanService.updateTripPlan(1L, tripPlanToSave));
        verify(tripPlanRepository, atLeastOnce()).existsById(id);
        verify(tripPlanRepository, atLeastOnce()).save(tripPlanToSave);
        assertEquals(tripPlanToSave, savedTripPlan);
    }

    @Test
    void updateTripPlan_notExisting() throws Exception {
        //Given
        Long id = 99L;
        TripPlan tripPlanToSave = new TripPlan();
        when(tripPlanRepository.existsById(id)).thenReturn(false);

        //When & Then
        assertThrows(TripPlanNotFoundException.class, () -> tripPlanService.updateTripPlan(id, tripPlanToSave));
        verify(tripPlanRepository, atLeastOnce()).existsById(id);
    }

    @Test
    void createTripPlan() {
        //Given
        TripPlan tripPlanToCreate = new TripPlan(
                "iataOrigin6",
                "iataDest6",
                LocalDate.of(2030, 05, 01),
                LocalDate.of(2030, 05, 02),
                1
        );
        User user3 = new User(
                3L,
                "username3",
                "userEmail3",
                UserRole.USER,
                "userPassword3",
                new Calendar()
        );
        TripPlan otherTripPlan = new TripPlan(
                "iataOrigin7",
                "iataDest7",
                LocalDate.of(2030, 05, 01),
                LocalDate.of(2030, 05, 02),
                1);
        tripPlanToCreate.setUser(user3);
        otherTripPlan.setUser(user3);
        when(tripPlanRepository.findByUser(user3)).thenReturn(List.of(otherTripPlan));
        when(tripPlanRepository.save(tripPlanToCreate)).thenReturn(tripPlanToCreate);

        //When
        TripPlan createdTripPlan = tripPlanService.createTripPlan(tripPlanToCreate);

        //Then
        assertEquals(tripPlanToCreate, createdTripPlan);
    }

    @Test
    void createTripPlansFromFavouriteRoutesAndHolidayPlans() {
        //Given
        User user = new User("username4", "userEmail4", "userPassword4");
        Route route1 = new Route(
                new Airport("Poland", "Warsaw", "WAW"),
                new Airport("Portugal", "Lisbon", "LIS"),
                Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                true,
                user
        );
        Route route2 = new Route(
                new Airport("Poland", "Modlin", "WMI"),
                new Airport("Portugal", "Lisbon", "LIS"),
                Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                true,
                user
        );
        when(routeRepository.findByUser(user)).thenReturn(List.of(route1, route2));
        HolidayPlan userHolidayPlan = new HolidayPlan(
                LocalDate.of(2023, 11, 6),
                LocalDate.of(2023, 11, 8)
        );
        when(calendarService.getHolidayPlansByUser(user)).thenReturn(List.of(userHolidayPlan));
        when(tripPlanRepository.findByUser(user)).thenReturn(new ArrayList<>());

        //When
        List<TripPlan> createdTripPlans = tripPlanService.createTripPlansFromFavouriteRoutesAndHolidayPlans(
                user,
                1
        );
        System.out.println(createdTripPlans);

        //Then
        assertFalse(createdTripPlans.isEmpty());
        assertEquals(1, createdTripPlans.size());
    }

    @Test
    void getWeatherForTripPlanDestination() throws Exception {
        //Given
        String destinationIata = "WAW";
        TripPlan tripPlan = new TripPlan(
                "RAD",
                destinationIata,
                LocalDate.of(2030, 05, 01),
                LocalDate.of(2030, 05, 02),
                1
        );
        Airport destinationAirport = new Airport("Poland", "Warsaw", "WAW");
        when(airportService.getAirportByIata(destinationIata)).thenReturn(
                destinationAirport
        );
        when(airportService.getWeatherForAirport(destinationAirport)).thenReturn("15");

        //When
        String resultWeather = tripPlanService.getWeatherForTripPlanDestination(tripPlan);

        //Then
        assertEquals("15", resultWeather);
    }

    @Test
    void getWeatherForTripPlanDestination_notFound() throws Exception {
        //Given
        String destinationIata = "WAW";
        TripPlan tripPlan = new TripPlan(
                "RAD",
                destinationIata,
                LocalDate.of(2030, 05, 01),
                LocalDate.of(2030, 05, 02),
                1
        );
        Airport destinationAirport = new Airport("Poland", "Warsaw", "WAW");
        when(airportService.getAirportByIata(destinationIata)).thenThrow(new AirportNotFoundException());

        //When
        String resultWeather = tripPlanService.getWeatherForTripPlanDestination(tripPlan);

        //Then
        assertEquals("Not found", resultWeather);
    }

    @Test
    void getCityForTripPlanDestination() throws Exception {
        //Given
        String destinationIata = "WAW";
        TripPlan tripPlan = new TripPlan(
                "RAD",
                destinationIata,
                LocalDate.of(2030, 05, 01),
                LocalDate.of(2030, 05, 02),
                1
        );
        when(airportService.getAirportByIata(destinationIata)).thenReturn(
                new Airport("Poland", "Warsaw", "WAW")
        );

        //When
        String resultCity = tripPlanService.getCityForTripPlanDestination(tripPlan);

        //Then
        assertEquals("Warsaw", resultCity);
    }

    @Test
    void getCityForTripPlanDestination_notFound() throws Exception {
        //Given
        String destinationIata = "WWW";
        TripPlan tripPlan = new TripPlan(
                "RAD",
                destinationIata,
                LocalDate.of(2030, 05, 01),
                LocalDate.of(2030, 05, 02),
                1
        );
        when(airportService.getAirportByIata(destinationIata)).thenThrow(new AirportNotFoundException());

        //When
        String resultCity = tripPlanService.getCityForTripPlanDestination(tripPlan);

        //Then
        assertEquals("Not found", resultCity);
    }
}
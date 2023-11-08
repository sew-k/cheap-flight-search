package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserRole;
import com.kodilla.cheapflightsearch.repository.TripPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripPlanServiceTestSuite {
    @InjectMocks
    private TripPlanService tripPlanService;
    @Mock
    private TripPlanRepository tripPlanRepository;

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
    void deleteTripPlan() {

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
        verify(tripPlanRepository, atLeastOnce()).existsById(id);
        verify(tripPlanRepository, atLeastOnce()).save(tripPlanToSave);
        assertEquals(tripPlanToSave, savedTripPlan);
    }

    @Test
    void createTripPlan() {
    }

    @Test
    void createTripPlansFromFavouriteRoutesAndHolidayPlans() {
    }

    @Test
    void getWeatherForTripPlanDestination() {
    }

    @Test
    void getCityForTripPlanDestination() {
    }
}
package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TripPlanRepositoryTestSuite {
    @Autowired
    TripPlanRepository tripPlanRepository;
    @Autowired
    UserRepository userRepository;
    private Long id1;
    private Long id2;
    private Long id3;

    @BeforeEach
    public void setUp() {
        TripPlan tripPlan1 = new TripPlan(
                "WAW",
                "WMI",
                LocalDate.of(2050, 1, 1),
                LocalDate.of(2050, 1, 2),
                1);
        TripPlan tripPlan2 = new TripPlan(
                "WMI",
                "CGN",
                LocalDate.of(2050, 2, 1),
                LocalDate.of(2050, 2, 2),
                2);
        TripPlan tripPlan3 = new TripPlan(
                "CGN",
                "AGP",
                LocalDate.of(2050, 3, 1),
                LocalDate.of(2050, 3, 2),
                3);
        tripPlanRepository.save(tripPlan1);
        tripPlanRepository.save(tripPlan2);
        tripPlanRepository.save(tripPlan3);
        id1 = tripPlan1.getTripPlanId();
        id2 = tripPlan2.getTripPlanId();
        id3 = tripPlan3.getTripPlanId();
    }

    @AfterEach
    void tearDown() {
        try {
            tripPlanRepository.deleteById(id1);
            tripPlanRepository.deleteById(id2);
            tripPlanRepository.deleteById(id3);
        } catch (Exception e) {

        }
    }

    @Test
    void testFindAll() {
        //When
        List<TripPlan> fetchedTripPlans = tripPlanRepository.findAll();

        //Then
        assertFalse(fetchedTripPlans.isEmpty());
        assertTrue(fetchedTripPlans.size() >= 3);
        assertEquals("WAW", fetchedTripPlans.get(0).getOriginIata());
        assertEquals("WMI", fetchedTripPlans.get(0).getDestinationIata());
        assertEquals("WMI", fetchedTripPlans.get(1).getOriginIata());
        assertEquals("CGN", fetchedTripPlans.get(1).getDestinationIata());
    }

    @Test
    void testSave() {
        //Given
        TripPlan tripPlan4 = new TripPlan(
                "AGP",
                "WAW",
                LocalDate.of(2050, 4, 1),
                LocalDate.of(2050, 4, 2),
                4);
        Long id4;

        //When
        tripPlanRepository.save(tripPlan4);
        id4 = tripPlan4.getTripPlanId();

        //Then
        assertTrue(tripPlanRepository.existsById(id4));

        //CleanUp
        tripPlanRepository.deleteById(id4);
    }

    @Test
    void testFindById() {
        //When
        Optional<TripPlan> fetchedTripPlan = tripPlanRepository.findById(id1);

        //Then
        assertTrue(fetchedTripPlan.isPresent());
        assertEquals("WAW", fetchedTripPlan.get().getOriginIata());
        assertEquals("WMI", fetchedTripPlan.get().getDestinationIata());
    }

    @Test
    void testDeleteById() {
        //When
        tripPlanRepository.deleteById(id3);

        //Then
        assertFalse(tripPlanRepository.existsById(id3));
    }

    @Test
    void testExistsById() {
        //When & Then
        assertTrue(tripPlanRepository.existsById(id1));
    }

    @Test
    void testFindByUser() {
        //Given
        User user = new User("username", "email", "password");
        userRepository.save(user);
        Long userId = user.getUserId();
        Optional<TripPlan> tripPlan = tripPlanRepository.findById(id2);
        if (tripPlan.isPresent()) {
            tripPlan.get().setUser(user);
            tripPlanRepository.save(tripPlan.get());
        }

        //When
        List<TripPlan> fetchedTripPlans = tripPlanRepository.findByUser(user);

        //Then
        assertFalse(fetchedTripPlans.isEmpty());
        assertEquals(1, fetchedTripPlans.size());
        assertEquals(user.getUsername(), fetchedTripPlans.get(0).getUser().getUsername());
        assertEquals(user.getPassword(), fetchedTripPlans.get(0).getUser().getPassword());
        assertEquals(user.getEmail(), fetchedTripPlans.get(0).getUser().getEmail());

        //CleanUp
        userRepository.deleteById(userId);
    }
}
package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ItineraryRepositoryTestSuite {
    @Autowired
    ItineraryRepository itineraryRepository;
    @Autowired
    TripPlanRepository tripPlanRepository;

    @Test
    void findAll() {
    }

    @Test
    void testSaveUpdate() {
        //Given
        Itinerary itinerary = new Itinerary("itinerary mark", 199.99, null,"link");
        itineraryRepository.save(itinerary);
        Long id = itinerary.getItineraryId();

        //When
        itinerary.setPrice(1000.00);
        Itinerary updatedItinerary = itineraryRepository.save(itinerary);

        //Then
        assertEquals(1000.00, updatedItinerary.getPrice());

        //CleanUp
        itineraryRepository.deleteById(id);
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void existsByItineraryMark() {
    }
    @Test
    void testExistsById() {
        //Given
        Itinerary itinerary = new Itinerary("itinerary mark", 199.99, null,"link");
        itineraryRepository.save(itinerary);
        Long id = itinerary.getItineraryId();

        //When&Then
        assertTrue(itineraryRepository.existsById(id));

        //CleanUp
        itineraryRepository.deleteById(id);
    }
    @Test
    void testExistsById_notExisting() {
        //Given
        Long id = 999L;

        //When&Then
        assertFalse(itineraryRepository.existsById(id));

        //CleanUp
        try {
            itineraryRepository.deleteById(id);
        } catch (Exception e) {

        }
    }
}
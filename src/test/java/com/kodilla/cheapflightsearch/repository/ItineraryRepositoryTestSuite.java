package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItineraryRepositoryTestSuite {
    @Autowired
    ItineraryRepository itineraryRepository;
    @Autowired
    TripPlanRepository tripPlanRepository;

    @Test
    void testFindAll() {
        //Given
        Itinerary itinerary1 = new Itinerary("itinerary mark 1", 199.99, null, "link 1");
        Itinerary itinerary2 = new Itinerary("itinerary mark 2", 199.99, null, "link 2");
        itineraryRepository.save(itinerary1);
        itineraryRepository.save(itinerary2);
        Long id1 = itinerary1.getItineraryId();
        Long id2 = itinerary2.getItineraryId();

        //When
        List<Itinerary> fetchedItineraries = itineraryRepository.findAll();

        //Then
        assertFalse(fetchedItineraries.isEmpty());
        assertTrue(fetchedItineraries.size() >= 2);
        assertEquals("itinerary mark 1", fetchedItineraries.get(0).getItineraryMark());
        assertEquals("itinerary mark 2", fetchedItineraries.get(1).getItineraryMark());

        //CleanUp
        itineraryRepository.deleteById(id1);
        itineraryRepository.deleteById(id2);
    }

    @Test
    void testSaveUpdate() {
        //Given
        Itinerary itinerary3 = new Itinerary("itinerary mark 3", 199.99, null, "link 3");
        itineraryRepository.save(itinerary3);
        Long id3 = itinerary3.getItineraryId();

        //When
        itinerary3.setPrice(1000.00);
        Itinerary updatedItinerary = itineraryRepository.save(itinerary3);

        //Then
        assertEquals(1000.00, updatedItinerary.getPrice());

        //CleanUp
        itineraryRepository.deleteById(id3);
    }

    @Test
    void testFindById() {
        //Given
        Itinerary itinerary4 = new Itinerary("itinerary mark 4", 199.99, null, "link 4");
        itineraryRepository.save(itinerary4);
        Long id4 = itinerary4.getItineraryId();

        //When
        Optional<Itinerary> fetchedItinerary = itineraryRepository.findById(id4);

        //Then
        assertTrue(fetchedItinerary.isPresent());
        assertEquals("itinerary mark 4", fetchedItinerary.get().getItineraryMark());

        //CleanUp
        itineraryRepository.deleteById(id4);
    }

    @Test
    void testDeleteById() {
        //Given
        Itinerary itinerary5 = new Itinerary("itinerary mark 5", 199.99, null, "link 5");
        itineraryRepository.save(itinerary5);
        Long id5 = itinerary5.getItineraryId();

        //When
        itineraryRepository.deleteById(id5);
        Optional<Itinerary> fetchedItinerary = itineraryRepository.findById(id5);

        //Then
        assertFalse(fetchedItinerary.isPresent());

        //CleanUp
        try {
            itineraryRepository.deleteById(id5);
        } catch (Exception e) {

        }
    }

    @Test
    void testExistsByItineraryMark() {
        //Given
        Itinerary itinerary6 = new Itinerary("itinerary mark 6", 199.99, null, "link 6");
        itineraryRepository.save(itinerary6);
        Long id6 = itinerary6.getItineraryId();

        //When & Then
        assertTrue(itineraryRepository.existsByItineraryMark("itinerary mark 6"));

        //CleanUp
        itineraryRepository.deleteById(id6);
    }

    @Test
    void testExistsById() {
        //Given
        Itinerary itinerary = new Itinerary("itinerary mark", 199.99, null, "link");
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
package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AirportRepositoryTestSuite {
    @Autowired
    AirportRepository airportRepository;

    @Test
    void testFindAll() {
        //Given
        Airport airport1 = new Airport(
                "Country1",
                "City1",
                "IATA1"
        );
        Airport airport2 = new Airport(
                "Country2",
                "City2",
                "IATA2"
        );
        airportRepository.save(airport1);
        airportRepository.save(airport2);
        Long id1 = airport1.getAirportId();
        Long id2 = airport2.getAirportId();

        //When
        List<Airport> fetchedAirports = airportRepository.findAll();

        //Then
        assertFalse(fetchedAirports.isEmpty());
        assertEquals(2, fetchedAirports.size());

        //CleanUp
        airportRepository.deleteById(id1);
        airportRepository.deleteById(id2);
    }

    @Test
    void testSave() {
        //Given
        Airport airport = new Airport(
                "Country_testSave",
                "City_testSave",
                "IATA_testSave"
        );

        //When
        Optional<Airport> savedAirport = Optional.of(airportRepository.save(airport));
        Long id = airport.getAirportId();

        //Then
        assertTrue(savedAirport.isPresent());
        assertEquals(airport.getAirportId(), savedAirport.get().getAirportId());
        assertEquals(airport.getCountry(), savedAirport.get().getCountry());
        assertEquals(airport.getCity(), savedAirport.get().getCity());
        assertEquals(airport.getIataCode(), savedAirport.get().getIataCode());

        //CleanUp
        airportRepository.deleteById(id);
    }

    @Test
    void testFindById() {
        //Given
        Airport airport = new Airport(
                "Country_testFindById",
                "City_testFindById",
                "IATA_testFindById"
        );
        airportRepository.save(airport);
        Long id = airport.getAirportId();

        //When
        Optional<Airport> fetchedAirport = airportRepository.findById(id);

        //Then
        assertTrue(fetchedAirport.isPresent());
        assertEquals(airport.getAirportId(), fetchedAirport.get().getAirportId());
        assertEquals(airport.getCountry(), fetchedAirport.get().getCountry());
        assertEquals(airport.getCity(), fetchedAirport.get().getCity());
        assertEquals(airport.getIataCode(), fetchedAirport.get().getIataCode());

        //CleanUp
        airportRepository.deleteById(id);
    }
    @Test
    void testFindById_notExisting() {
        //Given
        Long id = 9999L;

        //When
        Optional<Airport> fetchedAirport = airportRepository.findById(id);

        //Then
        assertFalse(fetchedAirport.isPresent());

        //CleanUp
        try {
            airportRepository.deleteById(id);
        } catch (Exception e) {

        }
    }

    @Test
    void testDeleteById() {
        //Given
        Airport airport = new Airport(
                "Country_testDeleteById",
                "City_testDeleteById",
                "IATA_testDeleteById"
        );
        airportRepository.save(airport);
        Long id = airport.getAirportId();

        //When
        airportRepository.deleteById(id);
        Optional<Airport> fetchedAirport = airportRepository.findById(id);

        //Then
        assertFalse(fetchedAirport.isPresent());

        //CleanUp
        try {
            airportRepository.deleteById(id);
        } catch (Exception e) {

        }
    }

    @Test
    void testFindByCountry() {
        //Given
        Airport airport1 = new Airport(
                "Country",
                "City1_testFindByCountry",
                "IATA1_testFindByCountry"
        );
        Airport airport2 = new Airport(
                "Country",
                "City2_testFindByCountry",
                "IATA2_testFindByCountry"
        );
        Airport airport3 = new Airport(
                "Other Country",
                "City3_testFindByCountry",
                "IATA3_testFindByCountry"
        );
        airportRepository.save(airport1);
        airportRepository.save(airport2);
        airportRepository.save(airport3);
        Long id1 = airport1.getAirportId();
        Long id2 = airport2.getAirportId();
        Long id3 = airport3.getAirportId();

        //When
        List<Airport> fetchedAirports = airportRepository.findByCountry("Country");

        //Then
        assertFalse(fetchedAirports.isEmpty());
        assertEquals(2, fetchedAirports.size());

        //CleanUp
        airportRepository.deleteById(id1);
        airportRepository.deleteById(id2);
        airportRepository.deleteById(id3);
    }

    @Test
    void testFindByCity() {
        //Given
        Airport airport1 = new Airport(
                "Country1_testFindByCity",
                "City",
                "IATA1_testFindByCity"
        );
        Airport airport2 = new Airport(
                "Country2_testFindByCity",
                "City",
                "IATA2_testFindByCity"
        );
        Airport airport3 = new Airport(
                "Country3_testFindByCity",
                "Other City",
                "IATA3_testFindByCity"
        );
        airportRepository.save(airport1);
        airportRepository.save(airport2);
        airportRepository.save(airport3);
        Long id1 = airport1.getAirportId();
        Long id2 = airport2.getAirportId();
        Long id3 = airport3.getAirportId();

        //When
        List<Airport> fetchedAirports = airportRepository.findByCity("City");

        //Then
        assertFalse(fetchedAirports.isEmpty());
        assertEquals(2, fetchedAirports.size());

        //CleanUp
        airportRepository.deleteById(id1);
        airportRepository.deleteById(id2);
        airportRepository.deleteById(id3);
    }

    @Test
    void testFindByIataCode() {
        //Given
        Airport airport1 = new Airport(
                "Country1",
                "City1",
                "IATA1"
        );
        Airport airport2 = new Airport(
                "Country2",
                "City2",
                "IATA2"
        );
        Airport airport3 = new Airport(
                "Country3",
                "City3",
                "IATA3"
        );
        airportRepository.save(airport1);
        airportRepository.save(airport2);
        airportRepository.save(airport3);
        Long id1 = airport1.getAirportId();
        Long id2 = airport2.getAirportId();
        Long id3 = airport3.getAirportId();

        //When
        Optional<Airport> fetchedAirport = airportRepository.findByIataCode("IATA1");

        //Then
        assertTrue(fetchedAirport.isPresent());
        assertEquals(airport1.getAirportId(), fetchedAirport.get().getAirportId());
        assertEquals(airport1.getCountry(), fetchedAirport.get().getCountry());
        assertEquals(airport1.getCity(), fetchedAirport.get().getCity());
        assertEquals(airport1.getIataCode(), fetchedAirport.get().getIataCode());

        //CleanUp
        airportRepository.deleteById(id1);
        airportRepository.deleteById(id2);
        airportRepository.deleteById(id3);
    }
}
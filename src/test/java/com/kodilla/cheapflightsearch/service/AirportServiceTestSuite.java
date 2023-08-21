package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.weather.Weather;
import com.kodilla.cheapflightsearch.exception.AirportNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class AirportServiceTestSuite {
    @Autowired
    AirportService airportService;

    @Test
    void testGetAirports() {
        //Given
        Airport airport1 = new Airport("Poland1", "Warsaw1", "WAW1");
        Airport airport2 = new Airport("Poland1", "Warsaw1", "WMI1");
        airportService.createAirport(airport1);
        airportService.createAirport(airport2);
        Long id1 = airport1.getAirportId();
        Long id2 = airport2.getAirportId();

        //When
        List<Airport> airportList = airportService.getAirports();

        //Then
        assertFalse(airportList.isEmpty());
        assertTrue(airportList.size() >= 2);
        assertTrue(airportList.contains(airport1));
        assertTrue(airportList.contains(airport2));

        //CleanUp
        try {
            airportService.deleteAirport(id1);
            airportService.deleteAirport(id2);
        } catch (Exception e) {

        }
    }

    @Test
    void testGetAirport() {
        //Given
        Airport airport3 = new Airport("Germany", "Cologne", "CGN");
        airportService.createAirport(airport3);
        Long id3 = airport3.getAirportId();

        //When
        Airport fetchedAirport = null;
        try {
            fetchedAirport = airportService.getAirport(id3);
        } catch (Exception e) {

        }

        //Then
        assertDoesNotThrow(() -> airportService.getAirport(id3));
        assertEquals(airport3, fetchedAirport);

        //CleanUp
        try {
            airportService.deleteAirport(id3);
        } catch (Exception e) {

        }
    }

    @Test
    void testGetAirport_NotExisting() {
        //Given & When & Then
        assertThrows(AirportNotFoundException.class, () -> airportService.getAirport(999999L));
    }

    @Test
    void testGetAirportByIata() {
        //Given
        Airport airport4 = new Airport("Greece", "Corfu", "CFU");
        airportService.createAirport(airport4);
        Long id4 = airport4.getAirportId();

        //When
        Airport fetchedAirport = null;
        try {
            fetchedAirport = airportService.getAirportByIata("CFU");
        } catch (Exception e) {

        }

        //Then
        assertDoesNotThrow(() -> airportService.getAirportByIata("CFU"));
        assertEquals(airport4, fetchedAirport);

        //CleanUp
        try {
            airportService.deleteAirport(id4);
        } catch (Exception e) {

        }
    }

    @Test
    void testGetAirportByIata_NotExisting() {
        //Given & When & Then
        assertThrows(AirportNotFoundException.class, () -> airportService.getAirportByIata("iata_not_existing"));
    }

    @Test
    void testDeleteAirport() {
        //Given
        Airport airport5 = new Airport("Greece", "Crete", "CHA");
        airportService.createAirport(airport5);
        Long id5 = airport5.getAirportId();

        //When & Then
        assertDoesNotThrow(() -> airportService.deleteAirport(id5));

        //CleanUp
        try {
            airportService.deleteAirport(id5);
        } catch (Exception e) {

        }
    }

    @Test
    void testDeleteAirport_NotExisting() {
        //Given & When & Then
        assertThrows(AirportNotFoundException.class, () -> airportService.deleteAirport(99999L));
    }

    @Test
    void testUpdateAirport() {
        //Given
        Airport airport6 = new Airport("Włochy", "Bari", "BRI");
        airportService.createAirport(airport6);
        Long id6 = airport6.getAirportId();

        //When
        airport6.setCountry("Italy");
        Airport airportUpdated = null;
        try {
            airportService.updateAirport(id6, airport6);
            airportUpdated = airportService.getAirport(id6);

        } catch (Exception e) {

        }

        //Then
        assertDoesNotThrow(() -> airportService.updateAirport(id6, airport6));
        assertEquals("Italy", airportUpdated.getCountry());

        //CleanUp
        try {
            airportService.deleteAirport(id6);
        } catch (Exception e) {

        }
    }

    @Test
    void testCreateAirport() {
        //Given
        Airport airport7 = new Airport("Portugal", "Faro", "FAO");

        //When
        airportService.createAirport(airport7);
        Long id7 = airport7.getAirportId();
        Airport fetchedAirport = null;
        try {
            fetchedAirport = airportService.getAirport(id7);
        } catch (Exception e) {

        }

        //Then
        assertDoesNotThrow(() -> airportService.getAirport(id7));
        assertEquals(airport7, fetchedAirport);

        //CleanUp
        try {
            airportService.deleteAirport(id7);
        } catch (Exception e) {

        }
    }

    @Test
    void testGetAirportsByCity() {
        //Given
        Airport airport8 = new Airport("Poland2", "Warsaw2", "WAW2");
        Airport airport9 = new Airport("Poland2", "Warsaw2", "WMI2");
        airportService.createAirport(airport8);
        airportService.createAirport(airport9);
        Long id8 = airport8.getAirportId();
        Long id9 = airport9.getAirportId();

        //When
        List<Airport> airportList = airportService.getAirportsByCity("Warsaw2");

        //Then
        assertFalse(airportList.isEmpty());
        assertEquals(2, airportList.size());

        //CleanUp
        try {
            airportService.deleteAirport(id8);
            airportService.deleteAirport(id9);
        } catch (Exception e) {

        }
    }

    @Test
    void testGetAirportsByCountry() {
        //Given
        Airport airport10 = new Airport("Poland3", "Warsaw3", "WAW3");
        Airport airport11 = new Airport("Poland3", "Warsaw3", "WMI3");
        airportService.createAirport(airport10);
        airportService.createAirport(airport11);
        Long id10 = airport10.getAirportId();
        Long id11 = airport11.getAirportId();

        //When
        List<Airport> airportListByCountry = airportService.getAirportsByCountry("Poland3");

        //Then
        assertFalse(airportListByCountry.isEmpty());
        assertEquals(2, airportListByCountry.size());

        //CleanUp
        try {
            airportService.deleteAirport(id10);
            airportService.deleteAirport(id11);
        } catch (Exception e) {

        }
    }

    @Test
    void testCheckIfAirportExistsByIata() {
        //Given
        Airport airport12 = new Airport("Portugal", "Lisbon", "LIS");
        airportService.createAirport(airport12);
        Long id12 = airport12.getAirportId();

        //When & Then
        assertTrue(airportService.checkIfAirportExistsByIata("LIS"));

        //CleanUp
        try {
            airportService.deleteAirport(id12);
        } catch (Exception e) {

        }
    }
    @Test
    void testCheckIfAirportExistsByIata_NotExisting() {
        //Given & When & Then
        assertFalse(airportService.checkIfAirportExistsByIata("iata_not_existing"));
    }
    @Test
    void testGetWeatherForAirport() {
        //Given
        Airport airport13 = new Airport("Portugal", "Porto", "OPO");

        //When
        Optional<String> fetchedTemperature = Optional.ofNullable(airportService.getWeatherForAirport(airport13));

        //Then
        assertTrue(fetchedTemperature.isPresent());
    }
}
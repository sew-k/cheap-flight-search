package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RouteRepositoryTestSuite {
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    AirportRepository airportRepository;
    @Test
    void testFindAll() {
        //Given
        Airport originTestAirport1 = new Airport("TestCountryOrigin1", "TestCityOrigin1", "TestIATAOrigin1");
        airportRepository.save(originTestAirport1);
        Long originTestAirport1Id = originTestAirport1.getAirportId();
        Airport destinationTestAirport1 = new Airport("TestCountryDestination1", "TestCityDestination1", "TestIATADestination1");
        airportRepository.save(destinationTestAirport1);
        Long destinationTestAirport1Id = destinationTestAirport1.getAirportId();
        Set<DayOfWeek> dayOfWeekSet1 = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        Route route1 = new Route(originTestAirport1, destinationTestAirport1, dayOfWeekSet1, false);
        Airport originTestAirport2 = new Airport("TestCountryOrigin2", "TestCityOrigin2", "TestIATAOrigin2");
        airportRepository.save(originTestAirport2);
        Long originTestAirport2Id = originTestAirport1.getAirportId();
        Airport destinationTestAirport2 = new Airport("TestCountryDestination2", "TestCityDestination2", "TestIATADestination2");
        airportRepository.save(destinationTestAirport2);
        Long destinationTestAirport2Id = destinationTestAirport1.getAirportId();
        Set<DayOfWeek> dayOfWeekSet2 = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        Route route2 = new Route(originTestAirport2, destinationTestAirport2, dayOfWeekSet2, false);
        routeRepository.save(route1);
        routeRepository.save(route2);
        Long routeId1 = route1.getRouteId();
        Long routeId2 = route2.getRouteId();

        //When
        List<Route> resultList = routeRepository.findAll();

        //Then
        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());

        //CleanUp
        try {
            routeRepository.deleteById(routeId1);
            routeRepository.deleteById(routeId2);
            airportRepository.deleteById(originTestAirport1Id);
            airportRepository.deleteById(destinationTestAirport1Id);
            airportRepository.deleteById(originTestAirport2Id);
            airportRepository.deleteById(destinationTestAirport2Id);
        } catch (Exception e) {
            System.out.println("Exception when tear down: " + e);
        }
    }

    @Test
    void testSave() {
        //Given
        Airport originTestAirport = new Airport("testSaveCountryOrigin", "testSaveCityOrigin", "testSaveIATAOrigin");
        airportRepository.save(originTestAirport);
        Long originTestAirportId = originTestAirport.getAirportId();
        Airport destinationTestAirport = new Airport("testSaveCountryDestination", "testSaveCityDestination", "testSaveIATADestination");
        airportRepository.save(destinationTestAirport);
        Long destinationTestAirportId = destinationTestAirport.getAirportId();
        Set<DayOfWeek> dayOfWeekSet = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        Route route = new Route(originTestAirport, destinationTestAirport, dayOfWeekSet, false);

        //When
        routeRepository.save(route);
        Long routeId = route.getRouteId();

        //Then
        assertTrue(routeRepository.findById(routeId).isPresent());
        assertEquals("testSaveIATAOrigin", routeRepository.findById(routeId).get().getOrigin().getIataCode());

        //CleanUp
        routeRepository.deleteById(routeId);
        airportRepository.deleteById(originTestAirportId);
        airportRepository.deleteById(destinationTestAirportId);
    }

    @Test
    void testFindById() {
        //Given
        Airport originTestAirport = new Airport("testFindByIdCountryOrigin", "testFindByIdCityDestination", "testFindByIdIataOrigin");
        airportRepository.save(originTestAirport);
        Long originTestAirportId = originTestAirport.getAirportId();
        Airport destinationTestAirport = new Airport("testFindByIdCountryDestination", "testFindByIdCityDestination", "testFindByIdIataDestination");
        airportRepository.save(destinationTestAirport);
        Long destinationTestAirportId = destinationTestAirport.getAirportId();
        Set<DayOfWeek> dayOfWeekSet = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        Route route = new Route(originTestAirport, destinationTestAirport, dayOfWeekSet, false);
        routeRepository.save(route);
        Long routeId = route.getRouteId();

        //When & Then
        assertTrue(routeRepository.findById(routeId).isPresent());
        assertEquals("testFindByIdCountryOrigin", routeRepository.findById(routeId).get().getOrigin().getCountry());
        assertEquals("testFindByIdCityDestination", routeRepository.findById(routeId).get().getOrigin().getCity());
        assertEquals("testFindByIdIataOrigin", routeRepository.findById(routeId).get().getOrigin().getIataCode());

        //CleanUp
        routeRepository.deleteById(routeId);
        airportRepository.deleteById(originTestAirportId);
        airportRepository.deleteById(destinationTestAirportId);
    }

    @Test
    void testDeleteById() {
        //Given
        Airport originTestAirport = new Airport("testDeleteByIdCountryOrigin", "testDeleteByIdCityOrigin", "testDeleteByIdIATAOrigin");
        airportRepository.save(originTestAirport);
        Long originTestAirportId = originTestAirport.getAirportId();
        Airport destinationTestAirport = new Airport("testDeleteByIdCountryDestination", "testDeleteByIdCityDestination", "testDeleteByIdIATADestination");
        airportRepository.save(destinationTestAirport);
        Long destinationTestAirportId = destinationTestAirport.getAirportId();
        Set<DayOfWeek> dayOfWeekSet = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        Route route = new Route(originTestAirport, destinationTestAirport, dayOfWeekSet, false);
        routeRepository.save(route);
        Long routeId = route.getRouteId();

        //When
        routeRepository.deleteById(routeId);

        //Then
        assertFalse(routeRepository.findById(routeId).isPresent());

        //CleanUp
        airportRepository.deleteById(originTestAirportId);
        airportRepository.deleteById(destinationTestAirportId);
        try {
            routeRepository.deleteById(routeId);
        } catch (Exception e) {
            System.out.println("Exception when tear down: " + e);
        }
    }
    @Test
    void testDeleteByIdAirportsShouldRemain() {
        //Given
        Airport originTestAirport = new Airport("testDeleteByIdCountryOrigin", "testDeleteByIdCityOrigin", "testDeleteByIdIATAOrigin");
        airportRepository.save(originTestAirport);
        Long originTestAirportId = originTestAirport.getAirportId();
        Airport destinationTestAirport = new Airport("testDeleteByIdCountryDestination", "testDeleteByIdCityDestination", "testDeleteByIdIATADestination");
        airportRepository.save(destinationTestAirport);
        Long destinationTestAirportId = destinationTestAirport.getAirportId();
        Set<DayOfWeek> dayOfWeekSet = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        Route route = new Route(originTestAirport, destinationTestAirport, dayOfWeekSet, false);
        routeRepository.save(route);
        Long routeId = route.getRouteId();

        //When
        routeRepository.deleteById(routeId);

        //Then
        assertFalse(routeRepository.findById(routeId).isPresent());
        assertTrue(airportRepository.existsById(originTestAirportId));
        assertTrue(airportRepository.existsById(destinationTestAirportId));

        //CleanUp
        airportRepository.deleteById(originTestAirportId);
        airportRepository.deleteById(destinationTestAirportId);
        try {
            routeRepository.deleteById(routeId);
        } catch (Exception e) {
            System.out.println("Exception when tear down: " + e);
        }
    }

    @Test
    void testFindByOrigin() {
        //Given
        Airport originTestAirport = new Airport("testFindByOriginCountryOrigin", "testFindByOriginCityOrigin", "testFindByOriginIATAOrigin");
        airportRepository.save(originTestAirport);
        Long originTestAirportId = originTestAirport.getAirportId();
        Airport destinationTestAirport = new Airport("testFindByOriginCountryDestination", "testFindByOriginCityDestination", "testFindByOriginIATADestination");
        airportRepository.save(destinationTestAirport);
        Long destinationTestAirportId = destinationTestAirport.getAirportId();
        Set<DayOfWeek> dayOfWeekSet = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        Route route = new Route(originTestAirport, destinationTestAirport, dayOfWeekSet, false);
        routeRepository.save(route);
        Long routeId = route.getRouteId();

        //When & Then
        assertTrue(routeRepository.findByOrigin(originTestAirport).isPresent());

        //CleanUp
        routeRepository.deleteById(routeId);
        airportRepository.deleteById(originTestAirportId);
        airportRepository.deleteById(destinationTestAirportId);
    }

    @Test
    void testFindByDestination() {
        //Given
        Airport originTestAirport = new Airport("testFindByDestinationCountryOrigin", "testFindByDestinationCityOrigin", "testFindByDestinationIATAOrigin");
        airportRepository.save(originTestAirport);
        Long originTestAirportId = originTestAirport.getAirportId();
        Airport destinationTestAirport = new Airport("testFindByDestinationCountryDestination", "testFindByDestinationCityDestination", "testFindByDestinationIATADestination");
        airportRepository.save(destinationTestAirport);
        Long destinationTestAirportId = destinationTestAirport.getAirportId();
        Set<DayOfWeek> dayOfWeekSet = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        Route route = new Route(originTestAirport, destinationTestAirport, dayOfWeekSet, false);
        routeRepository.save(route);
        Long routeId = route.getRouteId();

        //When & Then
        assertTrue(routeRepository.findByDestination(destinationTestAirport).isPresent());

        //CleanUp
        routeRepository.deleteById(routeId);
        airportRepository.deleteById(originTestAirportId);
        airportRepository.deleteById(destinationTestAirportId);
    }
}
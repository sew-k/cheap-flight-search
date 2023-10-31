package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTestSuite {
    @InjectMocks
    RouteService routeService;
    @Mock
    RouteRepository routeRepository;

    @Test
    void testGetRoutes() {
        //Given
        Route route1 = new Route();
        Route route2 = new Route();
        when(routeRepository.findAll()).thenReturn(List.of(route1, route2));

        //When
        List<Route> fetchedRoutes = routeService.getRoutes();

        //Then
        verify(routeRepository, atLeastOnce()).findAll();
        assertFalse(fetchedRoutes.isEmpty());
        assertEquals(2, fetchedRoutes.size());
    }

    @Test
    void getRoute() {
    }

    @Test
    void deleteRoute() {
    }

    @Test
    void updateRoute() {
    }

    @Test
    void createRoute() {
    }

    @Test
    void getRouteByOrigin() {
    }

    @Test
    void getRouteByDestination() {
    }

    @Test
    void getFavouriteRoutes() {
    }

    @Test
    void switchFavourite() {
    }

    @Test
    void getRoutesByUserId() {
    }
}
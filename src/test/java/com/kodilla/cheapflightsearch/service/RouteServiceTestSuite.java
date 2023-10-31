package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.exception.RouteNotFoundException;
import com.kodilla.cheapflightsearch.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
    void testGetRoute() throws Exception {
        //Given
        Route route3 = new Route();
        Long id = 3L;
        when(routeRepository.findById(id)).thenReturn(Optional.of(route3));

        //When
        Route fetchedRoute = routeService.getRoute(id);

        //Then
        assertDoesNotThrow(() -> routeService.getRoute(id));
        verify(routeRepository, atLeastOnce()).findById(id);
        assertEquals(route3, fetchedRoute);
    }

    @Test
    void testGetRoute_notExisting() throws Exception {
        //Given
        Long id = 4L;

        //When & Then
        assertThrows(RouteNotFoundException.class, () -> routeService.getRoute(id));
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
package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.user.User;
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
    void deleteRoute() throws Exception {
        //Given
        Route route5 = new Route();
        Long id = 5L;
        when(routeRepository.findById(id)).thenReturn(Optional.of(route5));

        //When
        routeService.deleteRoute(id);

        //Then
        assertDoesNotThrow(() -> routeService.deleteRoute(id));
        verify(routeRepository, atLeastOnce()).deleteById(id);
    }

    @Test
    void deleteRoute_notExisting() throws Exception {
        //Given
        Long id = 6L;

        //When & Then
        assertThrows(RouteNotFoundException.class, () -> routeService.deleteRoute(id));
    }

    @Test
    void updateRoute() throws Exception {
        //Given
        Route route7 = new Route();
        Long idToUpdate = 7L;
        Route route8 = new Route();
        Long idUpdated = 8L;
        when(routeRepository.findById(idToUpdate)).thenReturn(Optional.of(route7));
        when(routeRepository.save(route7)).thenReturn(route8);

        //When
        Route updatedRoute = routeService.updateRoute(idToUpdate, route7);

        //Then
        assertDoesNotThrow(() -> routeService.updateRoute(idToUpdate, route7));
        verify(routeRepository, atLeastOnce()).save(route7);
        assertEquals(route8, updatedRoute);
    }

    @Test
    void updateeRoute_notExisting() throws Exception {
        //Given
        Long id = 9L;

        //When & Then
        assertThrows(RouteNotFoundException.class, () -> routeService.updateRoute(id, new Route()));
    }

    @Test
    void createRoute() {
        //Given
        Route route10 = new Route();
        Long id = 10L;
        when(routeRepository.save(route10)).thenReturn(route10);

        //When
        Route createdRoute = routeService.createRoute(route10);

        //Then
        verify(routeRepository, atLeastOnce()).save(route10);
        assertEquals(route10, createdRoute);
    }

    @Test
    void getFavouriteRoutes() {
        //Given
        Route route11 = new Route();
        route11.setFavourite(true);
        Route route12 = new Route();
        route12.setFavourite(false);
        Route route13 = new Route();
        route13.setFavourite(true);
        List<Route> routes = List.of(route11, route12, route13);
        when(routeRepository.findAll()).thenReturn(routes);

        //When
        List<Route> fetchedRoutes = routeService.getFavouriteRoutes();

        //Then
        verify(routeRepository, atLeastOnce()).findAll();
        assertFalse(fetchedRoutes.isEmpty());
        assertEquals(2, fetchedRoutes.size());
        assertTrue(fetchedRoutes.contains(route11));
        assertTrue(fetchedRoutes.contains(route13));
    }

    @Test
    void switchFavourite() {
        //Given
        Route route14 = new Route();
        route14.setFavourite(false);
        when(routeRepository.save(route14)).thenReturn(route14);

        //When
        routeService.switchFavourite(route14);

        //Then
        verify(routeRepository, atLeastOnce()).save(route14);
        assertTrue(route14.isFavourite());
    }

    @Test
    void getRoutesByUserId() {
        //Given
        User user1 = new User();
        user1.setUserId(19L);
        Route route15 = new Route();
        route15.setUser(user1);
        Route route16 = new Route();
        route16.setUser(user1);
        User user2 = new User();
        user2.setUserId(20L);
        Route route17 = new Route();
        route17.setUser(user2);
        List<Route> routes = List.of(route15, route16, route17);
        when(routeRepository.findAll()).thenReturn(routes);

        //When
        List<Route> fetchedRoutes = routeService.getRoutesByUserId(19L);

        //Then
        verify(routeRepository, atLeastOnce()).findAll();
        assertFalse(fetchedRoutes.isEmpty());
        assertEquals(2, fetchedRoutes.size());
        assertTrue(fetchedRoutes.contains(route15));
        assertTrue(fetchedRoutes.contains(route16));
    }
}
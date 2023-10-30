package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.domain.trip.RouteDto;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserRole;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteMapperTestSuite {
    @InjectMocks
    RouteMapper routeMapper;
    @Mock
    UserService userService;
    @Mock
    AirportService airportService;
    private static Route route;
    private static RouteDto routeDto;
    private static Airport originAirport;
    private static Airport destinationAirport;
    private static User user;

    @BeforeAll
    static void setUp() {
        originAirport = new Airport(
                "Origin country",
                "Origin city",
                "Origin IATA"
        );
        destinationAirport = new Airport(
                "Destination country",
                "Destination city",
                "Destination IATA"
        );
        user = new User(
                91L,
                "username",
                "userEmail",
                UserRole.USER,
                "userPassword",
                new Calendar()
        );
        route = new Route(
                99L,
                originAirport,
                destinationAirport,
                Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                false,
                user
        );
        routeDto = RouteDto.builder()
                .routeId(99L)
                .origin("Origin IATA")
                .destination("Destination IATA")
                .daysOfWeek(Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY))
                .favourite(false)
                .userId(91L)
                .build();
    }

    @Test
    void testMapToRoute() throws Exception {
        //Given
        when(userService.getUser(91L)).thenReturn(user);
        when(airportService.getAirportByIata("Origin IATA")).thenReturn(originAirport);
        when(airportService.getAirportByIata("Destination IATA")).thenReturn(destinationAirport);

        //When
        Route resultRoute = routeMapper.mapToRoute(routeDto);

        //Then
        assertEquals(route.getRouteId(), resultRoute.getRouteId());
        assertEquals(route.getOrigin(), resultRoute.getOrigin());
        assertEquals(route.getDestination(), resultRoute.getDestination());
        assertEquals(route.getDaysOfWeek(), resultRoute.getDaysOfWeek());
        assertEquals(route.isFavourite(), resultRoute.isFavourite());
        assertEquals(route.getUser(), resultRoute.getUser());
    }

    @Test
    void testMapToRouteDto() {
        //Given & When
        RouteDto resultRouteDto = routeMapper.mapToRouteDto(route);

        //Then
        assertEquals(routeDto.getRouteId(), resultRouteDto.getRouteId());
        assertEquals(routeDto.getOrigin(), resultRouteDto.getOrigin());
        assertEquals(routeDto.getDestination(), resultRouteDto.getDestination());
        assertEquals(
                new HashSet<>(routeDto.getDaysOfWeek()),
                new HashSet<>(resultRouteDto.getDaysOfWeek())
        );
        assertEquals(routeDto.isFavourite(), resultRouteDto.isFavourite());
        assertEquals(routeDto.getUserId(), resultRouteDto.getUserId());
    }

    @Test
    void testMapToRouteDtoList() {
        //Given
        List<Route> routeList = new ArrayList<>();
        routeList.add(route);

        //When
        List<RouteDto> routeDtoList = routeMapper.mapToRouteDtoList(routeList);

        //Then
        assertFalse(routeDtoList.isEmpty());
        assertEquals(1, routeDtoList.size());
        assertEquals(routeDto, routeDtoList.get(0));
    }
}
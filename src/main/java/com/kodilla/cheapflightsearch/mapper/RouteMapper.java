package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.trip.*;
import com.kodilla.cheapflightsearch.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteMapper {
    @Autowired
    AirportService airportService;

    public Route mapToRoute(final RouteDto routeDto) throws Exception {
        return new Route(
                routeDto.getRouteId(),
                airportService.getAirportByIata(routeDto.getOrigin()),
                airportService.getAirportByIata(routeDto.getDestination()),
                routeDto.getDaysOfWeek().stream().collect(Collectors.toSet()),
                false
        );
    }
    public RouteDto mapToRouteDto(final Route route) {
        return RouteDto.builder()
                .routeId(route.getRouteId())
                .origin(route.getOrigin().getIataCode())
                .destination(route.getDestination().getIataCode())
                .daysOfWeek(route.getDaysOfWeek().stream().toList())
                .build();
    }
    public List<RouteDto> mapToRouteDtoList(final List<Route> routeList) {
        return routeList.stream().map(this::mapToRouteDto).collect(Collectors.toList());
    }
}

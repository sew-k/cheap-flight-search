package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.trip.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteMapper {

    public Route mapToRoute(final RouteDto routeDto) {
        return new Route(
                routeDto.getRouteId(),
                routeDto.getOrigin(),
                routeDto.getDestination(),
                routeDto.getDaysOfWeek(),
                routeDto.getAirline()
        );
    }
    public RouteDto mapToRouteDto(final Route route) {
        return new RouteDto(
                route.getRouteId(),
                route.getOrigin(),
                route.getDestination(),
                route.getDaysOfWeek(),
                route.getAirline()
        );
    }
    public List<RouteDto> mapToRouteDtoList(final List<Route> routeList) {
        return routeList.stream().map(this::mapToRouteDto).collect(Collectors.toList());
    }
    public List<Route> mapToRouteList(final List<RouteDto> routeDtoList) {
        return routeDtoList.stream().map(this::mapToRoute).collect(Collectors.toList());
    }
}

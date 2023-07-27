package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.trip.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteMapper {

    public Route mapToRoute(final RouteDto routeDto) {  //TODO - mapping correct values
//        List<Day> days = new ArrayList<>();
//        for (String dayName: routeDto.getDays()) {
//            days.add(new Day(DayOfWeek.valueOf(dayName)));
//        }
//        return new Route(
//                new Origin(new Airport(routeDto.getOrigin())),
//                new Destination(new Airport(routeDto.getDestination())),
//                days,
//                new Airline(routeDto.getAirline())
//        );
        return null;
    }
    public RouteDto mapToRouteDto(final Route route) {
//        return new RouteDto(
//                route.getRouteId(),
//                route.getOrigin().getAirport().getIataCode(),
//                route.getDestination().getAirport().getIataCode(),
//                route.getDays().stream()
//                        .map(d -> d.getDayName())
//                        .map(d -> d.toString())
//                        .collect(Collectors.toList()),
//                route.getAirline().getAirlineName()
//        );
        return null;
    }
    public List<RouteDto> mapToRouteDtoList(final List<Route> routeList) {
        return routeList.stream().map(this::mapToRouteDto).collect(Collectors.toList());
    }
    public List<Route> mapToRouteList(final List<RouteDto> routeDtoList) {
        return routeDtoList.stream().map(this::mapToRoute).collect(Collectors.toList());
    }
}

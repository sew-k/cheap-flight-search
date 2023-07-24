package com.kodilla.cheapflightsearch.domain.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteDto {
    private Long routeId;
    private Origin origin;
    private Destination destination;
    private List<Day> daysOfWeek;
    private Airline airline;
    public RouteDto(Origin origin, Destination destination, List<Day> daysOfWeek, Airline airline) {
        this.origin = origin;
        this.destination = destination;
        this.daysOfWeek = daysOfWeek;
        this.airline = airline;
    }
}

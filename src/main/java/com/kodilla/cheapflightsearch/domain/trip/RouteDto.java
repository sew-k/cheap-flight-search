package com.kodilla.cheapflightsearch.domain.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteDto {
    private Long routeId;
    private String origin;
    private String destination;
//    private List<String> days;
//    private String airline;
//    public RouteDto(String origin, String destination, List<String> days, String airline) {
//        this.origin = origin;
//        this.destination = destination;
//        this.days = days;
//        this.airline = airline;
//    }
//    public RouteDto(String origin, String destination, List<String> days) {
//        this.origin = origin;
//        this.destination = destination;
//        this.days = days;
//    }
    private List<DayOfWeek> daysOfWeek;
    public RouteDto(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public RouteDto(String origin, String destination, List<DayOfWeek> daysOfWeek) {
        this.origin = origin;
        this.destination = destination;
        this.daysOfWeek = daysOfWeek;
    }
}

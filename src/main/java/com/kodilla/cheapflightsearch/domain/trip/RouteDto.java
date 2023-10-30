package com.kodilla.cheapflightsearch.domain.trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RouteDto {
    private Long routeId;
    private String origin;
    private String destination;
    private Set<DayOfWeek> daysOfWeek;
    private boolean favourite;
    private Long userId;
}

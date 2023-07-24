package com.kodilla.cheapflightsearch.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherDto {
    private float temperature;
}

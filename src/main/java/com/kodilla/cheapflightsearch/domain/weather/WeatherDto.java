package com.kodilla.cheapflightsearch.domain.weather;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherDto {
    private float temperature;
}

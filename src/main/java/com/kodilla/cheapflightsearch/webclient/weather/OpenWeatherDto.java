package com.kodilla.cheapflightsearch.webclient.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenWeatherDto {
    private OpenWeatherMainDto main;
}

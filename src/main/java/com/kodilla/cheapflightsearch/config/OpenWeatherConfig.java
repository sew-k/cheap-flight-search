package com.kodilla.cheapflightsearch.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OpenWeatherConfig {
    @Value("${openweather.api.endpoint.prod}")
    private String openWeatherApiEndpoint;
    @Value("${openweather.api.key}")
    private String openWeatherApiKey;
    public static final String LANG_PL = "pl";
    public static final String LANG_EN = "en";
    public static final String UNITS_METRIC = "metric";
    public static final String UNITS_IMPERIAL = "imperial";
    public static final String CALL_FOR_CURRENT_WEATHER = "weather";
    public static final String CALL_FOR_FORECAST = "forecast";

}

package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.config.OpenWeatherConfig;
import com.kodilla.cheapflightsearch.domain.weather.Weather;
import com.kodilla.cheapflightsearch.domain.weather.WeatherDto;
import com.kodilla.cheapflightsearch.mapper.WeatherMapper;
import com.kodilla.cheapflightsearch.webclient.weather.WeatherClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;
    private final WeatherMapper weatherMapper;
    public Weather getWeather(String city) { //TODO - sparametryzować to zapytanie, dodać klasę Config i Controller
        return weatherMapper.mapToCurrentWeather(weatherClient.getWeatherForCity(
                city,
                OpenWeatherConfig.CALL_FOR_CURRENT_WEATHER,
                OpenWeatherConfig.UNITS_METRIC,
                OpenWeatherConfig.LANG_PL));
    }
}

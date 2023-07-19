package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.weather.WeatherDto;
import com.kodilla.cheapflightsearch.webclient.weather.WeatherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;
    public WeatherDto getWeather() { //TODO - sparametryzować to zapytanie, dodać klasę Config i Controller
        String response = weatherClient.getWeatherForCity("warszawa");
        return null;
    }
}

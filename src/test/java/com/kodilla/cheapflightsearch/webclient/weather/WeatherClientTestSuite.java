package com.kodilla.cheapflightsearch.webclient.weather;

import com.kodilla.cheapflightsearch.config.OpenWeatherConfig;
import com.kodilla.cheapflightsearch.domain.weather.WeatherDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WeatherClientTestSuite {
    @InjectMocks
    WeatherClient weatherClient;
    @Mock
    RestTemplate restTemplate;
    @Mock
    OpenWeatherConfig openWeatherConfig;

    @Test
    void testGetWeatherForCity() {
    }
}
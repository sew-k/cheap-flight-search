package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.config.OpenWeatherConfig;
import com.kodilla.cheapflightsearch.domain.weather.dto.WeatherDto;
import com.kodilla.cheapflightsearch.webclient.weather.WeatherClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTestSuite {
    @InjectMocks
    WeatherService weatherService;
    @Mock
    WeatherClient weatherClient;
    @Test
    void shouldCallWeatherClientMethod() {
        //Given
        WeatherDto weatherDto = WeatherDto.builder()
                .temperature(15.55f)
                .build();
        when(weatherClient.getWeatherForCity(
                "warsaw",
                OpenWeatherConfig.CALL_FOR_CURRENT_WEATHER,
                OpenWeatherConfig.UNITS_METRIC,
                OpenWeatherConfig.LANG_PL))
                .thenReturn(weatherDto);

        //When
        float resultTemp = weatherService.getWeather("warsaw").getTemperature();

        //Then
        verify(weatherClient, atLeastOnce()).getWeatherForCity(
                "warsaw",
                OpenWeatherConfig.CALL_FOR_CURRENT_WEATHER,
                OpenWeatherConfig.UNITS_METRIC,
                OpenWeatherConfig.LANG_PL);
        assertEquals(weatherDto.getTemperature(), resultTemp);
    }

}
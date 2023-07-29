package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.config.OpenWeatherConfig;
import com.kodilla.cheapflightsearch.domain.weather.Weather;
import com.kodilla.cheapflightsearch.domain.weather.WeatherDto;
import com.kodilla.cheapflightsearch.mapper.WeatherMapper;
import com.kodilla.cheapflightsearch.webclient.weather.WeatherClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTestSuite {
    @InjectMocks
    WeatherService weatherService;
    @Mock
    WeatherMapper weatherMapper;
    @Mock
    WeatherClient weatherClient;

    @Test
    void shouldCallWeatherClientMethod() {
        //Given
        WeatherDto weatherDto = WeatherDto.builder()
                .temperature(15.55f)
                .build();
        Weather weather = new Weather(weatherDto.getTemperature());
        when(weatherMapper.mapToCurrentWeather(weatherClient.getWeatherForCity(
                "warsaw",
                OpenWeatherConfig.CALL_FOR_CURRENT_WEATHER,
                OpenWeatherConfig.UNITS_METRIC,
                OpenWeatherConfig.LANG_PL)))
                .thenReturn(weather);

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
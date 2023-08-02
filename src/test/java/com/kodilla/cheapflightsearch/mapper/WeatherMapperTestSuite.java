package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.weather.CurrentWeather;
import com.kodilla.cheapflightsearch.domain.weather.Weather;
import com.kodilla.cheapflightsearch.domain.weather.WeatherDto;
import com.kodilla.cheapflightsearch.webclient.weather.OpenWeatherDto;
import com.kodilla.cheapflightsearch.webclient.weather.OpenWeatherMainDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherMapperTestSuite {
    @Autowired
    WeatherMapper weatherMapper;

    @Test
    void testMapToCurrentWeather() {
        //Given
        WeatherDto weatherDto = WeatherDto.builder()
                .temperature(33.01f)
                .build();

        //When
        Weather resultWeather = weatherMapper.mapToCurrentWeather(weatherDto);

        //Then
        assertEquals(weatherDto.getTemperature(), resultWeather.getTemperature());
    }

    @Test
    void testMapToWeatherDto() {
        //Given
        OpenWeatherDto openWeatherDto = new OpenWeatherDto(new OpenWeatherMainDto(32.9f));

        //When
        WeatherDto resultWeatherDto = weatherMapper.mapToWeatherDto(openWeatherDto);

        //Then
        assertEquals(openWeatherDto.getMain().getTemp(), resultWeatherDto.getTemperature());
    }
}
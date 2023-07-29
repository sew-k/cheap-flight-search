package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.weather.CurrentWeather;
import com.kodilla.cheapflightsearch.domain.weather.Weather;
import com.kodilla.cheapflightsearch.domain.weather.WeatherDto;
import org.springframework.stereotype.Service;

@Service
public class WeatherMapper {
    public Weather mapToCurrentWeather(WeatherDto weatherForCityDto) {
        return new CurrentWeather(weatherForCityDto.getTemperature());
    }
}

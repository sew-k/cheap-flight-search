package com.kodilla.cheapflightsearch.controller;

import com.kodilla.cheapflightsearch.service.WeatherService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cheapflightsearch/weather")
@RequiredArgsConstructor
@CrossOrigin("*")
@Api(tags = "Weather controller")
public class WeatherController {
    private final WeatherService weatherService;
    @GetMapping("/{city}")
    public ResponseEntity<Float> getWeatherByCity(@PathVariable("city") String city) {
        return ResponseEntity.ok(weatherService.getWeather(city).getTemperature());
    }
}

package com.kodilla.cheapflightsearch.webclient.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WeatherClient {
    public static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "c974a4a762d8482ef1987648068de827";
    private final RestTemplate restTemplate;

    public String getWeatherForCity(String city) {
        String response = restTemplate.getForObject(WEATHER_URL +
                "weather?q={city}&units=metric&lang=pl&appid={apiKey}", String.class, city, API_KEY);
        return response;
    }
    public String getForecastForCity(String city) {
        String response = restTemplate.getForObject(WEATHER_URL +
                "forecast?q={city}&units=metric&lang=pl&appid={apiKey}", String.class, city, API_KEY);
        return response;
    }
}

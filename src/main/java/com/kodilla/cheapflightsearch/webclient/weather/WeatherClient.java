package com.kodilla.cheapflightsearch.webclient.weather;

import com.kodilla.cheapflightsearch.config.OpenWeatherConfig;
import com.kodilla.cheapflightsearch.domain.weather.WeatherDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WeatherClient {
    private final OpenWeatherConfig openWeatherConfig;
    private final RestTemplate restTemplate;

    public OpenWeatherDto getWeatherForCity(String city,
                                        String weatherCallType,
                                        String weatherUnits,
                                        String weatherLang) {
        return restTemplate.getForObject(
                openWeatherConfig.getOpenWeatherApiEndpoint()
                + "/{weathercalltype}?q={city}&units={units}&lang={lang}&appid={apiKey}",
                OpenWeatherDto.class,
                weatherCallType,
                city,
                weatherUnits,
                weatherLang,
                openWeatherConfig.getOpenWeatherApiKey());
    }
}

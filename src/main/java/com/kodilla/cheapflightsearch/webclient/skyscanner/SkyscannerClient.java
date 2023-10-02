package com.kodilla.cheapflightsearch.webclient.skyscanner;

import com.kodilla.cheapflightsearch.config.SkyscannerConfig;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.mapper.SkyscannerMapper;
import com.kodilla.cheapflightsearch.webclient.skyscanner.responsedata.SkyscannerItineraryCreateDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SkyscannerClient {
    private final RestTemplate restTemplate;
    private final SkyscannerConfig skyscannerConfig;
    private final SkyscannerMapper skyscannerMapper;

    public ItineraryDto getItinerary(String addPath, String jsonBody) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", skyscannerConfig.getSkyscannerApiKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, httpHeaders);
        SkyscannerItineraryCreateDto skyscannerItineraryCreateDto = restTemplate.postForObject(
                skyscannerConfig.getSkyscannerApiEndpoint() + addPath,
                httpEntity,
                SkyscannerItineraryCreateDto.class);
        return skyscannerMapper.mapSkyscannerClientDtoToItineraryDto(skyscannerItineraryCreateDto);
    }
}

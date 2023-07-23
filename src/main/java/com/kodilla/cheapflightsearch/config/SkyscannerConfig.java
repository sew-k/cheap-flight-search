package com.kodilla.cheapflightsearch.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SkyscannerConfig {
    @Value("${skyscanner.api.endpoint.prod}")
    private String skyscannerApiEndpoint;
    @Value("${skyscanner.api.key}")
    private String skyscannerApiKey;
}

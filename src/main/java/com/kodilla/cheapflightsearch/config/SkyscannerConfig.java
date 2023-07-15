package com.kodilla.cheapflightsearch.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SkyscannerConfig {
    @Value("${skyscanner.api.endpoint.prod}")
    private String skyscannerApiEndpoint;
//    @Value("${skyscanner.api.key}") //TODO - change name and paste it to HEADER not param
//    private String skyscannerApiKey;
//    @Value("${skyscanner.api.value}") // TODO - delete?
//    private String skyscannerApiValue;
}

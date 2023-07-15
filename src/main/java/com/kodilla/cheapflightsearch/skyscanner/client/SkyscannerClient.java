package com.kodilla.cheapflightsearch.skyscanner.client;

import com.kodilla.cheapflightsearch.config.SkyscannerConfig;
import com.kodilla.cheapflightsearch.dto.ItineraryDto;
import lombok.RequiredArgsConstructor;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SkyscannerClient {
    private final RestTemplate restTemplate;
    private final SkyscannerConfig skyscannerConfig;

    private URI urlBuilder() {
        return UriComponentsBuilder.fromHttpUrl(skyscannerConfig.
                getSkyscannerApiEndpoint())
//                .queryParam("key", skyscannerConfig.getSkyscannerApiKey())
//                .queryParam("value", skyscannerConfig.getSkyscannerApiValue())
                .build()
                .encode()
                .toUri();
    }

//    public List<ItineraryDto> getItineraries() {
    public String getItineraries() {
        URI url = urlBuilder();
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            request.addHeader("x-api-key", "sh428739766321522266746152871799");


            HttpResponse response = httpClient.execute(request);

//            HttpClient httpclient = new HttpClient();
//            HttpRequest request = new HttpRequest(url);
//            request.addHeader("x-api-key", "sh428739766321522266746152871799");
//            //TODO mapping to list of ItineraryDto
//            HttpResponse response = httpclient.send(request);
//            return Collections.emptyList();     //TEMPORARILY
            return response.toString();
        } catch (IOException e) {
            System.out.println("\nEXCEPTION: " + e + "\n");
            return "no response!";
//            return Collections.emptyList();
        }
    }
}

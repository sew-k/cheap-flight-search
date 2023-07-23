package com.kodilla.cheapflightsearch.webclient.skyscanner;

import com.kodilla.cheapflightsearch.config.SkyscannerConfig;
import lombok.RequiredArgsConstructor;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
@RequiredArgsConstructor
public class SkyscannerClient {
    private final RestTemplate restTemplate;
    private final SkyscannerConfig skyscannerConfig;

    private URI urlBuilder() {
        return UriComponentsBuilder.fromHttpUrl(
                        skyscannerConfig.getSkyscannerApiEndpoint())
                .build()
                .encode()
                .toUri();
    }

    public String getItinerariesV1(String addPath, String jsonBody) {
        URI url = urlBuilder();
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url + addPath);
            request.addHeader("x-api-key", skyscannerConfig.getSkyscannerApiKey());
            HttpEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            request.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                return (Integer.toString(response.getStatusLine().getStatusCode()));
            }
        } catch (IOException e) {
            System.out.println("\nEXCEPTION: " + e + "\n");
            return "no response!";
        }
    }

    public SkyscannerItineraryCreateDto getItinerariesV2(String addPath, String jsonBody) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", skyscannerConfig.getSkyscannerApiKey());
        org.springframework.http.HttpEntity<String> httpEntity = new org.springframework.http.HttpEntity<>(jsonBody, httpHeaders);
        //response = restTemplate.exchange(httpEntity, )
        SkyscannerItineraryCreateDto skyscannerItineraryCreateDto = restTemplate.postForObject(
            skyscannerConfig.getSkyscannerApiEndpoint() + addPath,
                httpEntity,
                SkyscannerItineraryCreateDto.class);

        return skyscannerItineraryCreateDto;
//        return ItineraryDto.builder()
//
//                .build();
    }
}

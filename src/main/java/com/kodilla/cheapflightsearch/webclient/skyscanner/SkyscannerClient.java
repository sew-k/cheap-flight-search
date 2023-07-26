package com.kodilla.cheapflightsearch.webclient.skyscanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kodilla.cheapflightsearch.config.SkyscannerConfig;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.mapper.SkyscannerMapper;
import com.kodilla.cheapflightsearch.webclient.skyscanner.responsedata.SkyscannerItineraryCreateDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SkyscannerClient {
    private final RestTemplate restTemplate;
    private final SkyscannerConfig skyscannerConfig;
    private final SkyscannerMapper skyscannerMapper;

//    private URI urlBuilder() {
//        return UriComponentsBuilder.fromHttpUrl(
//                        skyscannerConfig.getSkyscannerApiEndpoint())
//                .build()
//                .encode()
//                .toUri();
//    }
//    public String getItinerariesV1(String addPath, String jsonBody) {
//        URI url = urlBuilder();
//        try {
//            HttpClient httpClient = HttpClientBuilder.create().build();
//            HttpPost request = new HttpPost(url + addPath);
//            request.addHeader("x-api-key", skyscannerConfig.getSkyscannerApiKey());
//            HttpEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
//            request.setEntity(stringEntity);
//            HttpResponse response = httpClient.execute(request);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                return EntityUtils.toString(response.getEntity());
//            } else {
//                return (Integer.toString(response.getStatusLine().getStatusCode()));
//            }
//        } catch (IOException e) {
//            System.out.println("\nEXCEPTION: " + e + "\n");
//            return "no response!";
//        }
//    }
    public ItineraryDto getItinerary(String addPath, String jsonBody) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", skyscannerConfig.getSkyscannerApiKey());
        org.springframework.http.HttpEntity<String> httpEntity = new org.springframework.http.HttpEntity<>(jsonBody, httpHeaders);
        SkyscannerItineraryCreateDto skyscannerItineraryCreateDto = restTemplate.postForObject(
            skyscannerConfig.getSkyscannerApiEndpoint() + addPath,
                httpEntity,
                SkyscannerItineraryCreateDto.class);
        return skyscannerMapper.mapSkyscannerClientDtoToItineraryDto(skyscannerItineraryCreateDto);
    }
}

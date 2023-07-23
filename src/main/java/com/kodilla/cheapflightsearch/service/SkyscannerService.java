package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkyscannerService {
    private final SkyscannerClient skyscannerClient;

//    public List<ItineraryDto> fetchItineraries() {
//        return skyscannerClient.getItineraries();
//    }

    //TEMPORARILY:
    public String fetchSessionToken(String jsonResponse) {
//        JsonParser parser = new JacksonJsonParser();
//        List<String> jsonList = parser.parseList(jsonResponse);
////        return skyscannerClient.getItineraries();
        return null;
    }
}

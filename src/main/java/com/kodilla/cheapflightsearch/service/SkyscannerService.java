package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.dto.ItineraryDto;
import com.kodilla.cheapflightsearch.skyscanner.client.SkyscannerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkyscannerService {
    private final SkyscannerClient skyscannerClient;

//    public List<ItineraryDto> fetchItineraries() {
//        return skyscannerClient.getItineraries();
//    }

    //TEMPORARILY:
    public String fetchItineraries() {
        return skyscannerClient.getItineraries();
    }
}

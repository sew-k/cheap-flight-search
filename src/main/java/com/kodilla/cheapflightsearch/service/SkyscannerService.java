package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.domain.trip.TripPlanDto;
import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkyscannerService {
    private final SkyscannerClient skyscannerClient;
    public ItineraryDto searchCreateGetItinerary(TripPlanDto tripPlanDto) {
        //TODO - convert TripPlanDto to jsonBody request
        String jsonBody = ""; //TEMPORARILY
        return skyscannerClient.getItinerary("/search/create", jsonBody);
    }
}

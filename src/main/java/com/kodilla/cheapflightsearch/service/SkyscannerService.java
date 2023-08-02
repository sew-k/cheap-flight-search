package com.kodilla.cheapflightsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.mapper.ItineraryMapper;
import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerClient;
import com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata.FlightSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkyscannerService {
    private final SkyscannerClient skyscannerClient;
    private final ItineraryMapper itineraryMapper;
    public Itinerary searchCreateGetItinerary(FlightSearchRequestDto flightSearchRequestDto) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(flightSearchRequestDto);
        return itineraryMapper.mapToItinerary(skyscannerClient.getItinerary("/search/create", jsonBody));
    }
}

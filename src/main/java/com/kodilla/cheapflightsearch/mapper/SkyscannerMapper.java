package com.kodilla.cheapflightsearch.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata.FlightSearchRequestDto;
import com.kodilla.cheapflightsearch.webclient.skyscanner.responsedata.SkyscannerItineraryCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkyscannerMapper {
    @Autowired
    ObjectMapper objectMapper;

    public ItineraryDto mapSkyscannerClientDtoToItineraryDto(SkyscannerItineraryCreateDto skyscannerItineraryCreateDto) throws Exception {
        String itineraryMark;
        float amount;
        String deepLink;
        itineraryMark = Optional.of(skyscannerItineraryCreateDto.getContent().getSortingOptions().getBest().stream()
                .map(i -> i.getItineraryId())
                .collect(Collectors.toList()).get(0)).orElseThrow();                                                          //TODO better sorting algorithm
        Object object = skyscannerItineraryCreateDto.getContent().getResults().getItineraries().get(itineraryMark);
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(object));
        amount = Float.parseFloat(jsonNode.get("pricingOptions").get(0).get("items").get(0).get("price").get("amount").textValue()) / 1000;
        deepLink = jsonNode.get("pricingOptions").get(0).get("items").get(0).get("deepLink").textValue();
        return new ItineraryDto(itineraryMark, amount, deepLink);
    }
    public FlightSearchRequestDto mapTripPlanToFlightSearchDto(TripPlan tripPlan) {
        return new FlightSearchRequestDto(
                tripPlan.getAdults(),
                tripPlan.getOriginIata(),
                tripPlan.getDestinationIata(),
                tripPlan.getBeginDate(),
                tripPlan.getEndDate()
        );
    }
}

package com.kodilla.cheapflightsearch.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.cheapflightsearch.controller.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
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
        String itineraryId;
        float amount;
        String deepLink;
        itineraryId = Optional.of(skyscannerItineraryCreateDto.getContent().getSortingOptions().getBest().stream()
                .map(i -> i.getItineraryId())
                .collect(Collectors.toList()).get(0)).orElseThrow();                                                          //TODO better sorting algorithm
        Object object = skyscannerItineraryCreateDto.getContent().getResults().getItineraries().get(itineraryId);
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(object));
        amount = Float.parseFloat(jsonNode.get("pricingOptions").get(0).get("items").get(0).get("price").get("amount").textValue()) / 1000;
        deepLink = jsonNode.get("pricingOptions").get(0).get("items").get(0).get("deepLink").textValue();

        return ItineraryDto.builder()
                .itineraryId(itineraryId)
                .price(amount)
                .purchaseLink(deepLink)
                .build();
    }
    public Itinerary mapItineraryDtoToItinerary(ItineraryDto itineraryDto) {
        return new Itinerary(
                itineraryDto.getItineraryId(),
                itineraryDto.getPrice(),
                itineraryDto.getPurchaseLink()
        );
    }
}

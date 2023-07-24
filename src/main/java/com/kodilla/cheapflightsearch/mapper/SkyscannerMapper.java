package com.kodilla.cheapflightsearch.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerItineraryCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkyscannerMapper {
    @Autowired
    ObjectMapper objectMapper;
    public ItineraryDto mapSkyscannerClientDtoToItineraryDto(SkyscannerItineraryCreateDto skyscannerItineraryCreateDto) {
        String itineraryId = "error parsing itineraryID";
        float amount = 0.01f;
        String deepLink = "error parsing link";
        itineraryId = skyscannerItineraryCreateDto.getContent().getSortingOptions().getBest().stream()
                .map(i -> i.getItineraryId())
                .collect(Collectors.toList()).get(0);               //TODO better sorting algorithm

        Object json = skyscannerItineraryCreateDto.getContent().getResults().getItineraries().get(itineraryId);
        try {
            JsonNode jsonNode = objectMapper.readTree(json.toString());
            amount = Float.parseFloat(jsonNode.get("pricingOptions").get(0).get("items").get(0).get("price").get("amount").textValue())/1000;
            deepLink = jsonNode.get("pricingOptions").get(0).get("items").get(0).get("deepLink").textValue();
        } catch (Exception e) {
                                                                    //TODO Logger implementation
        }
        return ItineraryDto.builder()
                .itineraryId(itineraryId)
                .price(amount)
                .purchaseLink(deepLink)
                .build();
    }
}

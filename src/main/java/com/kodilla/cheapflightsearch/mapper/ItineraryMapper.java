package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryMapper {
    public Itinerary mapToItinerary(ItineraryDto itineraryDto) {
        return new Itinerary(
                itineraryDto.getItineraryMark(),
                itineraryDto.getPrice(),
                itineraryDto.getPurchaseLink()
        );
    }
    public Itinerary mapToItineraryWithTripPlan(ItineraryDto itineraryDto, TripPlan tripPlan) {
        return new Itinerary(
                itineraryDto.getItineraryMark(),
                itineraryDto.getPrice(),
                tripPlan,
                itineraryDto.getPurchaseLink()
        );
    }
    public ItineraryDto mapToItineraryDto(Itinerary itinerary) {
        return ItineraryDto.builder()
                .itineraryMark(itinerary.getItineraryMark())
                .price(itinerary.getPrice())
                .purchaseLink(itinerary.getPurchaseLink())
                .build();
    }
    public List<ItineraryDto> mapToItineraryDtoList(List<Itinerary> itineraryList) {
        return itineraryList.stream()
                .map(i -> mapToItineraryDto(i))
                .collect(Collectors.toList());
    }
}

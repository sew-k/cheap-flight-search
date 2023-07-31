package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryMapper {
    public Itinerary mapItineraryDtoToItinerary(ItineraryDto itineraryDto) {
        return new Itinerary(
                itineraryDto.getItineraryMark(),
                itineraryDto.getPrice(),
                itineraryDto.getPurchaseLink()
        );
    }
    public Itinerary mapItineraryDtoToItineraryWithTripPlan(ItineraryDto itineraryDto, TripPlan tripPlan) {
        return new Itinerary(
                itineraryDto.getItineraryMark(),
                itineraryDto.getPrice(),
                tripPlan,
                itineraryDto.getPurchaseLink()
        );
    }
    public ItineraryDto mapItineraryToItineraryDto(Itinerary itinerary) {
        return new ItineraryDto(
                itinerary.getItineraryMark(),
                itinerary.getPrice(),
                itinerary.getPurchaseLink()
        );
    }
    public List<ItineraryDto> mapItineraryListToItineraryDtoList(List<Itinerary> itineraryList) {
        return itineraryList.stream()
                .map(i -> mapItineraryToItineraryDto(i))
                .collect(Collectors.toList());
    }
}

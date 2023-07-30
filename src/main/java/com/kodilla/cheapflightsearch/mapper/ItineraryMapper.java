package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryMapper {
    public Itinerary mapItineraryDtoToItinerary(ItineraryDto itineraryDto) {
        return new Itinerary(
                itineraryDto.getItineraryId(),
                itineraryDto.getPrice(),
                itineraryDto.getPurchaseLink()
        );
    }
    public Itinerary mapItineraryDtoToItineraryWithTripPlan(ItineraryDto itineraryDto, TripPlan tripPlan) {
        return new Itinerary(
                itineraryDto.getItineraryId(),
                itineraryDto.getPrice(),
                tripPlan,
                itineraryDto.getPurchaseLink()
        );
    }
}

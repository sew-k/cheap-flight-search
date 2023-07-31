package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItineraryMapperTestSuite {
    @Autowired
    ItineraryMapper itineraryMapper;
    private static ItineraryDto itineraryDto;
    private static Itinerary itinerary;
    private static TripPlan tripPlan;

    @BeforeAll
    static void setUp() {
        itineraryDto = new ItineraryDto("Test mark", 99.99, "Test purchase link");
        itinerary = new Itinerary("Test mark", 99.99, "Test purchase link");
        tripPlan = new TripPlan(
                "origin",
                "destination",
                LocalDate.of(2024, 4, 4),
                LocalDate.of(2024, 5, 5),
                1
        );
    }

    @Test
    void mapItineraryDtoToItinerary() {
        //Given

        //When
        Itinerary resultItinerary = itineraryMapper.mapItineraryDtoToItinerary(itineraryDto);

        //Then
        assertEquals(itineraryDto.getItineraryMark(), resultItinerary.getItineraryMark());
        assertEquals(itineraryDto.getPrice(), resultItinerary.getPrice());
        assertEquals(itineraryDto.getPurchaseLink(), resultItinerary.getPurchaseLink());
    }

    @Test
    void mapItineraryDtoToItineraryWithTripPlan() {
        //Given

        //When
        Itinerary resultItinerary = itineraryMapper.mapItineraryDtoToItineraryWithTripPlan(itineraryDto, tripPlan);

        //Then
        assertEquals(itineraryDto.getItineraryMark(), resultItinerary.getItineraryMark());
        assertEquals(itineraryDto.getPrice(), resultItinerary.getPrice());
        assertEquals(itineraryDto.getPurchaseLink(), resultItinerary.getPurchaseLink());
        assertEquals(tripPlan.getAdults(), resultItinerary.getTripPlan().getAdults());
        assertEquals(tripPlan.getBeginDate(), resultItinerary.getTripPlan().getBeginDate());
        assertEquals(tripPlan.getEndDate(), resultItinerary.getTripPlan().getEndDate());
        assertEquals(tripPlan.getOriginIata(), resultItinerary.getTripPlan().getOriginIata());
        assertEquals(tripPlan.getDestinationIata(), resultItinerary.getTripPlan().getDestinationIata());
    }

    @Test
    void mapItineraryToItineraryDto() {
        //Given

        //When
        ItineraryDto resultItineraryDto = itineraryMapper.mapItineraryToItineraryDto(itinerary);

        //Then
        assertEquals(itinerary.getItineraryMark(), resultItineraryDto.getItineraryMark());
        assertEquals(itinerary.getPrice(), resultItineraryDto.getPrice());
        assertEquals(itinerary.getPurchaseLink(), resultItineraryDto.getPurchaseLink());
    }

    @Test
    void mapItineraryListToItineraryDtoList() {
        //Given

        //When
        List<ItineraryDto> resultItineraryDtoList = itineraryMapper.mapItineraryListToItineraryDtoList(List.of(itinerary));

        //Then
        assertFalse(resultItineraryDtoList.isEmpty());
        assertEquals(1, resultItineraryDtoList.size());
        assertEquals(itineraryDto.getItineraryMark(), resultItineraryDtoList.get(0).getItineraryMark());
        assertEquals(itinerary.getPrice(), resultItineraryDtoList.get(0).getPrice());
        assertEquals(itinerary.getPurchaseLink(), resultItineraryDtoList.get(0).getPurchaseLink());
    }
}
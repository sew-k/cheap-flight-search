package com.kodilla.cheapflightsearch.controller;

import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.exception.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.mapper.ItineraryMapper;
import com.kodilla.cheapflightsearch.service.CalendarService;
import com.kodilla.cheapflightsearch.service.ItineraryService;
import com.kodilla.cheapflightsearch.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cheapflightsearch/itineraries")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ItineraryController {
    private final ItineraryService itineraryService;
    private final ItineraryMapper itineraryMapper;
    private final CalendarService calendarService;
    private final RouteService routeService;

    @GetMapping()
    public ResponseEntity<List<ItineraryDto>> getItineraries() {
        return ResponseEntity.ok(itineraryMapper.mapItineraryListToItineraryDtoList(itineraryService.getItineraries()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItineraryDto> getItineraryById(@PathVariable("id") long id) throws ItineraryNotFoundException {
        return ResponseEntity.ok(itineraryMapper.mapItineraryToItineraryDto(itineraryService.getItinerary(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable("id") long id) throws ItineraryNotFoundException {
        itineraryService.deleteItinerary(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItineraryDto> updateUser(@PathVariable("id") long id, @RequestBody ItineraryDto itineraryDto) throws ItineraryNotFoundException {
        itineraryService.updateItinerary(id, itineraryMapper.mapItineraryDtoToItinerary(itineraryDto));
        return ResponseEntity.ok(itineraryMapper.mapItineraryToItineraryDto(itineraryService.getItineraryByItineraryMark(itineraryDto.getItineraryMark())));
    }

    @GetMapping(path = "/itineraries/favourite/{calendar_id}/{adults}")
    public ResponseEntity<List<ItineraryDto>> getItinerariesMatchingFavouriteRoutesAndHolidayPlans(
            @PathVariable("calendar_id") long calendarId,
            @PathVariable("adults") int adults,
            @RequestBody List<ItineraryDto> itineraryDtoList
    ) throws Exception {
        return ResponseEntity.ok(itineraryMapper.mapItineraryListToItineraryDtoList(
                        itineraryService.searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(
                                calendarService.getCalendar(calendarId).getHolidayPlanList(),
                                routeService.getRoutes(),
                                adults
                        )
                )
        );
    }
}

package com.kodilla.cheapflightsearch.controller;

import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SkyscannerController {
    private final SkyscannerClient skyscannerClient;

//    @PostMapping(path = {"/create"}, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<ItineraryDto>> createItinerariesSearch(@RequestBody TripPlanDto tripPlanDto) {
//
//        return ResponseEntity.ok(Collections.emptyList());  //TODO TEMPORARILY
//    }

}

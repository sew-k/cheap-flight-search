package com.kodilla.cheapflightsearch.controller;

import com.kodilla.cheapflightsearch.dto.ItineraryDto;
import com.kodilla.cheapflightsearch.dto.TripPlanDto;
import com.kodilla.cheapflightsearch.skyscanner.client.SkyscannerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SkyscannerController {
    private final SkyscannerClient skyscannerClient;

    @PostMapping(path = {"/create"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItineraryDto>> createIniteraries(@RequestBody TripPlanDto tripPlanDto) {

        return ResponseEntity.ok(Collections.emptyList());  //TODO TEMPORARILY
    }

}

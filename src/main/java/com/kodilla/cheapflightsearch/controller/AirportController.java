package com.kodilla.cheapflightsearch.controller;

import com.kodilla.cheapflightsearch.domain.trip.AirportDto;
import com.kodilla.cheapflightsearch.exception.AirportNotFoundException;
import com.kodilla.cheapflightsearch.mapper.AirportMapper;
import com.kodilla.cheapflightsearch.service.AirportService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cheapflightsearch/airports")
@RequiredArgsConstructor
@CrossOrigin("*")
@Api(tags = "Airport controller")
public class AirportController {
    private final AirportService airportService;
    private final AirportMapper airportMapper;

    @GetMapping()
    public ResponseEntity<List<AirportDto>> getAirports() {
        return ResponseEntity.ok(airportMapper.mapToAirportDtoList(airportService.getAirports()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AirportDto> getAirportById(@PathVariable("id") long id) throws AirportNotFoundException {
        return ResponseEntity.ok(airportMapper.mapToAirportDto(airportService.getAirport(id)));
    }

    @GetMapping("/iata/{iata_code}")
    public ResponseEntity<AirportDto> getAirportByIataCode(@PathVariable("iata_code") String iataCode) throws AirportNotFoundException {
        return ResponseEntity.ok(airportMapper.mapToAirportDto(airportService.getAirportByIata(iataCode)));
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AirportDto> updateAirport(@PathVariable("id") long id, @RequestBody AirportDto airportDto) throws AirportNotFoundException {
        airportService.updateAirport(id, airportMapper.mapToAirport(airportDto));
        return ResponseEntity.ok(airportMapper.mapToAirportDto(airportService.getAirport(id)));
    }

    @PutMapping(path = "/update/iata/{iata_code}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AirportDto> updateAirportWithIata(@PathVariable("iata_code") String iataCode, @RequestBody AirportDto airportDto) throws AirportNotFoundException {
        airportService.updateAirport(airportService.getAirportByIata(iataCode).getAirportId(), airportMapper.mapToAirport(airportDto));
        return ResponseEntity.ok(airportMapper.mapToAirportDto(airportService.getAirportByIata(iataCode)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAirport(@RequestBody AirportDto airportDto) {
        airportService.createAirport(airportMapper.mapToAirport(airportDto));
        return ResponseEntity.ok().build();
    }
}

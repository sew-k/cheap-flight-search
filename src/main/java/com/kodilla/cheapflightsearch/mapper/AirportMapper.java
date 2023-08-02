package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.AirportDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportMapper {
    public Airport mapToAirport(AirportDto airportDto) {
        return new Airport(
                airportDto.getCountry(),
                airportDto.getCity(),
                airportDto.getIataCode()
        );
    }
    public AirportDto mapToAirportDto(Airport airport) {
        return AirportDto.builder()
                .airportId(airport.getAirportId())
                .country(airport.getCountry())
                .city(airport.getCity())
                .iataCode(airport.getIataCode())
                .build();
    }
    public List<AirportDto> mapToAirportDtoList(final List<Airport> airportList) {
        return airportList.stream().map(this::mapToAirportDto).collect(Collectors.toList());
    }
    public List<Airport> mapToAirportList(final List<AirportDto> airportDtoList) {
        return airportDtoList.stream().map(this::mapToAirport).collect(Collectors.toList());
    }

}

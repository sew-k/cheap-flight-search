package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.AirportDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AirportMapperTestSuite {
    @Autowired
    AirportMapper airportMapper;
    @Test
    void testMapToAirport() {
        //Given
        AirportDto airportDto = AirportDto.builder()
                .country("Country")
                .city("City")
                .iataCode("IataCode")
                .build();

        //When
        Airport resultAirport = airportMapper.mapToAirport(airportDto);

        //Then
        assertEquals(airportDto.getCountry(), resultAirport.getCountry());
        assertEquals(airportDto.getCity(), resultAirport.getCity());
        assertEquals(airportDto.getIataCode(), resultAirport.getIataCode());
    }

    @Test
    void testMapToAirportDto() {
        //Given
        Airport airport = new Airport("Country", "City", "IataCode");

        //When
        AirportDto resultAirportDto = airportMapper.mapToAirportDto(airport);

        //Then
        assertEquals(airport.getAirportId(), resultAirportDto.getAirportId());
        assertEquals(airport.getCountry(), resultAirportDto.getCountry());
        assertEquals(airport.getCity(), resultAirportDto.getCity());
        assertEquals(airport.getIataCode(), resultAirportDto.getIataCode());
    }

    @Test
    void testMapToAirportDtoList() {
        //Given
        List<Airport> airportList = List.of(
                new Airport("Country1", "City1", "IataCode1"),
                new Airport("Country2", "City2", "IataCode2")
        );

        //When
        List<AirportDto> resultAirportDtoList = airportMapper.mapToAirportDtoList(airportList);

        //Then
        assertFalse(resultAirportDtoList.isEmpty());
        assertEquals(airportList.size(), resultAirportDtoList.size());
        assertEquals(airportList.get(0).getCountry(), resultAirportDtoList.get(0).getCountry());
        assertEquals(airportList.get(0).getCity(), resultAirportDtoList.get(0).getCity());
        assertEquals(airportList.get(0).getIataCode(), resultAirportDtoList.get(0).getIataCode());
    }

    @Test
    void testMapToAirportList() {
        //Given
        List<AirportDto> airportDtoList = List.of(
                AirportDto.builder()
                        .country("Country1")
                        .city("City1")
                        .iataCode("IataCode1")
                        .build(),
                AirportDto.builder()
                        .country("Country2")
                        .city("City2")
                        .iataCode("IataCode2")
                        .build()
        );

        //When
        List<Airport> resultAirportList = airportMapper.mapToAirportList(airportDtoList);

        //Then
        assertFalse(resultAirportList.isEmpty());
        assertEquals(airportDtoList.size(), resultAirportList.size());
        assertEquals(airportDtoList.get(0).getCountry(), resultAirportList.get(0).getCountry());
        assertEquals(airportDtoList.get(0).getCity(), resultAirportList.get(0).getCity());
        assertEquals(airportDtoList.get(0).getIataCode(), resultAirportList.get(0).getIataCode());
    }
}
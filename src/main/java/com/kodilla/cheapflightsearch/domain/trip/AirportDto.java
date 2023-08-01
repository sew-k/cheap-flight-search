package com.kodilla.cheapflightsearch.domain.trip;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AirportDto {
    private Long airportId;
    private String country;
    private String city;
    private String iataCode;
    public AirportDto(String country, String city, String iataCode) {
        this.country = country;
        this.city = city;
        this.iataCode = iataCode;
    }
}

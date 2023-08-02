package com.kodilla.cheapflightsearch.domain.trip;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AirportDto {
    private Long airportId;
    private String country;
    private String city;
    private String iataCode;
}

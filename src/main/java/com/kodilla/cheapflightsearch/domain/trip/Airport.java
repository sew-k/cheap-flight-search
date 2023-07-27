package com.kodilla.cheapflightsearch.domain.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "airports")
public class Airport {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "airport_id", unique = true)
    private Long airportId;
//    @NotNull
    @Column(name = "country_name")
    private String countryName;
//    @NotNull
    @Column(name = "city_name")
    private String cityName;
    @NotNull
    @Column(name = "iata_code")
    private String iataCode;

    public Airport(String iataCode) {
        this.iataCode = iataCode;
    }

    public Airport(String countryName, String cityName, String iataCode) {
        this.countryName = countryName;
        this.cityName = cityName;
        this.iataCode = iataCode;
    }
}

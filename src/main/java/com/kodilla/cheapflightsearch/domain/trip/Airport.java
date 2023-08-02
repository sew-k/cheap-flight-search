package com.kodilla.cheapflightsearch.domain.trip;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "airports")
public class Airport {
    @Id
    @GeneratedValue
    @Column(name = "airport_id", unique = true)
    private Long airportId;
    @NotNull
    @Column(name = "country")
    private String country;
    @NotNull
    @Column(name = "city")
    private String city;
    @OneToOne(mappedBy = "origin", cascade = CascadeType.REMOVE)
    @JoinColumn(name = "origin_route_id")
    private Route originRoute;
    @OneToOne(mappedBy = "destination", cascade = CascadeType.REMOVE)
    @JoinColumn(name = "destination_route_id")
    private Route destinationRoute;
    @NotNull
    @Column(name = "iata_code", unique = true)
    private String iataCode;

    public Airport(String country, String city, String iataCode) {
        this.country = country;
        this.city = city;
        this.iataCode = iataCode;
    }

    @Override
    public String toString() {
        return iataCode + " [" + country + ", " + city + "]";
    }
}

package com.kodilla.cheapflightsearch.domain.trip;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return iataCode.equals(airport.iataCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iataCode);
    }
}

package com.kodilla.cheapflightsearch.domain;

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
@Entity(name = "airlines")
public class Airline {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "airline_id", unique = true)
    private Long airlineId;
    @NotNull
    @Column(name = "airline_name")
    private String airlineName;
    @NotNull
    @Column(name = "carrier_id")
    private String carrierId;
    @OneToMany
    private List<Route> routes;
}

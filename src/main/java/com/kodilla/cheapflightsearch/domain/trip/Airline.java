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
@Entity(name = "airlines")
public class Airline {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "airline_id", unique = true)
    private Long airlineId;
    @NotNull
    @Column(name = "airline_name", unique = true)
    private String airlineName;
    @NotNull
    @Column(name = "carrier_id", unique = true)
    private String carrierId;
    @OneToMany(
            targetEntity = Route.class,
            mappedBy = "airline",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Route> routes;
}

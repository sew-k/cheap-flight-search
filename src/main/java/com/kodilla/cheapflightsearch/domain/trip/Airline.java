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
    @GeneratedValue
    @Column(name = "airline_id", unique = true)
    private Long airlineId;
    @NotNull
    @Column(name = "airline_name")
    private String airlineName;
    @NotNull
    @Column(name = "carrier_id")
    private String carrierId;
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Route> routes;
    public Airline(String airlineName) {
        this.airlineName = airlineName;
    }
    public Airline(String airlineName, String carrierId) {
        this.airlineName = airlineName;
        this.carrierId = carrierId;
    }

//    public Airline(Long airlineId, String airlineName, String carrierId) {
//        this.airlineId = airlineId;
//        this.airlineName = airlineName;
//        this.carrierId = carrierId;
//    }
}

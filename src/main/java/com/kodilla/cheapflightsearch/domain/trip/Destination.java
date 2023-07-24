package com.kodilla.cheapflightsearch.domain.trip;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "destinations")
public class Destination {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "destination_id", unique = true)
    private Long destinationId;
    @NotNull
    @OneToOne
    @JoinColumn(name = "airport_id")
    private Airport airport;
}

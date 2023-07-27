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
@Entity(name = "origins")
public class Origin {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "origin_id", unique = true)
    private Long originId;
//    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "airport_id")
    private Airport airport;

    public Origin(Airport airport) {
        this.airport = airport;
    }
}

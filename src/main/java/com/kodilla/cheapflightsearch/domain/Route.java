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
@Entity(name = "routes")
public class Route {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "route_id", unique = true)
    private Long routeId;
    @NotNull
    @OneToOne
    @JoinColumn(name = "origin_id")
    private Origin origin;
    @NotNull
    @OneToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;
    @NotNull
    @OneToMany(
            targetEntity = Day.class,
            mappedBy = "route",
            cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY
    )
    @Column(name = "days_of_week", unique = true)
    private List<Day> daysOfWeek;
    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "airline_id")
    private Airline airline;
}

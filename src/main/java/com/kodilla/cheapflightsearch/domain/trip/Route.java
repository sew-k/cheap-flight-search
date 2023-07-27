package com.kodilla.cheapflightsearch.domain.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

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
//    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "origin_id")
    private Origin origin;
//    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "destination_id")
    private Destination destination;
//    @NotNull
//    @ManyToMany(
//            targetEntity = Day.class,
//            mappedBy = "route",
//            cascade = CascadeType.PERSIST,
//            fetch = FetchType.LAZY
//    )
//    @JoinTable(
//            name = "routes_on_days"
////            joinColumns =
////            @JoinColumn(name = "route_id", referencedColumnName = "route_id"),
////            inverseJoinColumns =
////            @JoinColumn(name = "day_id", referencedColumnName = "day_id")
//    )
//    private List<Day> days;
////    @NotNull


//    TODO - working
//    @ManyToMany(
//        cascade = CascadeType.ALL,
//        fetch = FetchType.LAZY
//    )
//    @JoinTable(name = "airlines_on_routes")
//    private List<Airline> airlines;



//
//    public Route(Origin origin, Destination destination, List<Day> days) {
//        this.origin = origin;
//        this.destination = destination;
//        this.days = days;
//    }
    @Transient
    private Set<DayOfWeek> daysOfWeek;
    public Route(Origin origin, Destination destination) {
        this.origin = origin;
        this.destination = destination;
    }
    public Route(Origin origin, Destination destination, Set<DayOfWeek> daysOfWeek) {
        this.origin = origin;
        this.destination = destination;
        this.daysOfWeek = daysOfWeek;
    }
//    public Route(Origin origin, Destination destination, List<Airline> airlines, Set<DayOfWeek> daysOfWeek) {
//        this.origin = origin;
//        this.destination = destination;
//        this.airlines = airlines;
//        this.daysOfWeek = daysOfWeek;
//    }
}

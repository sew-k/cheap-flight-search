package com.kodilla.cheapflightsearch.domain.trip;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "routes")
public class Route {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "route_id", unique = true)
    private Long routeId;

    @OneToOne(
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "origin_airport_id")
    private Airport origin;
    @OneToOne(
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "destination_airport_id")
    private Airport destination;
    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.EAGER)
    @Column(name = "days_of_week")
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();
    @Column(name = "favourite")
    private boolean favourite;

    public Route(Airport origin, Airport destination, Set<DayOfWeek> daysOfWeek, boolean favourite) {
        this.origin = origin;
        this.destination = destination;
        this.daysOfWeek = daysOfWeek;
        this.favourite = favourite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(origin, route.origin) && Objects.equals(destination, route.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination);
    }
}

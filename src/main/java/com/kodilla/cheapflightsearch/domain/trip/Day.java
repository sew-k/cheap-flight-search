package com.kodilla.cheapflightsearch.domain.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "days")
public class Day {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "day_id", unique = true)
    private Long dayId;
    @NotNull
    @Column(name = "day_name")
    private DayOfWeek dayName;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}

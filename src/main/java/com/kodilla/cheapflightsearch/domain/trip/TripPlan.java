package com.kodilla.cheapflightsearch.domain.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "trip_plans")
public class TripPlan {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "trip_plan_id", unique = true)
    private Long tripPlanId;
    @NotNull
    @Column(name = "origin_iata")
    private String originIata;
    @NotNull
    @Column(name = "destination_iata")
    private String destinationIata;
    @NotNull
    @Column(name = "begin_trip_date")
    private LocalDate beginDate;
    @NotNull
    @Column(name = "end_trip_date")
    private LocalDate endDate;
    @NotNull
    @Column(name = "adults")
    private int adults;

    public TripPlan(String originIata, String destinationIata, LocalDate beginDate, LocalDate endDate, int adults) {
        this.originIata = originIata;
        this.destinationIata = destinationIata;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.adults = adults;
    }
}

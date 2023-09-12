package com.kodilla.cheapflightsearch.domain.trip;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "trip_plans")
public class TripPlan {
    @Id
    @GeneratedValue
    @Column(name = "trip_plan_id", unique = true)
    private Long tripPlanId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
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
    @OneToOne(
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    public TripPlan(String originIata, String destinationIata, LocalDate beginDate, LocalDate endDate, int adults) {
        this.originIata = originIata;
        this.destinationIata = destinationIata;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.adults = adults;
    }

    public TripPlan(String originIata, String destinationIata, LocalDate beginDate, LocalDate endDate, int adults, Itinerary itinerary) {
        this.originIata = originIata;
        this.destinationIata = destinationIata;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.adults = adults;
        this.itinerary = itinerary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripPlan tripPlan = (TripPlan) o;
        return adults == tripPlan.adults && originIata.equals(tripPlan.originIata)
                && destinationIata.equals(tripPlan.destinationIata) && beginDate.equals(tripPlan.beginDate)
                && endDate.equals(tripPlan.endDate);
    }
}

package com.kodilla.cheapflightsearch.domain.skyscanner;

import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "itineraries")
public class Itinerary {
    @Id
    @GeneratedValue
    @Column(name = "itinerary_id")
    private Long itineraryId;
    @NotNull
    @Column(name = "itinerary_mark")
    private String itineraryMark;
    @NotNull
    @Column(name = "price")
    private double price;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "trip_plan_id")
    private TripPlan tripPlan;
    @NotNull
    @Column(name = "purchase_link", length = 3000)
    private String purchaseLink;
    @NotNull
    @Column(name = "is_purchased")
    private boolean purchased;

    public Itinerary(String itineraryMark, double price, String purchaseLink) {
        this.itineraryMark = itineraryMark;
        this.price = price;
        this.purchaseLink = purchaseLink;
        this.purchased = false;
    }

    public Itinerary(String itineraryMark, double price, TripPlan tripPlan, String purchaseLink) {
        this.itineraryMark = itineraryMark;
        this.price = price;
        this.tripPlan = tripPlan;
        this.purchaseLink = purchaseLink;
        this.purchased = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Itinerary itinerary = (Itinerary) o;
        return itineraryMark.equals(itinerary.itineraryMark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itineraryMark);
    }
}

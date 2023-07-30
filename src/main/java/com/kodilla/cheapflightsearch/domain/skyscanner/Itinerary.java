package com.kodilla.cheapflightsearch.domain.skyscanner;

import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "itineraries")
@Data
public class Itinerary {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "itinerary_id", unique = true)
    private Long itineraryId;
    @NotNull
    @Column(name = "itinerary_mark", unique = true)
    private String itineraryMark;
    @NotNull
    @Column(name = "price")
    private double price;
    @OneToOne(
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "itinerary_id")
    private TripPlan tripPlan;
    @NotNull
    @Column(name = "purchase_link")
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
}

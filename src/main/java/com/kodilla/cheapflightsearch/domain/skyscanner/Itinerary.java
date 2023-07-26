package com.kodilla.cheapflightsearch.domain.skyscanner;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    @NotNull
    @Column(name = "purchase_link")
    private String purchaseLink;

    public Itinerary(String itineraryMark, double price, String purchaseLink) {
        this.itineraryMark = itineraryMark;
        this.price = price;
        this.purchaseLink = purchaseLink;
    }

}

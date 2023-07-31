package com.kodilla.cheapflightsearch.domain.skyscanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class ItineraryDto {
    private String itineraryMark;
    private double price;
    private String purchaseLink;
}

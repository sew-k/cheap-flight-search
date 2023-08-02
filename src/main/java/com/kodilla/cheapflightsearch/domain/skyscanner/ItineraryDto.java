package com.kodilla.cheapflightsearch.domain.skyscanner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class ItineraryDto {
    private String itineraryMark;
    private double price;
    private String purchaseLink;
}

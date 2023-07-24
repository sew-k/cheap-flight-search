package com.kodilla.cheapflightsearch.domain.skyscanner;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItineraryDto {
    private String itineraryId;
    private float price;
    private String purchaseLink;
}

package com.kodilla.cheapflightsearch.domain.skyscanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.net.URL;

//@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Builder
public class ItineraryDto {
//    @JsonProperty("sessionToken")
//    private String sessionToken;
//    @JsonProperty("status")
//    private String status;
//    @JsonProperty("content")
//    private Content content;
    private float price;
    private String itineraryId;
    private String linkToPurchase;
}

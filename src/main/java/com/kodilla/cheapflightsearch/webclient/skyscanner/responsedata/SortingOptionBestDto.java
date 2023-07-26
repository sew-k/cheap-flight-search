package com.kodilla.cheapflightsearch.webclient.skyscanner.responsedata;

import lombok.Data;

@Data
public class SortingOptionBestDto {
    private float score;
    private String itineraryId;
}

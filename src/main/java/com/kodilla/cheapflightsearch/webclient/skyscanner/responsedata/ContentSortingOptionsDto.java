package com.kodilla.cheapflightsearch.webclient.skyscanner.responsedata;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContentSortingOptionsDto {
    private List<SortingOptionBestDto> best = new ArrayList<>();

//    private Map<Float, String> scoreAndItineraryIds = new HashMap<>();
}

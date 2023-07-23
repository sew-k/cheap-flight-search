package com.kodilla.cheapflightsearch.webclient.skyscanner;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ContentSortingOptionsDto {
    private List<SortingOptionBestDto> best = new ArrayList<>();

//    private Map<Float, String> scoreAndItineraryIds = new HashMap<>();
}

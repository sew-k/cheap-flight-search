package com.kodilla.cheapflightsearch.webclient.skyscanner;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ContentResultsDto {
    private Map<String, Object> itineraries = new HashMap<>();
}

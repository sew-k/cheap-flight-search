package com.kodilla.cheapflightsearch.webclient.skyscanner.responsedata;

import lombok.Data;

@Data
public class SkyscannerItineraryCreateDto {
    private String sessionToken;
    private String status;
    private CreateContentDto content;
}

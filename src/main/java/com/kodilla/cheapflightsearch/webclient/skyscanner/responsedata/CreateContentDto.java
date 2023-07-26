package com.kodilla.cheapflightsearch.webclient.skyscanner.responsedata;

import lombok.Data;

@Data
public class CreateContentDto {
    ContentResultsDto results;
    ContentSortingOptionsDto sortingOptions;
}

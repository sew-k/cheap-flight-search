package com.kodilla.cheapflightsearch.webclient.skyscanner;

import lombok.Data;

@Data
public class CreateContentDto {
    ContentResultsDto results;
    ContentSortingOptionsDto sortingOptions;
}

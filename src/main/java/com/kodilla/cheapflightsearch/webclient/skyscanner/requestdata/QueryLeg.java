package com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueryLeg {
    private OriginPlaceId originPlaceId;
    private DestinationPlaceId destinationPlaceId;
    private Date date;
}

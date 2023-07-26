package com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class Query {
    private String market;
    private String locale;
    private String currency;
    private int adults;
    private String cabinClass;
    private List<QueryLeg> queryLegs;
    private String includeCarriersIds;
}

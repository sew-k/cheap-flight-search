package com.kodilla.cheapflightsearch.webclient.skyscanner;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PricingOptionsItemDto {
//    private PriceDto price;
    private List<String> items = new ArrayList<>();
}

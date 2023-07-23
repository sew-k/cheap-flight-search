package com.kodilla.cheapflightsearch.webclient.skyscanner;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PricingOptionsDto {
    private List<PricingOptionsItemDto> pricingOptions = new ArrayList<>();
}

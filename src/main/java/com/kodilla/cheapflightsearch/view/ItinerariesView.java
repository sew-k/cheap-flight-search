package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;

@Route(value = "main/itineraries")
public class ItinerariesView {
    private Grid<Itinerary> itinerariesGrid = new Grid<>(Itinerary.class, false);
    public ItinerariesView() {

    }
}

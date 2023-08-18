package com.kodilla.cheapflightsearch;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import javax.annotation.security.PermitAll;

@AnonymousAllowed
@Route(value = "main")
public class MainView extends VerticalLayout {

    public MainView() {
        this.setAlignItems(Alignment.CENTER);
        add(new Label("CHEAP-FLIGHT-SEARCH"));
        add(new Button("Quick search", e -> UI.getCurrent().getPage().open("main/quick_search")));
        add(new Button("User", e -> UI.getCurrent().getPage().open("main/users")));
        add(new Button("Routes", e -> UI.getCurrent().getPage().open("main/routes")));
        add(new Button("Airports", e -> UI.getCurrent().getPage().open("main/airports")));
        add(new Button("Calendar", e -> UI.getCurrent().getPage().open("main/calendar")));
        add(new Button("Itineraries", e -> UI.getCurrent().getPage().open("main/itineraries")));
    }
}

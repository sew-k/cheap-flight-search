package com.kodilla.cheapflightsearch;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "main")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Button("Quick search", e -> UI.getCurrent().getPage().open("main/quick_search")));
        add(new Button("Routes", e -> UI.getCurrent().getPage().open("main/routes")));
        add(new Button("Calendar", e -> UI.getCurrent().getPage().open("main/calendar")));
    }
}

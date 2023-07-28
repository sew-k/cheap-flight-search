package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.trip.*;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.RouteService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Message;
import java.time.DayOfWeek;

@com.vaadin.flow.router.Route(value = "main/routes")
public class RoutesView extends VerticalLayout {
    private Airport originAirport;
    private Airport destinationAirport;
    private Grid<Route> routesGrid = new Grid<>(Route.class, false);
    @Autowired
    RouteService routeService;
    @Autowired
    AirportService airportService;

    public RoutesView() {
//        Airport originAirport = new Airport("Poland", "Warsaw", "WMI"); //TODO temporarily stubbed
//        Airport destinationAirport = new Airport("Greece", "Korfu", "CFU");   //TODO temporarily stubbed

        TextField originTextField = new TextField("Origin", "IATA code");
        originTextField.setClearButtonVisible(true);
        originTextField.setValue("WMI");
        TextField destinationTextField = new TextField("Destination", "IATA code");
        originTextField.setClearButtonVisible(true);
        destinationTextField.setValue("CFU");

        MultiSelectComboBox<DayOfWeek> selectionDaysOfWeekComboBox = new MultiSelectComboBox<>("Days of week");
        selectionDaysOfWeekComboBox.setItems(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        );
        selectionDaysOfWeekComboBox.setValue(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY);                   //TODO temporarily stubbed
        Button addToRoutesButton = new Button("Add to routes", i -> {
            try {
                originAirport = airportService.getAirportByIata(originTextField.getValue());
            } catch (Exception e) {
                Notification.show(originTextField.getLabel() + " doesn't exist!");
            }
            try {
                destinationAirport = airportService.getAirportByIata(destinationTextField.getValue());
            } catch (Exception e) {
                Notification.show(destinationTextField.getLabel() + " doesn't exist!");
            }
            if ((originAirport != null) && (destinationAirport != null)) {
                Route newRouteToAdd = new Route(
                        originAirport,
                        destinationAirport,
                        selectionDaysOfWeekComboBox.getValue(),
                        false
                );
                routeService.createRoute(newRouteToAdd);
            } else {
                Notification.show("Incorrect values");
            }
        });
        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                originTextField,
                destinationTextField,
                selectionDaysOfWeekComboBox
        );
        add(searchFieldsLayout);
        add(addToRoutesButton);
        routesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        routesGrid.addSelectionListener(new SelectionListener<Grid<Route>, Route>() {
            @Override
            public void selectionChange(SelectionEvent<Grid<Route>, Route> event) {
                event.getFirstSelectedItem().stream().forEach(route -> {
                    route.setFavourite(true);
                    try {
                        routeService.updateRoute(route.getRouteId(), route);
                    } catch (Exception e) {

                    } finally {
                        refreshRoutesGrid();
                    }
                    Notification.show("Route "
                            + route.getOrigin().toString() + "->"
                            + route.getDestination().toString()
                            + " added to favourites");
                });
            }
        });
        routesGrid.addColumn(route -> route.getOrigin().getIataCode()
                + " [" + route.getOrigin().getCity()
                + ", " + route.getOrigin().getCountry()
                + "]").setHeader("Origin").setSortable(true);
        routesGrid.addColumn(route -> route.getDestination().getIataCode()
                + " [" + route.getDestination().getCity()
                + ", " + route.getDestination().getCountry()
                + "]").setHeader("Destination").setSortable(true);
        routesGrid.addColumn(route -> route.getDaysOfWeek()).setHeader("Days of week").setSortable(true);
        add(routesGrid);
        add(new Button("Refresh", e -> refreshRoutesGrid()));
    }
    public void refreshRoutesGrid() {
        routesGrid.setItems(routeService.getRoutes());
    }
}

package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.trip.*;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.RouteService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.security.PermitAll;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
@PermitAll
@com.vaadin.flow.router.Route(value = "main/routes")
public class RoutesView extends VerticalLayout {
    private Airport originAirport;
    private Airport destinationAirport;
    private Grid<Route> routesGrid = new Grid<>(Route.class, false);
    private Set<Airport> airportSet = new HashSet<>();
    @Autowired
    RouteService routeService;
    @Autowired
    AirportService airportService;

    public RoutesView() {
        ComboBox<Airport> originAirportComboBox = new ComboBox<>("Origin airport");
        originAirportComboBox.setPattern("IATA code");
        originAirportComboBox.setAllowCustomValue(true);
        originAirportComboBox.setItems(airportSet);
        ComboBox<Airport> destinationAirportComboBox = new ComboBox<>("Destination airport");
        destinationAirportComboBox.setPattern("IATA code");
        destinationAirportComboBox.setAllowCustomValue(true);
        destinationAirportComboBox.setItems(airportSet);
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
        selectionDaysOfWeekComboBox.setValue();                   //TODO temporarily stubbed
        Button addToRoutesButton = new Button("Add to routes", i -> {
            originAirport = originAirportComboBox.getValue();
            destinationAirport = destinationAirportComboBox.getValue();
            if (airportSet.contains(originAirport) && airportSet.contains(destinationAirport)) {
                routeService.createRoute(new Route(
                        originAirport,
                        destinationAirport,
                        selectionDaysOfWeekComboBox.getValue(),
                        false
                        ));
                refreshRoutesGrid();
            } else {
                Notification.show("Incorrect input values!");
            }
        });
        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                originAirportComboBox,
                destinationAirportComboBox,
                selectionDaysOfWeekComboBox
        );
        add(new Button("Refresh all", e -> {
            setUpAirports();
            refreshRoutesGrid();
        }));
        add(searchFieldsLayout);
        add(addToRoutesButton);
        routesGrid.addColumn(route -> route.getOrigin().getIataCode()
                + " [" + route.getOrigin().getCity()
                + ", " + route.getOrigin().getCountry()
                + "]").setHeader("Origin").setSortable(true);
        routesGrid.addColumn(route -> route.getDestination().getIataCode()
                + " [" + route.getDestination().getCity()
                + ", " + route.getDestination().getCountry()
                + "]").setHeader("Destination").setSortable(true);
        routesGrid.addColumn(route -> route.getDaysOfWeek()).setHeader("Days of week").setSortable(true);
        routesGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, route) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeRoute(route));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        routesGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, route) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> routeService.switchFavourite(route));
                    button.setIcon(new Icon(VaadinIcon.EYE));
                })).setHeader("Favourite");
        add(routesGrid);
    }
    public void refreshRoutesGrid() {
        routesGrid.setItems(routeService.getRoutes());
    }
    private void removeRoute(Route route) {
        if (route == null)
            return;
        try {
            routeService.deleteRoute(route.getRouteId());
        } catch (Exception e) {
            Notification.show("Exception when trying to remove route: " + e);
        }
        this.refreshRoutesGrid();
    }
    @EventListener(ApplicationReadyEvent.class)
    private void setUpAirports() {
        try {
            Thread.sleep(1000);
            airportSet.addAll(airportService.getAirports());
        } catch (Exception e) {

        }
    }
}

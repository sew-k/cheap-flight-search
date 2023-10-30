package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.trip.*;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.UserNotFoundException;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.RouteService;
import com.kodilla.cheapflightsearch.service.SecurityService;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
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

import javax.annotation.security.PermitAll;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_AIRPORT_COMPONENTS_WIDTH;
import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_OPTION_BUTTONS_WIDTH;

@PermitAll
@com.vaadin.flow.router.Route(value = "main/routes")
public class RoutesView extends VerticalLayout {
    private User currentUser = null;
    private Airport originAirport;
    private Airport destinationAirport;
    private Grid<Route> routesGrid = new Grid<>(Route.class, false);
    private Set<Airport> airportSet = new HashSet<>();
    private ComboBox<Airport> originAirportComboBox = new ComboBox<>("Origin airport");
    private ComboBox<Airport> destinationAirportComboBox = new ComboBox<>("Destination airport");
    private MultiSelectComboBox<DayOfWeek> selectionDaysOfWeekComboBox = new MultiSelectComboBox<>("Days of week");

    @Autowired
    RouteService routeService;
    @Autowired
    AirportService airportService;
    @Autowired
    UserService userService;
    @Autowired
    SecurityService securityService;

    public RoutesView() {
        addOptionButtons();
        addInputComponents();
        addRoutesGrid();
    }

    private void addOptionButtons() {
        Button backToMainButton = new Button("Back to Main", e -> UI.getCurrent().getPage().open("main"));
        Button refreshButton = new Button("Refresh", e -> {
            setCurrentUser();
            setUpAirports();
            refreshAll();
        });
        backToMainButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        refreshButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        add(backToMainButton);
        add(refreshButton);
    }

    private void addInputComponents() {
        setupAirportComboBoxComponents();
        setupSelectionDaysOfWeekComboBox();
        Button addToRoutesButton = new Button("Add to routes", e -> {
            setCurrentUser();
            readViewForms();
            if (checkCorrectAirportInput()) {
                createRoute();
                refreshRoutesGrid();
            } else {
                Notification.show("Incorrect input values!");
            }
        });
        addToRoutesButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                originAirportComboBox,
                destinationAirportComboBox,
                selectionDaysOfWeekComboBox
        );
        add(searchFieldsLayout);
        add(addToRoutesButton);
    }

    private void addRoutesGrid() {
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
                    button.addClickListener(e -> {
                        routeService.switchFavourite(route);
                        refreshRoutesGrid();
                    });
                    button.setIcon(getFavouriteIndicator(route));
                })).setHeader("Favourite");
        add(routesGrid);
    }

    private void readViewForms() {
        this.originAirport = originAirportComboBox.getValue();
        this.destinationAirport = destinationAirportComboBox.getValue();
    }

    private void createRoute() {
        routeService.createRoute(new Route(
                originAirport,
                destinationAirport,
                selectionDaysOfWeekComboBox.getValue(),
                false,
                currentUser
        ));
    }

    private boolean checkCorrectAirportInput() {
        return airportSet.contains(originAirport) && airportSet.contains(destinationAirport);
    }

    private void setupAirportComboBoxComponents() {
        originAirportComboBox.setPattern("IATA code");
        originAirportComboBox.setAllowCustomValue(true);
        originAirportComboBox.setItems(airportSet);
        originAirportComboBox.setClearButtonVisible(true);
        originAirportComboBox.setMinWidth(MINIMUM_AIRPORT_COMPONENTS_WIDTH, Unit.PIXELS);
        destinationAirportComboBox.setPattern("IATA code");
        destinationAirportComboBox.setAllowCustomValue(true);
        destinationAirportComboBox.setItems(airportSet);
        destinationAirportComboBox.setClearButtonVisible(true);
        destinationAirportComboBox.setMinWidth(MINIMUM_AIRPORT_COMPONENTS_WIDTH, Unit.PIXELS);
    }

    private void setupSelectionDaysOfWeekComboBox() {
        selectionDaysOfWeekComboBox.setItems(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        );
        selectionDaysOfWeekComboBox.setValue();
    }

    private void refreshAll() {
        refreshRoutesGrid();
    }

    public void refreshRoutesGrid() {
        routesGrid.setItems(routeService.getRoutesByUserId(currentUser.getUserId()));
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

    private void setUpAirports() {
        airportSet.addAll(airportService.getAirports());
    }

    private Icon getFavouriteIndicator(Route route) {
        if (route.isFavourite()) {
            return new Icon(VaadinIcon.STAR);
        } else {
            return new Icon(VaadinIcon.STAR_O);
        }
    }

    private void setCurrentUser() {
        try {
            currentUser = userService.getUserByName(securityService.getAuthenticatedUser().getUsername());
        } catch (UserNotFoundException e) {
            Notification.show("User not found: " + e);
        }
    }
}

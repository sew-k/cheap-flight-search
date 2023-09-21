package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.UserNotFoundException;
import com.kodilla.cheapflightsearch.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.text.NumberFormat;
import java.util.*;

@PermitAll
@Route(value = "main/suggestions")
public class SuggestionsView extends VerticalLayout {
    private Grid<Itinerary> itineraryGrid = new Grid<>(Itinerary.class, false);
    private User currentUser = null;
    private int adults = 1; //TODO temporarily stubbed
    private List<HolidayPlan> holidayPlans = new ArrayList<>();
    private List<com.kodilla.cheapflightsearch.domain.trip.Route> routes = new ArrayList<>();

    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
    @Autowired
    SecurityService securityService;
    @Autowired
    UserService userService;
    @Autowired
    ItineraryService itineraryService;
    @Autowired
    CalendarService calendarService;
    @Autowired
    RouteService routeService;

    public SuggestionsView() {

        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        add(new Button("Search/Refresh all", e -> {
            setCurrentUser();
            getHolidayPlans();
            getRoutes();
            searchAll();
            refreshAll();
        }));

        itineraryGrid.addColumn(i -> i.getTripPlan().getOriginIata()).setHeader("Origin");
        itineraryGrid.addColumn(i -> i.getTripPlan().getDestinationIata()).setHeader("Destination");
        itineraryGrid.addColumn(i -> i.getTripPlan().getBeginDate()).setHeader("Begin trip date");
        itineraryGrid.addColumn(i -> i.getTripPlan().getEndDate()).setHeader("End trip date");
        itineraryGrid.addColumn(i -> i.getTripPlan().getAdults()).setHeader("Passengers");
        itineraryGrid.addColumn(i -> currencyFormatter.format(i.getPrice())).setHeader("Price");
        itineraryGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, i) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeItinerary(i));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        itineraryGrid.addComponentColumn(i -> new Button("Press to buy",
                e -> UI.getCurrent().getPage().open(i.getPurchaseLink())));
        itineraryGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        add(itineraryGrid);
    }

    private void getRoutes() {
        routes = routeService.getRoutesByUserId(currentUser.getUserId());
    }

    private void getHolidayPlans() {
        holidayPlans = calendarService.getHolidayPlansByUser(currentUser);
    }

    private void searchAll() {
        try {
            itineraryGrid.setItems(
                    itineraryService.searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(
                            holidayPlans,
                            routes,
                            adults
                    )
            );
        } catch (Exception exception) {
            Notification.show("Exception while trying to search for Itineraries: " + exception);
        }
    }

    public void refreshItinerariesGrid() {
        itineraryGrid.setItems(itineraryService.getItinerariesByUser(currentUser));
    }

    public void refreshAll() {
        refreshItinerariesGrid();
    }

    private void removeItinerary(Itinerary itinerary) {
        if (itinerary == null)
            return;
        try {
            itineraryService.deleteItinerary(itinerary.getItineraryId());
        } catch (Exception e) {
            Notification.show("Exception when trying to remove itinerary: " + e);
        }
        refreshAll();
    }

    private void setCurrentUser() {
        try {
            currentUser = userService.getUserByName(securityService.getAuthenticatedUser().getUsername());
        } catch (UserNotFoundException e) {
            Notification.show("User not found: " + e);
        }
    }

    private void readViewForms() {

    }
}


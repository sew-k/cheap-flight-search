package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.UserNotFoundException;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.ItineraryService;
import com.kodilla.cheapflightsearch.service.SecurityService;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

@PermitAll
@Route(value = "main/itineraries")
public class ItinerariesView extends VerticalLayout {
    private Grid<TripPlan> tripPlanGrid = new Grid<>(TripPlan.class, false);
    private Grid<Itinerary> itineraryGrid = new Grid<>(Itinerary.class, false);
    private Set<Airport> airportSet = new HashSet<>();
    private User currentUser = null;
    private Airport originAirport;
    private Airport destinationAirport;
    private LocalDate beginDate;
    private LocalDate endDate;
    private int adults;
    private ComboBox<Airport> originAirportComboBox = new ComboBox<>("Origin airport");
    private ComboBox<Airport> destinationAirportComboBox = new ComboBox<>("Destination airport");
    private DatePicker beginDatePicker = new DatePicker("Begin trip date");
    private DatePicker endDatePicker = new DatePicker("End trip date");
    private TextField adultsTextField = new TextField("Passengers", "1-5");

    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
    @Autowired
    SecurityService securityService;
    @Autowired
    UserService userService;
    @Autowired
    ItineraryService itineraryService;
    @Autowired
    AirportService airportService;

    public ItinerariesView() {

        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        add(new Button("Refresh all", e -> {
            setCurrentUser();
            setUpAirports();
            refreshAll();
        }));

        originAirportComboBox.setPattern("IATA code");
        originAirportComboBox.setAllowCustomValue(true);
        originAirportComboBox.setItems(airportSet);
        originAirportComboBox.setClearButtonVisible(true);
        destinationAirportComboBox.setPattern("IATA code");
        destinationAirportComboBox.setAllowCustomValue(true);
        destinationAirportComboBox.setItems(airportSet);
        destinationAirportComboBox.setClearButtonVisible(true);
        beginDatePicker.setWeekNumbersVisible(true);
        beginDatePicker.setValue(LocalDate.now());
        endDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setValue(LocalDate.now().plusDays(1L));

        Button addToTripPlansButton = new Button("Add to Trip Plans", e -> {
            readViewForms();
            addNewTripPlan();
            refreshTripPlansGrid();
        });

        VerticalLayout searchButtonsLayout = new VerticalLayout();
        searchButtonsLayout.add(addToTripPlansButton);
        HorizontalLayout customSearchFieldsLayout = new HorizontalLayout(
                originAirportComboBox,
                destinationAirportComboBox,
                beginDatePicker,
                endDatePicker,
                adultsTextField,
                searchButtonsLayout
        );
        add(customSearchFieldsLayout);
        tripPlanGrid.addColumn(TripPlan::getOriginIata).setHeader("Origin");
        tripPlanGrid.addColumn(TripPlan::getDestinationIata).setHeader("Destination");
        tripPlanGrid.addColumn(t -> itineraryService.getCityForTripPlanDestination(t)).setHeader("City");
        tripPlanGrid.addColumn(t -> itineraryService.getWeatherForTripPlanDestination(t)).setHeader("Weather");
        tripPlanGrid.addColumn(TripPlan::getBeginDate).setHeader("Begin trip date");
        tripPlanGrid.addColumn(TripPlan::getEndDate).setHeader("End trip date");
        tripPlanGrid.addColumn(TripPlan::getAdults).setHeader("Passengers");
        tripPlanGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, t) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeTripPlan(t));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        tripPlanGrid.addComponentColumn(t -> new Button("Search",
                e -> {
                    searchForItinerary(t);
                }));
        tripPlanGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        add(tripPlanGrid);

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

    private void setUpAirports() {
        airportSet.addAll(airportService.getAirports());
    }

    public void refreshItinerariesGrid() {
        itineraryGrid.setItems(itineraryService.getItinerariesByUser(currentUser));
    }

    public void refreshTripPlansGrid() {
        tripPlanGrid.setItems(itineraryService.getTripPlansByUser(currentUser));
    }

    public void refreshAll() {
        refreshTripPlansGrid();
        refreshItinerariesGrid();
    }

    public TripPlan newTripPlanFromReadInput() throws UserNotFoundException {
        User currentUser = userService.getUserByName(securityService.getAuthenticatedUser().getUsername());
        return TripPlan.builder()
                .user(currentUser)
                .originIata(originAirport.getIataCode())
                .destinationIata(destinationAirport.getIataCode())
                .beginDate(beginDate)
                .endDate(endDate)
                .adults(adults)
                .build();
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

    private void removeTripPlan(TripPlan tripPlan) {
        if (tripPlan == null)
            return;
        try {
            itineraryService.deleteTripPlan(tripPlan.getTripPlanId());
        } catch (Exception e) {
            Notification.show("Exception when trying to remove trip plan: " + e);
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
        this.originAirport = originAirportComboBox.getValue();
        this.destinationAirport = destinationAirportComboBox.getValue();
        this.beginDate = beginDatePicker.getValue();
        this.endDate = endDatePicker.getValue();
        this.adults = Integer.parseInt(adultsTextField.getValue());
    }

    private void addNewTripPlan() {
        itineraryService.createTripPlan(
                TripPlan.builder()
                        .originIata(originAirport.getIataCode())
                        .destinationIata(destinationAirport.getIataCode())
                        .beginDate(beginDate)
                        .endDate(endDate)
                        .adults(adults)
                        .user(currentUser)
                        .build()
        );
    }

    private void searchForItinerary(TripPlan tripPlan) {
        try {
            itineraryService.createItineraryBasedOnTripPlan(tripPlan);
            Notification.show("Itinerary successfully saved!");
        } catch (Exception exception) {
            Notification.show("Itinerary not found!: " + exception);
        } finally {
            refreshItinerariesGrid();
            refreshTripPlansGrid();
        }
    }
}

package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.UserNotFoundException;
import com.kodilla.cheapflightsearch.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
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

import static com.kodilla.cheapflightsearch.view.ViewsConfig.*;

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
    private TextField adultsTextField = new TextField("Passengers", "1", "1-5");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(
            new Locale("pl", "PL")
    );

    @Autowired
    SecurityService securityService;
    @Autowired
    UserService userService;
    @Autowired
    ItineraryService itineraryService;
    @Autowired
    TripPlanService tripPlanService;
    @Autowired
    AirportService airportService;

    public ItinerariesView() {
        addOptionButtons();
        addInputComponents();
        addTripPlansGrid();
        addItineraryGrid();
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
        setupDatePickerComponents();
        setupAdultsTextFieldComponent();
        Button createCustomTripPlanButton = new Button("Add custom", e -> {
            readViewForms();
            addNewTripPlan();
            refreshTripPlansGrid();
        });
        createCustomTripPlanButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        Button createMultipleTripPlansButton = new Button("Create multiple", e -> {
            readViewForms();
            createMultipleTripPlans();
            refreshTripPlansGrid();
        });
        createMultipleTripPlansButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        HorizontalLayout customSearchFieldsLayout = new HorizontalLayout(
                originAirportComboBox,
                destinationAirportComboBox,
                beginDatePicker,
                endDatePicker,
                adultsTextField
        );
        add(customSearchFieldsLayout, createCustomTripPlanButton, createMultipleTripPlansButton);
    }

    private void addTripPlansGrid() {
        tripPlanGrid.addColumn(TripPlan::getOriginIata).setHeader("Origin");
        tripPlanGrid.addColumn(TripPlan::getDestinationIata).setHeader("Destination");
        tripPlanGrid.addColumn(t -> tripPlanService.getCityForTripPlanDestination(t)).setHeader("City");
        tripPlanGrid.addColumn(t -> tripPlanService.getWeatherForTripPlanDestination(t)).setHeader("Weather");
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
    }

    private void addItineraryGrid() {
        itineraryGrid.addColumn(i -> i.getTripPlan().getOriginIata()).setHeader("Origin");
        itineraryGrid.addColumn(i -> i.getTripPlan().getDestinationIata()).setHeader("Destination");
        itineraryGrid.addColumn(i -> i.getTripPlan().getBeginDate()).setHeader("Begin trip date");
        itineraryGrid.addColumn(i -> i.getTripPlan().getEndDate()).setHeader("End trip date");
        itineraryGrid.addColumn(i -> i.getTripPlan().getAdults()).setHeader("Passengers");
        itineraryGrid.addColumn(i -> CURRENCY_FORMATTER.format(i.getPrice())).setHeader("Price");
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

    private void setupDatePickerComponents() {
        beginDatePicker.setWeekNumbersVisible(true);
        beginDatePicker.setValue(LocalDate.now());
        beginDatePicker.setMinWidth(MINIMUM_DATE_COMPONENTS_WIDTH, Unit.PIXELS);
        endDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setValue(LocalDate.now().plusDays(1L));
        endDatePicker.setMinWidth(MINIMUM_DATE_COMPONENTS_WIDTH, Unit.PIXELS);
    }

    private void setupAdultsTextFieldComponent() {
        adultsTextField.setMaxWidth(ADULTS_COMPONENTS_WIDTH, Unit.PIXELS);
    }

    private void createMultipleTripPlans() {
        tripPlanService.createTripPlansFromFavouriteRoutesAndHolidayPlans(currentUser, adults);
    }

    private void setUpAirports() {
        airportSet.addAll(airportService.getAirports());
    }

    private void refreshItinerariesGrid() {
        itineraryGrid.setItems(itineraryService.getItinerariesByUser(currentUser));
    }

    private void refreshTripPlansGrid() {
        tripPlanGrid.setItems(tripPlanService.getTripPlansByUser(currentUser));
    }

    private void refreshAll() {
        refreshTripPlansGrid();
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

    private void removeTripPlan(TripPlan tripPlan) {
        if (tripPlan == null)
            return;
        try {
            tripPlanService.deleteTripPlan(tripPlan.getTripPlanId());
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
        tripPlanService.createTripPlan(
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
            refreshAll();
        }
    }
}

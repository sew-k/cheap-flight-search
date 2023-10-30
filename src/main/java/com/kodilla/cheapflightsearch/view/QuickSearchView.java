package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.SkyscannerService;
import com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata.FlightSearchRequestDto;
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
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

import static com.kodilla.cheapflightsearch.view.ViewsConfig.*;
import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_DATE_COMPONENTS_WIDTH;

@AnonymousAllowed
@Route(value = "main/quick_search")
public class QuickSearchView extends VerticalLayout {
    private Airport originAirport;
    private Airport destinationAirport;
    private LocalDate beginDate;
    private LocalDate endDate;
    private int adults;
    private ComboBox<Airport> originAirportComboBox = new ComboBox<>("Origin airport");
    private ComboBox<Airport> destinationAirportComboBox = new ComboBox<>("Destination airport");
    private DatePicker beginDatePicker = new DatePicker("Begin trip date", LocalDate.now());
    private DatePicker endDatePicker = new DatePicker("End trip date", LocalDate.now().plusDays(1L));
    private TextField adultsTextField = new TextField("Passengers", "1-5");
    private Set<Airport> airportSet = new HashSet<>();
    private Grid<Itinerary> itineraryGrid = new Grid<>(Itinerary.class, false);
    private List<Itinerary> itineraryList = new ArrayList<>();
    private List<TripPlan> tripPlanList = new ArrayList<>();            //TODO - add trip plans to list and join with itinerary
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
    @Autowired
    SkyscannerService skyscannerService;
    @Autowired
    AirportService airportService;

    public QuickSearchView() {
        addOptionButtons();
        addInputComponents();
        addItineraryGrid();
    }

    private void addInputComponents() {
        setupAirportComboBoxComponents();
        setupDatePickerComponents();
        setupAdultsTextFieldComponent();
        HorizontalLayout customSearchFieldsLayout = new HorizontalLayout(
                originAirportComboBox,
                destinationAirportComboBox,
                beginDatePicker,
                endDatePicker,
                adultsTextField
        );
        Button quickSearchButton = new Button("Quick search", e -> {
            readViewForms();
            Notification.show("Searching for flight from: " + originAirport
                    + " to: " + destinationAirport
                    + " on " + beginDate
                    + " to " + endDate);
            quickSearchForItinerary();
        });
        quickSearchButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        add(customSearchFieldsLayout, quickSearchButton);
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

    private void setupAdultsTextFieldComponent() {
        adultsTextField.setMaxWidth(ADULTS_COMPONENTS_WIDTH, Unit.PIXELS);
    }

    private void setupDatePickerComponents() {
        beginDatePicker.setWeekNumbersVisible(true);
        beginDatePicker.setValue(LocalDate.now());
        beginDatePicker.setMinWidth(MINIMUM_DATE_COMPONENTS_WIDTH, Unit.PIXELS);
        endDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setValue(LocalDate.now().plusDays(1L));
        endDatePicker.setMinWidth(MINIMUM_DATE_COMPONENTS_WIDTH, Unit.PIXELS);
    }

    private void addOptionButtons() {
        Button backToMainButton = new Button("Back to Main", e -> UI.getCurrent().getPage().open("main"));
        Button refreshButton = new Button("Refresh", e -> {
            setUpAirports();
            refreshItinerariesGrid();
        });
        backToMainButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        refreshButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        add(backToMainButton);
        add(refreshButton);
    }

    private void quickSearchForItinerary() {
        FlightSearchRequestDto flightSearchRequestDto = new FlightSearchRequestDto(
                adults,
                originAirport.getIataCode(),
                destinationAirport.getIataCode(),
                beginDate,
                endDate);
        try {
            Itinerary newItinerary = skyscannerService.searchCreateGetItinerary(flightSearchRequestDto);
            Notification.show("Best price for this flight is: " + Double.toString(newItinerary.getPrice()) + " PLN");
            TripPlan newTripPlan = TripPlan.builder()
                    .originIata(originAirport.getIataCode())
                    .destinationIata(destinationAirport.getIataCode())
                    .beginDate(beginDate)
                    .endDate(endDate)
                    .adults(adults)
                    .itinerary(newItinerary)
                    .build();
            newItinerary.setTripPlan(newTripPlan);

            if (needToUpdateExistingItinerary(newItinerary)) {
                removeOutdatedItineraryWithSameTripPlan(newItinerary);
            }
            itineraryList.add(newItinerary);
            tripPlanList.add(newTripPlan);
        } catch (Exception e) {
            Notification.show("Exception: " + e);
        } finally {
            refreshItinerariesGrid();
        }
    }

    private boolean needToUpdateExistingItinerary(Itinerary itinerary) {
        return itineraryList.stream()
                .anyMatch(i -> i.getTripPlan().equals(itinerary.getTripPlan()));
    }

    private void removeOutdatedItineraryWithSameTripPlan(Itinerary newItinerary) {
        Itinerary itineraryToRemove = itineraryList.stream()
                .filter(i -> i.getTripPlan().equals(newItinerary.getTripPlan()))
                .findFirst().get();
        itineraryList.remove(itineraryToRemove);
    }

    private void setUpAirports() {
        airportSet.addAll(airportService.getAirports());
    }

    private void refreshItinerariesGrid() {
        itineraryGrid.setItems(itineraryList);
    }

    private void readViewForms() {
        this.originAirport = originAirportComboBox.getValue();
        this.destinationAirport = destinationAirportComboBox.getValue();
        this.beginDate = beginDatePicker.getValue();
        this.endDate = endDatePicker.getValue();
        this.adults = Integer.parseInt(adultsTextField.getValue());
    }

    private void removeItinerary(Itinerary itinerary) {
        itineraryList.remove(itinerary);
        refreshItinerariesGrid();
    }
}

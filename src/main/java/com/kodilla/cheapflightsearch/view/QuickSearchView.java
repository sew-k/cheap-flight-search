package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.SkyscannerService;
import com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata.FlightSearchRequestDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
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
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
    @Autowired
    SkyscannerService skyscannerService;
    @Autowired
    AirportService airportService;

    public QuickSearchView() {
        originAirportComboBox.setItems(airportSet);
        originAirportComboBox.setClearButtonVisible(true);
        destinationAirportComboBox.setItems(airportSet);
        destinationAirportComboBox.setClearButtonVisible(true);
        beginDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setWeekNumbersVisible(true);
        Button quickSearchButton = new Button("Quick search", e -> {
            readViewForms();
            Notification.show("Searching for flight from: " + originAirport
                    + " to: " + destinationAirport
                    + " on " + beginDate
                    + " to " + endDate);
            quickSearchForItinerary();
        });
        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        add(new Button("Refresh all", e -> {
            setUpAirports();
            refreshItinerariesGrid();
        }));
        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                originAirportComboBox,
                destinationAirportComboBox,
                beginDatePicker,
                endDatePicker,
                adultsTextField
        );
        add(searchFieldsLayout);
        add(quickSearchButton);
        itineraryGrid.addColumn(itinerary -> originAirport).setHeader("Origin");
        itineraryGrid.addColumn(itinerary -> destinationAirport).setHeader("Destination");
        itineraryGrid.addColumn(itinerary -> beginDate).setHeader("Begin trip date");
        itineraryGrid.addColumn(itinerary -> endDate).setHeader("End trip date");
        itineraryGrid.addColumn(itinerary -> adults).setHeader("Passengers");
        itineraryGrid.addColumn(itinerary -> currencyFormatter.format(itinerary.getPrice())).setHeader("Price");
        itineraryGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, itinerary) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeItinerary(itinerary));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        itineraryGrid.addComponentColumn(i -> new Button("Press to buy",
                e -> UI.getCurrent().getPage().open(i.getPurchaseLink())));
        add(itineraryGrid);
    }

    private void quickSearchForItinerary() {
        FlightSearchRequestDto flightSearchRequestDto = new FlightSearchRequestDto(
                adults,
                originAirport.getIataCode(),
                destinationAirport.getIataCode(),
                beginDate,
                endDate);
        try {
            Itinerary itinerary = skyscannerService.searchCreateGetItinerary(flightSearchRequestDto);
            Notification.show("ItineraryID: " + itinerary.getItineraryId());
            Notification.show("Best price for this flight is: " + Double.toString(itinerary.getPrice()) + " PLN");
            itineraryList.add(itinerary);
        } catch (Exception exception) {
            Notification.show("Exception: " + exception);
        } finally {
            Notification.show("Searching end");
            refreshItinerariesGrid();
        }
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

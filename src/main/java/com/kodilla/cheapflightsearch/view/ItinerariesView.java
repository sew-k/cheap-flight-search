package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.mapper.ItineraryMapper;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.ItineraryService;
import com.kodilla.cheapflightsearch.service.SkyscannerService;
import com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata.FlightSearchRequestDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = "main/itineraries")
public class ItinerariesView extends VerticalLayout {

    private Grid<Itinerary> itineraryGrid = new Grid<>(Itinerary.class, false);
    private Set<Airport> airportSet = new HashSet<>();
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pl", "PL"));
    @Autowired
    ItineraryService itineraryService;
    @Autowired
    ItineraryMapper itineraryMapper;
    @Autowired
    AirportService airportService;

    public ItinerariesView() {
        add(new Button("set airports"));
        ComboBox<Airport> originAirport = new ComboBox<>("Origin airport");
        originAirport.setPattern("IATA code");
        originAirport.setAllowCustomValue(true);
        originAirport.setItems(airportSet);

        ComboBox<Airport> destinationAirport = new ComboBox<>("Destination airport");
        destinationAirport.setPattern("IATA code");
        destinationAirport.setAllowCustomValue(true);
        destinationAirport.setItems(airportSet);


//        originTextField.setClearButtonVisible(true);
//        originTextField.setValue("WAW");                                                            //TODO temporarily stubbed
//        TextField destinationTextField = new TextField("Destination", "IATA code");
//        originTextField.setClearButtonVisible(true);
//        destinationTextField.setValue("CFU");                                                       //TODO temporarily stubbed
//        DatePicker beginDatePicker = new DatePicker("Begin trip date");
//        beginDatePicker.setWeekNumbersVisible(true);
//        beginDatePicker.setValue(LocalDate.of(2023, 8, 4));                     //TODO temporarily stubbed
//        DatePicker endDatePicker = new DatePicker("End trip date");
//        endDatePicker.setWeekNumbersVisible(true);
//        endDatePicker.setValue(LocalDate.of(2023, 8, 6));                       //TODO temporarily stubbed
//        TextField adultsTextField = new TextField("Passengers", "1-5");
//        adultsTextField.setValue(Integer.toString(adults));
//        Button quickSearchButton = new Button("Quick search", e -> {
//            origin = originTextField.getValue();
//            destination = destinationTextField.getValue();
//            beginTripDate = beginDatePicker.getValue();
//            endTripDate = endDatePicker.getValue();
//            adults = Integer.parseInt(adultsTextField.getValue());
//            Notification.show("Searching for flight from: " + origin
//                    + " to: " + destination
//                    + " on " + beginTripDate
//                    + " to " + endTripDate);
//            FlightSearchRequestDto flightSearchRequestDto = new FlightSearchRequestDto(
//                    adults,
//                    origin,
//                    destination,
//                    beginTripDate,
//                    endTripDate);
//            try {
//                Itinerary itinerary = skyscannerService.searchCreateGetItinerary(flightSearchRequestDto);
//                Notification.show("ItineraryID: " + itinerary.getItineraryId());
//                Notification.show("Best price for this flight is: " + Double.toString(itinerary.getPrice()) + " PLN");
//                itineraryList.add(itinerary);
//            } catch (Exception exception) {
//                Notification.show("Exception: " + exception);
//            } finally {
//                Notification.show("Searching end");
//                refreshItineraryGrid();
//            }
//        });
//        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));

        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                originAirport,
                destinationAirport
//                beginDatePicker,
//                endDatePicker,
//                adultsTextField
        );
        add(searchFieldsLayout);

//        add(quickSearchButton);
//        itineraryGrid.addColumn(i -> origin).setHeader("Origin");
//        itineraryGrid.addColumn(i -> destination).setHeader("Destination");
//        itineraryGrid.addColumn(i -> beginTripDate).setHeader("Begin trip date");
//        itineraryGrid.addColumn(i -> endTripDate).setHeader("End trip date");
//        itineraryGrid.addColumn(i -> adults).setHeader("Passengers");
//        itineraryGrid.addColumn(i -> currencyFormatter.format(i.getPrice())).setHeader("Price");
//        itineraryGrid.addComponentColumn(i -> new Button("Press to buy",
//                e -> UI.getCurrent().getPage().open(i.getPurchaseLink())));
//        add(itineraryGrid);
    }
}

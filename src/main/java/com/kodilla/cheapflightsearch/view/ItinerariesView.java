package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.exception.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.mapper.ItineraryMapper;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.ItineraryService;
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
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

@Route(value = "main/itineraries")
public class ItinerariesView extends VerticalLayout {
    private Grid<TripPlan> tripPlanGrid = new Grid<>(TripPlan.class, false);
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

        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        add(new Button("Refresh all", e -> {
            setUpAirports();
            refreshItinerariesGrid();
            refreshTripPlansGrid();
        }));

        ComboBox<Airport> originAirportComboBox = new ComboBox<>("Origin airport");
        originAirportComboBox.setPattern("IATA code");
        originAirportComboBox.setAllowCustomValue(true);
        originAirportComboBox.setItems(airportSet);

        ComboBox<Airport> destinationAirportComboBox = new ComboBox<>("Destination airport");
        destinationAirportComboBox.setPattern("IATA code");
        destinationAirportComboBox.setAllowCustomValue(true);
        destinationAirportComboBox.setItems(airportSet);
        DatePicker beginDatePicker = new DatePicker("Begin trip date");
        beginDatePicker.setWeekNumbersVisible(true);
        beginDatePicker.setValue(LocalDate.of(2023, 8, 4));                     //TODO temporarily stubbed
        DatePicker endDatePicker = new DatePicker("End trip date");
        endDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setValue(LocalDate.of(2023, 8, 6));                       //TODO temporarily stubbed
        TextField adultsTextField = new TextField("Passengers", "1-5");

        Button customSearchButton = new Button("Search", e -> {
            try {
                Optional<Itinerary> searchingResult = itineraryService.searchForItineraryBasedOnTripPlan(
                        newTripPlanFromInput(
                                originAirportComboBox,
                                destinationAirportComboBox,
                                beginDatePicker,
                                endDatePicker,
                                adultsTextField
                        )
                );
                if (searchingResult.isPresent()) {
//                    itineraryService.createItinerary(searchingResult.get());
                    refreshTripPlansGrid();
                    refreshItinerariesGrid();
                }
            } catch (Exception exception) {
                Notification.show("Exception: " + exception);
            }
        });
        Button addToTripPlansButton = new Button("Add to Trip Plans", e -> {
            itineraryService.createTripPlan(newTripPlanFromInput(
                    originAirportComboBox,
                    destinationAirportComboBox,
                    beginDatePicker,
                    endDatePicker,
                    adultsTextField
            ));
            refreshTripPlansGrid();
        });

        VerticalLayout searchButtonsLayout = new VerticalLayout();
        searchButtonsLayout.add(customSearchButton, addToTripPlansButton);
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
        tripPlanGrid.addColumn(t  -> itineraryService.getCityForTripPlanDestination(t)).setHeader("City");
        tripPlanGrid.addColumn(t  -> itineraryService.getWeatherForTripPlanDestination(t)).setHeader("Weather");
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
                    try {
                        Optional<Itinerary> searchingResult = itineraryService.searchForItineraryBasedOnTripPlan(t);
                        if (searchingResult.isPresent()) {
//                            itineraryService.createItinerary(searchingResult.get());
                            refreshTripPlansGrid();
                            refreshItinerariesGrid();
                        }
                    } catch (ItineraryNotFoundException exception) {
                        Notification.show("Itinerary not found!");
                    } finally {
                        refreshItinerariesGrid();
                        refreshTripPlansGrid();
                    }
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

    @EventListener(ApplicationReadyEvent.class)
    private void setUpAirports() {
        try {
            Thread.sleep(1000);
            airportSet.addAll(airportService.getAirports());
        } catch (Exception e) {

        }
    }

    public void refreshItinerariesGrid() {
        itineraryGrid.setItems(itineraryService.getItineraries());
    }

    public void refreshTripPlansGrid() {
        tripPlanGrid.setItems(itineraryService.getTripPlans());
    }

    public TripPlan newTripPlanFromInput(ComboBox<Airport> origin, ComboBox<Airport> destination, DatePicker begin, DatePicker end, TextField adults) {
        return new TripPlan(
                origin.getValue().getIataCode(),
                destination.getValue().getIataCode(),
                begin.getValue(),
                end.getValue(),
                Integer.parseInt(adults.getValue())
        );
    }

    private void removeItinerary(Itinerary itinerary) {
        if (itinerary == null)
            return;
        try {
            itineraryService.deleteItinerary(itinerary.getItineraryId());
        } catch (Exception e) {
            Notification.show("Exception when trying to remove itinerary: " + e);
        }
        this.refreshItinerariesGrid();
    }

    private void removeTripPlan(TripPlan tripPlan) {
        if (tripPlan == null)
            return;
        try {
            itineraryService.deleteTripPlan(tripPlan.getTripPlanId());
        } catch (Exception e) {
            Notification.show("Exception when trying to remove trip plan: " + e);
        }
        this.refreshTripPlansGrid();
    }
}

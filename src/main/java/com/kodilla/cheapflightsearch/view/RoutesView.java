package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.controller.RouteController;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.domain.trip.*;
import com.kodilla.cheapflightsearch.mapper.RouteMapper;
import com.kodilla.cheapflightsearch.repository.AirlineRepository;
import com.kodilla.cheapflightsearch.service.RouteService;
import com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata.FlightSearchRequestDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@com.vaadin.flow.router.Route(value = "main/routes")
public class RoutesView extends VerticalLayout {
    private int passengers = 1;
    private String originAirport;
    private String destinationAirport;
    private Set<DayOfWeek> dayOfWeekSet;
    private Grid<Route> routesGrid = new Grid<>(Route.class, false);
    @Autowired
    RouteService routeService;
    @Autowired
    RouteMapper routeMapper;
    @Autowired
    AirlineRepository airlineRepository;                        //TODO temporarily stubbed

    public RoutesView() {
        TextField originTextField = new TextField("Origin", "IATA code");
        originTextField.setClearButtonVisible(true);
        originTextField.setValue("WAW");                                                            //TODO temporarily stubbed
        TextField destinationTextField = new TextField("Destination", "IATA code");
        originTextField.setClearButtonVisible(true);
        destinationTextField.setValue("CFU");                                                       //TODO temporarily stubbed

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

        TextField adultsTextField = new TextField("Passengers", "1-5");
        adultsTextField.setValue(Integer.toString(passengers));

        Button addToRoutesButton = new Button("Add to routes", e -> {
            originAirport = originTextField.getValue();
            destinationAirport = destinationTextField.getValue();
            dayOfWeekSet = selectionDaysOfWeekComboBox.getValue();

            Origin origin = new Origin();
            Destination destination = new Destination();
            Airport warsawAirport = new Airport("Poland", "Warsaw", "WMI"); //TODO temporarily stubbed
            Airport corfuAirport = new Airport("Greece", "Korfu", "CFU");   //TODO temporarily stubbed
            Airline allAirlines = new Airline("all", "all");


            origin.setAirport(warsawAirport);
            destination.setAirport(corfuAirport);

//            airlineRepository.save(allAirlines);
            Route newRouteToAdd = new Route(origin, destination, dayOfWeekSet);       //TODO temporarily stubbed

//            newRouteToAdd.setAirlines(List.of(allAirlines));

            routeService.createRoute(newRouteToAdd);
        });
        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                originTextField,
                destinationTextField,
                selectionDaysOfWeekComboBox,
                adultsTextField
        );
        add(searchFieldsLayout);
        add(addToRoutesButton);

        routesGrid.addColumn(i -> originAirport).setHeader("Origin");
        routesGrid.addColumn(i -> destinationAirport).setHeader("Destination");
        routesGrid.addColumn(i -> "STUB").setHeader("Airlines");               //TODO temporarily stubbed

        routesGrid.addColumn(i -> dayOfWeekSet).setHeader("Days of week");
        routesGrid.addColumn(i -> passengers).setHeader("Passengers");
//        routesGrid.addColumn(i -> new Icon).setHeader("Favourite");
//        routesGrid.addComponentColumn(i -> new Button("Press to buy",
//                e -> UI.getCurrent().getPage().open(i.getPurchaseLink())));
        add(routesGrid);
        add(new Button("Refresh", e -> refreshRoutesGrid()));

    }
    public void refreshRoutesGrid() {
        routesGrid.setItems(routeService.getRoutes());
    }
}

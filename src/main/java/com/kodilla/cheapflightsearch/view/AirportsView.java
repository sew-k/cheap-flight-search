package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.kodilla.cheapflightsearch.service.WeatherService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@Route(value = "main/airports")
public class AirportsView extends VerticalLayout {
    @Autowired
    AirportService airportService;
    @Autowired
    WeatherService weatherService;
    private Grid<Airport> airportsGrid = new Grid<>(Airport.class, false);

    public AirportsView() {
        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        add(new Button("New Airport", e -> newAirportDialog().open()));
        add(new Button("Refresh", e -> refreshAirportsGrid()));
        airportsGrid.addColumn(Airport::getIataCode).setHeader("IATA code").setSortable(true);
        airportsGrid.addColumn(Airport::getCountry).setHeader("Country").setSortable(true);
        airportsGrid.addColumn(Airport::getCity).setHeader("City").setSortable(true);
        airportsGrid.addColumn(a -> airportService.getWeatherForAirport(a)).setHeader("Weather").setSortable(true);
        airportsGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, airport) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeAirport(airport));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage airports");
        add(airportsGrid);
    }

    private void removeAirport(Airport airport) {
        try {
            airportService.deleteAirport(airport.getAirportId());
        } catch (Exception exception) {
            Notification.show("Exception when trying to remove an airport: " + exception);
        } finally {
            refreshAirportsGrid();
        }
    }

    private void refreshAirportsGrid() {
        airportsGrid.setItems(airportService.getAirports());
    }

    public Dialog newAirportDialog() {
        Dialog newAirportDialog = new Dialog();
        newAirportDialog.setHeaderTitle("New airport");
        TextField countryTextField = new TextField("Country");
        TextField cityTextField = new TextField("City");
        TextField iataCodeTextField = new TextField("IATA code");
        VerticalLayout layout = new VerticalLayout(countryTextField, cityTextField, iataCodeTextField);
        newAirportDialog.add(layout);
        Button saveButton = createSaveAirportButton(
                newAirportDialog,
                countryTextField,
                cityTextField,
                iataCodeTextField
        );
        Button cancelButton = new Button("Cancel", e -> newAirportDialog.close());
        newAirportDialog.getFooter().add(cancelButton);
        newAirportDialog.getFooter().add(saveButton);
        return newAirportDialog;
    }

    public Button createSaveAirportButton(Dialog newAirportDialog, TextField countryTextField,
                                          TextField cityTextField, TextField iataCodeTextField) {
        return new Button("Save", e -> {
            if (!airportService.checkIfAirportExistsByIata(iataCodeTextField.getValue())) {
                airportService.createAirport(
                        new Airport(
                                countryTextField.getValue(),
                                cityTextField.getValue(),
                                iataCodeTextField.getValue()));
                newAirportDialog.close();
            } else {
                Notification.show("Airport with given IATA code exists!");
            }
        });
    }
}

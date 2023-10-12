package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.service.AirportService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
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

import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_OPTION_BUTTONS_WIDTH;

@RolesAllowed("ADMIN")
@Route(value = "main/airports")
public class AirportsView extends VerticalLayout {
    @Autowired
    AirportService airportService;
    private Grid<Airport> airportsGrid = new Grid<>(Airport.class, false);

    public AirportsView() {
        addOptionButtons();
        addGrid();
    }

    private void addOptionButtons() {
        Button backToMainButton = new Button("Back to Main", e -> UI.getCurrent().getPage().open("main"));
        Button newAirportButton = new Button("New Airport", e -> newAirportDialog().open());
        Button refreshButton = new Button("Refresh", e -> refreshAirportsGrid());
        backToMainButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        newAirportButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        refreshButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        add(backToMainButton);
        add(newAirportButton);
        add(refreshButton);
    }

    private void addGrid() {
        airportsGrid.addColumn(Airport::getIataCode).setHeader("IATA code").setSortable(true);
        airportsGrid.addColumn(Airport::getCountry).setHeader("Country").setSortable(true);
        airportsGrid.addColumn(Airport::getCity).setHeader("City").setSortable(true);
        airportsGrid.addColumn(airport -> airportService.getWeatherForAirport(airport)).setHeader("Weather").setSortable(true);
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
        } catch (Exception e) {
            Notification.show("Exception when trying to remove an airport: " + e);
        } finally {
            refreshAirportsGrid();
        }
    }

    private Dialog newAirportDialog() {
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

    private void refreshAirportsGrid() {
        airportsGrid.setItems(airportService.getAirports());
    }

    private Button createSaveAirportButton(Dialog newAirportDialog, TextField countryTextField,     //TODO to validate this input
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

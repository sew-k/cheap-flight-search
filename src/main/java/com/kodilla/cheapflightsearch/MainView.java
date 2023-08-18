package com.kodilla.cheapflightsearch;

import com.kodilla.cheapflightsearch.service.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route(value = "main")
public class MainView extends VerticalLayout {
    @Autowired
    SecurityService securityService;

    public MainView(SecurityService securityService) {
        this.securityService = securityService;
        this.setAlignItems(Alignment.CENTER);
        HorizontalLayout authenticationLayout = new HorizontalLayout();
        String username = securityService.getAuthenticatedUser().getUsername();
        Button loginButton = new Button("Sign in", e -> UI.getCurrent().getPage().open("login"));
        Button logoutButton = new Button("Log out ", e -> securityService.logout());
        if (username == null) {
            logoutButton.setEnabled(false);
        } else {
            logoutButton.setEnabled(true);
            logoutButton.setText("Log out: " + username);
        }
        authenticationLayout.add(loginButton, logoutButton);
        authenticationLayout.setAlignItems(Alignment.STRETCH);
        add(authenticationLayout);
        add(new Label("CHEAP-FLIGHT-SEARCH"));
        add(new Button("Quick search", e -> UI.getCurrent().getPage().open("main/quick_search")));
        add(new Button("User", e -> UI.getCurrent().getPage().open("main/users")));
        add(new Button("Routes", e -> UI.getCurrent().getPage().open("main/routes")));
        add(new Button("Airports", e -> UI.getCurrent().getPage().open("main/airports")));
        add(new Button("Calendar", e -> UI.getCurrent().getPage().open("main/calendar")));
        add(new Button("Itineraries", e -> UI.getCurrent().getPage().open("main/itineraries")));
    }
}

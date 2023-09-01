package com.kodilla.cheapflightsearch;

import com.kodilla.cheapflightsearch.service.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route(value = "main")
@RouteAlias(value = "")
public class MainView extends VerticalLayout {

    private final SecurityService securityService;

    public MainView(SecurityService securityService) {
        this.securityService = securityService;
        this.setAlignItems(Alignment.CENTER);
        HorizontalLayout authenticationLayout = new HorizontalLayout();
        String username = null;
        try {
            username = securityService.getAuthenticatedUser().getUsername();
        } catch (Exception e) {
            Notification.show("No user logged in. Using application as Anonymous.");
        }
        Button signupButton = new Button("Sign up", e -> UI.getCurrent().getPage().open("signup"));
        Button loginButton = new Button("Log in", e -> UI.getCurrent().getPage().open("login"));
        Button logoutButton = new Button("Log out ", e -> securityService.logout());
        if (username == null) {
            logoutButton.setEnabled(false);
        } else {
            logoutButton.setEnabled(true);
            logoutButton.setText("Log out: " + username);
        }
        authenticationLayout.add(signupButton, loginButton, logoutButton);
        authenticationLayout.setAlignItems(Alignment.STRETCH);
        add(authenticationLayout);
        add(new H1("CheapFlightSearch"));
        add(new Button("Quick search", e -> UI.getCurrent().getPage().open("main/quick_search")));
        add(new Button("User", e -> UI.getCurrent().getPage().open("main/users")));
        add(new Button("Routes", e -> UI.getCurrent().getPage().open("main/routes")));
        add(new Button("Airports", e -> UI.getCurrent().getPage().open("main/airports")));
        add(new Button("Calendar", e -> UI.getCurrent().getPage().open("main/calendar")));
        add(new Button("Itineraries", e -> UI.getCurrent().getPage().open("main/itineraries")));
    }
}

package com.kodilla.cheapflightsearch;

import com.kodilla.cheapflightsearch.domain.user.UserRole;
import com.kodilla.cheapflightsearch.service.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AnonymousAllowed
@Route(value = "main")
@RouteAlias(value = "")
public class MainView extends VerticalLayout {
    private final SecurityService securityService;
    private String currentUsername;
    private final float MINIMUM_MENU_BUTTONS_WIDTH = 200f;

    public MainView(SecurityService securityService) {
        this.securityService = securityService;
        this.setAlignItems(Alignment.CENTER);
        addAuthenticationLayout();
        addMainTitle();
        addMenuButtons();
    }

    private void addAuthenticationLayout() {
        HorizontalLayout authenticationLayout = new HorizontalLayout();
        setCurrentUsername();
        Button signupButton = new Button("Sign up", e -> UI.getCurrent().getPage().open("signup"));
        Button loginButton = new Button("Log in", e -> UI.getCurrent().getPage().open("login"));
        Button logoutButton = new Button("Log out ", e -> securityService.logout());
        if (!securityService.isCurrentUserAllowed()) {
            logoutButton.setEnabled(false);
        } else {
            logoutButton.setEnabled(true);
            logoutButton.setText("Log out: " + currentUsername);
        }
        authenticationLayout.add(signupButton, loginButton, logoutButton);
        authenticationLayout.setAlignItems(Alignment.STRETCH);
        add(authenticationLayout);
    }

    private void addMainTitle() {
        add(new H1("CheapFlightSearch"));
    }

    private void addMenuButtons() {
        Button quickSearchButton = new Button("Quick search",
                e -> UI.getCurrent().getPage().open("main/quick_search"));
        Button myItinerariesButton = new Button("My Itineraries",
                e -> UI.getCurrent().getPage().open("main/itineraries"));
        Button myRoutesButton = new Button("My Routes",
                e -> UI.getCurrent().getPage().open("main/routes"));
        Button myCalendarButton = new Button("My Calendar",
                e -> UI.getCurrent().getPage().open("main/calendar"));
        quickSearchButton.setMinWidth(MINIMUM_MENU_BUTTONS_WIDTH, Unit.PIXELS);
        myItinerariesButton.setMinWidth(MINIMUM_MENU_BUTTONS_WIDTH, Unit.PIXELS);
        myRoutesButton.setMinWidth(MINIMUM_MENU_BUTTONS_WIDTH, Unit.PIXELS);
        myCalendarButton.setMinWidth(MINIMUM_MENU_BUTTONS_WIDTH, Unit.PIXELS);
        add(quickSearchButton);
        add(myItinerariesButton);
        add(myRoutesButton);
        add(myCalendarButton);

        ifLoggedUserIsAdministratorAddOptions();
    }

    private void setCurrentUsername() {
        if (securityService.isCurrentUserAllowed()) {
            currentUsername = securityService.getAuthenticatedUser().getUsername();
        } else {
            Notification.show("No user logged in. Using application as Anonymous.");
        }
    }

    private void ifLoggedUserIsAdministratorAddOptions() {
        if (securityService.isCurrentUserAllowed() && securityService.getAuthenticatedUser().getAuthorities()
                .contains(adminSimpleGrantedAuthority())) {
            Button manageUsersButton = new Button("Manage Users",
                    e -> UI.getCurrent().getPage().open("main/users"));
            Button airports = new Button("Airports",
                    e -> UI.getCurrent().getPage().open("main/airports"));
            manageUsersButton.setMinWidth(MINIMUM_MENU_BUTTONS_WIDTH, Unit.PIXELS);
            airports.setMinWidth(MINIMUM_MENU_BUTTONS_WIDTH, Unit.PIXELS);
            add(manageUsersButton);
            add(airports);
        }
    }

    private SimpleGrantedAuthority adminSimpleGrantedAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + UserRole.ADMIN);
    }
}

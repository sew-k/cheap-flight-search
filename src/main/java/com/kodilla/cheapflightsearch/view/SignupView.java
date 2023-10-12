package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserRole;
import com.kodilla.cheapflightsearch.exception.UserAlreadyExistsException;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_SUBMIT_COMPONENTS_WIDTH;

@Route(value = "signup")
@PageTitle("Sign up | CheapFlightSearch")
@AnonymousAllowed
public class SignupView extends VerticalLayout {
    private TextField usernameField = new TextField("Username");
    private TextField emailField = new TextField("Email");
    private PasswordField passwordField = new PasswordField("Password");
    @Autowired
    UserService userService;

    public SignupView() {
        addClassName("login_view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addTitle();
        addTextFields();
        addSubmitButton();
    }

    private void addTitle() {
        add(new H1("CheapFlightSearch"));
        add(new H2("Sign up"));
    }

    private void addTextFields() {
        usernameField.setMinWidth(MINIMUM_SUBMIT_COMPONENTS_WIDTH, Unit.PIXELS);
        emailField.setMinWidth(MINIMUM_SUBMIT_COMPONENTS_WIDTH, Unit.PIXELS);
        passwordField.setMinWidth(MINIMUM_SUBMIT_COMPONENTS_WIDTH, Unit.PIXELS);
        usernameField.setAutofocus(true);
        add(usernameField);
        add(emailField);
        add(passwordField);
    }

    private void addSubmitButton() {
        Button submitButton = new Button("Submit", event -> tryToSubmit());
        submitButton.setAutofocus(true);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.setMinWidth(MINIMUM_SUBMIT_COMPONENTS_WIDTH, Unit.PIXELS);
        add(submitButton);
    }

    private void tryToSubmit() {
        try {
            submit();
        } catch (UserAlreadyExistsException e) {
            Notification.show("User " + usernameField.getValue() + " already exists in database!");
            clearTextFields();
        }
    }

    private void submit() throws UserAlreadyExistsException {
        User newUser = new User(
                usernameField.getValue(),
                emailField.getValue(),
                UserRole.USER,
                passwordField.getValue(),
                new Calendar()
        );
        if (!userExists(newUser)) {
            saveNewUser(newUser);
            UI.getCurrent().getPage().open("login");
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    private boolean userExists(User user) {
        return userService.checkIfUserExists(user);
    }

    private void saveNewUser(User user) {
        User savedUser = userService.createUser(user);
        Notification.show("User " + savedUser.getUsername() + ", saved to database.");
    }

    private void clearTextFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
    }
}

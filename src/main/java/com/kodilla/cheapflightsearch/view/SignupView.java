package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.UserAlreadyExistsException;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "signup")
@PageTitle("Sign up | CheapFlightSearch")
@AnonymousAllowed
public class SignupView extends VerticalLayout {
    private TextField usernameField = new TextField("Username");
    private TextField emailField = new TextField("Email");
    private TextField passwordField = new TextField("Password");
    @Autowired
    UserService userService;

    public SignupView() {
        addClassName("login_view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(usernameField);
        add(emailField);
        add(passwordField);
        add(new Button("Submit", event -> {
            try {
                submit();
            } catch (UserAlreadyExistsException e) {
                Notification.show("User " + usernameField.getValue() + " already exists in database!");
                clearTextFields();
            }
        }));
    }

    private void submit() throws UserAlreadyExistsException {
        User newUser = new User(
                usernameField.getValue(),
                emailField.getValue()
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

package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_OPTION_BUTTONS_WIDTH;

@RolesAllowed("ADMIN")
@Route(value = "main/users")
public class UserView extends VerticalLayout {
    private Grid<User> userGrid = new Grid<>(User.class, false);
    private User selectedUser = null;
    @Autowired
    UserService userService;

    public UserView() {
        addOptionButtons();
        addUsersGrid();
    }

    private void addOptionButtons() {
        Button backToMainButton = new Button("Back to Main", e -> UI.getCurrent().getPage().open("main"));
        Button showAllUsersButton = new Button("Show all users", e -> {
            refreshUserGrid();
            Notification.show("Showing all users");
        });
        backToMainButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        showAllUsersButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        add(backToMainButton);
        add(showAllUsersButton);
    }

    private void addUsersGrid() {
        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        userGrid.addSelectionListener(selection -> selectUser(selection));
        userGrid.addColumn(User::getUserId).setHeader("User ID");
        userGrid.addColumn(User::getUsername).setHeader("Username");
        userGrid.addColumn(User::getEmail).setHeader("e-mail");
        userGrid.addColumn(User::getRole).setHeader("User role");
        userGrid.addColumn(u -> u.getCalendar().getCalendarId()).setHeader("Calendar ID");
        userGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, user) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        this.removeUser(user);
                        if (user.equals(this.selectedUser)) {
                            this.selectedUser = null;
                        }
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage");
        add(userGrid);
    }

    private void selectUser(SelectionEvent<Grid<User>, User> selection) {
        Optional<User> selectedUser = selection.getFirstSelectedItem();
        if (selectedUser.isPresent()) {
            this.selectedUser = selectedUser.get();
        }
    }

    private void refreshUserGrid() {
        userGrid.setItems(userService.getUsers());
    }

    private void removeUser(User user) {
        if (user == null)
            return;
        try {
            userService.deleteUser(user.getUserId());
        } catch (Exception e) {
            Notification.show("Exception when trying to remove user: " + e);
        }
        this.refreshUserGrid();
    }
}

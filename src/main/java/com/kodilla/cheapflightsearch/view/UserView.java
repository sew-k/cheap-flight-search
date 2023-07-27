package com.kodilla.cheapflightsearch.view;

import com.helger.commons.callback.exception.IExceptionCallback;
import com.kodilla.cheapflightsearch.controller.UserController;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserDto;
import com.kodilla.cheapflightsearch.mapper.UserMapper;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Route(value= "main/users")
public class UserView extends VerticalLayout {
    private Grid<User> userGrid = new Grid<>(User.class);
    @Autowired
    UserController userController;
    @Autowired
    UserMapper userMapper;
//    @Autowired
//    UserService userService;
    public UserView() {
        add(new Button("Add User", e -> {
            userController.createUser(new UserDto("John1", "john1@test.com"));
            Notification.show("user added");
        }));
        add(new Button("Select User profile", e -> Notification.show("User selected")));
        add(new Button("Delete selected", e -> {
            removeSelectedUsers();
            Notification.show("User deleted");
        }));
//        add(new Button("Incognito mode", e -> Notification.show("Incognito mode - no user selected - without notifications")));
        add(new Button("Show all users", e -> {
            refreshUserGrid();
            Notification.show("Showing all users");
        }));
//        userGrid.setColumns("username", "email");

        userGrid.setColumns("userId", "username", "email");
        userGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        add(userGrid);
        setSizeFull();
        try {
            Thread.sleep(1000);
            refreshUserGrid();
        } catch (Exception e) {

        }
    }
    private void refreshUserGrid() {
//        userGrid.setItems(userService.getUsers());
        userGrid.setItems(userMapper.mapToUserList(userController.getUsers().getBody()));
    }
    private void removeSelectedUsers() {
        Set<User> usersToRemove = new HashSet<>(userGrid.getSelectedItems());
        for (User user: usersToRemove) {
            try {
                userController.deleteUser(user.getUserId());
            } catch (Exception e) {
                                                //TODO - catch exception
            }
        }
        refreshUserGrid();
    }

}

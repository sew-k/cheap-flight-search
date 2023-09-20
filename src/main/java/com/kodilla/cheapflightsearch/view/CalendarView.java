package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.UserNotFoundException;
import com.kodilla.cheapflightsearch.service.CalendarService;
import com.kodilla.cheapflightsearch.service.SecurityService;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;

@PermitAll
@Route(value = "main/calendar")
public class CalendarView extends VerticalLayout {
    private Calendar currentCalendar;
    private User currentUser = null;
    private LocalDate beginDate;
    private LocalDate endDate;
    private DatePicker beginDatePicker = new DatePicker("Begin Holiday date");
    private DatePicker endDatePicker = new DatePicker("End Holiday date");
    private Grid<HolidayPlan> holidaysGrid = new Grid<>(HolidayPlan.class);
    @Autowired
    CalendarService calendarService;
    @Autowired
    SecurityService securityService;
    @Autowired
    UserService userService;

    public CalendarView() {
        beginDatePicker.setWeekNumbersVisible(true);
        beginDatePicker.setValue(LocalDate.now());
        endDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setValue(LocalDate.now().plusDays(1l));
        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        add(new Button("Refresh all", e -> {
            setCurrentUser();
            setCurrentCalendar();
            refreshHolidaysGridForCalendar();
        }));
        Button addHolidaysButton = new Button("Add to calendar", event -> {
            readViewForms();
            setNewHolidayPlanInCalendar();
            refreshHolidaysGridForCalendar();
        });
        HorizontalLayout addHolidaysToCalendarLayout = new HorizontalLayout(
                beginDatePicker,
                endDatePicker,
                addHolidaysButton
        );
        add(addHolidaysToCalendarLayout);
        holidaysGrid.setColumns("beginDate", "endDate");
        holidaysGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, holidayPlan) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> this.removeHolidayPlan(holidayPlan));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setHeader("Manage holiday plans");
        add(holidaysGrid);
    }

    public void refreshHolidaysGridForCalendar() {
//        holidaysGrid.setItems(getCurrentCalendar().getHolidayPlanList());
        holidaysGrid.setItems(calendarService.getHolidayPlansByUser(currentUser));
//        tripPlanGrid.setItems(itineraryService.getTripPlansByUser(currentUser));
    }

    public void setCurrentCalendar() {
        this.currentCalendar = currentUser.getCalendar();
    }

    public Calendar getCurrentCalendar() {
        return this.currentCalendar;
    }

    private void removeHolidayPlan(HolidayPlan holidayPlan) {
        if (holidayPlan == null)
            return;
        try {
            calendarService.updateCalendar(
                    calendarService.removeHolidayPlanFromCalendar(
                            this.getCurrentCalendar(),
                            holidayPlan)
            );
        } catch (Exception e) {
            Notification.show("Exception when trying to remove holiday plan: " + e);
        }
        this.refreshHolidaysGridForCalendar();
    }

    private void setCurrentUser() {
        try {
            currentUser = userService.getUserByName(securityService.getAuthenticatedUser().getUsername());
        } catch (UserNotFoundException e) {
            Notification.show("User not found: " + e);
        }
    }

    private void readViewForms() {
        this.beginDate = beginDatePicker.getValue();
        this.endDate = endDatePicker.getValue();
    }

    private void setNewHolidayPlanInCalendar() {
        calendarService.setNewHolidayPlanInCalendar(
                currentCalendar.getCalendarId(),
                new HolidayPlan(
                        beginDatePicker.getValue(),
                        endDatePicker.getValue()
                )
        );
    }
}

package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.exception.UserNotFoundException;
import com.kodilla.cheapflightsearch.service.CalendarService;
import com.kodilla.cheapflightsearch.service.SecurityService;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
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

import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_DATE_COMPONENTS_WIDTH;
import static com.kodilla.cheapflightsearch.view.ViewsConfig.MINIMUM_OPTION_BUTTONS_WIDTH;

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
        addOptionButtons();
        addInputComponents();
        addHolidaysGrid();
    }

    private void addOptionButtons() {
        Button backToMainButton = new Button("Back to Main", e -> UI.getCurrent().getPage().open("main"));
        Button refreshButton = new Button("Refresh", e -> {
            setCurrentUser();
            setCurrentCalendar();
            refreshAll();
        });
        backToMainButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        refreshButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        add(backToMainButton);
        add(refreshButton);
    }

    private void addInputComponents() {
        setupDatePickerComponents();
        Button addHolidaysButton = new Button("Add to calendar", e -> {
            readViewForms();
            setNewHolidayPlanInCalendar();
            refreshHolidaysGridForCalendar();
        });
        addHolidaysButton.setMinWidth(MINIMUM_OPTION_BUTTONS_WIDTH, Unit.PIXELS);
        HorizontalLayout inputHolidaysToCalendarLayout = new HorizontalLayout(
                beginDatePicker,
                endDatePicker,
                addHolidaysButton
        );
        inputHolidaysToCalendarLayout.setAlignItems(Alignment.BASELINE);
        add(inputHolidaysToCalendarLayout);
    }

    private void setupDatePickerComponents() {
        beginDatePicker.setWeekNumbersVisible(true);
        beginDatePicker.setValue(LocalDate.now());
        beginDatePicker.setMinWidth(MINIMUM_DATE_COMPONENTS_WIDTH, Unit.PIXELS);
        endDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setValue(LocalDate.now().plusDays(1L));
        endDatePicker.setMinWidth(MINIMUM_DATE_COMPONENTS_WIDTH, Unit.PIXELS);
    }

    private void addHolidaysGrid() {
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

    private void refreshAll() {
        refreshHolidaysGridForCalendar();
    }

    public void refreshHolidaysGridForCalendar() {
        holidaysGrid.setItems(calendarService.getHolidayPlansByUser(currentUser));
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
                            getCurrentCalendar(),
                            holidayPlan)
            );
            setCurrentCalendar();
        } catch (Exception e) {
            Notification.show("Exception when trying to remove holiday plan: " + e);
        }
        refreshAll();
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
        HolidayPlan holidayPlanToSet = new HolidayPlan(
                beginDatePicker.getValue(),
                endDatePicker.getValue()
        );
        try {
            calendarService.setNewHolidayPlanInCalendar(
                    currentCalendar.getCalendarId(),
                    holidayPlanToSet
            );
        } catch (Exception e) {
            Notification.show("Exception: " + e);
        } finally {
            refreshAll();
        }
    }
}

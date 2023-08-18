package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.service.CalendarService;
import com.kodilla.cheapflightsearch.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
@PermitAll
@Route(value = "main/calendar")
public class CalendarView extends VerticalLayout {
    @Autowired
    CalendarService calendarService;
    private Calendar calendar;                                 //TODO temporarily. To associate with User

    private Grid<HolidayPlan> holidaysGrid = new Grid<>(HolidayPlan.class);

    public CalendarView() {
        DatePicker beginDatePicker = new DatePicker("Begin Holiday date");
        beginDatePicker.setWeekNumbersVisible(true);
        beginDatePicker.setValue(LocalDate.of(2023, 8, 4));                     //TODO temporarily stubbed
        DatePicker endDatePicker = new DatePicker("End Holiday date");
        endDatePicker.setWeekNumbersVisible(true);
        endDatePicker.setValue(LocalDate.of(2023, 8, 6));                       //TODO temporarily stubbed
        HorizontalLayout addHolidaysToCalendarLayout = new HorizontalLayout(
                beginDatePicker,
                endDatePicker
        );
        add(new Button("Back to Main", e -> UI.getCurrent().getPage().open("main")));
        TextField currentUserTextField = new TextField("Current User");
        if (UserService.getCurrentUser() != null) {
            currentUserTextField.setValue(UserService.getCurrentUser().getUsername());
        }
        currentUserTextField.setEnabled(false);
        add(currentUserTextField);
        add(new Button("Get calendar", c -> {
            setCurrentUserCalendarToCurrent();
            refreshHolidaysGridForCalendar();
        }));
        add(addHolidaysToCalendarLayout);
        add(new Button("Add to calendar", h -> {
            try {
                calendarService.setNewHolidayPlanInCalendar(
                        calendar.getCalendarId(),
                        new HolidayPlan(
                                beginDatePicker.getValue(),
                                endDatePicker.getValue()
                        )
                );
            } catch (Exception e) {
                Notification.show("Exception when adding new holiday plan to calendar: " + e);
            } finally {
                refreshHolidaysGridForCalendar();
            }
        }));
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
        try {
            holidaysGrid.setItems(getCurrentCalendar().getHolidayPlanList());
        } catch (Exception e) {
            Notification.show("Exception when trying to refresh: " + e);
        }
    }

    public void setCurrentUserCalendarToCurrent() {
        this.calendar = UserService.getCurrentUser().getCalendar();
    }

    public Calendar getCurrentCalendar() {
        return this.calendar;
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
}

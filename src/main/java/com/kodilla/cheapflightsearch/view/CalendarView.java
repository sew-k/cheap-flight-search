package com.kodilla.cheapflightsearch.view;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.service.CalendarService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

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
        add(new Button("New calendar", c -> {
            calendar = calendarService.createCalendar(new Calendar());
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
                refreshHolidaysGridForCalendar(calendar.getCalendarId());
            }
        }));
        add(new Button("Refresh", e -> refreshHolidaysGridForCalendar(calendar.getCalendarId())));
        holidaysGrid.setColumns("beginDate", "endDate");
        add(holidaysGrid);
    }

    public void refreshHolidaysGridForCalendar(Long id) {
        try {
            holidaysGrid.setItems(calendarService.getCalendar(id).getHolidayPlanList());
        } catch (Exception e) {
            Notification.show("Exception when trying to refresh: " + e);
        }
    }
}

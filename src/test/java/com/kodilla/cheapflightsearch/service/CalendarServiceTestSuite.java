package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CalendarServiceTestSuite {
    @Autowired
    CalendarService calendarService;
    @Test
    void testCreateCalendar() {
        //Given
        Calendar calendar = new Calendar();
        Long id;

        //When
        calendarService.createCalendar(calendar);
        id = calendar.getCalendarId();

        //Then
        assertTrue(calendarService.isCalendarExisting(id));

        //CleanUp
        try {
            calendarService.deleteCalendar(id);
        } catch (Exception e) {

        }
    }

    @Test
    void getCalendars() {
    }

    @Test
    void getCalendar() {
    }

    @Test
    void deleteCalendar() {
    }

    @Test
    void updateCalendar() {
    }

    @Test
    void createCalendar() {
    }

    @Test
    void testSetNewHolidayPlanInCalendar() throws Exception {
        //Given
        HolidayPlan holidayPlan = new HolidayPlan(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,1,2)
        );
        Calendar calendar = new Calendar();
        calendar = calendarService.createCalendar(calendar);
        Long calendarId = calendar.getCalendarId();

        //When
        calendarService.setNewHolidayPlanInCalendar(calendarId, holidayPlan);

        //Then
        assertTrue(calendarService.isCalendarExisting(calendarId));
        assertFalse(calendarService.getCalendar(calendarId).getHolidayPlanList().isEmpty());
        assertEquals(1, calendarService.getCalendar(calendarId).getHolidayPlanList().size());

        //CleanUp
        calendarService.deleteCalendar(calendarId);
    }

    @Test
    void removeHolidayPlanFromCalendar() {
    }

    @Test
    void isCalendarExisting() {
    }
}
package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserRole;
import com.kodilla.cheapflightsearch.exception.CalendarNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalendarServiceTestSuite {
    @Autowired
    CalendarService calendarService;
    @Autowired
    UserService userService;
    private Long calendarId1;
    private Long userId;
    private Calendar calendar1;
    private User user;

    @BeforeEach
    void setUp() {
        calendar1 = new Calendar();
        HolidayPlan holidayPlan1 = new HolidayPlan(
                LocalDate.of(2030, 1, 1),
                LocalDate.of(2030, 1, 2)
        );
        HolidayPlan holidayPlan2 = new HolidayPlan(
                LocalDate.of(2030, 2, 1),
                LocalDate.of(2030, 2, 2)
        );
        HolidayPlan holidayPlan3 = new HolidayPlan(
                LocalDate.of(2030, 3, 1),
                LocalDate.of(2030, 3, 2)
        );
        List<HolidayPlan> holidayPlans = new ArrayList<>();
        holidayPlans.add(holidayPlan1);
        holidayPlans.add(holidayPlan2);
        holidayPlans.add(holidayPlan3);
        calendar1.setHolidayPlanList(holidayPlans);
        user = new User("username", "email", UserRole.USER, "password", calendar1);
        userService.createUser(user);
        calendarId1 = calendar1.getCalendarId();
        userId = user.getUserId();
    }

    @AfterEach
    void tearDown() {
        try {
            userService.deleteUser(userId);
            calendarService.deleteCalendar(calendarId1);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

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
    void testGetCalendars() {
        //When
        List<Calendar> fetchedCalendars = calendarService.getCalendars();

        //Then
        assertFalse(fetchedCalendars.isEmpty());
        assertTrue(fetchedCalendars.size() >= 1);
        assertEquals(calendarId1, fetchedCalendars.get(0).getCalendarId());
    }

    @Test
    void testGetCalendar() {
        //When
        Calendar fetchedCalendar = null;
        try {
            fetchedCalendar = calendarService.getCalendar(calendarId1);
        } catch (Exception e) {

        }

        //Then
        assertDoesNotThrow(() -> calendarService.getCalendar(calendarId1));
        assertTrue(fetchedCalendar != null);
        assertEquals(calendarId1, fetchedCalendar.getCalendarId());
    }

    @Test
    void testDeleteCalendar() {
        //When
        Calendar calendar2 = new Calendar();
        calendarService.createCalendar(calendar2);
        Long calendarId2 = calendar2.getCalendarId();

        //Then
        assertDoesNotThrow(() -> calendarService.deleteCalendar(calendarId2));
        assertThrows(CalendarNotFoundException.class, () -> calendarService.getCalendar(calendarId2));
    }

    @Test
    void testUpdateCalendar() throws Exception {
        //Given
        List<HolidayPlan> emptyHolidayPlanList = new ArrayList<>();
        Calendar calendarToUpdate = calendarService.getCalendar(calendarId1);
        calendarToUpdate.setHolidayPlanList(emptyHolidayPlanList);

        //When
        Optional<Calendar> updatedCalendarOptional =
                Optional.of(calendarService.updateCalendar(calendarId1, calendarToUpdate));

        //Then
        assertTrue(updatedCalendarOptional.isPresent());
        assertEquals(0, updatedCalendarOptional.get().getHolidayPlanList().size());
    }

    @Test
    void testSetNewHolidayPlanInCalendar() throws Exception {
        //Given
        HolidayPlan holidayPlan4 = new HolidayPlan(
                LocalDate.of(2030, 4, 1),
                LocalDate.of(2030, 4, 2)
        );

        //When
        calendarService.setNewHolidayPlanInCalendar(calendarId1, holidayPlan4);

        //Then
        assertTrue(calendarService.isCalendarExisting(calendarId1));
        assertFalse(calendarService.getCalendar(calendarId1).getHolidayPlanList().isEmpty());
        assertEquals(4, calendarService.getCalendar(calendarId1).getHolidayPlanList().size());
    }

    @Test
    void testRemoveHolidayPlanFromCalendar() throws Exception {
        //Given
        HolidayPlan holidayPlanToRemove = calendarService.getCalendar(calendarId1).getHolidayPlanList().get(0);

        //When
        calendarService.removeHolidayPlanFromCalendar(calendar1, holidayPlanToRemove);

        //Then
        assertEquals(2, calendarService.getCalendar(calendarId1).getHolidayPlanList().size());
    }

    @Test
    void testIsCalendarExisting() {
        //When & Then
        assertTrue(calendarService.isCalendarExisting(calendarId1));
    }

    @Test
    void testGetHolidayPlansByUser() {
        //When
        List<HolidayPlan> fetchedHolidayPlans = calendarService.getHolidayPlansByUser(user);

        //Then
        assertEquals(3, fetchedHolidayPlans.size());
    }
}
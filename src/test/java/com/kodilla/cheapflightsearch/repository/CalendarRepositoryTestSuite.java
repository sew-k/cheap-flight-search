package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalendarRepositoryTestSuite {
    @Autowired
    CalendarRepository calendarRepository;

    @Test
    void testFindAll() {
        //Given
        Calendar calendar1 = new Calendar();
        Calendar calendar2 = new Calendar();
        calendarRepository.save(calendar1);
        calendarRepository.save(calendar2);
        Long id1 = calendar1.getCalendarId();
        Long id2 = calendar2.getCalendarId();

        //When
        List<Calendar> fetchedCalendars = calendarRepository.findAll();

        //Then
        assertFalse(fetchedCalendars.isEmpty());
        assertTrue(fetchedCalendars.size() >= 2);

        //CleanUp
        calendarRepository.deleteById(id1);
        calendarRepository.deleteById(id2);
    }

    @Test
    void testSave() {
        //Given
        Calendar calendar3 = new Calendar();

        //When
        calendarRepository.save(calendar3);
        Long id3 = calendar3.getCalendarId();

        //Then
        assertTrue(calendarRepository.existsById(id3));

        //CleanUp
        calendarRepository.deleteById(id3);
    }

    @Test
    void testFindById() {
        //Given
        Calendar calendar4 = new Calendar();
        calendarRepository.save(calendar4);
        Long id4 = calendar4.getCalendarId();

        //When
        Optional<Calendar> fetchedCalendar = calendarRepository.findById(id4);

        //Then
        assertTrue(fetchedCalendar.isPresent());
        assertEquals(id4, fetchedCalendar.get().getCalendarId());

        //CleanUp
        calendarRepository.deleteById(id4);
    }

    @Test
    void testFindById_notExisting() {
        //Given & When
        Optional<Calendar> fetchedCalendar = calendarRepository.findById(99999L);

        //Then
        assertFalse(fetchedCalendar.isPresent());
    }

    @Test
    void testDeleteById() {
        //Given
        Calendar calendar5 = new Calendar();
        calendarRepository.save(calendar5);
        Long id5 = calendar5.getCalendarId();

        //When
        calendarRepository.deleteById(id5);
        Optional<Calendar> deletedCalendar = calendarRepository.findById(id5);


        //Then
        assertFalse(deletedCalendar.isPresent());

        //CleanUp
        try {
            calendarRepository.deleteById(id5);
        } catch (Exception e) {

        }
    }

    @Test
    void testExistsById() {
        //Given
        Calendar calendar6 = new Calendar();
        calendarRepository.save(calendar6);
        Long id6 = calendar6.getCalendarId();

        //When & Then
        assertTrue(calendarRepository.existsById(id6));

        //CleanUp
        calendarRepository.deleteById(id6);
    }

    @Test
    void testExistsById_notExisting() {
        //Given & When & Then
        assertFalse(calendarRepository.existsById(99999L));
    }
}
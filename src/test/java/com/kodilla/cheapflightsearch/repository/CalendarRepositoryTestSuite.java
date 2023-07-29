package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CalendarRepositoryTestSuite {
    @Autowired
    CalendarRepository calendarRepository;

    @Test
    void findAll() {
    }

    @Test
    void testSave() {
        //Given
        Calendar calendar = new Calendar();

        //When
        calendarRepository.save(calendar);
        Long id = calendar.getCalendarId();

        //Then
        assertTrue(calendarRepository.existsById(id));

        //CleanUp
        calendarRepository.deleteById(id);
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void existsById() {
    }
}
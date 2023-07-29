package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class HolidayPlanRepositoryTestSuite {
    @Autowired
    HolidayPlanRepository holidayPlanRepository;

    @Test
    void testFindAll() {
        //Given

        //When

        //Then

    }

    @Test
    void save() {
        //Given
        HolidayPlan holidayPlan = new HolidayPlan(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,1,2)
        );

        //When
        holidayPlanRepository.save(holidayPlan);
        Long holidayPlanId = holidayPlan.getHolidayPlanId();

        //Then
        assertTrue(holidayPlanRepository.existsById(holidayPlanId));

        //CleanUp
        holidayPlanRepository.deleteById(holidayPlanId);
    }

    @Test
    void findById() {
        //Given

        //When

        //Then

    }

    @Test
    void deleteById() {
        //Given

        //When

        //Then

    }
}
package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HolidayPlanRepositoryTestSuite {
    @Autowired
    HolidayPlanRepository holidayPlanRepository;

    @Test
    void testFindAll() {
        //Given
        HolidayPlan holidayPlan1 = new HolidayPlan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2)
        );
        HolidayPlan holidayPlan2 = new HolidayPlan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2)
        );
        holidayPlanRepository.save(holidayPlan1);
        holidayPlanRepository.save(holidayPlan2);
        Long holidayPlanId1 = holidayPlan1.getHolidayPlanId();
        Long holidayPlanId2 = holidayPlan2.getHolidayPlanId();

        //When
        List<HolidayPlan> fetchedHolidayPlans = holidayPlanRepository.findAll();

        //Then
        assertFalse(fetchedHolidayPlans.isEmpty());
        assertTrue(fetchedHolidayPlans.size() >= 2);
        assertTrue(holidayPlanRepository.existsById(holidayPlanId1));
        assertTrue(holidayPlanRepository.existsById(holidayPlanId2));

        //CleanUp
        holidayPlanRepository.deleteById(holidayPlanId1);
        holidayPlanRepository.deleteById(holidayPlanId2);
    }

    @Test
    void testSave() {
        //Given
        HolidayPlan holidayPlan3 = new HolidayPlan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2)
        );

        //When
        holidayPlanRepository.save(holidayPlan3);
        Long holidayPlanId3 = holidayPlan3.getHolidayPlanId();

        //Then
        assertTrue(holidayPlanRepository.existsById(holidayPlanId3));

        //CleanUp
        holidayPlanRepository.deleteById(holidayPlanId3);
    }

    @Test
    void testFindById() {
        //Given
        HolidayPlan holidayPlan4 = new HolidayPlan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2)
        );
        holidayPlanRepository.save(holidayPlan4);
        Long holidayPlanId4 = holidayPlan4.getHolidayPlanId();

        //When
        Optional<HolidayPlan> fetchedHolidayPlan = holidayPlanRepository.findById(holidayPlanId4);

        //Then
        assertTrue(fetchedHolidayPlan.isPresent());
        assertEquals(holidayPlanId4, fetchedHolidayPlan.get().getHolidayPlanId());

        //CleanUp
        holidayPlanRepository.deleteById(holidayPlanId4);
    }

    @Test
    void testFindById_notExisting() {
        //Given & When
        Optional<HolidayPlan> fetchedHolidayPlan = holidayPlanRepository.findById(9999L);

        //Then
        assertFalse(fetchedHolidayPlan.isPresent());
    }

    @Test
    void testDeleteById() {
        //Given
        HolidayPlan holidayPlan5 = new HolidayPlan(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2)
        );
        holidayPlanRepository.save(holidayPlan5);
        Long holidayPlanId5 = holidayPlan5.getHolidayPlanId();

        //When
        holidayPlanRepository.deleteById(holidayPlanId5);

        //Then
        assertFalse(holidayPlanRepository.existsById(holidayPlanId5));

        //CleanUp
        try {
            holidayPlanRepository.deleteById(holidayPlanId5);
        } catch (Exception e) {

        }
    }
}
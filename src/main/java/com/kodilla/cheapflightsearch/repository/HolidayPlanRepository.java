package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface HolidayPlanRepository extends CrudRepository<HolidayPlan, Long> {
    @Override
    List<HolidayPlan> findAll();
    @Override
    HolidayPlan save(HolidayPlan holidayPlan);
    @Override
    Optional<HolidayPlan> findById(Long id);
    @Override
    void deleteById(Long id);
}

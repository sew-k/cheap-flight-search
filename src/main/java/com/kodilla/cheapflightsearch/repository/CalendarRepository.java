package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CalendarRepository extends CrudRepository<Calendar, Long> {
    @Override
    List<Calendar> findAll();
    @Override
    Calendar save(Calendar calendar);
    @Override
    Optional<Calendar> findById(Long id);
    @Override
    void deleteById(Long id);
    @Override
    boolean existsById(Long id);
}

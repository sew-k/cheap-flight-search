package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import org.springframework.data.repository.CrudRepository;

public interface CalendarRepository extends CrudRepository<Calendar, Long> {
}

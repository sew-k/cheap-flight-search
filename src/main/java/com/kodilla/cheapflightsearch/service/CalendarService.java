package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.controller.CalendarNotFoundException;
import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.repository.CalendarRepository;
import com.kodilla.cheapflightsearch.repository.HolidayPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final HolidayPlanRepository holidayPlanRepository;

    public List<Calendar> getCalendars() {
        return calendarRepository.findAll();
    }

    public Calendar getCalendar(Long id) throws CalendarNotFoundException {
        return calendarRepository.findById(id).orElseThrow(CalendarNotFoundException::new);
    }

    public void deleteCalendar(Long id) throws CalendarNotFoundException {
        if (calendarRepository.findById(id).isPresent()) {
            calendarRepository.deleteById(id);
        } else {
            throw new CalendarNotFoundException();
        }
    }

    public Calendar updateCalendar(Long id, Calendar calendar) throws CalendarNotFoundException {
        if (calendarRepository.findById(id).isPresent()) {
            return calendarRepository.save(calendar);
        } else {
            throw new CalendarNotFoundException();
        }
    }

    public Calendar createCalendar(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    public Calendar setNewHolidayPlanInCalendar(Long calendarId, HolidayPlan holidayPlan) {
        Calendar calendar = calendarRepository.findById(calendarId).orElseThrow();
        calendar.getHolidayPlanList().add(holidayPlan);
        calendar = calendarRepository.save(calendar);
        return calendar;
    }

    public Calendar removeHolidayPlanFromCalendar(Long id, Calendar calendar, HolidayPlan holidayPlan) throws CalendarNotFoundException {
        calendar.getHolidayPlanList().remove(holidayPlanRepository.findById(holidayPlan.getHolidayPlanId()).get());
        return updateCalendar(id, calendar);
    }

    public boolean isCalendarExisting(Long id) {
        return calendarRepository.existsById(id);
    }
}

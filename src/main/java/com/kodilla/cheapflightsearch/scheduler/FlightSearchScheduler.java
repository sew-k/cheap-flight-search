package com.kodilla.cheapflightsearch.scheduler;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.service.CalendarService;
import com.kodilla.cheapflightsearch.service.ItineraryService;
import com.kodilla.cheapflightsearch.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightSearchScheduler {
    private final ItineraryService itineraryService;
    private final CalendarService calendarService;
    private final RouteService routeService;

    @Scheduled(cron = "0 0 0 * * *")
    public void search() throws Exception {
        Long userId = 1L; //TODO - temporarily stubbed

        List<HolidayPlan> holidayPlans = calendarService.getCalendar(userId).getHolidayPlanList();
        List<Route> favouriteRoutes = routeService.getFavouriteRoutes();
        List<Itinerary> itineraryList = itineraryService
                .searchForItinerariesMatchingFavouriteRoutesAndHolidayPlans(
                        holidayPlans,
                        favouriteRoutes,
                        1       //TODO - temporarily stubbed
                );
    }
}

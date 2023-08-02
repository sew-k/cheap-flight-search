package com.kodilla.cheapflightsearch.domain.calendar;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "calendars")
public class Calendar {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "calendar_id", unique = true)
    private Long calendarId;
    @OneToMany(
            targetEntity = HolidayPlan.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "calendar_id")
    private List<HolidayPlan> holidayPlanList = new ArrayList<>();

    public Calendar(List<HolidayPlan> holidayPlanList) {
        this.holidayPlanList = holidayPlanList;
    }
}

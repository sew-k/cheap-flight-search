package com.kodilla.cheapflightsearch.domain.calendar;

import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "calendars")
public class Calendar {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "calendar_id", unique = true)
    private Long calendarId;
    @OneToMany(
            targetEntity = HolidayPlan.class,
            mappedBy = "calendar",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<HolidayPlan> holidayPlanList;
}

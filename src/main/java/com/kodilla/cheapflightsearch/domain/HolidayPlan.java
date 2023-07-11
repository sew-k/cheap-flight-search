package com.kodilla.cheapflightsearch.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "holiday_plans")
public class HolidayPlan {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "holiday_plan_id", unique = true)
    private Long holidayPlanId;
    @NotNull
    @Column(name = "begin_date")
    private LocalDate beginDate;
    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;
    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;
}

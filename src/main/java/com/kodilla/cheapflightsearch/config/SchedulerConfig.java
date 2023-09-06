package com.kodilla.cheapflightsearch.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SchedulerConfig {
    @Value("${scheduler.cron.expression}")
    private String schedulerCronExpression;
}

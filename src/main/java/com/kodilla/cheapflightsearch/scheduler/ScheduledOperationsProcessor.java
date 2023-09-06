package com.kodilla.cheapflightsearch.scheduler;

import com.kodilla.cheapflightsearch.config.SchedulerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledOperationsProcessor {
    private final SchedulerConfig schedulerConfig;
    @Scheduled(cron = "#{schedulerConfig.getSchedulerCronExpression}")
    public void runScheduledOperations() {
        System.out.println("*** Operations started ***"); //TODO - temporarily
    }
}

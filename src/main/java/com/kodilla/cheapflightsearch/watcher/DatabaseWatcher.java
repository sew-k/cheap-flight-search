package com.kodilla.cheapflightsearch.watcher;

import com.kodilla.cheapflightsearch.domain.watcher.DatabaseManipulation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseWatcher {
    private LocalDateTime beginTimestamp;
    private LocalDateTime endTimeStamp;
    private Deque<DatabaseManipulation> databaseManipulationDeque = new ArrayDeque<>();
    private final DatabaseManipulationRepository databaseManipulationRepository;

    @Before("execution(* com.kodilla.cheapflightsearch.repository.*.*(..))")
    public void setBeginTimestamp() {
        beginTimestamp = LocalDateTime.now();
    }

    @After("execution(* com.kodilla.cheapflightsearch.repository.*.*(..))")
    public void logRepositoryMethodsExecution(JoinPoint joinPoint){
        endTimeStamp = LocalDateTime.now();
        log.info("Logging the event caused by: " + joinPoint.getSignature().getDeclaringTypeName()
                + ", method: " + joinPoint.getSignature().getName()
        + ", started: " + beginTimestamp + ", ended: " + endTimeStamp);
        DatabaseManipulation databaseManipulation = DatabaseManipulation.builder()
                .beginTimestamp(beginTimestamp)
                .endTimestamp(endTimeStamp)
                .classDescriptor(joinPoint.getSignature().getDeclaringTypeName())
                .method(joinPoint.getSignature().getName())
                .build();
        databaseManipulationDeque.offer(databaseManipulation);
        saveDatabaseManipulationsToDatabase(databaseManipulationDeque);
    }
    private void saveDatabaseManipulationsToDatabase(Deque<DatabaseManipulation> databaseManipulationDeque) {   //TODO only operations like save/ update/ delete should be saved to database
        while (!databaseManipulationDeque.isEmpty()) {
            databaseManipulationRepository.save(databaseManipulationDeque.poll());
        }
    }
}

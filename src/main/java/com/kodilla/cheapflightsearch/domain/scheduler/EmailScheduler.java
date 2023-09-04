package com.kodilla.cheapflightsearch.domain.scheduler;

import com.kodilla.cheapflightsearch.domain.email.Mail;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.service.SimpleEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailScheduler {
    private final SimpleEmailService simpleEmailService;
    private User mailToUser;
    private User mailToCcUser;
    private String subject;
    private String message;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendInformationEmail() {
        simpleEmailService.sendScheduledFlightSearchEmail(
                new Mail(
                        mailToUser.getEmail(),
                        mailToCcUser.getEmail(),
                        subject,
                        message
                )
        );
    }
}

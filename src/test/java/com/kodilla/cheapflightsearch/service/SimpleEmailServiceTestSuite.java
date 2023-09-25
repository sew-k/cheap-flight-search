package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.email.Mail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleEmailServiceTestSuite {
    @InjectMocks
    SimpleEmailService simpleEmailService;
    @Mock
    JavaMailSender javaMailSender;

    @Test
    void sendScheduledFlightSearchEmail() {
    }

    @Test
    void sendUpcomingTripEmail() {
    }

    @Test
    void testSend() {
        //Given
        Mail testMail = Mail.builder()
                .mailTo("cheapflightsearch.app@gmail.com")
                .subject("cheap-flight-search test mail")
                .message("Test mail message")
                .build();
        SimpleMailMessage simpleTestMail = simpleEmailService.createMailMessage(testMail);

        //When
        try {
            simpleEmailService.send(testMail);
        } catch (Exception e) {

        }

        //Then
        verify(javaMailSender, atLeastOnce()).send(simpleTestMail);
    }

    @Test
    void testSend_shouldThrowException() {
        //Given
        Mail testMailWithoutRecipient = Mail.builder()
                .subject("cheap-flight-search test mail")
                .message("Test mail message")
                .build();

        //When & Then
        assertThrows(MailException.class, () -> simpleEmailService.send(testMailWithoutRecipient));
    }

    @Test
    void testCreateMailMessage() {
        //Given
        Mail testMail = Mail.builder()
                .mailTo("cheapflightsearch.app@gmail.com")
                .mailToCc("other.test@gmail.com")
                .subject("cheap-flight-search test mail")
                .message("Test mail message")
                .build();

        //When
        SimpleMailMessage simpleTestMail = simpleEmailService.createMailMessage(testMail);

        //Then
        assertEquals(testMail.getMailTo(), simpleTestMail.getTo()[0]);
        assertEquals(testMail.getMailToCc(), simpleTestMail.getCc()[0]);
        assertEquals(testMail.getSubject(), simpleTestMail.getSubject());
        assertEquals(testMail.getMessage(), simpleTestMail.getText());
    }

    @Test
    void testCreateMailMessage_shouldThrowException() {
        //Given
        Mail testMailWithoutRecipient = Mail.builder()
                .subject("cheap-flight-search test mail")
                .message("Test mail message")
                .build();

        //When & Then
        assertThrows(MailException.class, () -> simpleEmailService.createMailMessage(testMailWithoutRecipient));
    }
}
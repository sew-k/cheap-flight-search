package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.email.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleEmailService {
    private final JavaMailSender javaMailSender;

    public void sendScheduledFlightSearchEmail(Mail mail) {

    }

    public void sendUpcomingTripEmail(Mail mail) {

    }

    public void send(final Mail mail) throws MailException {
        javaMailSender.send(createMailMessage(mail));
    }

    private SimpleMailMessage createMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        String toCc = mail.getMailToCc();
        Optional<String> optionalToCc = Optional.ofNullable(toCc);
        if (optionalToCc.isPresent()) {
            mailMessage.setCc(mail.getMailToCc());
        }
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());
        return mailMessage;
    }
}

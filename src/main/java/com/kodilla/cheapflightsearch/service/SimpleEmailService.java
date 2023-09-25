package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.email.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
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

    public SimpleMailMessage createMailMessage(final Mail mail) throws MailException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        Optional <String> optionalTo = Optional.ofNullable(mail.getMailTo());
        if (optionalTo.isPresent()) {
            mailMessage.setTo(mail.getMailTo());
        } else {
            throw new MailParseException("No message recipient!");
        }
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

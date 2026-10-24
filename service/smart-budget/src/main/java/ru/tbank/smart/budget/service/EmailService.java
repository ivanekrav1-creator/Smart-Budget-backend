package ru.tbank.smart.budget.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender mailSender;
  private final String mailFrom;

  public EmailService(
      JavaMailSender mailSender, @Value("${smart.budget.mail-from}") String mailFrom) {
    this.mailSender = mailSender;
    this.mailFrom = mailFrom;
  }

  public void sendEmail(String to, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(mailFrom);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
  }
}

package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Implementation of the EmailService interface for sending emails.
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    /**
     * Constructor to initialize the JavaMailSender.
     *
     * @param emailSender the JavaMailSender to use for sending emails
     */
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends an email with the specified recipient, subject, and text.
     *
     * @param to the recipient email address
     * @param subject the subject of the email
     * @param text the text content of the email
     */
    @Override
    public void sendEmail(String to, String subject, String text) {
        log.info("Preparing to send email to: {}, with subject: {}", to, subject);

        try {
            // Create a MIME message
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set the recipient, subject, and text content of the email
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            // Send the email
            emailSender.send(message);
            log.info("Email successfully sent to: {}", to);
        } catch (Exception e) {
            // Log the error and rethrow it
            log.error("Error occurred while sending email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

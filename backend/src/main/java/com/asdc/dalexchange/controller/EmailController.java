package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling email-related requests.
 */
@RestController
@Slf4j
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    /**
     * Endpoint to send an email.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param body    the body of the email
     * @return a success message if the email is sent
     */
    @GetMapping("/sendEmail")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        log.info("Sending email to: {}, Subject: {}", to, subject);
        try {
            emailService.sendEmail(to, subject, body);
            log.info("Email sent successfully to: {}", to);
            return "Email sent successfully";
        } catch (Exception e) {
            log.error("Failed to send email to: {}, Subject: {}", to, subject, e);
            return "Failed to send email";
        }
    }
}

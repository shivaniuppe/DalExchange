package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.model.PasswordResetToken;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.PasswordResetTokenRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Controller for handling password reset requests.
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class PasswordResetController {

    final private EmailService emailService;
    final private UserRepository userRepository;
    final private PasswordResetTokenRepository passwordResetTokenRepository;
    final private PasswordEncoder passwordEncoder;

    @Value("${frontend.url}")
    private String frontendUrl;

    /**
     * Endpoint to handle forgot password requests.
     *
     * @param email the email address of the user who forgot their password
     * @return a message indicating that the password reset email was sent
     */
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        log.info("Received password reset request for email: {}", email);
        // Generate a unique token for password reset
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour

        // Save the token in the repository
        passwordResetTokenRepository.save(resetToken);

        // Create the password reset URL
        String resetUrl = frontendUrl + "/reset-password?token=" + token + "&email=" + email;

        // Send the password reset email
        try {
            emailService.sendEmail(email, "Password Reset Request", "Click the link to reset your password: " + resetUrl);
            log.info("Password reset email sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", email, e);
            return "Failed to send password reset email.";
        }

        return "Password reset email sent.";
    }

    /**
     * Endpoint to reset the user's password.
     *
     * @param email       the email address of the user
     * @param token       the password reset token
     * @param newPassword the new password
     * @return a message indicating that the password was reset successfully
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String token, @RequestParam String newPassword) {
        log.info("Password reset attempt for email: {} with token: {}", email, token);
        try {
            // Validate the token
            PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                    .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now())) // Check token expiration
                    .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

            // Check if the email associated with the token matches the provided email
            if (!resetToken.getEmail().equals(email)) {
                log.warn("Email address mismatch: token email is {} but provided email is {}", resetToken.getEmail(), email);
                throw new RuntimeException("Invalid email address");
            }

            // Find the user by email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Encode the new password and update the user's password
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            // Delete the used password reset token
            passwordResetTokenRepository.delete(resetToken);

            log.info("Password successfully reset for user: {}", email);
            return "Password reset successfully.";
        } catch (Exception e) {
            log.error("Error resetting password for email: {} with token: {}", email, token, e);
            return "Error resetting password.";
        }
    }
}

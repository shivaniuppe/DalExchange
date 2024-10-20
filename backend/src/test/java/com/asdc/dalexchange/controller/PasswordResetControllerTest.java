package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.model.PasswordResetToken;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.PasswordResetTokenRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PasswordResetControllerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testForgotPassword() {
        String email = "test@example.com";
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());

        String response = passwordResetController.forgotPassword(email);

        assertEquals("Password reset email sent.", response);
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    void testForgotPassword_EmailServiceFailure() {
        String email = "test@example.com";
        doThrow(new RuntimeException("Email service failed")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        String response = passwordResetController.forgotPassword(email);

        assertEquals("Failed to send password reset email.", response);
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    void testResetPassword() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword123";

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        User user = new User();
        user.setEmail(email);
        user.setPassword("oldPassword");

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        String response = passwordResetController.resetPassword(email, token, newPassword);

        assertEquals("Password reset successfully.", response);
        verify(passwordResetTokenRepository, times(1)).findByToken(token);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(user);
        verify(passwordResetTokenRepository, times(1)).delete(resetToken);
    }

    @Test
    void testResetPassword_InvalidOrExpiredToken() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword123";

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        String response = passwordResetController.resetPassword(email, token, newPassword);

        assertEquals("Error resetting password.", response);
        verify(passwordResetTokenRepository, times(1)).findByToken(token);
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetTokenRepository, never()).delete(any(PasswordResetToken.class));
    }

    @Test
    void testResetPassword_EmailMismatch() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword123";

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail("other@example.com");
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        String response = passwordResetController.resetPassword(email, token, newPassword);

        assertEquals("Error resetting password.", response);
        verify(passwordResetTokenRepository, times(1)).findByToken(token);
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetTokenRepository, never()).delete(any(PasswordResetToken.class));
    }

    @Test
    void testResetPassword_UserNotFound() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword123";

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        String response = passwordResetController.resetPassword(email, token, newPassword);

        assertEquals("Error resetting password.", response);
        verify(passwordResetTokenRepository, times(1)).findByToken(token);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordResetTokenRepository, never()).delete(any(PasswordResetToken.class));
    }
}


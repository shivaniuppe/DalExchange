package com.asdc.dalexchange.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() {
        mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    public void sendEmailTest() throws Exception {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        doNothing().when(emailSender).send(mimeMessage);

        emailService.sendEmail(to, subject, text);

        verify(emailSender, times(1)).createMimeMessage();
        verify(emailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void sendEmailTest_ThrowsException() throws Exception {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        doThrow(new RuntimeException("Email sending failed")).when(emailSender).send(mimeMessage);

        try {
            emailService.sendEmail(to, subject, text);
        } catch (RuntimeException e) {
            verify(emailSender, times(1)).createMimeMessage();
            verify(emailSender, times(1)).send(mimeMessage);
        }
    }
}

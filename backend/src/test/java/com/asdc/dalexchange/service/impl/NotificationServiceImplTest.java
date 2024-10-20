package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.model.Notification;
import com.asdc.dalexchange.model.User;
import com.asdc.dalexchange.repository.NotificationRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.specifications.NotificationSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendNotification() {
        Notification notification = new Notification();
        User user = new User();
        user.setEmail("test@example.com");
        notification.setUser(user);
        notification.setMessage("Test message");

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(notificationRepository.save(notification)).thenReturn(notification);

        notificationService.sendNotification(notification);

        verify(notificationRepository).save(notification);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testGetNotifications() {
        Notification notification = new Notification();
        List<Notification> notifications = List.of(notification);

        Specification<Notification> spec = NotificationSpecification.hasUserId(1L);
        when(notificationRepository.findAll(any(Specification.class))).thenReturn(notifications);

        List<Notification> result = notificationService.getNotifications();

        assertEquals(notifications, result);
        verify(notificationRepository).findAll(any(Specification.class));
    }

    @Test
    public void testMarkNotificationAsRead() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setIsRead(false);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.markNotificationAsRead(1L);

        assertEquals(true, result.getIsRead());
        verify(notificationRepository).findById(1L);
        verify(notificationRepository).save(notification);
    }

    @Test
    public void testMarkNotificationAsRead_NotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.markNotificationAsRead(1L);
        });

        assertEquals("Notification not found with id 1", exception.getMessage());
        verify(notificationRepository).findById(1L);
    }
}

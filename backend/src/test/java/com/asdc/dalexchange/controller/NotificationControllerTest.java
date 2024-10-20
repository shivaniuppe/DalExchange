package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.NotificationDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Notification;
import com.asdc.dalexchange.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Mapper<Notification, NotificationDTO> notificationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetNotifications() {
        Notification notification = new Notification();
        NotificationDTO notificationDTO = new NotificationDTO();
        List<Notification> notifications = List.of(notification);
        List<NotificationDTO> notificationDTOs = List.of(notificationDTO);

        when(notificationService.getNotifications()).thenReturn(notifications);
        when(notificationMapper.mapTo(notification)).thenReturn(notificationDTO);

        List<NotificationDTO> result = notificationController.getNotifications();

        assertEquals(notificationDTOs, result);
        verify(notificationService).getNotifications();
        verify(notificationMapper).mapTo(notification);
    }

    @Test
    public void testMarkAsRead() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setIsRead(true);

        when(notificationService.markNotificationAsRead(1L)).thenReturn(notification);

        ResponseEntity<Notification> response = notificationController.markAsRead(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notification, response.getBody());
        verify(notificationService).markNotificationAsRead(1L);
    }
}

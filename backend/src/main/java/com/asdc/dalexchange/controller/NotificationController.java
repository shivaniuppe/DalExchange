package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.NotificationDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Notification;
import com.asdc.dalexchange.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing notifications.
 */
@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final Mapper<Notification, NotificationDTO> notificationMapper;

    /**
     * Retrieve all notifications.
     *
     * @return a list of NotificationDTO objects.
     */
    @GetMapping("")
    public List<NotificationDTO> getNotifications() {
        log.info("Fetching all notifications.");
        List<Notification> notifications = notificationService.getNotifications();
        log.debug("Fetched {} notifications.", notifications.size());
        return notifications.stream()
                .map(notificationMapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * Mark a notification as read.
     *
     * @param id the ID of the notification to mark as read.
     * @return a ResponseEntity containing the updated Notification object.
     */
    @PutMapping("/mark/{id}")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        log.info("Marking notification with id {} as read.", id);
        Notification notification = notificationService.markNotificationAsRead(id);
        log.debug("Notification with id {} marked as read.", id);
        return ResponseEntity.ok(notification);
    }
}

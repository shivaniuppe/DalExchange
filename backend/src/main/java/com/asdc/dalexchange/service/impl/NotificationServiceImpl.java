package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.model.Notification;
import com.asdc.dalexchange.repository.NotificationRepository;
import com.asdc.dalexchange.repository.UserRepository;
import com.asdc.dalexchange.service.NotificationService;
import com.asdc.dalexchange.specifications.NotificationSpecification;
import com.asdc.dalexchange.util.AuthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the NotificationService interface for managing notifications.
 */
@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    /**
     * Sends a notification and an email to the user.
     *
     * @param notification the Notification to be sent.
     */
    @Override
    public void sendNotification(Notification notification) {
        log.info("Sending notification with id: {} to user: {}", notification.getId(), notification.getUser().getUserId());
        notificationRepository.save(notification);
        log.debug("Notification saved with id: {}", notification.getId());
        sendEmailNotification(notification);
        log.debug("Email notification sent to user: {}", notification.getUser().getEmail());
    }

    /**
     * Sends an email notification to the user.
     *
     * @param notification the Notification for which the email is to be sent.
     */
    private void sendEmailNotification(Notification notification) {
        log.info("Preparing email notification for user: {}", notification.getUser().getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getUser().getEmail());
        message.setSubject("DalExchange Notification");
        message.setText(notification.getMessage());
        mailSender.send(message);
        log.debug("Email sent to: {}", notification.getUser().getEmail());
    }

    /**
     * Retrieves all notifications for the current user.
     *
     * @return a list of Notifications for the current user.
     */
    @Override
    public List<Notification> getNotifications() {
        Long userId = AuthUtil.getCurrentUserId(userRepository);
        log.info("Fetching notifications for user id: {}", userId);
        Specification<Notification> spec = Specification.where(NotificationSpecification.hasUserId(userId));
        List<Notification> notifications = notificationRepository.findAll(spec);
        log.debug("Fetched {} notifications for user id: {}", notifications.size(), userId);
        return notifications;
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId the ID of the notification to mark as read.
     * @return the updated Notification with isRead set to true.
     * @throws RuntimeException if the notification is not found.
     */
    @Override
    public Notification markNotificationAsRead(Long notificationId) {
        log.info("Marking notification as read with id: {}", notificationId);
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setIsRead(true);
            Notification savedNotification = notificationRepository.save(notification);
            log.debug("Notification marked as read with id: {}", notificationId);
            return savedNotification;
        } else {
            log.error("Notification not found with id: {}", notificationId);
            throw new RuntimeException("Notification not found with id " + notificationId);
        }
    }
}

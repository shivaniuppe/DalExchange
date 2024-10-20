package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.NotificationDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between Notification and NotificationDTO.
 */
@Component
@AllArgsConstructor
@Slf4j
public class NotificationMapperImpl implements Mapper<Notification, NotificationDTO> {

    private final ModelMapper modelMapper;

    /**
     * Maps a Notification entity to a NotificationDTO.
     *
     * @param notification the Notification entity to map from.
     * @return the mapped NotificationDTO.
     */
    @Override
    public NotificationDTO mapTo(Notification notification) {
        log.info("Mapping Notification to NotificationDTO with id: {}", notification.getId());
        NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);
        log.debug("Mapped NotificationDTO: {}", notificationDTO);
        return notificationDTO;
    }

    /**
     * Maps a NotificationDTO to a Notification entity.
     *
     * @param notificationDTO the NotificationDTO to map from.
     * @return the mapped Notification entity.
     */
    @Override
    public Notification mapFrom(NotificationDTO notificationDTO) {
        log.info("Mapping NotificationDTO to Notification with id: {}", notificationDTO.getId());
        Notification notification = modelMapper.map(notificationDTO, Notification.class);
        log.debug("Mapped Notification: {}", notification);
        return notification;
    }
}

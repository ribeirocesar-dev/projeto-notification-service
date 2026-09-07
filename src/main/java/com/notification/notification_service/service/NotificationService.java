package com.notification.notification_service.service;

import org.springframework.stereotype.Service;
import com.notification.notification_service.dto.request.NotificationRequestDTO;
import com.notification.notification_service.dto.response.NotificationResponseDTO;
import com.notification.notification_service.entity.NotificationEntity;
import com.notification.notification_service.producer.NotificationProducer;
import com.notification.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationProducer notificationProducer;
    private final NotificationRepository notificationRepository;

    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        NotificationEntity notificationEntity = new NotificationEntity(notificationRequestDTO);
        notificationEntity = notificationRepository.save(notificationEntity);
        notificationProducer.publishNotification(notificationEntity.getId());

        return new NotificationResponseDTO(
                notificationEntity.getId(),
                notificationEntity.getRecipient(),
                notificationEntity.getStatus(),
                notificationEntity.getCreatedAt());
    }
}

package com.notification.notification_service.producer;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.notification.notification_service.entity.NotificationEntity;
import com.notification.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;

    @Value("${notification.exchanges.main}")
    private String mainExchangeName;

    @Value("${notification.routing-keys.email}")
    private String emailRoutingKey;

    public void publishNotification(UUID notificationUUID) {
        NotificationEntity notificationEntity = notificationRepository.findById(notificationUUID)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found for ID: " + notificationUUID));
        rabbitTemplate.convertAndSend(mainExchangeName, emailRoutingKey, notificationEntity);
    }
}

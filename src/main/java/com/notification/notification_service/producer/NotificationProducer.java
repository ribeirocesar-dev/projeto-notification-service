package com.notification.notification_service.producer;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${notification.exchanges.main}")
    private String mainExchangeName;

    @Value("${notification.routing-keys.email}")
    private String emailRoutingKey;

    public void publishNotification(UUID notificationUUID) {
        rabbitTemplate.convertAndSend(mainExchangeName, emailRoutingKey, notificationUUID);
    }
}

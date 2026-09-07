package com.notification.notification_service.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.notification.notification_service.entity.NotificationEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${notification.exchanges.main}")
    private String mainExchangeName;

    @Value("${notification.routing-keys.email}")
    private String emailRoutingKey;

    public void publishNotification(NotificationEntity notificationEntity) {
        rabbitTemplate.convertAndSend(mainExchangeName, emailRoutingKey, notificationEntity);
    }
}

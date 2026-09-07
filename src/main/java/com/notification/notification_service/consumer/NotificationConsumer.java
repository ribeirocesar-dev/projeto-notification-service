package com.notification.notification_service.consumer;

import java.time.Duration;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.notification.notification_service.entity.NotificationEntity;
import com.notification.notification_service.repository.NotificationRepository;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final MeterRegistry meterRegistry;
    private final NotificationRepository notificationRepository;
    private final StringRedisTemplate redisTemplate;

    @RabbitListener(queues = "${notification.queues.email}")
    public void consumeNotification(UUID notificationUUID) {
        NotificationEntity notificationEntity = notificationRepository.findById(notificationUUID)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found for ID: " + notificationUUID));

        String lockKey = "notification:lock:" + notificationEntity.getId();

        Boolean isNewProcess = redisTemplate.opsForValue().setIfAbsent(lockKey, "PROCESSING", Duration.ofMinutes(10));

        if (Boolean.FALSE.equals(isNewProcess)) {
            log.warn("Notification already sent or in processing for ID: {}", notificationEntity.getId());
            meterRegistry.counter("notification.sent.duplicated").increment();
            return;
        }

        try {
            log.info("Processing email dispatch for ID: {}", notificationEntity.getId());

            if ("fail@test.com".equalsIgnoreCase(notificationEntity.getRecipient())) {
                log.error("Simulated error by sending email to: {}", notificationEntity.getRecipient());
                meterRegistry.counter("notification.sent.failed").increment();
                throw new RuntimeException("Email provider failed");
            }

            notificationEntity.setStatusSent();
            notificationRepository.save(notificationEntity);
            meterRegistry.counter("notification.sent.success").increment();
            log.info("Notification sent successfully for ID: {}", notificationEntity.getId());

        } catch (Exception e) {
            redisTemplate.delete(lockKey);
            log.error("Error processing notification ID {}: {}", notificationEntity.getId(), e.getMessage());

            throw e;
        }
    }

    @RabbitListener(queues = "${notification.queues.dlq}")
    public void consumeDeadLetterQueue(UUID notificationUUID) {
        log.warn("Received message on DLQ for ID: {}", notificationUUID);
        meterRegistry.counter("notification.dlq.processed").increment();

        notificationRepository.findById(notificationUUID).ifPresentOrElse(entity -> {
            entity.setStatusFailed();
            notificationRepository.save(entity);
            log.info("Status of notification ID {} updated to FAILED.", entity.getId());
        }, () -> log.error("Notification of DLQ ID {} not found on Database.", notificationUUID));
    }
}
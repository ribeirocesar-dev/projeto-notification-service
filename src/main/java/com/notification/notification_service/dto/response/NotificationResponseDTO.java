package com.notification.notification_service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com.notification.notification_service.enums.NotificationStatus;

public record NotificationResponseDTO(UUID id, String recipient, NotificationStatus status, LocalDateTime createdAt) {
}

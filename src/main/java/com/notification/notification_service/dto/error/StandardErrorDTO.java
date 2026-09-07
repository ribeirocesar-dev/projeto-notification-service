package com.notification.notification_service.dto.error;

import java.time.Instant;
import java.util.Map;

public record StandardErrorDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        Map<String, String> fieldErrors) {
}
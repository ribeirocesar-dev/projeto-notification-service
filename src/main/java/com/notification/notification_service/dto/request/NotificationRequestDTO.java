package com.notification.notification_service.dto.request;

import com.notification.notification_service.enums.ChannelType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequestDTO(
        @NotBlank String recipient, String subject, @NotBlank String content, @NotNull ChannelType channel) {
}

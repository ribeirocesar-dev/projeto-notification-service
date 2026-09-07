package com.notification.notification_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.notification_service.dto.request.NotificationRequestDTO;
import com.notification.notification_service.dto.response.NotificationResponseDTO;
import com.notification.notification_service.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> publishNotification(
            @Valid @RequestBody NotificationRequestDTO notificationRequestDTO) {
        NotificationResponseDTO notificationResponseDTO = notificationService
                .createNotification(notificationRequestDTO);
        return ResponseEntity.accepted().body(notificationResponseDTO);
    }
}

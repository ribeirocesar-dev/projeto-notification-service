package com.notification.notification_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.notification_service.dto.request.NotificationRequestDTO;
import com.notification.notification_service.dto.response.NotificationResponseDTO;
import com.notification.notification_service.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Endpoints for management and dispatch of async notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Sends a new notification", description = "Receive the notification's payload, saves it with PENDING status and sent the event for processing on RabbitMQ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Notification accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> publishNotification(
            @Valid @RequestBody NotificationRequestDTO notificationRequestDTO) {
        NotificationResponseDTO notificationResponseDTO = notificationService
                .createNotification(notificationRequestDTO);
        return ResponseEntity.accepted().body(notificationResponseDTO);
    }
}

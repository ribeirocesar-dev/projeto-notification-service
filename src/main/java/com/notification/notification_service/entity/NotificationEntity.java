package com.notification.notification_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.notification.notification_service.dto.request.NotificationRequestDTO;
import com.notification.notification_service.enums.ChannelType;
import com.notification.notification_service.enums.NotificationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ChannelType channel;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private String recipient;
    private String subject;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NotificationEntity(ChannelType channel, String recipient, String subject,
            String content) {
        this.channel = channel;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        setStatusPending();
    }

    public NotificationEntity(NotificationRequestDTO notificationRequestDTO) {
        channel = notificationRequestDTO.channel();
        recipient = notificationRequestDTO.recipient();
        subject = notificationRequestDTO.subject();
        content = notificationRequestDTO.content();
        setStatusPending();
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setStatusPending() {
        status = NotificationStatus.PENDING;
    }

    public void setStatusSent() {
        status = NotificationStatus.SENT;
    }

    public void setStatusFailed() {
        status = NotificationStatus.FAILED;
    }
}

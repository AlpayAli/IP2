package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NotificationDto {
    UUID notificationId;
    LocalDateTime date;
    NotificationType type;
    String senderName;
    UUID senderId;

    public NotificationDto(UUID notificationId, LocalDateTime date, NotificationType type, String senderName, UUID senderId) {
        this.notificationId = notificationId;
        this.date = date;
        this.type = type;
        this.senderName = senderName;
        this.senderId = senderId;
    }

    public NotificationDto(UUID notificationId, LocalDateTime date, NotificationType type) {
        this.notificationId = notificationId;
        this.date = date;
        this.type = type;
    }
}


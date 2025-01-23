package be.kdg.backendgameservice.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendRequestResponse(
        UUID id,
        UUID senderId,
        UUID receiverId,
        boolean accepted,
        LocalDateTime requestDate
) {
}
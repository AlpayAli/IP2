package be.kdg.backendgameservice.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetFriendRequestsResponse(
        UUID id,
        UUID senderId,
        UUID receiverId,
        String senderUsername,
        boolean accepted,
        LocalDateTime requestDate
) {
}

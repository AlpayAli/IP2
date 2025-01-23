package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.NotificationDto;
import be.kdg.backendgameservice.controller.dto.NotificationType;
import be.kdg.backendgameservice.domain.FriendRequest;
import be.kdg.backendgameservice.domain.Invitation;
import be.kdg.backendgameservice.domain.Player;

import java.time.LocalDate;

public class NotificationMapper {

    public NotificationDto toDto(Invitation invitation) {
        return new NotificationDto(invitation.getId(), invitation.getDateSend(), NotificationType.INVITATION, invitation.getSender().getUsername(), invitation.getSender().getId());
    }

    public NotificationDto toDto(FriendRequest friendRequest) {
        return new NotificationDto(friendRequest.getId(), friendRequest.getRequestDate(), NotificationType.FRIEND_REQUEST, friendRequest.getSender().getUsername(), friendRequest.getSender().getId());
    }

    public NotificationDto toDto(Player player) {
        return new NotificationDto(player.getId(), LocalDate.now().atStartOfDay(), NotificationType.DAILY_SPIN);
    }
}

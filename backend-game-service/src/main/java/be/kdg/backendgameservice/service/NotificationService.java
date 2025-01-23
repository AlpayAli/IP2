package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.NotificationDto;
import be.kdg.backendgameservice.domain.FriendRequest;
import be.kdg.backendgameservice.domain.Invitation;
import be.kdg.backendgameservice.domain.InvitationStatus;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.exceptions.PlayerNotFoundException;
import be.kdg.backendgameservice.mapper.NotificationMapper;
import be.kdg.backendgameservice.repository.FriendRequestRepository;
import be.kdg.backendgameservice.repository.InvitationRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final InvitationRepository invitationRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final PlayerRepository playerRepository;
    private final NotificationMapper notificationMapper = new NotificationMapper();


    @Transactional
    public List<NotificationDto> getNotificationsForPlayer(UUID playerId) {
        Player player = getPlayerById(playerId);
        List<Invitation> openInvitations = invitationRepository.findAllByReceiverIdAndStatus(playerId, InvitationStatus.PENDING);
        List<FriendRequest> openFriendRequests = friendRequestRepository.findAllByReceiverIdAndAccepted(playerId, false);
        List<NotificationDto> notifications = new ArrayList<>();

        openInvitations.forEach(invitation -> notifications.add(notificationMapper.toDto(invitation)));
        openFriendRequests.forEach(friendRequest -> notifications.add(notificationMapper.toDto(friendRequest)));
        if (player.isDailySpinAvailable()) {
            notifications.add(notificationMapper.toDto(player));
        }

        return notifications;
    }

    private Player getPlayerById(UUID id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id: %s not found.", id)));
    }
}


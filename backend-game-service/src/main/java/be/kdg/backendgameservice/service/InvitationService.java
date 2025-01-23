package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.InvitationDto;
import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Invitation;
import be.kdg.backendgameservice.domain.InvitationStatus;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.exceptions.GameNotFoundException;
import be.kdg.backendgameservice.domain.exceptions.InvitationNotFoundException;
import be.kdg.backendgameservice.domain.exceptions.PlayerNotFoundException;
import be.kdg.backendgameservice.domain.exceptions.WrongInviteException;
import be.kdg.backendgameservice.event.MailEvent;
import be.kdg.backendgameservice.mapper.InvitationMapper;
import be.kdg.backendgameservice.repository.GameRepository;
import be.kdg.backendgameservice.repository.InvitationRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper = new InvitationMapper();
    private final GameService gameService;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Transactional
    public InvitationDto createInvitation(UUID gameId, UUID senderId, String receiverName) {
        Player sender = this.getPlayer(senderId);
        Player receiver = this.getPlayer(receiverName);
        Game game = this.getGame(gameId);
        Invitation invitation = new Invitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setGame(game);
        invitation.setSender(sender);
        invitation.setReceiver(receiver);
        invitation.setDateSend(LocalDateTime.now());
        MailEvent mailEvent = new MailEvent(this, receiver.getEmail(), "Invitation to game", "You have been invited to a game by " + sender.getUsername() + "\n\n Game URL: " + frontendUrl + "/game/" + game.getId());
        eventPublisher.publishEvent(mailEvent);
        return invitationMapper.toDto(invitationRepository.save(invitation));
    }

    @Transactional
    public List<InvitationDto> inviteAllFriends(UUID gameUUID, UUID playerUUID) {
        Game game = getGame(gameUUID);
        Player player = getPlayer(playerUUID);
        List<Player> friends = player.getFriends().stream().toList();
        List<Invitation> invitations = friends.stream()
                .map(friend -> {
                    Invitation invitation = new Invitation();
                    invitation.setStatus(InvitationStatus.PENDING);
                    invitation.setGame(game);
                    invitation.setSender(player);
                    invitation.setReceiver(friend);
                    invitation.setDateSend(LocalDateTime.now());
                    return invitation;
                })
                .toList();
        return invitationRepository.saveAll(invitations).stream()
                .map(invitationMapper::toDto)
                .toList();
    }

    @Transactional
    public InvitationDto acceptInvitation(UUID invitationId, UUID playerId) {
        Invitation invitation = checkInvitationForErrors(invitationId, playerId);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        gameService.joinGame(invitation.getGame().getId(), invitation.getReceiver().getId());
        return invitationMapper.toDto(invitationRepository.save(invitation));
    }

    @Transactional
    public InvitationDto declineInvitation(UUID invitationId, UUID playerId) {
        Invitation invitation = checkInvitationForErrors(invitationId, playerId);
        invitation.setStatus(InvitationStatus.DECLINED);
        return invitationMapper.toDto(invitationRepository.save(invitation));
    }

    private Invitation checkInvitationForErrors(UUID invitationId, UUID receiverId) {
        Invitation invitation = this.getInvitation(invitationId);
        Player receiver = this.getPlayer(receiverId);
        if (!invitation.getReceiver().equals(receiver)) {
            throw new WrongInviteException("Invitation with id " + invitationId + " does not belong to player with id " + receiver.getId());
        }
        return invitation;
    }

    @Transactional
    public List<InvitationDto> getInvitationsByPlayer(UUID playerId) {
        List<Invitation> invitations = invitationRepository.getInvitationsByReceiverId(playerId);

        if (invitations.isEmpty()) {
            return Collections.emptyList();
        }

        return invitations.stream()
                .map(invitationMapper::toDto)
                .toList();
    }


    private Player getPlayer(UUID playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id: " + playerId + " not found."));
    }

    private Player getPlayer(String playerName) {
        return playerRepository.findByUsernameIgnoreCase(playerName)
                .or(() -> playerRepository.findByEmailIgnoreCase(playerName))
                .orElseThrow(() -> new PlayerNotFoundException("Player with name/email: " + playerName + " not found."));
    }

    private Game getGame(UUID gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id: " + gameId + " not found."));
    }

    private Invitation getInvitation(UUID invitationId) {
        return invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation with id: " + invitationId + " not found."));
    }
}

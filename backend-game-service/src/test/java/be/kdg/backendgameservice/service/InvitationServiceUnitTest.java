package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.controller.dto.InvitationDto;
import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Invitation;
import be.kdg.backendgameservice.domain.InvitationStatus;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.exceptions.PlayerNotFoundException;
import be.kdg.backendgameservice.repository.InvitationRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Import(TestcontainersConfiguration.class)
public class InvitationServiceUnitTest {

    @InjectMocks
    private InvitationService invitationService;


    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createInvitation_PlayerNotFound_ShouldThrowException() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        String receiverName = "receiver";

        when(playerRepository.findById(senderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlayerNotFoundException.class, () ->
                invitationService.createInvitation(gameId, senderId, receiverName)
        );
    }


    @Test
    void acceptInvitation_ShouldReturnAcceptedInvitationDto() {
        // Arrange
        UUID invitationId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Player receiver = new Player();
        receiver.setId(playerId);

        Player sender = new Player();

        Game game = new Game();
        game.setId(UUID.randomUUID());

        Invitation invitation = new Invitation();
        invitation.setId(invitationId);
        invitation.setReceiver(receiver);
        invitation.setGame(game);
        invitation.setSender(sender);
        invitation.setStatus(InvitationStatus.PENDING);

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(receiver));
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);

        // Act
        InvitationDto result = invitationService.acceptInvitation(invitationId, playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(InvitationStatus.ACCEPTED.toString());
        verify(gameService, times(1)).joinGame(game.getId(), playerId);
    }


    @Test
    void declineInvitation_ShouldReturnDeclinedInvitationDto() {
        // Arrange
        UUID invitationId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Game game = new Game();

        Player sender = new Player();


        Player receiver = new Player();
        receiver.setId(playerId);

        Invitation invitation = new Invitation();
        invitation.setId(invitationId);
        invitation.setReceiver(receiver);
        invitation.setGame(game);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setSender(sender);

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(receiver));
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);

        // Act
        InvitationDto result = invitationService.declineInvitation(invitationId, playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(InvitationStatus.DECLINED.toString());
        verify(invitationRepository, times(1)).save(any(Invitation.class));
    }
}

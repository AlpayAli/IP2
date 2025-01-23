package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.controller.dto.InvitationDto;
import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Invitation;
import be.kdg.backendgameservice.domain.InvitationStatus;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.repository.InvitationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class InvitationServiceIntegrationTest {

    @MockitoBean
    private InvitationRepository invitationRepository;

    @Autowired
    private InvitationService invitationService;


    @Test
    void getInvitationsByPlayer_ShouldReturnListOfInvitationDtos() {
        // Arrange
        UUID playerId = UUID.randomUUID();

        Game game = new Game();

        Player sender = new Player();

        Player receiver = new Player();
        receiver.setId(playerId);

        Invitation invitation = new Invitation();
        invitation.setId(UUID.randomUUID());
        invitation.setReceiver(receiver);
        invitation.setGame(game);
        invitation.setSender(sender);
        invitation.setStatus(InvitationStatus.PENDING);

        when(invitationRepository.getInvitationsByReceiverId(playerId)).thenReturn(List.of(invitation));

        // Act
        List<InvitationDto> result = invitationService.getInvitationsByPlayer(playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(invitationRepository, times(1)).getInvitationsByReceiverId(playerId);
    }

    @Test
    void getInvitationsByPlayer_NoInvitations_ShouldReturnEmptyList() {
        // Arrange
        UUID playerId = UUID.randomUUID();

        when(invitationRepository.getInvitationsByReceiverId(playerId)).thenReturn(Collections.emptyList());

        // Act
        List<InvitationDto> result = invitationService.getInvitationsByPlayer(playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(invitationRepository, times(1)).getInvitationsByReceiverId(playerId);
    }
}

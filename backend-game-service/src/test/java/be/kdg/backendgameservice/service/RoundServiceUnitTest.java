package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.domain.*;
import be.kdg.backendgameservice.domain.exceptions.GameNotFoundException;
import be.kdg.backendgameservice.domain.exceptions.NoActivePlayersFoundException;
import be.kdg.backendgameservice.domain.exceptions.RoundNotFoundException;
import be.kdg.backendgameservice.repository.GameRepository;
import be.kdg.backendgameservice.repository.RoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Import(TestcontainersConfiguration.class)
class RoundServiceUnitTest {

    @InjectMocks
    private RoundService roundService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RoundRepository roundRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void initializeRound_ShouldThrowGameNotFoundException() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFoundException.class, () -> roundService.initializeRound(gameId));
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    void advanceRound_ShouldThrowRoundNotFoundException() {
        // Arrange
        UUID roundId = UUID.randomUUID();
        when(roundRepository.findById(roundId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoundNotFoundException.class, () -> roundService.advanceRound(roundId));
        verify(roundRepository, times(1)).findById(roundId);
    }

    @Test
    void executeShowdown_ShouldThrowNoActivePlayersFoundException() {
        // Arrange
        Round round = new Round();
        round.setActivePlayers(new ArrayList<>());

        // Act & Assert
        assertThrows(NoActivePlayersFoundException.class, () -> roundService.executeShowdown(round));
    }

    @Test
    void assignRolesToPlayers_ShouldAssignRolesCorrectly() {
        // Arrange
        Player player1 = new Player();
        player1.setId(UUID.randomUUID());
        Player player2 = new Player();
        player2.setId(UUID.randomUUID());
        Player player3 = new Player();
        player3.setId(UUID.randomUUID());

        PlayerEntry player1Entry = new PlayerEntry();
        player1Entry.setId(UUID.randomUUID());
        player1Entry.setPlayer(player1);

        PlayerEntry player2Entry = new PlayerEntry();
        player2Entry.setId(UUID.randomUUID());
        player2Entry.setPlayer(player2);

        PlayerEntry player3Entry = new PlayerEntry();
        player3Entry.setId(UUID.randomUUID());
        player3Entry.setPlayer(player3);



        Round round = new Round();
        round.setActivePlayers(new ArrayList<>(List.of(player1Entry, player2Entry, player3Entry)));
        round.setGame(new Game());

        // Act
        roundService.assignRolesToPlayers(round);

        // Assert
        assertThat(player1Entry.getRole()).isEqualTo(PlayerRole.SMALL_BLIND);
        assertThat(player2Entry.getRole()).isEqualTo(PlayerRole.BIG_BLIND);
        assertThat(player3Entry.getRole()).isEqualTo(PlayerRole.NONE);
    }
}

package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.controller.dto.PlayerActionDto;
import be.kdg.backendgameservice.domain.*;
import be.kdg.backendgameservice.domain.exceptions.GameNotFoundException;
import be.kdg.backendgameservice.domain.exceptions.NotEnoughBalanceException;
import be.kdg.backendgameservice.domain.exceptions.WrongPlayerException;
import be.kdg.backendgameservice.repository.BettingRoundRepository;
import be.kdg.backendgameservice.repository.GameRepository;
import be.kdg.backendgameservice.repository.PlayerEntryRepository;
import be.kdg.backendgameservice.repository.RoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Import(TestcontainersConfiguration.class)
class BettingRoundServiceUnitTest {

    @InjectMocks
    private BettingRoundService bettingRoundService;

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private BettingRoundRepository bettingRoundRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerEntryRepository playerEntryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handlePlayerAction_ShouldThrowExceptionForInvalidUUID() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                bettingRoundService.handlePlayerAction(UUID.randomUUID(), "invalid-uuid", PlayerActionType.RAISE, 200));
    }

    @Test
    void handlePlayerAction_ShouldThrowExceptionIfNotPlayersTurn() {
        // Arrange
        Game game = new Game();
        game.setId(UUID.randomUUID());

        UUID roundId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Player player = new Player();
        player.setId(playerId);
        player.setBalance(500);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Player otherPlayer = new Player();
        otherPlayer.setId(UUID.randomUUID());
        otherPlayer.setBalance(500);

        PlayerEntry otherPlayerEntry = new PlayerEntry();
        otherPlayerEntry.setId(UUID.randomUUID());
        otherPlayerEntry.setPlayer(otherPlayer);

        game.setPlayerEntries(new ArrayList<>(List.of(playerEntry, otherPlayerEntry)));
        game.setSmallBlind(5);

        Round round = new Round();
        round.setId(roundId);
        round.setActivePlayers(new ArrayList<>(List.of(playerEntry, otherPlayerEntry))); // Gebruik een bewerkbare lijst
        round.setGame(game);

        BettingRound bettingRound = new BettingRound();
        bettingRound.setRound(round);
        bettingRound.setCurrentPlayer(otherPlayerEntry);
        round.setBettingRounds(new ArrayList<>(List.of(bettingRound))); // Gebruik een bewerkbare lijst

        // Mocking correct instellen
        when(roundRepository.findById(roundId)).thenAnswer(invocation -> Optional.of(round));

        // Act & Assert
        WrongPlayerException exception = assertThrows(WrongPlayerException.class, () ->
                bettingRoundService.handlePlayerAction(playerId, round.getId().toString(), PlayerActionType.CALL, 0));

        assertThat(exception.getMessage()).isEqualTo("It's not your turn!");
        verify(roundRepository, times(1)).findById(roundId);
    }

    @Test
    void processRaise_ShouldUpdatePlayerBalanceAndHighestBet() {
        // Arrange
        Player player = new Player();
        player.setBalance(500);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setBalanceInGame(1000);
        playerEntry.setPlayer(player);

        BettingRound bettingRound = new BettingRound();
        bettingRound.setCurrentHighestBet(100);
        bettingRound.setPlayerActions(new ArrayList<>());

        Round round = new Round();
        round.setPot(0);
        bettingRound.setRound(round);

        // Act
        bettingRoundService.processRaise(playerEntry, bettingRound, 200);

        // Assert
        assertThat(playerEntry.getBalanceInGame()).isEqualTo(800); // Balance moet worden bijgewerkt
        assertThat(bettingRound.getCurrentHighestBet()).isEqualTo(200);
        assertThat(round.getPot()).isEqualTo(200); // Pot wordt bijgewerkt
        verify(bettingRoundRepository, times(1)).save(bettingRound);
    }

    @Test
    void processRaise_ShouldThrowExceptionForInsufficientBalance() {
        // Arrange
        Player player = new Player();
        player.setBalance(10); // Speler heeft te weinig saldo

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setPlayer(player);

        BettingRound bettingRound = new BettingRound();
        bettingRound.setCurrentHighestBet(50); // Huidige hoogste inzet is 50

        Round round = new Round();
        round.setPot(100);
        bettingRound.setRound(round);

        // Act & Assert
        NotEnoughBalanceException exception = assertThrows(NotEnoughBalanceException.class, () ->
                bettingRoundService.processRaise(playerEntry, bettingRound, 200)); // Speler probeert 200 te raisen

        assertThat(exception.getMessage()).isEqualTo("Player does not have enough balance to raise.");
    }

    @Test
    void validatePlayerAction_ShouldThrowExceptionWhenNotPlayersTurn() {
        // Arrange
        Player player = new Player();
        player.setId(UUID.randomUUID());

        Player currentPlayer = new Player();
        currentPlayer.setId(UUID.randomUUID()); // Initialize the ID

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        PlayerEntry currentPlayerEntry = new PlayerEntry();
        currentPlayerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(currentPlayer);

        BettingRound bettingRound = new BettingRound();
        bettingRound.setCurrentPlayer(currentPlayerEntry); // Set the properly initialized player

        // Act & Assert
        WrongPlayerException exception = assertThrows(WrongPlayerException.class, () ->
                bettingRoundService.validatePlayerAction(playerEntry, bettingRound, PlayerActionType.CHECK, 0));

        assertThat(exception.getMessage()).isEqualTo("It's not your turn!");
    }

    @Test
    void validatePlayerAction_ShouldThrowExceptionForInvalidRaiseAmount() {
        // Arrange
        Player player = new Player();
        player.setId(UUID.randomUUID());
        player.setBalance(100);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Player currentPlayer = new Player();
        currentPlayer.setId(player.getId()); // Set the current player to match the player being validated

        PlayerEntry currentPlayerEntry = new PlayerEntry();
        currentPlayerEntry.setId(playerEntry.getId());
        currentPlayerEntry.setPlayer(currentPlayer);

        BettingRound bettingRound = new BettingRound();
        bettingRound.setCurrentHighestBet(50);
        bettingRound.setCurrentPlayer(currentPlayerEntry); // Set the current player in the betting round

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bettingRoundService.validatePlayerAction(playerEntry, bettingRound, PlayerActionType.RAISE, 50));

        assertThat(exception.getMessage()).isEqualTo("Raise must be higher than current bet.");
    }

    @Test
    void processCheck_ShouldCreateCheckAction() {
        // Arrange
        Player player = new Player();
        player.setId(UUID.randomUUID());

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Round round = new Round();
        round.setPot(10);

        BettingRound bettingRound = new BettingRound();
        bettingRound.setRound(round);

        // Act
        PlayerActionDto playerActionDto = bettingRoundService.processCheck(playerEntry, bettingRound);

        // Assert
        assertThat(playerActionDto).isNotNull();
        verify(bettingRoundRepository, times(1)).save(bettingRound);
    }



    @Test
    void processFold_ShouldRemovePlayerFromActivePlayers() {
        // Arrange
        Player player = new Player();
        player.setId(UUID.randomUUID());

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Round round = new Round();
        round.setActivePlayers(new ArrayList<>(List.of(playerEntry)));

        BettingRound bettingRound = new BettingRound();
        bettingRound.setRound(round);

        // Act
        PlayerActionDto playerActionDto = bettingRoundService.processFold(playerEntry, bettingRound);

        // Assert
        assertThat(playerActionDto).isNotNull();
        assertThat(round.getActivePlayers()).doesNotContain(playerEntry);
        verify(bettingRoundRepository, times(1)).save(bettingRound);
    }


    @Test
    void getLatestBettingRoundOfGame_ShouldThrowExceptionIfGameNotFound() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () ->
                bettingRoundService.getLatestBettingRoundOfGame(gameId));

        assertThat(exception.getMessage()).isEqualTo("Game with ID " + gameId + " not found");
    }
}

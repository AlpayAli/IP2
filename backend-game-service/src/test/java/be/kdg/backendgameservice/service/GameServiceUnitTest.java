package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.controller.dto.GameDto;
import be.kdg.backendgameservice.controller.dto.PlayerDto;
import be.kdg.backendgameservice.controller.dto.PlayerEntryDto;
import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.GameStatus;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.PlayerEntry;
import be.kdg.backendgameservice.domain.exceptions.AlreadyParticipatingException;
import be.kdg.backendgameservice.domain.exceptions.GameNotFoundException;
import be.kdg.backendgameservice.mapper.GameMapper;
import be.kdg.backendgameservice.repository.GameRepository;
import be.kdg.backendgameservice.repository.PlayerEntryRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Import(TestcontainersConfiguration.class)
public class GameServiceUnitTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private PlayerService playerService;

    @Mock
    private GameRepository gameRepository;


    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerEntryRepository playerEntryRepository;

    @Mock
    private RoundService roundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGameDtoById_ShouldReturnGameDto() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        Game game = new Game();
        game.setId(gameId);

        Player player = new Player();
        player.setId(UUID.randomUUID()); // Zorg ervoor dat de Player een ID heeft

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        playerEntry.setCurrentCards(new ArrayList<>());
        game.setPlayerEntries(new ArrayList<>(List.of(playerEntry)));// Voeg de Player toe aan de Game
        game.setHost(playerEntry);
        game.setGameStatus(GameStatus.WAITING_FOR_PLAYERS);
        game.setRounds(new ArrayList<>());


        PlayerDto playerDto = new PlayerDto();
        playerDto.setId(UUID.randomUUID());

        PlayerEntryDto playerEntryDto = new PlayerEntryDto();
        playerEntryDto.setId(UUID.randomUUID());
        playerEntryDto.setCurrentCards(new ArrayList<>());

        GameDto gameDto = new GameDto();
        gameDto.setPlayers(new ArrayList<>(List.of(playerEntryDto)));
        gameDto.setHost(playerDto);
        gameDto.setStatus("WAITING_FOR_PLAYERS");


        // Mock dependencies
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.toDto(game)).thenReturn(gameDto);

        // Act
        GameDto result = gameService.getGameDtoById(gameId);

        // Assert
        assertThat(result).isNotNull();
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    void getGameDtoById_ShouldThrowGameNotFoundException() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFoundException.class, () -> gameService.getGameDtoById(gameId));
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    void createGame_ShouldReturnGameDto() {
        // Arrange
        UUID hostId = UUID.randomUUID();
        Player host = new Player();
        host.setId(hostId);

        PlayerEntry hostEntry = new PlayerEntry();
        hostEntry.setId(UUID.randomUUID());
        hostEntry.setPlayer(host);

        hostEntry.setCurrentCards(new ArrayList<>());

        Game game = new Game();
        game.setHost(hostEntry);
        GameDto gameDto = new GameDto();

        when(playerRepository.findById(hostId)).thenReturn(Optional.of(host));
        when(playerEntryRepository.findById(hostEntry.getId())).thenReturn(Optional.of(hostEntry));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameMapper.toDto(game)).thenReturn(gameDto);

        // Act
        GameDto result = gameService.createGame(hostId, 2, 5, 10, "public");

        // Assert
        assertThat(result).isNotNull();
        verify(playerRepository, times(1)).findById(hostId);
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void joinGame_ShouldAddPlayerToGame() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerEntries(new ArrayList<>());
        game.setGameStatus(GameStatus.WAITING_FOR_PLAYERS);


        Player player = new Player();
        player.setId(playerId);
        player.setBalance(10000);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(gameMapper.toDto(game)).thenReturn(new GameDto());

        // Act
        GameDto result = gameService.joinGame(gameId, playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(game.getPlayerEntries().stream().anyMatch(playerEntry -> playerEntry.getPlayer() == player));
        verify(gameRepository, times(1)).findById(gameId);
        verify(playerRepository, times(1)).findById(playerId);
    }


    @Test
    void joinGame_ShouldThrowAlreadyParticipatingException() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Player player = new Player();
        player.setId(playerId);
        player.setBalance(10000);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerEntries(Collections.singletonList(playerEntry));

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // Act & Assert
        assertThrows(AlreadyParticipatingException.class, () -> gameService.joinGame(gameId, playerId));
        verify(gameRepository, times(1)).findById(gameId);
        verify(playerRepository, times(1)).findById(playerId);
    }


    @Test
    void leaveGame_ShouldRemovePlayerFromGame() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Player player = new Player();
        player.setId(playerId);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerEntries(new ArrayList<>(Collections.singletonList(playerEntry)));
        game.setHost(playerEntry);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerEntryRepository.findByPlayerAndGame(player, game)).thenReturn(Optional.of(playerEntry));
        when(gameMapper.toDto(game)).thenReturn(new GameDto());

        // Act
        GameDto result = gameService.leaveGame(gameId, playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(game.getPlayerEntries().stream().map(PlayerEntry::getPlayer).noneMatch(player1 -> player1.getId() == player.getId()));
        verify(gameRepository, times(1)).findById(gameId);
        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    void leaveGame_ShouldThrowIllegalArgumentException() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Player player = new Player();
        player.setId(playerId);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerEntries(new ArrayList<>());

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> gameService.leaveGame(gameId, playerId));
        verify(gameRepository, times(1)).findById(gameId);
        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    void startGame_ShouldSetGameStatusToOngoingAndInitializeRound() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Player player = new Player();
        player.setId(playerId);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerEntries(new ArrayList<>(Collections.singletonList(playerEntry)));
        game.setGameStatus(GameStatus.WAITING_FOR_PLAYERS);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(gameMapper.toDto(game)).thenReturn(new GameDto());

        // Act
        GameDto result = gameService.startGame(gameId, playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(game.getGameStatus()).isEqualTo(GameStatus.ONGOING);
        verify(roundService, times(1)).initializeRound(gameId);
    }

    @Test
    void startGame_PlayerNotInGame_ShouldJoinAndStart() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Player player = new Player();
        player.setId(playerId);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(player);

        Game game = new Game();
        game.setId(gameId);
        game.setPlayerEntries(new ArrayList<>()); // Geen spelers

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(gameMapper.toDto(game)).thenReturn(new GameDto());

        // Act
        GameDto result = gameService.startGame(gameId, playerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(game.getGameStatus()).isEqualTo(GameStatus.ONGOING);
        verify(roundService, times(1)).initializeRound(gameId);
        verify(gameRepository, times(1)).findById(gameId); // Controleer dat de game correct is opgehaald
    }


    @Test
    void startGame_GameNotFound_ShouldThrowException() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFoundException.class, () -> gameService.startGame(gameId, playerId));
    }


    @Test
    void getLatestGameFromHost_ShouldReturnLatestGame() {
        // Arrange
        UUID hostId = UUID.randomUUID();
        Player host = new Player();
        host.setId(hostId);

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(UUID.randomUUID());
        playerEntry.setPlayer(host);

        Game game = new Game();
        game.setId(UUID.randomUUID());
        game.setHost(playerEntry);
        game.setStartTime(LocalDateTime.now().minusHours(1));

        when(gameRepository.findTopByHostIdAndStartTimeNotNullOrderByStartTimeDesc(hostId))
                .thenReturn(Optional.of(game));
        when(gameMapper.toDto(game)).thenReturn(new GameDto());

        // Act
        GameDto result = gameService.getLatestGameFromHost(hostId);

        // Assert
        assertThat(result).isNotNull();
        verify(gameRepository, times(1))
                .findTopByHostIdAndStartTimeNotNullOrderByStartTimeDesc(hostId);
    }


    @Test
    void getLatestGameFromHost_GameNotFound_ShouldThrowException() {
        // Arrange
        UUID hostId = UUID.randomUUID();

        when(gameRepository.findTopByHostIdAndStartTimeNotNullOrderByStartTimeDesc(hostId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFoundException.class, () -> gameService.getLatestGameFromHost(hostId));
    }
}

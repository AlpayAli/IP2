package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.GameDto;
import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.GameStatus;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.*;
import be.kdg.backendgameservice.domain.exceptions.*;
import be.kdg.backendgameservice.mapper.GameMapper;
import be.kdg.backendgameservice.repository.GameRepository;
import be.kdg.backendgameservice.repository.PlayerEntryRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final RoundService roundService;
    private final PlayerRepository playerRepository;
    private final GameMapper gameMapper = new GameMapper();
    private final PlayerEntryRepository playerEntryRepository;

    @Transactional
    public GameDto getGameDtoById(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id: " + gameId + " not found."));
        return gameMapper.toDto(game);
    }

    @Transactional
    public List<GameDto> getOpenGames() {
        List<Game> openGames = gameRepository.findAllByIsOpenTrue();
        return openGames.stream()
                .map(gameMapper::toDto)
                .toList();
    }

    @Transactional
    public List<GameDto> getGamesOfPlayer(UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException("Player with id: " + playerId + " not found."));
        List<Game> games = gameRepository.findGamesByPlayerId(playerId);
        return games.stream().map(gameMapper::toDto).toList();
    }

    @Transactional
    public GameDto createGame(UUID hostId, int minPlayers, int maxPlayers, int smallBlind, String security) {
        Player host = playerRepository.findById(hostId).orElseThrow(() -> new PlayerNotFoundException("Player with id: " + hostId + " not found."));
        PlayerEntry hostEntry = new PlayerEntry();
        hostEntry.setPlayer(host);
        Game game = new Game();
        game.setHost(hostEntry);
        game.setGameStatus(GameStatus.WAITING_FOR_PLAYERS);
        game.setStartTime(LocalDateTime.now());
        game.setRounds(new ArrayList<>());
        game.setMinPlayers(minPlayers);
        game.setMaxPlayers(maxPlayers);
        game.setOpen(security.equals("public"));
        game.setSmallBlind(smallBlind);
        game.setBuyInAmount(100*smallBlind);
        hostEntry.setBalanceInGame(game.getBuyInAmount());
        game.setPlayerEntries(Collections.singletonList(hostEntry));
        return gameMapper.toDto(gameRepository.save(game));
    }

    @Transactional
    public GameDto joinGame(UUID gameId, UUID playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id: " + playerId + " not found."));

        if (player.getBalance() < 10) {
            throw new BrokeBoyException("You're too broke to play this game!");
        }

        boolean alreadyParticipating = game.getPlayerEntries().stream()
                .anyMatch(activePlayer -> activePlayer.getPlayer().getId() == playerId);

        if (alreadyParticipating) {
            throw new AlreadyParticipatingException("Player with Id " + playerId + " is already in the game");
        }

        PlayerEntry newPlayerEntry = new PlayerEntry();
        newPlayerEntry.setPlayer(player);
        newPlayerEntry.setBalanceInGame(Math.min(player.getBalance(), game.getBuyInAmount()));
        player.setBalance(player.getBalance() - game.getBuyInAmount());
        game.getPlayerEntries().add(newPlayerEntry);

        if (game.getPlayerEntries().size() == game.getMaxPlayers()) {
            startGame(gameId, playerId);
        }

        return gameMapper.toDto(game);
    }

    @Transactional
    public GameDto leaveGame(UUID gameId, UUID playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id: " + playerId + " not found."));

        boolean alreadyParticipating = game.getPlayerEntries().stream()
                .anyMatch(activePlayer -> activePlayer.getPlayer().getId() == playerId);

        if (!alreadyParticipating) {
            throw new NotParticipatingException("Player is not in the game");
        }

        PlayerEntry playerEntryToRemove = playerEntryRepository.findByPlayerAndGame(player, game)
                .orElseThrow(() -> new PlayerEntryNotFoundException("Entry for player " + player.getUsername() + " in game " + game.getId() + " not found!"));

        playerEntryToRemove.setCurrentCards(new ArrayList<>());
        player.setBalance(player.getBalance() + playerEntryToRemove.getBalanceInGame());
        game.getPlayerEntries().remove(playerEntryToRemove);
        gameRepository.save(game);
        return gameMapper.toDto(game);
    }

    @Transactional
    public GameDto startGame(UUID gameId, UUID playerId) {
        Game game = this.getGameById(gameId);
        game.setGameStatus(GameStatus.ONGOING);
        roundService.initializeRound(gameId);
        return gameMapper.toDto(game);
    }

    public GameDto getLatestGameFromHost(UUID hostId) {
        Game game = gameRepository.findTopByHostIdAndStartTimeNotNullOrderByStartTimeDesc(hostId)
                .orElseThrow(() -> new GameNotFoundException("Game with id: " + hostId + " not found."));
        return gameMapper.toDto(game);
    }

    private Game getGameById(UUID gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id: " + gameId + " not found."));
    }
}

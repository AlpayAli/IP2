package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.GameDto;
import be.kdg.backendgameservice.controller.dto.PlayerDto;
import be.kdg.backendgameservice.controller.dto.PlayerEntryDto;
import be.kdg.backendgameservice.domain.Game;

import java.util.List;

public class GameMapper {
    private final RoundMapper roundMapper = new RoundMapper();
    private final PlayerMapper playerMapper = new PlayerMapper();
    private final PlayerEntryMapper playerEntryMapper = new PlayerEntryMapper();

    public GameDto toDto(Game game) {
        GameDto gameDto = new GameDto(
                game.getId(),
                game.getMinPlayers(),
                game.getMaxPlayers(),
                game.getSmallBlind()
        );

        if (game.getHost() == null) {
            gameDto.setHost(null);
        } else {
            gameDto.setHost(playerMapper.toDto(game.getHost().getPlayer()));
        }

        if (game.getGameStatus().toString().isEmpty()) {
            gameDto.setStatus("null");
        } else {
            gameDto.setStatus(game.getGameStatus().toString());
        }

        if (game.getRounds() == null || game.getRounds().isEmpty()) {
            gameDto.setCurrentRound(null);
        } else {
            gameDto.setCurrentRound(roundMapper.toDto(game.getRounds().get(game.getRounds().size() - 1)));
        }

        // Map players
        List<PlayerEntryDto> players = game.getPlayerEntries().stream()
                .map(playerEntryMapper::toDto)
                .toList();
        gameDto.setPlayers(players);

        if (game.getWinner() != null) {
            gameDto.setWinner(playerMapper.toDto(game.getWinner()));
        }

        return gameDto;
    }
}


package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.BettingRoundDto;
import be.kdg.backendgameservice.domain.BettingRound;

public class BettingRoundMapper {
    private final PlayerActionMapper playerActionMapper = new PlayerActionMapper();
    private final PlayerMapper playerMapper = new PlayerMapper();

    public BettingRoundDto toDto(BettingRound bettingRound) {
        BettingRoundDto bettingRoundDto = new BettingRoundDto();

        bettingRoundDto.setId(bettingRound.getId());

        bettingRoundDto.setPlayerActionDtoList(
                bettingRound.getPlayerActions().stream()
                        .map(playerActionMapper::toDto)
                        .toList()
        );

        bettingRoundDto.setCurrentHighestBet(bettingRound.getCurrentHighestBet());

        bettingRoundDto.setActivePlayer(playerMapper.toDto(bettingRound.getCurrentPlayer().getPlayer()));

        return bettingRoundDto;
    }
}


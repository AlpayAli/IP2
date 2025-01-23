package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.RoundDto;
import be.kdg.backendgameservice.domain.BettingRound;
import be.kdg.backendgameservice.domain.Round;
import be.kdg.backendgameservice.mapper.PlayerMapper;

import java.util.ArrayList;
import java.util.Comparator;

public class RoundMapper {
    private final PlayerMapper playerMapper = new PlayerMapper();
    private final BettingRoundMapper bettingRoundMapper = new BettingRoundMapper();
    private final PlayerEntryMapper playerEntryMapper = new PlayerEntryMapper();

    public RoundDto toDto(Round round) {
        RoundDto roundDto = new RoundDto();
        roundDto.setId(round.getId());
        roundDto.setStatus(round.getStatus().name());
        roundDto.setTableCards(round.getTableCards().stream().map(Enum::toString).toList());
        roundDto.setGameId(round.getGame().getId());
        roundDto.setPot(round.getPot());
        roundDto.setPlayers(round.getActivePlayers().stream()
                .map(playerEntryMapper::toDto)
                .toList()
        );

        if (round.getBettingRounds() != null && round.getBettingRounds().size() > 0) {
            roundDto.setBettingRounds(
                    round.getBettingRounds().stream()
                            .sorted(Comparator.comparing(BettingRound::getCreatedAt))
                            .map(bettingRoundMapper::toDto)
                            .toList());
        }else {
            roundDto.setBettingRounds(new ArrayList<>());
        }


        roundDto.setWinners(
                round.getWinners().stream()
                        .map(playerEntryMapper::toDto).toList()
        );


        return roundDto;
    }
}

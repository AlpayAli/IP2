package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class RoundDto {
    private UUID id;
    private String status;
    private List<String> tableCards;
    private UUID gameId;
    private List<PlayerEntryDto> players;
    private List<BettingRoundDto> bettingRounds;
    private int pot;
    private List<PlayerEntryDto> winners;
}

package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BettingRoundDto {
    List<PlayerActionDto> playerActionDtoList;
    int currentHighestBet = 0;
    PlayerDto activePlayer;
    private UUID id;
}

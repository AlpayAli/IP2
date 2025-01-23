package be.kdg.backendgameservice.controller.dto;

import be.kdg.backendgameservice.domain.PlayerActionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPlayerActionDto {
    private String roundId;
    private PlayerActionType actionType;
    private int raiseAmount;
}

package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerActionDto {
    private String playerName;
    private String actionType;
    private int amount;
}

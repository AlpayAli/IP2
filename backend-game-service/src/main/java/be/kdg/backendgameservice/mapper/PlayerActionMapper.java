package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.PlayerActionDto;
import be.kdg.backendgameservice.domain.PlayerAction;

public class PlayerActionMapper {
    public PlayerActionDto toDto(PlayerAction playerAction) {
        PlayerActionDto playerActionDto = new PlayerActionDto();
        playerActionDto.setPlayerName(playerAction.getPlayerEntry().getPlayer().getUsername());
        playerActionDto.setActionType(playerAction.getActionType().name());
        playerActionDto.setAmount(playerAction.getAmount());
        return playerActionDto;
    }
}

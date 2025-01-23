package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.PlayerDto;
import be.kdg.backendgameservice.domain.Player;

public class PlayerMapper {
    public PlayerDto toDto(Player player) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setId(player.getId());
        playerDto.setName(player.getUsername());
        playerDto.setBalance((int) player.getBalance());
        playerDto.setAvatarUrl(player.getAvatarUrl());
        playerDto.setXp(player.getXp());
        return playerDto;
    }

    public PlayerDto toDtoLeaderboard(Player player) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setId(player.getId());
        playerDto.setName(player.getUsername());
        playerDto.setBalance((int) player.getBalance());
        playerDto.setXp(player.getXp());
        return playerDto;
    }
}

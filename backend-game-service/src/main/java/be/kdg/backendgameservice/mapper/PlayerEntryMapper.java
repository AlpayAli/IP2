package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.PlayerEntryDto;
import be.kdg.backendgameservice.domain.Card;
import be.kdg.backendgameservice.domain.PlayerEntry;

public class PlayerEntryMapper {
    public PlayerEntryDto toDto(PlayerEntry playerEntry) {
        PlayerEntryDto playerDto = new PlayerEntryDto();
        playerDto.setId(playerEntry.getPlayer().getId());
        playerDto.setName(playerEntry.getPlayer().getUsername());
        playerDto.setCurrentCards(playerEntry.getCurrentCards().stream().map(Card::toString).toList());
        playerDto.setBalance((int) playerEntry.getBalanceInGame());
        playerDto.setAvatarUrl(playerEntry.getPlayer().getAvatarUrl());
        playerDto.setXp(playerEntry.getPlayer().getXp());
        return playerDto;
    }
}

package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewGameDto {
    private int minPlayers;
    private int maxPlayers;
    private int smallBlind;
    private String security;

    public NewGameDto(int minPlayers, int maxPlayers, int smallBlind, String security) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.smallBlind = smallBlind;
        this.security = security;
    }
}

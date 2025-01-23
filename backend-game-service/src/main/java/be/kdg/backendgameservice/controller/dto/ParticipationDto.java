package be.kdg.backendgameservice.controller.dto;

import java.util.UUID;

public class ParticipationDto {
    private UUID id;
    private String playerName;

    public ParticipationDto(UUID id, String playerName) {
        this.id = id;
        this.playerName = playerName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

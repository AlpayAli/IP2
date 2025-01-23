package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GameDto {
    private UUID id;
    private int minPlayers;
    private int maxPlayers;
    private long smallBlind;
    private RoundDto currentRound;
    private List<PlayerEntryDto> players;
    private PlayerDto host;
    private String status;
    private PlayerDto winner;

    public GameDto(UUID id, int minPlayers, int maxPlayers, long smallBlind) {
        this.id = id;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.smallBlind = smallBlind;
    }


}

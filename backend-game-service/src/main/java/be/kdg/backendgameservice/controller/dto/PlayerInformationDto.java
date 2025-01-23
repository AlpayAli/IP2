package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlayerInformationDto {
    private String username;
    private int xp;
    private double balance;
    private int friendConnections;
    private int mutualFriends;
    private int achievementsGathered;
    private int roundsWon;
    private int gamesWon;
    private String avatarUrl;
    private boolean alreadyFriends;
    private List<String> friendNames;
    private List<String> achievementNames;
}

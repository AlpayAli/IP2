package be.kdg.backendgameservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class AchievementDto {
    private UUID achievementId;
    private String achievementName;
    private String achievementDescription;
    private int achievementXp;

}

package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.AchievementDto;
import be.kdg.backendgameservice.domain.Achievement;

public class AchievementMapper {
    public AchievementDto toDto(Achievement achievement) {
        AchievementDto dto = new AchievementDto();
        dto.setAchievementId(achievement.getAchievementId());
        dto.setAchievementName(achievement.getAchievementName());
        dto.setAchievementDescription(achievement.getAchievementDescription());
        dto.setAchievementXp(achievement.getAchievementXp());
        return dto;
    }

}

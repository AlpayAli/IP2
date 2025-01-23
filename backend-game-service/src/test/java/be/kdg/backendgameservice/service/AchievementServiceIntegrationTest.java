package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.controller.dto.AchievementDto;
import be.kdg.backendgameservice.domain.Achievement;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.repository.AchievementRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AchievementServiceIntegrationTest {

    @Autowired
    private AchievementService achievementService;

    @MockitoBean
    private AchievementRepository achievementRepository;

    @MockitoBean
    private PlayerRepository playerRepository;

    @Test
    void getAchievements_ShouldReturnAllAchievements() {
        // Arrange
        Achievement achievement1 = new Achievement();
        achievement1.setAchievementName("Win with Flush");

        Achievement achievement2 = new Achievement();
        achievement2.setAchievementName("Win with Straight");

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));

        // Act
        List<AchievementDto> achievementDtos = achievementService.getAchievements();

        // Assert
        assertThat(achievementDtos).hasSize(2);
        assertThat(achievementDtos.get(0).getAchievementName()).isEqualTo("Win with Flush");
        assertThat(achievementDtos.get(1).getAchievementName()).isEqualTo("Win with Straight");
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void getAchievementsOfPlayer_ShouldReturnAchievementsOfPlayer() {
        // Arrange
        UUID playerId = UUID.randomUUID();
        Player player = new Player();
        Achievement achievement = new Achievement();
        achievement.setAchievementName("Win with Flush");

        player.setAchievements(List.of(achievement));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // Act
        List<AchievementDto> achievementDtos = achievementService.getAchievementsOfPlayer(playerId);

        // Assert
        assertThat(achievementDtos).hasSize(1);
        assertThat(achievementDtos.get(0).getAchievementName()).isEqualTo("Win with Flush");
        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    void getAchievementsOfPlayer_ShouldThrowExceptionWhenPlayerNotFound() {
        // Arrange
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                achievementService.getAchievementsOfPlayer(playerId));
        assertThat(exception.getMessage()).isEqualTo("Player not found");
        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    void getRecentAchievements_ShouldReturnRecentAchievements() {
        // Arrange
        UUID playerId = UUID.randomUUID();
        Player player = new Player();

        Achievement achievement1 = new Achievement();
        achievement1.setAchievementName("Win with Flush");
        achievement1.setUnlockTime(LocalDateTime.now().minusSeconds(3));

        Achievement achievement2 = new Achievement();
        achievement2.setAchievementName("Win with Straight");
        achievement2.setUnlockTime(LocalDateTime.now().minusSeconds(10));

        player.setAchievements(List.of(achievement1, achievement2));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // Act
        List<AchievementDto> recentAchievements = achievementService.getRecentAchievements(playerId);

        // Assert
        assertThat(recentAchievements).hasSize(1);
        assertThat(recentAchievements.get(0).getAchievementName()).isEqualTo("Win with Flush");
        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    void getRecentAchievements_ShouldThrowExceptionWhenPlayerNotFound() {
        // Arrange
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                achievementService.getRecentAchievements(playerId));
        assertThat(exception.getMessage()).isEqualTo("Player not found");
        verify(playerRepository, times(1)).findById(playerId);
    }
}

package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.domain.Achievement;
import be.kdg.backendgameservice.domain.HandRank;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.event.PlayerWonWithHandRankEvent;
import be.kdg.backendgameservice.repository.AchievementRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Import(TestcontainersConfiguration.class)
class AchievementServiceUnitTest {

    @InjectMocks
    private AchievementService achievementService;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handlePlayerWonWithHandRank_ShouldUnlockNewAchievements() {
        // Arrange
        Player player = new Player();
        player.setUsername("TestPlayer");
        player.setXp(100);

        HandRank handRank = HandRank.FLUSH;

        Achievement achievement = new Achievement();
        achievement.setHandRank(handRank);
        achievement.setAchievementName("Win with a flush");
        achievement.setAchievementXp(50);

        PlayerWonWithHandRankEvent event = new PlayerWonWithHandRankEvent(new Object(), player, handRank);

        when(achievementRepository.findAllByHandRank(handRank)).thenReturn(List.of(achievement));
        when(playerRepository.save(player)).thenReturn(player);

        // Act
        achievementService.handlePlayerWonWithHandRank(event);

        // Assert
        assertThat(player.getAchievements()).contains(achievement);
        assertThat(player.getXp()).isEqualTo(150);
        verify(achievementRepository, times(1)).findAllByHandRank(handRank);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void handlePlayerWonWithHandRank_ShouldSkipExistingAchievements() {
        // Arrange
        Player player = new Player();
        player.setUsername("TestPlayer");
        Achievement existingAchievement = new Achievement();
        existingAchievement.setHandRank(HandRank.FLUSH);
        existingAchievement.setAchievementName("Flush Master");
        player.getAchievements().add(existingAchievement);
        player.setXp(100);

        HandRank handRank = HandRank.FLUSH;

        PlayerWonWithHandRankEvent event = new PlayerWonWithHandRankEvent(new Object(), player, handRank);

        when(achievementRepository.findAllByHandRank(handRank)).thenReturn(List.of(existingAchievement));

        // Act
        achievementService.handlePlayerWonWithHandRank(event);

        // Assert
        assertThat(player.getAchievements()).contains(existingAchievement);
        assertThat(player.getXp()).isEqualTo(100); // XP should not change
        verify(achievementRepository, times(1)).findAllByHandRank(handRank);
        verify(playerRepository, never()).save(player); // Player should not be saved
        verify(logger, never()).info(anyString());
    }
}

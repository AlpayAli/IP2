package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.AchievementDto;
import be.kdg.backendgameservice.domain.Achievement;
import be.kdg.backendgameservice.domain.HandRank;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.exceptions.PlayerNotFoundException;
import be.kdg.backendgameservice.event.PlayerWonWithHandRankEvent;
import be.kdg.backendgameservice.mapper.AchievementMapper;
import be.kdg.backendgameservice.repository.AchievementRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final PlayerRepository playerRepository;
    private final AchievementMapper achievementMapper = new AchievementMapper();
    private static final Logger logger = Logger.getLogger(RoundService.class.getName());




    @EventListener
    public void handlePlayerWonWithHandRank(PlayerWonWithHandRankEvent event) {
        Player player = event.getPlayer();
        HandRank handRank = event.getHandRank();

        // Zoek achievements voor de handrang
        List<Achievement> matchingAchievements = achievementRepository.findAllByHandRank(handRank);

        for (Achievement achievement : matchingAchievements) {
            if (!player.getAchievements().contains(achievement)) {
                achievement.setUnlockTime(LocalDateTime.now());
                player.getAchievements().add(achievement);
                player.setXp(player.getXp() + achievement.getAchievementXp());
                playerRepository.save(player);

                // Log een bericht of stuur een notificatie
                logger.info("Player " + player.getUsername() + " unlocked achievement: " + achievement.getAchievementName());
            }
        }
    }

    @Transactional
    public List<AchievementDto> getAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        return achievements.stream()
                .map(achievementMapper::toDto)
                .toList();
    }

    @Transactional
    public List<AchievementDto> getAchievementsOfPlayer(UUID playerId) {
        Player player1 = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        List<Achievement> achievements = player1.getAchievements();

        return achievements.stream()
                .map(achievementMapper::toDto)
                .toList();
    }



    @Transactional
    public List<AchievementDto> getRecentAchievements(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        // Huidige tijd
        LocalDateTime now = LocalDateTime.now();

        List<Achievement> achievements = player.getAchievements();
        if (achievements == null || achievements.isEmpty()) {
            return List.of(); // Geef een lege lijst terug als er geen achievements zijn
        }
        // Filter achievements die binnen de laatste 5 seconden zijn ontgrendeld
        return achievements.stream()
                .filter(achievement -> achievement.getUnlockTime() != null &&
                        achievement.getUnlockTime().isAfter(now.minusSeconds(5)))
                .peek(achievement -> logger.info("Mapping achievement: " + achievement.getAchievementName()))
                .map(achievementMapper::toDto)
                .toList();
    }

}


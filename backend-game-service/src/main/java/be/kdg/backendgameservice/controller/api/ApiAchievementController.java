package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.AchievementDto;
import be.kdg.backendgameservice.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class ApiAchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<List<AchievementDto>> getAchievements() {
        return ResponseEntity.ok(achievementService.getAchievements());
    }

    @GetMapping("/player/recent")
    public ResponseEntity<List<AchievementDto>> getRecentAchievements(@AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(achievementService.getRecentAchievements(playerId));
    }

}

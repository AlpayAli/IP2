package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.PlayerDto;
import be.kdg.backendgameservice.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leaderboard")
public class ApiLeaderboardController {
    private final LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<PlayerDto>> getLeaderboardOnBalance() {
        return ResponseEntity.ok(leaderboardService.getLeaderboardOnBalance());
    }

    @GetMapping("/xp")
    public ResponseEntity<List<PlayerDto>> getLeaderboardOnXp() {
        return ResponseEntity.ok(leaderboardService.getLeaderboardOnXp());
    }


}

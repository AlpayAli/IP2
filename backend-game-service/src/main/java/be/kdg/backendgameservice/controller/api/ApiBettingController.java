package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.BettingRoundDto;
import be.kdg.backendgameservice.controller.dto.NewPlayerActionDto;
import be.kdg.backendgameservice.controller.dto.PlayerActionDto;
import be.kdg.backendgameservice.service.BettingRoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class ApiBettingController {
    private final BettingRoundService bettingRoundService;


    @PostMapping()
    public ResponseEntity<PlayerActionDto> performAction(
            @AuthenticationPrincipal Jwt principal,
            @RequestBody NewPlayerActionDto newPlayerAction) {
        UUID playerUUID = UUID.fromString(principal.getSubject());

        return ResponseEntity.ok(bettingRoundService.handlePlayerAction(playerUUID, newPlayerAction.getRoundId(),  newPlayerAction.getActionType(), newPlayerAction.getRaiseAmount()));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<BettingRoundDto> getLatestBettingRoundOfGame(@PathVariable UUID gameId) {
        return ResponseEntity.ok(bettingRoundService.getLatestBettingRoundOfGame(gameId));
    }
}

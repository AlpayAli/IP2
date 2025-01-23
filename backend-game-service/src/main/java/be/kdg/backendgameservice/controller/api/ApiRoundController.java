package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.RoundDto;
import be.kdg.backendgameservice.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
public class ApiRoundController {
    private final RoundService roundService;

    @PostMapping("/initialize/{gameId}")
    public ResponseEntity<RoundDto> initializeRound(@PathVariable UUID gameId) {
        return ResponseEntity.ok(roundService.initializeRound(gameId));
    }
}

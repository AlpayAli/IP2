package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.PlayerInformationDto;
import be.kdg.backendgameservice.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ApiPlayerProfileController {
    private final PlayerService playerService;

    @GetMapping("/{username}")
    public ResponseEntity<PlayerInformationDto> getProfile(@PathVariable String username, @AuthenticationPrincipal Jwt principal) {
        UUID requestingPlayerId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(playerService.getPlayerInformation(username, requestingPlayerId));
    }
}

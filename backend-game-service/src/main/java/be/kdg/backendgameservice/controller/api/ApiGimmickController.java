package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.GimmickDto;
import be.kdg.backendgameservice.service.GimmickService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gimmicks")
@RequiredArgsConstructor
public class ApiGimmickController {

    private final GimmickService gimmickService;

    @GetMapping
    public ResponseEntity<List<GimmickDto>> getGimmicks() {
        return ResponseEntity.ok(gimmickService.getAllGimmicks());
    }

    @PostMapping("/purchase/{gimmickId}/{type}")
    public ResponseEntity<GimmickDto> purchaseGimmick(@PathVariable UUID gimmickId, @PathVariable String type, @AuthenticationPrincipal Jwt principal){
        UUID playerUUID = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(gimmickService.purchaseGimmick(gimmickId, playerUUID, type));
    }



    @GetMapping("/player")
    public ResponseEntity<List<GimmickDto>> getGimmicksOfPlayer(@AuthenticationPrincipal Jwt principal) {
        UUID playerUUID = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(gimmickService.getGimmicksByPlayer(playerUUID));
    }

    @GetMapping("/available")
    public ResponseEntity<List<GimmickDto>> getAvailableGimmicks(@AuthenticationPrincipal Jwt principal) {
        UUID playerUUID = UUID.fromString(principal.getSubject());
        List<GimmickDto> availableGimmicks = gimmickService.getAvailableGimmicks(playerUUID);
        return ResponseEntity.ok(availableGimmicks);
    }

}

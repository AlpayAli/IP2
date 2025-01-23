package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.MessageDto;
import be.kdg.backendgameservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ApiMessageController {
    private final MessageService messageService;

    @PostMapping("/send/{gameId}")
    public ResponseEntity<MessageDto> sendMessage(@RequestBody String content, @PathVariable String gameId, @AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        UUID gameUUID = UUID.fromString(gameId);
        return ResponseEntity.ok(messageService.sendMessage(gameUUID, playerId, content));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable String gameId) {
        UUID gameUUID = UUID.fromString(gameId);
        return ResponseEntity.ok(messageService.getMessagesForGame(gameUUID));
    }
}

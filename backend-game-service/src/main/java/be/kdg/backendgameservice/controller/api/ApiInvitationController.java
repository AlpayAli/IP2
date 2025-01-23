package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.InvitationDto;
import be.kdg.backendgameservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invites")
public class ApiInvitationController {
    private final InvitationService invitationService;

    @PostMapping("/{gameId}/{receiverName}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InvitationDto> sendInvite(@AuthenticationPrincipal Jwt principal, @PathVariable String gameId, @PathVariable String receiverName) {
        UUID gameUUID = UUID.fromString(gameId);
        UUID senderUUID = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(invitationService.createInvitation(gameUUID, senderUUID, receiverName));
    }

    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<InvitationDto> acceptInvite(@AuthenticationPrincipal Jwt principal, @PathVariable String invitationId) {
        UUID invitationUUID = UUID.fromString(invitationId);
        UUID playerUUID = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(invitationService.acceptInvitation(invitationUUID, playerUUID));
    }

    @PostMapping("/{invitationId}/decline")
    public ResponseEntity<InvitationDto> declineInvite(@AuthenticationPrincipal Jwt principal, @PathVariable String invitationId) {
        UUID invitationUUID = UUID.fromString(invitationId);
        UUID playerUUID = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(invitationService.declineInvitation(invitationUUID, playerUUID));
    }

    @PostMapping("/{gameId}/all")
    public ResponseEntity<List<InvitationDto>> inviteAllFriends(@AuthenticationPrincipal Jwt principal, @PathVariable String gameId) {
        UUID gameUUID = UUID.fromString(gameId);
        UUID playerUUID = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(invitationService.inviteAllFriends(gameUUID, playerUUID));
    }

    @GetMapping
    public ResponseEntity<List<InvitationDto>> getInvitations(@AuthenticationPrincipal Jwt principal) {
        UUID playerUUID = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(invitationService.getInvitationsByPlayer(playerUUID));
    }
}

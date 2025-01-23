package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.FriendDto;
import be.kdg.backendgameservice.controller.dto.FriendRequestResponse;
import be.kdg.backendgameservice.controller.dto.GetFriendRequestsResponse;
import be.kdg.backendgameservice.domain.FriendRequest;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class ApiFriendController {
    private final PlayerService playerService;

    @GetMapping("")
    public ResponseEntity<List<FriendDto>> getFriends(@AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        Player player = playerService.getPlayerByIdWithFriends(playerId);
        return ResponseEntity.ok(player.getFriends().stream().map(friend -> new FriendDto(friend.getId(), friend.getUsername())).toList());
    }

    @GetMapping("/requests")
    public ResponseEntity<List<GetFriendRequestsResponse>> getFriendRequests(@AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        List<FriendRequest> friendRequests = playerService.getFriendRequests(playerId);
        return ResponseEntity.ok(friendRequests.stream().map(friendRequest -> new GetFriendRequestsResponse(friendRequest.getId(), friendRequest.getSender().getId(), friendRequest.getReceiver().getId(), friendRequest.getSender().getUsername(), friendRequest.isAccepted(), friendRequest.getRequestDate())).toList());
    }

    @PostMapping("/{friendId}")
    public ResponseEntity<FriendRequestResponse> createFriendRequest(@AuthenticationPrincipal Jwt principal, @PathVariable UUID friendId) {
        UUID playerId = UUID.fromString(principal.getSubject());
        FriendRequest friendRequest = playerService.createFriendRequest(playerId, friendId);
        return ResponseEntity.ok(new FriendRequestResponse(friendRequest.getId(), friendRequest.getSender().getId(), friendRequest.getReceiver().getId(), friendRequest.isAccepted(), friendRequest.getRequestDate()));
    }

    @PostMapping("/nickname/{nickname}")
    public ResponseEntity<FriendRequestResponse> createFriendRequestByNickname(@AuthenticationPrincipal Jwt principal, @PathVariable String nickname) {
        UUID playerId = UUID.fromString(principal.getSubject());
        FriendRequest friendRequest = playerService.createFriendRequestByNickname(playerId, nickname);
        return ResponseEntity.ok(new FriendRequestResponse(friendRequest.getId(), friendRequest.getSender().getId(), friendRequest.getReceiver().getId(), friendRequest.isAccepted(), friendRequest.getRequestDate()));
    }

    @DeleteMapping("friendRequests/{playerId}")
    public ResponseEntity<Boolean> declineFriendRequest(@AuthenticationPrincipal Jwt principal, @PathVariable UUID playerId) {
        UUID pId = UUID.fromString(principal.getSubject());
        playerService.declineFriendRequest(pId, playerId);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("{friendId}")
    public ResponseEntity<Boolean> removeFriend(@AuthenticationPrincipal Jwt principal, @PathVariable UUID friendId) {
        UUID playerId = UUID.fromString(principal.getSubject());
        playerService.removeFriend(playerId, friendId);
        return ResponseEntity.ok(true);
    }
}

package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.GameDto;
import be.kdg.backendgameservice.controller.dto.NewGameDto;
import be.kdg.backendgameservice.mapper.GameMapper;
import be.kdg.backendgameservice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class ApiGameController {
    private final GameService gameService;
    private final GameMapper gameMapper = new GameMapper();

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GameDto> createNewGame(@AuthenticationPrincipal Jwt principal, @RequestBody NewGameDto newGameDto) {
        UUID hostId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(gameService.createGame(hostId, newGameDto.getMinPlayers(), newGameDto.getMaxPlayers(), newGameDto.getSmallBlind(), newGameDto.getSecurity()));
    }

    @GetMapping("/open")
    public ResponseEntity<List<GameDto>> getOpenGames() {
        return ResponseEntity.ok(gameService.getOpenGames());
    }

    @PostMapping("/{gameId}/start")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GameDto> startGame(@PathVariable String gameId, @AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        UUID gameUUID = UUID.fromString(gameId);
        return ResponseEntity.ok(gameService.startGame(gameUUID, playerId));
    }

    @PostMapping("/{gameId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GameDto> joinGame(@PathVariable String gameId, @AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        UUID gameUUID = UUID.fromString(gameId);
        return ResponseEntity.ok(gameService.joinGame(gameUUID, playerId));
    }

    @PostMapping("/{gameId}/leave")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GameDto> leaveGame(@PathVariable String gameId, @AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        UUID gameUUID = UUID.fromString(gameId);
        return ResponseEntity.ok(gameService.leaveGame(gameUUID, playerId));
    }

    @GetMapping("/{gameId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<GameDto> getGame(@PathVariable UUID gameId) {
        GameDto gameDto = gameService.getGameDtoById(gameId);
        return ResponseEntity.ok(gameDto);
    }

    @GetMapping("/latest")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<GameDto> getLatestGameFromHost(@AuthenticationPrincipal Jwt principal) {
        UUID hostId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(gameService.getLatestGameFromHost(hostId));
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<GameDto>> getActiveGamesOfPlayer(@AuthenticationPrincipal Jwt principal) {
        UUID hostId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(gameService.getGamesOfPlayer(hostId));
    }
}

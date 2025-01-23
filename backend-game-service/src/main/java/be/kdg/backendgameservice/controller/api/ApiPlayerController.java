package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.PlayerDto;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.mapper.PlayerMapper;
import be.kdg.backendgameservice.service.GoogleCloudStorageService;
import be.kdg.backendgameservice.service.PlayerService;
import be.kdg.backendgameservice.utils.RandomAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class ApiPlayerController {
    private final PlayerService playerService;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final PlayerMapper playerMapper = new PlayerMapper();

    @GetMapping("/isRegistered")
    public ResponseEntity<Boolean> isRegistered(@AuthenticationPrincipal Jwt principal) {
        String uuid = principal.getSubject();
        boolean isRegistered = playerService.isPlayerRegistered(uuid);
        return ResponseEntity.ok(isRegistered);
    }

    @PostMapping("/register")
    public ResponseEntity<Player> register(@AuthenticationPrincipal Jwt principal) {
        String uuid = principal.getSubject();
        String email = principal.getClaim("email");
        String username = principal.getClaim("preferred_username");
        String firstName = principal.getClaim("given_name");
        String lastName = principal.getClaim("family_name");
        boolean isMailVerified = principal.getClaim("email_verified");
        String avatarUrl = RandomAvatar.getRandomAvatar();
        Player player = playerService.registerPlayer(uuid, email, username, firstName, lastName, isMailVerified, avatarUrl);
        return ResponseEntity.ok(player);
    }


    @GetMapping("/currentPlayer")
    public ResponseEntity<PlayerDto> getCurrentPlayer(@AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(playerService.getPlayerDtoById(playerId));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable UUID id) {
        Double balance = playerService.getPlayerBalance(id);
        return ResponseEntity.ok(balance);
    }



    @PostMapping("/update")
    public ResponseEntity<PlayerDto> changeNameAndAvatar(
            @AuthenticationPrincipal Jwt principal,
            @RequestParam(value = "newName", required = false) String newName,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        UUID playerId = UUID.fromString(principal.getSubject());

        String avatarUrl = null;

        try {
            if (file != null && !file.isEmpty()) {
                // Upload the avatar file to Google Cloud Storage
                String folder = "general/avatars"; // Example folder in the bucket
                String bucketName = "image_bucket_ip2";

                Path tempFile = Files.createTempFile("avatar-", file.getOriginalFilename());
                file.transferTo(tempFile.toFile());

                String uniqueFileName = googleCloudStorageService.uploadFileWithUniqueName(bucketName, folder, tempFile);

                Files.deleteIfExists(tempFile);

                // Set the avatar URL
                avatarUrl = "https://storage.googleapis.com/" + bucketName + "/" + uniqueFileName;
            }

            return ResponseEntity.ok(playerService.updatePlayerDetails(playerId, newName, avatarUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/spin")
    public ResponseEntity<Integer> spinBonusWheel(@AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        int spin = playerService.spinBonusWheel(playerId);
        return ResponseEntity.ok(spin);
    }
}

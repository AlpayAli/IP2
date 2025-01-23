package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.NotificationDto;
import be.kdg.backendgameservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class ApiNotificationController {
    private final NotificationService notificationService;

    @RequestMapping("")
    public List<NotificationDto> getNotificationsForPlayer(@AuthenticationPrincipal Jwt principal) {
        UUID playerId = UUID.fromString(principal.getSubject());
        return notificationService.getNotificationsForPlayer(playerId);

    }
}

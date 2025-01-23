package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.controller.dto.ChatBotRequestDto;
import be.kdg.backendgameservice.controller.dto.StatsRequestDtoRequest;
import be.kdg.backendgameservice.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ApiChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping
    public ResponseEntity<String> getChatBotResponse(@RequestBody ChatBotRequestDto chatBotRequestDto) {
        return ResponseEntity.ok(chatBotService.getChatBotResponse(chatBotRequestDto.message()));
    }

    @PostMapping("/stats")
    public ResponseEntity<String> getStats(@RequestBody StatsRequestDtoRequest statsRequestDtoRequest) {
        return ResponseEntity.ok(chatBotService.getStats(statsRequestDtoRequest.minPlayers(), statsRequestDtoRequest.maxPlayers(), statsRequestDtoRequest.playTime(), statsRequestDtoRequest.minAge(), statsRequestDtoRequest.usersRated(), statsRequestDtoRequest.domains(), statsRequestDtoRequest.mechanics()).toString());
    }
}

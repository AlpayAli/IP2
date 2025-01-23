package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.ChatBotRequestDto;
import be.kdg.backendgameservice.controller.dto.StatsRequestDtoRequest;
import be.kdg.backendgameservice.controller.dto.StatsRequestDtoResponse;
import be.kdg.backendgameservice.domain.exceptions.ChatBotException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ChatBotService {
    private final Logger _logger = Logger.getLogger(ChatBotService.class.getName());
    @Value("${chatbot.url}")
    private String chatBotUrl;

    public String getChatBotResponse(String message) {
        RestTemplate restTemplate = new RestTemplate();
        _logger.info("Sending message to chatbot: " + message);
        _logger.info("Chatbot url: " + chatBotUrl);
        try {
            String response = restTemplate.postForObject(String.format("%s/query", chatBotUrl), new ChatBotRequestDto(message), String.class);
            _logger.info("Received response from chatbot: " + response);
            return response;
        } catch (Exception e) {
            _logger.severe("An error occurred while trying to get a response from the chatbot.");
            throw new ChatBotException("An error occurred while trying to get a response from the chatbot.", e);
        }
    }

    public StatsRequestDtoResponse getStats(int minPlayers, int maxPlayers, int playTime, int minAge, int usersRated, List<String> domains, List<String> mechanics) {
        RestTemplate restTemplate = new RestTemplate();
        _logger.info("Sending message to AI: \n" + minPlayers + "\n" + maxPlayers + "\n" + playTime + "\n" + minAge + "\n" + usersRated + "\n" + domains + "\n" + mechanics);
        try {
            StatsRequestDtoResponse response = restTemplate.postForObject(String.format("%s/stats", chatBotUrl), new StatsRequestDtoRequest(minPlayers, maxPlayers, playTime, minAge, usersRated, domains, mechanics), StatsRequestDtoResponse.class);
            _logger.info("Received response from chatbot: " + response);
            return response;
        } catch (Exception e) {
            _logger.severe("An error occurred while trying to get a response from the chatbot.");
            throw new ChatBotException("An error occurred while trying to get a response from the chatbot.", e);
        }
    }
}

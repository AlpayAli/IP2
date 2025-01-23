package be.kdg.backendgameservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatBotRequestDto(
        @JsonProperty("query_text")
        String message
) {
}

package be.kdg.backendgameservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StatsRequestDtoRequest(
        @JsonProperty("min_players")
        int minPlayers,
        @JsonProperty("max_players")
        int maxPlayers,
        @JsonProperty("play_time")
        int playTime,
        @JsonProperty("min_age")
        int minAge,
        @JsonProperty("users_rated")
        int usersRated,
        @JsonProperty("domains")
        List<String> domains,
        @JsonProperty("mechanics")
        List<String> mechanics
) {
}

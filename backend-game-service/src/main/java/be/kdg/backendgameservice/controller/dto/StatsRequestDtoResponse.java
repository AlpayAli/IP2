package be.kdg.backendgameservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatsRequestDtoResponse(
        @JsonProperty("average_rating")
        float averageRating,
        @JsonProperty("complexity_rating")
        float complexityRating,
        @JsonProperty("owned_users")
        float ownedUsers
) {
}

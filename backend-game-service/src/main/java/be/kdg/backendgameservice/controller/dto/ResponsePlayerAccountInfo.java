package be.kdg.backendgameservice.controller.dto;


import be.kdg.backendgameservice.domain.Card;

import java.util.List;

public record ResponsePlayerAccountInfo(
        String id,
        String name,
        String email,
        double balance,
        List<Card> currentCards
) {
}

package be.kdg.backendgameservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ShowDownResult {
    private final List<PlayerEntry> winners;
    private final double totalPot;
    private final List<Card> communityCards;
}

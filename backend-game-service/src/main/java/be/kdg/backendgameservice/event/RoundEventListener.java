package be.kdg.backendgameservice.event;

import be.kdg.backendgameservice.domain.Round;
import be.kdg.backendgameservice.service.RoundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoundEventListener {

    private final RoundService roundService;

    @EventListener
    public void handleRoundAdvanced(RoundAdvancedEvent event) {
        Round round = event.getRound();
        log.info("Round advanced to status: {}", round.getStatus());
        roundService.advanceRound(round.getId());
        log.info("Round advanced to status: {}", round.getStatus());

        // Eventueel verdere acties, zoals het informeren van spelers
    }
}

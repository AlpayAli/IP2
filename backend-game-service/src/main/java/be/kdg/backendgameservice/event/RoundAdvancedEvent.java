package be.kdg.backendgameservice.event;

import be.kdg.backendgameservice.domain.Round;
import org.springframework.context.ApplicationEvent;

public class RoundAdvancedEvent extends ApplicationEvent {
    private final Round round;

    public RoundAdvancedEvent(Object source, Round round) {
        super(source);
        this.round = round;
    }

    public Round getRound() {
        return round;
    }
}

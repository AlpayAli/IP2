package be.kdg.backendgameservice.event;

import be.kdg.backendgameservice.domain.HandRank;
import be.kdg.backendgameservice.domain.Player;
import org.springframework.context.ApplicationEvent;

public class PlayerWonWithHandRankEvent extends ApplicationEvent {
    private final Player player;
    private final HandRank handRank;

    public PlayerWonWithHandRankEvent(Object source, Player player, HandRank handRank) {
        super(source);
        this.player = player;
        this.handRank = handRank;
    }

    public Player getPlayer() {
        return player;
    }

    public HandRank getHandRank() {
        return handRank;
    }
}


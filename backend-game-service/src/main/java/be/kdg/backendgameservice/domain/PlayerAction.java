package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAction {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PlayerEntry playerEntry;
    private PlayerActionType actionType;
    private int amount;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BettingRound bettingRound;
}

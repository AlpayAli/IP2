package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BettingRound {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Round round;
    private int currentHighestBet;
    @ManyToOne
    private PlayerEntry currentPlayer;
    @OneToMany(mappedBy = "bettingRound", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PlayerAction> playerActions = new ArrayList<>();
    private LocalDateTime createdAt;
}

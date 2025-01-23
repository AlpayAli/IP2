package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Round {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Game game;
    @OneToOne
    private Deck deck;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlayerEntry> activePlayers = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PlayerEntry> winners = new ArrayList<>();
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Card> tableCards = new ArrayList<>();
    private int pot;
    @Enumerated(EnumType.STRING)
    private RoundStatus status;
    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
    private List<BettingRound> bettingRounds = new ArrayList<>();
}

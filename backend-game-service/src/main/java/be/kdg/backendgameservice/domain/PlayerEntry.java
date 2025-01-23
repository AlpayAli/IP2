package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PlayerEntry {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Player player;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Card> currentCards = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Game game;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private PlayerRole role;
    private double balanceInGame;
}

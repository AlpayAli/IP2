package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Game {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerEntry> playerEntries = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Round> rounds = new ArrayList<>();
    private LocalDateTime startTime;
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus = GameStatus.WAITING_FOR_PLAYERS;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitation> invitations;
    @ManyToOne
    private PlayerEntry host;
    @Column(nullable = false)
    private boolean isOpen;
    private int smallBlind;
    private int minPlayers;
    private int maxPlayers;
    @OneToMany
    private List<Message> messages;
    @ManyToOne
    private Player winner;
    private double buyInAmount = this.smallBlind * 100;
}

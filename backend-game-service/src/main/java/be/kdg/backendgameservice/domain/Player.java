package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Player {
    @Id
    private UUID id;
    private String name;
    private String username;
    private String firstName;
    private String lastName;
    private double balance;
    private int xp;
    private String email;
    private LocalDateTime registrationDate;
    private String avatarUrl;
    private LocalDate lastBonusDate;
    @Column(columnDefinition = "boolean default true")
    private boolean isDailySpinAvailable = true;
    @Column(columnDefinition = "boolean default false")
    private boolean isMailVerified = false;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "player_friends",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<Player> friends = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FriendRequest> friendRequests = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FriendRequest> sentFriendRequests = new HashSet<>();

    @ManyToMany
    private List<Achievement> achievements = new ArrayList<>();

    @ManyToMany
    private List<Gimmick> gimmicks = new ArrayList<>();

    @OneToMany
    private List<PlayerEntry> playerEntries = new ArrayList<>();


    public Player(UUID playerId) {
        this.id = playerId;
    }
}

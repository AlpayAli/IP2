package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Achievement {
    @Id
    @GeneratedValue
    private UUID achievementId;
    private String achievementName;
    private String achievementDescription;
    private int achievementXp;
    private LocalDateTime unlockTime = null;


    @Enumerated(EnumType.STRING)
    private HandRank handRank;


}

package be.kdg.backendgameservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Invitation {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Game game;
    @ManyToOne
    private Player sender;
    @ManyToOne
    private Player receiver;
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;
    private LocalDateTime dateSend;
}

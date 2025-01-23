package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.BettingRound;
import be.kdg.backendgameservice.domain.PlayerAction;
import be.kdg.backendgameservice.domain.PlayerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerActionRepository extends JpaRepository<PlayerAction, UUID> {
    List<PlayerAction> findAllByPlayerEntryAndBettingRound(PlayerEntry playerEntry, BettingRound bettingRound);

    List<PlayerAction> findAllByBettingRound(BettingRound bettingRound);
}

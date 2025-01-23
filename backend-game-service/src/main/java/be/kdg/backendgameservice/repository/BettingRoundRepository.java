package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.BettingRound;
import be.kdg.backendgameservice.domain.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BettingRoundRepository extends JpaRepository<BettingRound, UUID> {
    Optional<BettingRound> findAllByRound(Round round);
}

package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoundRepository extends JpaRepository<Round, UUID> {
    Optional<Round> findByGame(Game game);

    @Query("SELECT r FROM Round r JOIN r.winners w WHERE w.player = :player")
    List<Round> findRoundsWonByPlayer(@Param("player") Player player);

}

package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.PlayerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findAllByIsOpenTrue();

    @Query("SELECT g FROM Game g JOIN g.playerEntries pe WHERE pe.player.id = :playerId")
    List<Game> findGamesByPlayerId(@Param("playerId") UUID playerId);

    Optional<Game> findTopByHostIdAndStartTimeNotNullOrderByStartTimeDesc(UUID hostId);

    List<Game> findByWinner(Player player);
}

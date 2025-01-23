package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.PlayerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerEntryRepository extends JpaRepository<PlayerEntry, UUID> {
    Optional<PlayerEntry> findByPlayerAndGame(Player player, Game game);
    List<PlayerEntry> findByPlayer(Player player);

}

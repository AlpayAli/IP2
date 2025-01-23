package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
    List<Player> findAllByOrderByXpDesc();

    @Query("SELECT DISTINCT p FROM Player p LEFT JOIN FETCH p.friends WHERE p.id IN :ids")
    List<Player> findAllByIdWithFriends(@Param("ids") List<UUID> ids);

    @Query("SELECT p FROM Player p LEFT JOIN FETCH p.friends WHERE p.id = :playerId")
    Optional<Player> findByIdWithFriends(UUID playerId);

    @Modifying
    @Query(value = "DELETE FROM player_friends WHERE player_id = :playerId OR friend_id = :playerId", nativeQuery = true)
    void deletePlayerFriends(@Param("playerId") UUID playerId);

    @Query("SELECT p FROM Player p WHERE p.username = :username")
    Optional<Player> findByUsername(String username);

    Optional<Player> findByUsernameIgnoreCase(String name);

    Optional<Player> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"gimmicks"})
    Optional<Player> findWithGimmicksById(UUID playerId);


    List<Player> findAllByOrderByBalanceDesc();
}

package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.FriendRequest;
import be.kdg.backendgameservice.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {

    boolean existsBySenderAndReceiver(Player sender, Player receiver);

    Optional<FriendRequest> findBySenderAndReceiver(Player sender, Player receiver);

    @Query("SELECT fr FROM FriendRequest fr LEFT JOIN FETCH fr.sender LEFT JOIN FETCH fr.receiver WHERE fr.id = :friendRequestId")
    Optional<FriendRequest> findByIdWithSenderAndReceiverWithFriends(UUID friendRequestId);

    @Query("SELECT fr FROM FriendRequest fr LEFT JOIN FETCH fr.sender LEFT JOIN FETCH fr.receiver WHERE fr.receiver = :receiver AND fr.accepted = false")
    List<FriendRequest> findAllByReceiverAndAcceptedIsFalse(Player receiver);

    List<FriendRequest> findAllByReceiverIdAndAccepted(UUID playerId, Boolean accepted);
}

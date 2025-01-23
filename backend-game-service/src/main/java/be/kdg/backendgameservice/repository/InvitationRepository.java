package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Invitation;
import be.kdg.backendgameservice.domain.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    List<Invitation> getInvitationsByReceiverId(UUID userId);

    List<Invitation> findAllByReceiverIdAndStatus(UUID playerId, InvitationStatus invitationStatus);
}

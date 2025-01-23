package be.kdg.backendgameservice.repository;


import be.kdg.backendgameservice.domain.Gimmick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GimmickRepository  extends JpaRepository<Gimmick, UUID> {

    Optional<Gimmick> findById(UUID uuid);
}

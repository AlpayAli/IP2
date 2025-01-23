package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeckRepository extends JpaRepository<Deck, UUID> {
}

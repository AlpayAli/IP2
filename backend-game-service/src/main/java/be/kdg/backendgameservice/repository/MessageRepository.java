package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByGameOrderByDateSendAsc(Game game);
}

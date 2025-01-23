package be.kdg.backendgameservice.repository;

import be.kdg.backendgameservice.domain.Achievement;
import be.kdg.backendgameservice.domain.HandRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, UUID> {
    List<Achievement> findAllByHandRank(HandRank handRank);
}

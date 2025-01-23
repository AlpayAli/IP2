package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.PlayerDto;
import be.kdg.backendgameservice.mapper.PlayerMapper;
import be.kdg.backendgameservice.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final PlayerRepository playerRepository;

    public List<PlayerDto> getLeaderboardOnBalance() {
        PlayerMapper playerMapper = new PlayerMapper();
        return playerRepository.findAllByOrderByBalanceDesc().stream()
                .map(playerMapper::toDtoLeaderboard)
                .toList();
    }

    public List<PlayerDto> getLeaderboardOnXp() {
        PlayerMapper playerMapper = new PlayerMapper();
        return playerRepository.findAllByOrderByXpDesc().stream()
                .map(playerMapper::toDtoLeaderboard)
                .toList();
    }
}

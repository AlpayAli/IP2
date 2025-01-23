package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.GimmickDto;
import be.kdg.backendgameservice.domain.Gimmick;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.exceptions.*;
import be.kdg.backendgameservice.mapper.GimmickMapper;
import be.kdg.backendgameservice.repository.GimmickRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GimmickService {

    private final GimmickRepository gimmickRepository;
    private final PlayerRepository playerRepository;
    private static final Logger logger = Logger.getLogger(RoundService.class.getName());
    GimmickMapper mapper = new GimmickMapper();


    public List<GimmickDto> getAllGimmicks() {
        List<Gimmick> gimmicks = gimmickRepository.findAll();

        return gimmicks.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    public Gimmick getGimmickById(UUID id) {
        return gimmickRepository.findById(id).orElseThrow(() -> new GimmickNotFoundException(String.format("Gimmick with id: %s not found.", id)));
    }

    @Transactional
    public GimmickDto purchaseGimmick(UUID gimmickId, UUID playerId, String costType) {
        Gimmick gimmick = getGimmickById(gimmickId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id: %s not found.", playerId)));

        // Check if the player already owns the gimmick
        if (player.getGimmicks().contains(gimmick)) {
            throw new AlreadyOwningException(String.format("Player %s already owns the gimmick: %s", player.getUsername(), gimmick.getName()));
        }

        // Determine cost type and validate points
        int cost = costType.equalsIgnoreCase("xp") ? gimmick.getXpCost() : gimmick.getBalanceCost();
        int playerPoints = costType.equalsIgnoreCase("xp") ? player.getXp() : (int) player.getBalance();

        if (cost > playerPoints) {
            logger.warning(String.format("Player %s doesn't have enough %s to purchase gimmick: %s",
                    player.getUsername(), costType, gimmick.getName()));
            throw new NotEnoughBalanceException(String.format("Insufficient %s for player %s to purchase gimmick %s",
                    costType, player.getUsername(), gimmick.getName()));
        }

        // Deduct points and add gimmick
        if (costType.equalsIgnoreCase("xp")) {
            player.setXp(player.getXp() - cost);
        } else {
            player.setBalance(player.getBalance() - cost);
        }
        player.getGimmicks().add(gimmick);

        // Save changes
        playerRepository.save(player);

        // Log successful purchase
        logger.info(String.format("Player %s successfully purchased gimmick: %s", player.getUsername(), gimmick.getName()));

        return mapper.toDto(gimmick);
    }


    @Transactional
    public List<GimmickDto> getGimmicksByPlayer(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id: %s not found.", playerId)));

        List<Gimmick> gimmicks = player.getGimmicks();
        return gimmicks.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GimmickDto> getAvailableGimmicks(UUID playerId) {
        List<Gimmick> allGimmicks = gimmickRepository.findAll();
        Player player = playerRepository.findWithGimmicksById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id: %s not found.", playerId)));

        List<Gimmick> playerGimmicks = player.getGimmicks();
        List<Gimmick> availableGimmicks = allGimmicks.stream()
                .filter(gimmick -> !playerGimmicks.contains(gimmick))
                .toList();

        return availableGimmicks.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}

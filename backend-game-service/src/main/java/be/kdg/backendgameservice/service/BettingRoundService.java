package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.BettingRoundDto;
import be.kdg.backendgameservice.controller.dto.PlayerActionDto;
import be.kdg.backendgameservice.domain.*;
import be.kdg.backendgameservice.domain.Round;
import be.kdg.backendgameservice.domain.exceptions.*;
import be.kdg.backendgameservice.event.RoundAdvancedEvent;
import be.kdg.backendgameservice.mapper.BettingRoundMapper;
import be.kdg.backendgameservice.mapper.PlayerActionMapper;
import be.kdg.backendgameservice.repository.*;
import be.kdg.backendgameservice.repository.PlayerActionRepository;
import be.kdg.backendgameservice.repository.GameRepository;
import be.kdg.backendgameservice.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BettingRoundService {
    private final ApplicationEventPublisher eventPublisher;
    private final BettingRoundRepository bettingRoundRepository;
    private final BettingRoundMapper bettingRoundMapper = new BettingRoundMapper();
    private final PlayerActionMapper playerActionMapper = new PlayerActionMapper();
    private final RoundRepository roundRepository;
    private final PlayerActionRepository playerActionRepository;
    private final GameRepository gameRepository;
    private final PlayerEntryRepository playerEntryRepository;

    @Transactional
    public PlayerActionDto handlePlayerAction(UUID playerId, String roundId, PlayerActionType actionType, int amount) {
        UUID roundUuid;
        try {
            roundUuid = UUID.fromString(roundId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for roundId: " + roundId);
        }


        Round round = roundRepository.findById(roundUuid)
                .orElseThrow(() -> new RoundNotFoundException("Round not found with id: " + roundId));

        PlayerEntry playerEntry = round.getActivePlayers().stream()
                .filter(p -> p.getPlayer().getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException("PlayerEntry not found in this round"));


        List<BettingRound> bettingRounds = round.getBettingRounds();
        bettingRounds.sort(Comparator.comparing(BettingRound::getCreatedAt));
        BettingRound bettingRound = bettingRounds.get(bettingRounds.size() - 1);

        validatePlayerAction(playerEntry, bettingRound, actionType, amount);

        PlayerActionDto playerActionDto = new PlayerActionDto();

        switch (actionType) {
            case CHECK -> {
                playerActionDto = processCheck(playerEntry, bettingRound);
            }
            case FOLD -> {
                playerActionDto = processFold(playerEntry, bettingRound);
            }
            case RAISE -> {
                playerActionDto = processRaise(playerEntry, bettingRound, amount);
            }
            case CALL -> {
                playerActionDto = processCall(playerEntry, bettingRound);
            }
        }
        moveToNextPlayer(bettingRound);
        return playerActionDto;
    }

    protected void validatePlayerAction(PlayerEntry playerEntry, BettingRound bettingRound, PlayerActionType actionType, int amount) {
        if (!bettingRound.getCurrentPlayer().getId().equals(playerEntry.getId())) {
            throw new WrongPlayerException("It's not your turn!");
        }
        switch (actionType) {
            case CHECK -> {
                if (bettingRound.getCurrentPlayer().getBalanceInGame() == 0) {
                    break;
                }
                if (bettingRound.getCurrentHighestBet() > 0) {
                    List<PlayerAction> playerActions = playerActionRepository.findAllByPlayerEntryAndBettingRound(playerEntry, bettingRound);
                    int amountAlreadyBet = 0;
                    for (PlayerAction action : playerActions) {
                        amountAlreadyBet += action.getAmount();
                    }
                    if (amountAlreadyBet == bettingRound.getCurrentHighestBet()) {
                        break;
                    }

                    throw new NotAbleToCheckException("Player cannot check; a bet exists.");
                }
            }
            case RAISE -> {
                int alreadyBet = bettingRound.getPlayerActions()
                        .stream()
                        .filter(bet -> bet.getPlayerEntry().equals(playerEntry))
                        .mapToInt(PlayerAction::getAmount)
                        .sum();

                if (amount <= bettingRound.getCurrentHighestBet()) {
                    throw new CannotRaiseArgumentException("Raise must be higher than current bet.");
                }

                if (playerEntry.getBalanceInGame() + alreadyBet < amount) {
                    throw new CannotRaiseStateException("Player does not have enough balance to raise.");
                }
                if (amount <= bettingRound.getRound().getGame().getSmallBlind() * 2) {
                    throw new CannotRaiseArgumentException("Raise must be higher than big blind.");
                }

            }
            case CALL -> {
                int alreadyBet = bettingRound.getPlayerActions()
                        .stream()
                        .filter(bet -> bet.getPlayerEntry().equals(playerEntry))
                        .mapToInt(PlayerAction::getAmount)
                        .sum();
                if (bettingRound.getCurrentHighestBet() == 0) {
                    throw new CannotCallException("Player cannot call; no bet exists.");
                }

                if (playerEntry.getBalanceInGame() + alreadyBet < bettingRound.getCurrentHighestBet() ) {
                    throw new CannotCallException("Player does not have enough balance to call.");
                }

            }
        }
    }

    public PlayerActionDto processCheck(PlayerEntry playerEntry, BettingRound bettingRound) {
        return processPlayerAction(playerEntry, bettingRound, 0, PlayerActionType.CHECK);
    }

    public PlayerActionDto processFold(PlayerEntry playerEntry, BettingRound bettingRound) {
        bettingRound.getRound().getActivePlayers().remove(playerEntry);
        return processPlayerAction(playerEntry, bettingRound, 0, PlayerActionType.FOLD);
    }

    public PlayerActionDto processRaise(PlayerEntry playerEntry, BettingRound bettingRound, int amount) {
        int alreadyBet = bettingRound.getPlayerActions()
                .stream()
                .filter(bet -> bet.getPlayerEntry().equals(playerEntry))
                .mapToInt(PlayerAction::getAmount)
                .sum();

        int actualRaise = amount - alreadyBet;

        if (playerEntry.getBalanceInGame() < actualRaise) {
            throw new NotEnoughBalanceException("Player does not have enough balance to raise.");
        }

        playerEntry.setBalanceInGame(playerEntry.getBalanceInGame() - actualRaise);
        bettingRound.setCurrentHighestBet(amount);

        return processPlayerAction(playerEntry, bettingRound, actualRaise, PlayerActionType.RAISE);
    }


    public PlayerActionDto processCall(PlayerEntry playerEntry, BettingRound bettingRound) {
        List<PlayerAction> playerActions = playerActionRepository.findAllByPlayerEntryAndBettingRound(playerEntry, bettingRound);
        int amountAlreadyBet = 0;

        for (PlayerAction action : playerActions) {
            amountAlreadyBet += action.getAmount();
        }

        int amountToCall = bettingRound.getCurrentHighestBet() - amountAlreadyBet;
        playerEntry.setBalanceInGame(playerEntry.getBalanceInGame() - amountToCall);

        return processPlayerAction(playerEntry, bettingRound, amountToCall, PlayerActionType.CALL);
    }


    public PlayerActionDto processPlayerAction(PlayerEntry playerEntry, BettingRound bettingRound, int amount, PlayerActionType actionType) {
        PlayerAction playerAction = new PlayerAction();
        playerAction.setPlayerEntry(playerEntry);
        playerAction.setActionType(actionType);
        playerAction.setAmount(amount);
        playerAction.setBettingRound(bettingRound);
        bettingRound.getPlayerActions().add(playerAction);
        bettingRound.getRound().setPot(bettingRound.getRound().getPot() + amount);
        bettingRoundRepository.save(bettingRound);
        playerEntryRepository.save(playerEntry);

        return playerActionMapper.toDto(playerAction);
    }

    @Transactional
    public BettingRound addFirstBettingRoundToRound(Round round) {
        BettingRound bettingRound = new BettingRound();
        bettingRound.setRound(round);
        bettingRound.setCreatedAt(LocalDateTime.now());
        round.getBettingRounds().add(bettingRound);
        roundRepository.save(round);
        placeBlinds(round);
        return bettingRound;
    }

    public void placeBlinds(Round round) {
        PlayerEntry smallBlindPlayer = round.getActivePlayers().stream()
                .filter(p -> p.getRole() == PlayerRole.SMALL_BLIND)
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("No player with SMALL_BLIND role"));

        PlayerEntry bigBlindPlayer = round.getActivePlayers().stream()
                .filter(p -> p.getRole() == PlayerRole.BIG_BLIND)
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("No player with BIG_BLIND role"));

        int smallBlindAmount = round.getGame().getSmallBlind();
        int bigBlindAmount = smallBlindAmount * 2;

        processRaise(smallBlindPlayer, round.getBettingRounds().get(0), smallBlindAmount);
        processRaise(bigBlindPlayer, round.getBettingRounds().get(0), bigBlindAmount);

        List<PlayerEntry> activePlayers = round.getActivePlayers();
        int bigBlindIndex = activePlayers.indexOf(bigBlindPlayer);
        int nextPlayerIndex = (bigBlindIndex + 1) % activePlayers.size();

        round.getBettingRounds().get(0).setCurrentPlayer(activePlayers.get(nextPlayerIndex));
        bettingRoundRepository.save(round.getBettingRounds().get(0));
    }

    @Transactional
    public void moveToNextPlayer(BettingRound bettingRound) {
        List<PlayerEntry> activePlayers = bettingRound.getRound().getActivePlayers();
        PlayerEntry currentPlayer = bettingRound.getCurrentPlayer();
        int currentIndex = activePlayers.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % activePlayers.size();

        if (isBettingRoundComplete(bettingRound)) {
            handleBettingRoundCompletion(bettingRound);
        }

        bettingRound.setCurrentPlayer(activePlayers.get(nextIndex));
        bettingRoundRepository.save(bettingRound);
    }

    public BettingRoundDto getLatestBettingRoundOfGame(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with ID " + gameId + " not found"));

        Round round = roundRepository.findByGame(game)
                .orElseThrow(() -> new RoundNotFoundException("No round found for Game ID " + gameId));

        List<BettingRound> bettingRounds = round.getBettingRounds();

        if (bettingRounds.isEmpty()) {
            throw new BettingRoundNotFoundException("No betting rounds found for Round ID " + round.getId());
        }

        // Zorg ervoor dat de lijst sorteerbaar is
        bettingRounds.sort(Comparator.comparing(BettingRound::getCreatedAt));
        BettingRound latestBettingRound = bettingRounds.get(bettingRounds.size() - 1);

        return bettingRoundMapper.toDto(latestBettingRound);
    }


    private boolean isBettingRoundComplete(BettingRound bettingRound) {
        List<PlayerEntry> activePlayers = bettingRound.getRound().getActivePlayers();

        if (activePlayers.size() <=  1) {
            return true;
        }

        List<PlayerEntry> playersWithActions = playerActionRepository.findAllByBettingRound(bettingRound)
                .stream()
                .map(PlayerAction::getPlayerEntry)
                .distinct()
                .toList();

        List<BettingRound> bettingRounds = bettingRound.getRound().getBettingRounds();
        bettingRounds.sort(Comparator.comparing(BettingRound::getCreatedAt));
        boolean isFirstBettingRound = bettingRounds.get(0) == bettingRound;

        Optional<PlayerEntry> bigBlindPlayerOptional = activePlayers.stream()
                .filter(p -> p.getRole() == PlayerRole.BIG_BLIND)
                .findFirst();

        if (isFirstBettingRound && bigBlindPlayerOptional.isPresent()) {
            PlayerEntry bigBlindPlayer = bigBlindPlayerOptional.get();
            int bigBlindActions = playerActionRepository.findAllByPlayerEntryAndBettingRound(bigBlindPlayer, bettingRound)
                    .size();

            if (bigBlindActions < 2) {
                return false;
            }
        }

        if (!new HashSet<>(playersWithActions).containsAll(activePlayers)) {
            return false;
        }


        int currentHighestBet = bettingRound.getCurrentHighestBet();
        for (PlayerEntry playerEntry : activePlayers) {
            int amountBetByPlayer = playerActionRepository.findAllByPlayerEntryAndBettingRound(playerEntry, bettingRound)
                    .stream()
                    .mapToInt(PlayerAction::getAmount)
                    .sum();

            if (amountBetByPlayer != currentHighestBet) {
                return false;
            }
        }
        return true;
    }

    private void handleBettingRoundCompletion(BettingRound bettingRound) {
        Round round = bettingRound.getRound();
        eventPublisher.publishEvent(new RoundAdvancedEvent(this, round));
    }
}





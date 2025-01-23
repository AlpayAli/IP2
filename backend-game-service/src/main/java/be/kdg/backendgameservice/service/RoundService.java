package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.RoundDto;
import be.kdg.backendgameservice.domain.*;
import be.kdg.backendgameservice.domain.exceptions.GameNotFoundException;
import be.kdg.backendgameservice.domain.exceptions.NoActivePlayersFoundException;
import be.kdg.backendgameservice.domain.exceptions.RoundNotFoundException;
import be.kdg.backendgameservice.event.PlayerWonWithHandRankEvent;
import be.kdg.backendgameservice.factory.DeckFactory;
import be.kdg.backendgameservice.mapper.RoundMapper;
import be.kdg.backendgameservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final ApplicationEventPublisher eventPublisher;
    private final DeckService deckService;
    private final RoundRepository roundRepository;
    private final PlayerService playerService;
    private final BettingRoundService bettingRoundService;
    private final GameRepository gameRepository;
    private final DeckFactory deckFactory;

    private final RoundMapper roundMapper = new RoundMapper();
    private static final Logger logger = Logger.getLogger(RoundService.class.getName());
    private final BettingRoundRepository bettingRoundRepository;
    private final PlayerEntryRepository playerEntryRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public RoundDto initializeRound(UUID gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " not found."));

        Round round = new Round();
        round.setGame(game);
        round.setStatus(RoundStatus.PRE_FLOP);
        //TODO: Factory
        Deck deck = deckFactory.createDeck();
        round.setDeck(deck);
        List<PlayerEntry> copiedPlayers = new ArrayList<>(game.getPlayerEntries());
        round.setActivePlayers(copiedPlayers);
        round.setTableCards(new ArrayList<>());

        assignRolesToPlayers(round);
        dealHoleCards(round);

        game.getRounds().add(round);
        round.getBettingRounds().add(bettingRoundService.addFirstBettingRoundToRound(round));

        return roundMapper.toDto(round);
    }


    public void dealHoleCards(Round round) {
        Deck deck = round.getDeck();
        for (PlayerEntry playerEntry : round.getActivePlayers()) {
            playerService.removeCardsFromPlayer(playerEntry);
            for (int i = 0; i < 2; i++) {
                playerService.addCardToPlayer(playerEntry, deck);
            }
        }
    }

    @Transactional
    public RoundDto advanceRound(UUID roundId) {
        Round round = roundRepository.findById(roundId).orElseThrow(() -> new RoundNotFoundException("Round with id " + roundId + " not found."));

        RoundStatus currentStatus = round.getStatus();
        Deck deck = round.getDeck();

        if (currentStatus == null) {
            throw new IllegalStateException("Round status is undefined.");
        }

        if (currentStatus == RoundStatus.ENDED) {
            throw new IllegalStateException("Round status has already ended.");
        }

        int cardsToDeal = 0;
        RoundStatus nextStatus = null;

        switch (currentStatus) {
            case PRE_FLOP:
                cardsToDeal = 3; // 3 kaarten voor de flop
                nextStatus = RoundStatus.FLOP;
                break;
            case FLOP:
                cardsToDeal = 1; // Eén kaart voor de turn
                nextStatus = RoundStatus.TURN;
                break;

            case TURN:
                cardsToDeal = 1; // Eén kaart voor de river
                nextStatus = RoundStatus.RIVER;
                break;

            case RIVER:
                // SHOWDOWN
                nextStatus = RoundStatus.ENDED;
                break;
        }

        if (round.getActivePlayers().size() == 1) {
            cardsToDeal = 0;
            nextStatus = RoundStatus.ENDED;
        }

        if (cardsToDeal > 0) {
            List<Card> dealtCards = deckService.dealCards(deck, cardsToDeal);
            round.getTableCards().addAll(dealtCards);
        }
        round.setStatus(nextStatus);

        if (nextStatus == RoundStatus.ENDED) {
            ShowDownResult result = executeShowdown(round);
            List<PlayerEntry> copiedWinners = new ArrayList<>(result.getWinners());
            round.setWinners(copiedWinners);
            round.getActivePlayers().stream().filter(playerEntry -> playerEntry.getBalanceInGame() < 1).forEach(
                    playerEntry ->
                    {
                        round.getGame().getPlayerEntries().remove(playerEntry);
                    }
            );

            if (round.getGame().getPlayerEntries().size() == 1) {
                round.getGame().setWinner(round.getWinners().get(0).getPlayer());
                round.getGame().setGameStatus(GameStatus.ENDED);
            }
            gameRepository.save(round.getGame());
        } else {
            round.getBettingRounds().add(addNewBettingRoundToRound(round));
        }

        return roundMapper.toDto(roundRepository.save(round));
    }

    @Transactional
    public ShowDownResult executeShowdown(Round round) {
        List<PlayerEntry> activePlayers = round.getActivePlayers();

        if (activePlayers.isEmpty()) {
            throw new NoActivePlayersFoundException("Geen actieve spelers aanwezig voor de showdown.");
        }

        // Bepaal de winnaar(s)
        List<PlayerEntry> winners = determineWinner(round);

        // Haal de handrang op van de winnaar(s)
        for (PlayerEntry winner : winners) {
            PokerHand winningHand = evaluatePlayerHand(winner, round.getTableCards());
            HandRank winningHandRank = winningHand.getRank();

            // Publiceer een event om achievements toe te kennen
            eventPublisher.publishEvent(new PlayerWonWithHandRankEvent(this, winner.getPlayer(), winningHandRank));
        }

        // Pot verdelen onder de winnaars
        double totalPot = round.getPot();
        distributePot(winners, totalPot / winners.size());

        List<String> winnerNames = winners.stream()
                .map(p -> p.getPlayer().getUsername())
                .toList();

        String result = String.join(",", winnerNames);

        logger.info("Showdown completed. Total pot: " + totalPot + ", Winners: " + result);

        return new ShowDownResult(winners, totalPot, round.getTableCards());
    }


    private PokerHand evaluatePlayerHand(PlayerEntry playerEntry, List<Card> tableCards) {
        List<Card> fullHand = new ArrayList<>(tableCards);
        fullHand.addAll(playerEntry.getCurrentCards());
        return new PokerHand(fullHand);
    }


    private List<PlayerEntry> determineWinner(Round round) {
        if (round.getActivePlayers() == null || round.getActivePlayers().isEmpty()) {
            throw new NoActivePlayersFoundException("No active players found to determine the winner.");
        }

        // Controleer of de tableCards niet null is
        List<Card> tableCards = round.getTableCards();
        if (tableCards == null) {
            tableCards = new ArrayList<>();
        }

        Map<PlayerEntry, PokerHand> playerHands = new HashMap<>();

        for (PlayerEntry playerEntry : round.getActivePlayers()) {
            List<Card> fullHand = new ArrayList<>(tableCards);

            // Controleer of de speler kaarten heeft
            if (playerEntry.getCurrentCards() != null) {
                fullHand.addAll(playerEntry.getCurrentCards());
            }

            playerHands.put(playerEntry, new PokerHand(fullHand));
        }

        PokerHand bestHand = Collections.max(playerHands.values());

        return playerHands.entrySet().stream()
                .filter(entry -> entry.getValue().equals(bestHand))
                .map(Map.Entry::getKey)
                .toList();
    }

    @Transactional
    public void distributePot(List<PlayerEntry> winners, double winningsPerPlayer) {
        for (PlayerEntry winnerEntry : winners) {
            winnerEntry.setBalanceInGame(winnerEntry.getBalanceInGame() + winningsPerPlayer);
            playerEntryRepository.save(winnerEntry);
        }
    }

    @Transactional
    public void assignRolesToPlayers(Round round) {
        List<PlayerEntry> activePlayers = round.getActivePlayers();

        if (activePlayers.isEmpty()) {
            throw new IllegalStateException("No active players to assign roles.");
        }

        int smallBlindIndex = round.getGame().getRounds().isEmpty()
                ? 0
                : (round.getGame().getRounds().size() - 1) % activePlayers.size();
        int bigBlindIndex = (smallBlindIndex + 1) % activePlayers.size();


        for (int i = 0; i < activePlayers.size(); i++) {
            PlayerEntry playerEntry = activePlayers.get(i);
            if (i == smallBlindIndex) {
                playerEntry.setRole(PlayerRole.SMALL_BLIND);
            } else if (i == bigBlindIndex) {
                playerEntry.setRole(PlayerRole.BIG_BLIND);
            } else {
                playerEntry.setRole(PlayerRole.NONE);
            }
        }

        logger.info("Roles assigned: " + ", Small Blind - " + activePlayers.get(smallBlindIndex).getPlayer().getUsername()
                + ", Big Blind - " + activePlayers.get(bigBlindIndex).getPlayer().getUsername());
    }


    private BettingRound addNewBettingRoundToRound(Round round) {
        BettingRound bettingRound = new BettingRound();
        bettingRound.setRound(round);
        bettingRound.setCreatedAt(LocalDateTime.now());
        int roundNumber = round.getGame().getRounds().size();
        int activePlayersInGame = round.getActivePlayers().size();
        bettingRound.setCurrentPlayer(round.getActivePlayers().get(roundNumber % activePlayersInGame));
        return bettingRound;
    }
}

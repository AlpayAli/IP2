package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.PlayerDto;
import be.kdg.backendgameservice.controller.dto.PlayerInformationDto;
import be.kdg.backendgameservice.domain.*;
import be.kdg.backendgameservice.domain.exceptions.*;
import be.kdg.backendgameservice.event.MailEvent;
import be.kdg.backendgameservice.mapper.PlayerMapper;
import be.kdg.backendgameservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final DeckService deckService;
    private final ApplicationEventPublisher eventPublisher;
    private final PlayerMapper playerMapper = new PlayerMapper();
    private final PlayerEntryRepository playerEntryRepository;
    private final RoundRepository roundRepository;
    private final GameRepository gameRepository;

    @Value("${player.starting-balance}")
    private double startingBalance;
    @Value("${player.starting-xp}")
    private int startingXp;

    public List<Player> findAllPlayersByXp() {
        return playerRepository.findAllByOrderByXpDesc();
    }

    @Transactional
    public Player getPlayerById(UUID id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id: %s not found.", id)));
    }

    @Transactional
    public PlayerDto getPlayerDtoById(UUID id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id: %s not found.", id)));
        return playerMapper.toDto(player);
    }

    public List<Player> findPlayersByIds(List<UUID> ids) {
        return playerRepository.findAllById(ids);
    }

    public boolean isPlayerRegistered(String playerId) {
        try {
            UUID playerUUID = UUID.fromString(playerId);
            return isPlayerRegistered(playerUUID);
        } catch (IllegalArgumentException e) {
            throw new InvalidPlayerIdException("Invalid player ID format: " + playerId);
        }
    }

    public boolean isPlayerRegistered(UUID playerId) {
        try {
            return getPlayerById(playerId) != null;
        } catch (PlayerNotFoundException pnfe) {
            return false;
        }
    }


    public Player registerPlayer(String playerId, String email, String username, String firstName, String lastName, boolean isMailVerified, String avatarUrl) {
        try {
            UUID playerUUID = UUID.fromString(playerId);
            return registerPlayer(playerUUID, email, username, firstName, lastName, isMailVerified, avatarUrl);
        } catch (IllegalArgumentException e) {
            throw new InvalidPlayerIdException("Invalid player ID format: " + playerId);
        }
    }

    public Player registerPlayer(UUID playerId, String email, String username, String firstName, String lastName, boolean isMailVerified, String avatarUrl) {
        if (isPlayerRegistered(playerId)) {
            throw new PlayerAlreadyExistsException("Player with ID: " + playerId + " already exists.", null);
        }
        Player player = new Player(playerId);
        player.setEmail(email);
        player.setUsername(username);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setXp(startingXp);
        player.setBalance(startingBalance);
        player.setRegistrationDate(LocalDateTime.now());
        player.setMailVerified(isMailVerified);
        player.setAvatarUrl(avatarUrl);

        try {
            player = playerRepository.save(player);
        } catch (DataIntegrityViolationException e) {
            throw new PlayerAlreadyExistsException("Player with ID: " + playerId + " already exists.", e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while registering player.", e);
        }
        MailEvent mailEvent = new MailEvent(this, player.getEmail(), "Confirmation mail", "You have successfully registered for KdG Poker!");
        eventPublisher.publishEvent(mailEvent);
        return player;
    }

    public void removeCardsFromPlayer(PlayerEntry playerEntry) {
        playerEntry.getCurrentCards().clear();
        playerEntryRepository.save(playerEntry);
    }

    public void addCardToPlayer(PlayerEntry playerEntry, Deck deck) {
        playerEntry.getCurrentCards().add(deckService.drawOneCard(deck));
        playerEntryRepository.save(playerEntry);
    }

    public void addBalanceToPlayer(Player player, double winnings) {
        player.setBalance(player.getBalance() + winnings);
        playerRepository.save(player);
    }


    public double getPlayerBalance(UUID playerId) {
        Player player = getPlayerById(playerId);
        return player.getBalance();
    }

    public FriendRequest createFriendRequest(UUID senderId, UUID receiverId) {
        Player sender = getPlayerById(senderId);
        Player receiver = getPlayerById(receiverId);
        return createFriendRequest(sender, receiver);
    }

    public FriendRequest createFriendRequest(Player sender, Player receiver) {
        if (sender.equals(receiver)) {
            throw new WrongInviteException("Player cannot send friend request to themselves");
        }
        if (hasFriendRequest(sender, receiver)) {
            throw new WrongInviteException("Friend request already exists");
        }

        if (hasFriendRequest(receiver, sender)) {
            return acceptFriendRequest(sender, getFriendRequest(receiver, sender));
        }

        if (isFriend(sender.getId(), receiver.getId())) {
            throw new WrongInviteException("Players are already friends");
        }

        if (sender.getId() == null || receiver.getId() == null) {
            throw new WrongInviteException("Sender or receiver is not saved yet.");
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setRequestDate(LocalDateTime.now());
        MailEvent mailEvent = new MailEvent(this, receiver.getEmail(), "Friend request", "You have received a friend request from " + sender.getUsername());
        eventPublisher.publishEvent(mailEvent);
        return friendRequestRepository.save(friendRequest);
    }


    public FriendRequest getFriendRequestById(UUID friendRequestId) {
        return friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new FriendRequestNotFoundException("Friend request with ID: " + friendRequestId + " doesn't exist."));
    }

    public FriendRequest acceptFriendRequest(UUID playerId, UUID friendRequestId) {
        Player player = getPlayerByIdWithFriends(playerId);
        FriendRequest friendRequest = getFriendRequestWithUserAndSenderWithFriends(friendRequestId);
        return acceptFriendRequest(player, friendRequest);
    }

    public FriendRequest acceptFriendRequest(Player receiver, FriendRequest friendRequest) {
        if (isFriend(receiver.getId(), friendRequest.getSender().getId())) {
            throw new WrongInviteException("Players are already friends");
        }
        if (!friendRequest.getReceiver().getId().equals(receiver.getId())) {
            throw new WrongInviteException("Player with ID: " + receiver.getId() + " is not the receiver of the friend request with ID: " + friendRequest.getId());
        }

        // Markeer het verzoek als geaccepteerd
        friendRequest.setAcceptedDate(LocalDateTime.now());
        friendRequest.setAccepted(true);

        // Voeg de zender en ontvanger toe aan elkaars vriendenlijst
        receiver.getFriends().add(friendRequest.getSender());
        friendRequest.getSender().getFriends().add(receiver);

        // Sla de gewijzigde spelers op in de database
        playerRepository.saveAll(List.of(friendRequest.getSender(), receiver));
        // Sla het geaccepteerde vriendschapsverzoek op
        MailEvent mailEvent = new MailEvent(this, friendRequest.getSender().getEmail(), "Friend request accepted", "Your friend request to " + receiver.getUsername() + " has been accepted.");
        eventPublisher.publishEvent(mailEvent);
        return friendRequestRepository.save(friendRequest);
    }


    public void removeFriend(UUID playerId, UUID friendId) {
        List<Player> players = findPlayersByIds(List.of(playerId, friendId));
        if (!players.get(0).getFriends().contains(players.get(1)) || !players.get(1).getFriends().contains(players.get(0))) {
            throw new NotFriendsException("Players are not friends");
        }
        players.get(0).getFriends().remove(players.get(1));
        players.get(1).getFriends().remove(players.get(0));
        playerRepository.saveAll(players);
    }

    private boolean isFriend(UUID playerId, UUID friendId) {
        Player player = getPlayerById(playerId);
        Player friend = getPlayerById(friendId);
        return player.getFriends().contains(friend) && friend.getFriends().contains(player);
    }

    private boolean hasFriendRequest(UUID senderId, UUID receiverId) {
        Player sender = getPlayerById(senderId);
        Player receiver = getPlayerById(receiverId);
        return hasFriendRequest(sender, receiver);
    }

    public boolean hasFriendRequest(Player sender, Player receiver) {
        return friendRequestRepository.existsBySenderAndReceiver(sender, receiver);
    }

    public FriendRequest getFriendRequest(UUID senderId, UUID receiverId) {
        Player sender = getPlayerById(senderId);
        Player receiver = getPlayerById(receiverId);
        return getFriendRequest(sender, receiver);
    }

    public FriendRequest getFriendRequest(Player sender, Player receiver) {
        return friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new FriendRequestNotFoundException("Friend request not found"));
    }

    private List<Player> findPlayersByIdsWithFriends(List<UUID> ids) {
        return playerRepository.findAllByIdWithFriends(ids);
    }

    public FriendRequest getFriendRequestWithUserAndSenderWithFriends(UUID friendRequestId) {
        return friendRequestRepository.findByIdWithSenderAndReceiverWithFriends(friendRequestId)
                .orElseThrow(() -> new FriendRequestNotFoundException("Friend request with ID: " + friendRequestId + " doesn't exist."));
    }


    public Player getPlayerByIdWithFriends(UUID playerId) {
        return playerRepository.findByIdWithFriends(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with ID: " + playerId + " not found."));
    }

    public FriendRequest createFriendRequestByNickname(UUID playerId, String username) {
        Player sender = getPlayerById(playerId);
        Player receiver = playerRepository.findByUsernameIgnoreCase(username)
                .or(() -> playerRepository.findByEmailIgnoreCase(username))
                .orElseThrow(() -> new PlayerNotFoundException("Player with username/email: " + username + " not found."));
        return createFriendRequest(sender, receiver);
    }

    public List<FriendRequest> getFriendRequests(UUID playerId) {
        Player player = getPlayerById(playerId);
        return friendRequestRepository.findAllByReceiverAndAcceptedIsFalse(player);
    }

    public void declineFriendRequest(UUID playerId, UUID friendId) {
        Player player = getPlayerById(playerId);
        FriendRequest friendRequest = getFriendRequest(friendId, playerId);
        declineFriendRequest(player, friendRequest);
    }

    public void declineFriendRequest(Player player, FriendRequest friendRequest) {
        if (!friendRequest.getReceiver().getId().equals(player.getId())) {
            throw new WrongInviteException("Player with ID: " + player.getId() + " is not the receiver of the friend request with ID: " + friendRequest.getId());
        }
        friendRequestRepository.delete(friendRequest);
    }


    @Transactional
    public PlayerDto updatePlayerDetails(UUID playerId, String newName, String avatarUrl) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        if (newName != null && !newName.trim().isEmpty()) {
            player.setUsername(newName);
        }
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            player.setAvatarUrl(avatarUrl); // Assuming Player entity has an avatarUrl field
        }

        return playerMapper.toDto(playerRepository.save(player));
    }


    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Amsterdam")
    public void updatePlayerDailySpinScheduled() {
        List<Player> players = playerRepository.findAll();
        for (Player player : players) {
            player.setDailySpinAvailable(true);
        }
        playerRepository.saveAll(players);
    }


    public int spinBonusWheel(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        if (player.isDailySpinAvailable()) {
            int bonus = spinBonus();
            addBalanceToPlayer(player, bonus);
            player.setDailySpinAvailable(false);
            playerRepository.save(player);
            return bonus;
        }
        return 0;
    }

    private int spinBonus() {
        int[] BONUS_OPTIONS = {10, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500}; // Bonusopties
        Random random = new Random();
        return BONUS_OPTIONS[random.nextInt(BONUS_OPTIONS.length)];
    }

    @Transactional
    public PlayerInformationDto getPlayerInformation(String username, UUID requestingPlayerUUID) {
        Player playerInQuestion = playerRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new PlayerNotFoundException("Profile not found"));

        Player requestingPlayer = playerRepository.findById(requestingPlayerUUID)
                .orElseThrow(() -> new PlayerNotFoundException("Requesting player not found"));

        PlayerInformationDto result = new PlayerInformationDto();
        result.setUsername(playerInQuestion.getUsername());
        result.setXp(playerInQuestion.getXp());
        result.setBalance(playerInQuestion.getBalance());
        result.setFriendConnections(playerInQuestion.getFriends().size());

        result.setMutualFriends(playerInQuestion
                .getFriends()
                .stream()
                .filter(requestingPlayer.getFriends()::contains)
                .toList().size()
        );
        result.setAchievementsGathered(playerInQuestion.getAchievements().size());
        result.setAvatarUrl(playerInQuestion.getAvatarUrl());
        result.setAlreadyFriends(playerInQuestion.getFriends().contains(requestingPlayer) && requestingPlayer.getFriends().contains(playerInQuestion));
        result.setFriendNames(playerInQuestion.getFriends().stream().map(Player::getUsername).toList());
        result.setAchievementNames(playerInQuestion.getAchievements().stream().map(Achievement::getAchievementName).toList());

        result.setRoundsWon(roundRepository.findRoundsWonByPlayer(playerInQuestion).size());
        result.setGamesWon(gameRepository.findByWinner(playerInQuestion).size());
        return result;
    }
}

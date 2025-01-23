package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.domain.FriendRequest;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.exceptions.PlayerAlreadyExistsException;
import be.kdg.backendgameservice.repository.FriendRequestRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(TestcontainersConfiguration.class)
class PlayerServiceTest {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void isPlayerRegisteredShouldReturnTrue() {
        Player player = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        player = playerRepository.save(player);
        assertTrue(playerService.isPlayerRegistered("00000000-0000-0000-0000-000000000000"));
        playerRepository.delete(player);
    }

    @Test
    void isPlayerRegisteredShouldReturnFalse() {
        assertFalse(playerService.isPlayerRegistered(UUID.randomUUID().toString()));
    }

    @Test
    void registerPlayerShouldSucceed() {
        Player player = playerService.registerPlayer(UUID.randomUUID().toString(), "test@test.com", "testUser", "Test", "User", true,"");
        assertNotNull(player);
        playerRepository.delete(player);
    }

    @Test
    void registerPlayerShouldFail() {
        Player player = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        player = playerRepository.save(player);
        assertThrows(PlayerAlreadyExistsException.class, () -> playerService.registerPlayer("00000000-0000-0000-0000-000000000000", "test@test.com", "testUser", "Test", "User",  true,""));
        playerRepository.delete(player);
    }

    @Test
    void createFriendRequestShouldSucceed() {
        Player player1 = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player2 = new Player(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        player1 = playerRepository.save(player1);
        player2 = playerRepository.save(player2);
        FriendRequest friendRequest = playerService.createFriendRequest(UUID.fromString("00000000-0000-0000-0000-000000000000"), UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertTrue(friendRequestRepository.existsBySenderAndReceiver(player1, player2));
        friendRequestRepository.delete(friendRequest);
        playerRepository.delete(player1);
        playerRepository.delete(player2);
    }

    @Test
    void createFriendRequestShouldAcceptBecauseOfExistingFriendRequest() {
        Player player1 = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player2 = new Player(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        player1 = playerRepository.save(player1);
        player2 = playerRepository.save(player2);
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(player1);
        friendRequest.setReceiver(player2);
        friendRequest.setRequestDate(LocalDateTime.now());
        friendRequest.setAccepted(true);
        friendRequest = friendRequestRepository.save(friendRequest);
        FriendRequest friendRequest2 = playerService.createFriendRequest(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertTrue(friendRequest2.isAccepted());
        friendRequestRepository.delete(friendRequest);
        friendRequestRepository.delete(friendRequest2);
        playerRepository.delete(player1);
        playerRepository.delete(player2);
    }

    @Test
    void getFriendRequestById() {
        Player sender = new Player(UUID.randomUUID());
        Player receiver = new Player(UUID.randomUUID());
        FriendRequest friendRequest = new FriendRequest();
        sender = playerRepository.save(sender);
        receiver = playerRepository.save(receiver);
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setRequestDate(LocalDateTime.now());
        friendRequest = friendRequestRepository.save(friendRequest);
        assertEquals(friendRequest.getId(), playerService.getFriendRequestById(friendRequest.getId()).getId());
        friendRequestRepository.delete(friendRequest);
        playerRepository.delete(sender);
        playerRepository.delete(receiver);
    }

    @Test
    void acceptFriendRequest() {
        // Create two new players
        Player sender = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player receiver = new Player(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        // Save the players in the database
        sender = playerRepository.save(sender);
        receiver = playerRepository.save(receiver);

        // Create and save the FriendRequest
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setRequestDate(LocalDateTime.now());
        friendRequest = friendRequestRepository.save(friendRequest);

        // Accept the FriendRequest via the service
        friendRequest = playerService.acceptFriendRequest(receiver.getId(), friendRequest.getId());

        // Verify that the FriendRequest was accepted
        assertTrue(friendRequest.isAccepted());

        // Verify that the players are now friends
        assertTrue(sender.getFriends().contains(receiver));
        assertTrue(receiver.getFriends().contains(sender));

        // Remove the FriendRequest and associated relationships
        friendRequestRepository.delete(friendRequest);

        // Remove friendship relationships and delete the players
        playerRepository.deletePlayerFriends(sender.getId());
        playerRepository.deletePlayerFriends(receiver.getId());
        playerRepository.delete(sender);
        playerRepository.delete(receiver);
    }


    @Test
    void removeFriend() {
        // Create two new players
        Player player1 = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player player2 = new Player(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        // Save the players in the database
        player1 = playerRepository.save(player1);
        player2 = playerRepository.save(player2);

        // Add player2 as a friend of player1
        player1.getFriends().add(player2);
        player2.getFriends().add(player1);

        // Save the players with the new friendship
        player1 = playerRepository.save(player1);
        player2 = playerRepository.save(player2);

        // Remove the friendship
        playerService.removeFriend(player1.getId(), player2.getId());

        // Verify that the friendship was removed
        assertFalse(player1.getFriends().contains(player2));
        assertFalse(player2.getFriends().contains(player1));

        // Remove the players
        playerRepository.delete(player1);
        playerRepository.delete(player2);
    }

    @Test
    void hasFriendRequest() {
        // Create two new players
        Player sender = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player receiver = new Player(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        // Save the players in the database
        sender = playerRepository.save(sender);
        receiver = playerRepository.save(receiver);

        // Create and save the FriendRequest
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setRequestDate(LocalDateTime.now());
        friendRequest = friendRequestRepository.save(friendRequest);

        // Verify that the receiver has a friend request
        assertTrue(playerService.hasFriendRequest(sender, receiver));

        // Remove the FriendRequest and associated relationships
        friendRequestRepository.delete(friendRequest);

        // Remove friendship relationships and delete the players
        playerRepository.deletePlayerFriends(sender.getId());
        playerRepository.deletePlayerFriends(receiver.getId());
        playerRepository.delete(sender);
        playerRepository.delete(receiver);
    }

    @Test
    void getFriendRequest() {
        // Create two new players
        Player sender = new Player(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Player receiver = new Player(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        // Save the players in the database
        sender = playerRepository.save(sender);
        receiver = playerRepository.save(receiver);

        // Create and save the FriendRequest
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setRequestDate(LocalDateTime.now());
        friendRequest = friendRequestRepository.save(friendRequest);

        // Verify that the receiver has a friend request
        assertEquals(friendRequest, playerService.getFriendRequest(sender, receiver));

        // Remove the FriendRequest and associated relationships
        friendRequestRepository.delete(friendRequest);

        // Remove friendship relationships and delete the players
        playerRepository.deletePlayerFriends(sender.getId());
        playerRepository.deletePlayerFriends(receiver.getId());
        playerRepository.delete(sender);
        playerRepository.delete(receiver);
    }
}
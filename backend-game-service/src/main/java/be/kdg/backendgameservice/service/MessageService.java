package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.controller.dto.MessageDto;
import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.domain.Message;
import be.kdg.backendgameservice.domain.Player;
import be.kdg.backendgameservice.domain.exceptions.GameNotFoundException;
import be.kdg.backendgameservice.domain.exceptions.PlayerNotFoundException;
import be.kdg.backendgameservice.mapper.MessageMapper;
import be.kdg.backendgameservice.repository.GameRepository;
import be.kdg.backendgameservice.repository.MessageRepository;
import be.kdg.backendgameservice.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final GameService gameService;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final MessageMapper messageMapper = new MessageMapper();

    @Transactional
    public MessageDto sendMessage(UUID gameId, UUID playerId, String message) {
        Game game = this.getGame(gameId);
        Player player = this.getPlayer(playerId);
        Message newMessage = new Message();
        newMessage.setGame(game);
        newMessage.setSender(player);
        newMessage.setContent(message);
        newMessage.setDateSend(LocalDateTime.now());
        return messageMapper.toDto(messageRepository.save(newMessage));
    }

    @Transactional
    public List<MessageDto> getMessagesForGame(UUID gameId) {
        Game game = this.getGame(gameId);
        return messageMapper.toDtoList(messageRepository.findByGameOrderByDateSendAsc(game));
    }

    private Player getPlayer(UUID playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id: " + playerId + " not found."));
    }

    private Game getGame(UUID gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + gameId + " not found"));
    }
}

package be.kdg.backendgameservice.mapper;


import be.kdg.backendgameservice.controller.dto.MessageDto;
import be.kdg.backendgameservice.domain.Message;

import java.util.List;
import java.util.stream.Collectors;

public class MessageMapper {
    public MessageDto toDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setPlayerName(message.getSender().getUsername());
        dto.setContent(message.getContent());
        dto.setDateSend(message.getDateSend());
        return dto;
    }

    public List<MessageDto> toDtoList(List<Message> messages) {
        return messages.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

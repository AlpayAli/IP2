package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {
    private String playerName;
    private String content;
    private LocalDateTime dateSend;
}

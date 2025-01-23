package be.kdg.backendgameservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InvitationDto {
    private UUID invitationId;
    private UUID gameId;
    private String senderName;
    private String receiverName;
    private String status;
    private LocalDateTime dateSend;

}

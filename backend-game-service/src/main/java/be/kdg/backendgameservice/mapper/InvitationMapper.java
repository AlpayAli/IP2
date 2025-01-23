package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.InvitationDto;
import be.kdg.backendgameservice.domain.Invitation;

public class InvitationMapper {
    public InvitationDto toDto(Invitation invitation) {
        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setInvitationId(invitation.getId());
        invitationDto.setGameId(invitation.getGame().getId());
        invitationDto.setReceiverName(invitation.getReceiver().getUsername());
        invitationDto.setSenderName(invitation.getSender().getUsername());
        invitationDto.setStatus(invitation.getStatus().toString());
        invitationDto.setDateSend(invitation.getDateSend());
        return invitationDto;
    }
}

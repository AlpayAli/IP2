package be.kdg.backendgameservice.mapper;

import be.kdg.backendgameservice.controller.dto.GimmickDto;
import be.kdg.backendgameservice.domain.Gimmick;

public class GimmickMapper {
    public GimmickDto toDto(Gimmick gimmick) {
        GimmickDto dto = new GimmickDto();
        dto.setId(gimmick.getId());
        dto.setName(gimmick.getName());
        dto.setXpCost(gimmick.getXpCost());
        dto.setBalanceCost(gimmick.getBalanceCost());
        dto.setImageUrl(gimmick.getImageUrl());
        return dto;
    }
}

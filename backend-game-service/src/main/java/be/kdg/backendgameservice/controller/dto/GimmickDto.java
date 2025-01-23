package be.kdg.backendgameservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GimmickDto {

    private UUID id;
    private String name;
    private int xpCost;
    private int balanceCost;
    private String imageUrl;

}

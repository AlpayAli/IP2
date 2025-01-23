package be.kdg.backendgameservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntryDto {
    private UUID id;
    private String name;
    private List<String> currentCards;
    private int balance;
    private String avatarUrl;
    private int xp;

}

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
public class PlayerDto {
    private UUID id;
    private String name;
    private int balance;
    private String avatarUrl;
    private int xp;
}

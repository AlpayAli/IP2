package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.controller.dto.GameDto;
import be.kdg.backendgameservice.domain.Game;
import be.kdg.backendgameservice.mapper.GameMapper;
import be.kdg.backendgameservice.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class GameServiceIntegrationTest {

    @MockitoBean
    private GameRepository gameRepository;

    @MockitoBean
    private GameMapper gameMapper;

    @Autowired
    private GameService gameService;



    @Test
    void getOpenGames_ShouldReturnListOfOpenGames() {
        // Arrange
        Game game1 = new Game();
        game1.setId(UUID.randomUUID());
        game1.setOpen(true);

        Game game2 = new Game();
        game2.setId(UUID.randomUUID());
        game2.setOpen(true);

        List<Game> openGames = List.of(game1, game2);

        when(gameRepository.findAllByIsOpenTrue()).thenReturn(openGames);

        // Act
        List<GameDto> result = gameService.getOpenGames();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2); // Controleer dat het aantal games klopt
        verify(gameRepository, times(1)).findAllByIsOpenTrue();
    }


    @Test
    void getOpenGames_NoOpenGames_ShouldReturnEmptyList() {
        // Arrange
        when(gameRepository.findAllByIsOpenTrue()).thenReturn(Collections.emptyList());

        // Act
        List<GameDto> result = gameService.getOpenGames();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty(); // Controleer dat de lijst leeg is
        verify(gameRepository, times(1)).findAllByIsOpenTrue();
        verifyNoInteractions(gameMapper); // Controleer dat de mapper niet werd aangeroepen
    }

    @Test
    void getOpenGames_NullRepositoryResponse_ShouldThrowException() {
        // Arrange
        when(gameRepository.findAllByIsOpenTrue()).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> gameService.getOpenGames());
    }


}

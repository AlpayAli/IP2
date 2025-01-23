package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ApiLeaderboardControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    void getLeaderboardOnBalanceShouldReturnSortedByBalance() throws Exception {
        mockMvc.perform(get("/api/leaderboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(10)) // Validate total number of players

                // Validate top players based on balance
                .andExpect(jsonPath("$[0].balance").value(999990))
                .andExpect(jsonPath("$[1].balance").value(666660))
                .andExpect(jsonPath("$[2].balance").value(500000))

                // Validate a player in the middle of the leaderboard
                .andExpect(jsonPath("$[5].balance").value(10000))

                // Validate the last player based on balance
                .andExpect(jsonPath("$[9].balance").value(500));
    }

    @Test
    void getLeaderboardOnXpShouldReturnSortedByXp() throws Exception {
        mockMvc.perform(get("/api/leaderboard/xp")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(10)) // Validate total number of players

                // Validate top players based on XP
                .andExpect(jsonPath("$[0].xp").value(700))
                .andExpect(jsonPath("$[1].xp").value(700))
                .andExpect(jsonPath("$[2].xp").value(700))

                // Validate a player in the middle of the leaderboard
                .andExpect(jsonPath("$[5].xp").value(200))

                // Validate the last player based on XP
                .andExpect(jsonPath("$[8].xp").value(100));
    }

}
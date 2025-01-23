package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.domain.Deck;
import be.kdg.backendgameservice.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//TODO: mag weg denk ik?

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class ApiDeckController {

    private final DeckService deckService;

    @GetMapping("shuffle")
    public Deck getShuffledDeck() {
        Deck deck = new Deck();
        return deckService.shuffle(deck);
    }
}

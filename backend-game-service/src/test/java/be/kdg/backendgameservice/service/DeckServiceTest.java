package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.config.TestcontainersConfiguration;
import be.kdg.backendgameservice.domain.Card;
import be.kdg.backendgameservice.domain.Deck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class DeckServiceTest {
    @Autowired
    private DeckService deckService;

    @Test
    void testInitializeDeck() {
        Deck deck = deckService.initializeDeck();

        assertNotNull(deck);
        assertEquals(52, deck.getCards().size());
    }

    @Test
    void testDealCards() {
        Deck deck = deckService.initializeDeck();
        List<Card> dealtCards = deckService.dealCards(deck, 3);

        assertEquals(dealtCards.size(), 3);
        // 1 burn card & 3 normal cards uit het deck.
        assertEquals(deck.getCards().size(), 48);
    }

    @Test
    void testShuffleCards() {
        Deck deck = deckService.initializeDeck();
        Deck shuffledDeck = deckService.shuffle(deck);

        assertNotEquals(deck, shuffledDeck);
    }

}
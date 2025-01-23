package be.kdg.backendgameservice.factory;

import be.kdg.backendgameservice.domain.Card;
import be.kdg.backendgameservice.domain.Deck;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DeckFactory {
    public Deck createDeck() {
        Deck deck = new Deck();
        deck.setCards(new ArrayList<>(List.of(Card.values())));
        return shuffle(deck);
    }

    private Deck shuffle(Deck deck) {
        List<Card> cards = deck.getCards();
        Collections.shuffle(cards);
        deck.setCards(cards);
        return deck;
    }
}

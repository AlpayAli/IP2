package be.kdg.backendgameservice.service;

import be.kdg.backendgameservice.domain.Card;
import be.kdg.backendgameservice.domain.Deck;
import be.kdg.backendgameservice.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;

    public Deck initializeDeck() {
        Deck deck = new Deck();
        deck.setCards(new ArrayList<>(List.of(Card.values())));
        return shuffle(deck);
    }

    public Deck shuffle(Deck deck) {
        List<Card> cards = deck.getCards();
        Collections.shuffle(cards);
        deck.setCards(cards);
        return deckRepository.save(deck);
    }

    public Card drawOneCard(Deck deck) {
        List<Card> cards = deck.getCards();
        Card cardToDraw = deck.getCards().get(0);
        cards.remove(cardToDraw);
        deck.setCards(cards);
        deckRepository.save(deck);
        return cardToDraw;
    }

    @Transactional
    public List<Card> dealCards(Deck deck, int numberOfCards) {
        List<Card> cards = deck.getCards();
        // burn 1 card
        cards.remove(0);

        List<Card> dealtCards = new ArrayList<Card>(cards.subList(0, numberOfCards));
        cards.subList(0, numberOfCards).clear();
        deck.setCards(cards);
        deckRepository.save(deck);
        return dealtCards;
    }
}

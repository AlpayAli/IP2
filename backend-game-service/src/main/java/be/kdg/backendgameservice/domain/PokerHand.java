package be.kdg.backendgameservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class PokerHand implements Comparable<PokerHand> {
    private List<Card> cards;
    private HandRank rank;

    public PokerHand(List<Card> cards) {
        this.cards = cards;
        this.rank = evaluateHandRank(cards);
    }

    private HandRank evaluateHandRank(List<Card> cards) {
        if (isRoyalFlush(cards)) return HandRank.ROYAL_FLUSH;
        if (isStraightFlush(cards)) return HandRank.STRAIGHT_FLUSH;
        if (isFourOfAKind(cards)) return HandRank.FOUR_OF_A_KIND;
        if (isFullHouse(cards)) return HandRank.FULL_HOUSE;
        if (isFlush(cards)) return HandRank.FLUSH;
        if (isStraight(cards)) return HandRank.STRAIGHT;
        if (isThreeOfAKind(cards)) return HandRank.THREE_OF_A_KIND;
        if (isTwoPair(cards)) return HandRank.TWO_PAIR;
        if (isOnePair(cards)) return HandRank.ONE_PAIR;
        return HandRank.HIGH_CARD;
    }

    private boolean isRoyalFlush(List<Card> cards) {
        return isStraightFlush(cards) && cards.stream().anyMatch(card -> card.getRank() == Rank.ACE);
    }

    private boolean isStraightFlush(List<Card> cards) {
        return isFlush(cards) && isStraight(cards);
    }

    private boolean isFourOfAKind(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting())).containsValue(4L);
    }

    private boolean isFullHouse(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));
        return rankCounts.containsValue(3L) && rankCounts.containsValue(2L);
    }

    private boolean isFlush(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()))
                .size() == 1;
    }

    private boolean isStraight(List<Card> cards) {
        List<Integer> sortedRanks = cards.stream()
                .map(card -> card.getRank().getValue())
                .distinct()
                .sorted()
                .toList();

        // Check voor standaard straights
        for (int i = 0; i <= sortedRanks.size() - 5; i++) {
            if (sortedRanks.get(i + 4) - sortedRanks.get(i) == 4) {
                return true;
            }
        }

        // Speciale case voor ACE als laagste kaart (5, 4, 3, 2, ACE)
        return sortedRanks.containsAll(Arrays.asList(14, 2, 3, 4, 5));
    }


    private boolean isThreeOfAKind(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting())).containsValue(3L);
    }

    private boolean isTwoPair(List<Card> cards) {
        long pairs = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()))
                .values().stream()
                .filter(count -> count == 2)
                .count();
        return pairs == 2;
    }

    private boolean isOnePair(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting())).containsValue(2L);
    }


    @Override
    public int compareTo(PokerHand other) {
        if (this.rank != other.rank) {
            return Integer.compare(this.rank.getValue(), other.rank.getValue());
        }
        // Als de rank gelijk is, vergelijk de kaarten.
        return compareHighCards(this.cards, other.cards);
    }

    private int compareHighCards(List<Card> cards1, List<Card> cards2) {
        // Sorteer de kaarten in aflopende volgorde
        List<Integer> sortedRanks1 = cards1.stream()
                .map(card -> card.getRank().getValue())
                .sorted(Comparator.reverseOrder())
                .toList();

        List<Integer> sortedRanks2 = cards2.stream()
                .map(card -> card.getRank().getValue())
                .sorted(Comparator.reverseOrder())
                .toList();

        // Vergelijk elke kaart van hoog naar laag
        for (int i = 0; i < Math.min(sortedRanks1.size(), sortedRanks2.size()); i++) {
            int comparison = Integer.compare(sortedRanks1.get(i), sortedRanks2.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }
        // Als alle kaarten gelijk zijn
        return 0;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PokerHand pokerHand = (PokerHand) obj;

        if (this.rank != pokerHand.rank) {
            return false;
        }

        // Vergelijk kaarten
        return compareHighCards(this.cards, pokerHand.cards) == 0;
    }

    @Override
    public int hashCode() {
        return rank.hashCode();
    }
}

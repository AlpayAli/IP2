package be.kdg.backendgameservice.domain;


public enum Card {
    // Clubs
    TWO_OF_CLUBS(Suit.CLUBS, Rank.TWO),
    THREE_OF_CLUBS(Suit.CLUBS, Rank.THREE),
    FOUR_OF_CLUBS(Suit.CLUBS, Rank.FOUR),
    FIVE_OF_CLUBS(Suit.CLUBS, Rank.FIVE),
    SIX_OF_CLUBS(Suit.CLUBS, Rank.SIX),
    SEVEN_OF_CLUBS(Suit.CLUBS, Rank.SEVEN),
    EIGHT_OF_CLUBS(Suit.CLUBS, Rank.EIGHT),
    NINE_OF_CLUBS(Suit.CLUBS, Rank.NINE),
    TEN_OF_CLUBS(Suit.CLUBS, Rank.TEN),
    JACK_OF_CLUBS(Suit.CLUBS, Rank.JACK),
    QUEEN_OF_CLUBS(Suit.CLUBS, Rank.QUEEN),
    KING_OF_CLUBS(Suit.CLUBS, Rank.KING),
    ACE_OF_CLUBS(Suit.CLUBS, Rank.ACE),

    // Diamonds
    TWO_OF_DIAMONDS(Suit.DIAMONDS, Rank.TWO),
    THREE_OF_DIAMONDS(Suit.DIAMONDS, Rank.THREE),
    FOUR_OF_DIAMONDS(Suit.DIAMONDS, Rank.FOUR),
    FIVE_OF_DIAMONDS(Suit.DIAMONDS, Rank.FIVE),
    SIX_OF_DIAMONDS(Suit.DIAMONDS, Rank.SIX),
    SEVEN_OF_DIAMONDS(Suit.DIAMONDS, Rank.SEVEN),
    EIGHT_OF_DIAMONDS(Suit.DIAMONDS, Rank.EIGHT),
    NINE_OF_DIAMONDS(Suit.DIAMONDS, Rank.NINE),
    TEN_OF_DIAMONDS(Suit.DIAMONDS, Rank.TEN),
    JACK_OF_DIAMONDS(Suit.DIAMONDS, Rank.JACK),
    QUEEN_OF_DIAMONDS(Suit.DIAMONDS, Rank.QUEEN),
    KING_OF_DIAMONDS(Suit.DIAMONDS, Rank.KING),
    ACE_OF_DIAMONDS(Suit.DIAMONDS, Rank.ACE),

    // Hearts
    TWO_OF_HEARTS(Suit.HEARTS, Rank.TWO),
    THREE_OF_HEARTS(Suit.HEARTS, Rank.THREE),
    FOUR_OF_HEARTS(Suit.HEARTS, Rank.FOUR),
    FIVE_OF_HEARTS(Suit.HEARTS, Rank.FIVE),
    SIX_OF_HEARTS(Suit.HEARTS, Rank.SIX),
    SEVEN_OF_HEARTS(Suit.HEARTS, Rank.SEVEN),
    EIGHT_OF_HEARTS(Suit.HEARTS, Rank.EIGHT),
    NINE_OF_HEARTS(Suit.HEARTS, Rank.NINE),
    TEN_OF_HEARTS(Suit.HEARTS, Rank.TEN),
    JACK_OF_HEARTS(Suit.HEARTS, Rank.JACK),
    QUEEN_OF_HEARTS(Suit.HEARTS, Rank.QUEEN),
    KING_OF_HEARTS(Suit.HEARTS, Rank.KING),
    ACE_OF_HEARTS(Suit.HEARTS, Rank.ACE),

    // Spades
    TWO_OF_SPADES(Suit.SPADES, Rank.TWO),
    THREE_OF_SPADES(Suit.SPADES, Rank.THREE),
    FOUR_OF_SPADES(Suit.SPADES, Rank.FOUR),
    FIVE_OF_SPADES(Suit.SPADES, Rank.FIVE),
    SIX_OF_SPADES(Suit.SPADES, Rank.SIX),
    SEVEN_OF_SPADES(Suit.SPADES, Rank.SEVEN),
    EIGHT_OF_SPADES(Suit.SPADES, Rank.EIGHT),
    NINE_OF_SPADES(Suit.SPADES, Rank.NINE),
    TEN_OF_SPADES(Suit.SPADES, Rank.TEN),
    JACK_OF_SPADES(Suit.SPADES, Rank.JACK),
    QUEEN_OF_SPADES(Suit.SPADES, Rank.QUEEN),
    KING_OF_SPADES(Suit.SPADES, Rank.KING),
    ACE_OF_SPADES(Suit.SPADES, Rank.ACE);

    private final Suit suit;
    private final Rank rank;

    Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
}

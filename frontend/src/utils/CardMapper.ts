const cardValueMapping: { [key: string]: string } = {
    ace: "1",
    two: "2",
    three: "3",
    four: "4",
    five: "5",
    six: "6",
    seven: "7",
    eight: "8",
    nine: "9",
    ten: "10",
    jack: "11",
    queen: "12",
    king: "13",
};

export function formatCardUrl(card: string): string {
    // Splits de kaartnaam op "OF"
    const [rank, suit] = card.toLowerCase().split("_of_");

    // Haal de korte naam op uit de mapping
    const formattedRank = cardValueMapping[rank] || rank; // Fallback naar originele rank indien niet gemapt
    const formattedSuit = suit; // bv. "spades", "hearts", etc.


    return `https://storage.googleapis.com/image_bucket_ip2/playingcards-${formattedSuit}/${formattedRank}.png`;
}
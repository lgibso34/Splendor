package splendor.common.cards;

import java.util.ArrayList;

public class CardRow {

    private ArrayList<Card> cards = new ArrayList<>(4); // 4 cards per row
    private final int row;
    private final Deck deck;

    public CardRow(int row, Deck deck, int dealt) {
        this.row = row;
        this.deck = deck;
        // deal cards and place in corresponding spot in CardRow
        for (int i = 0; i < dealt; i++) {
            cards.add(i, deck.dealCard());
            cards.get(i).setFaceUp(true);
        }
    }

    // UNTESTED
    public Card removeAndReplace(int index) {
        if (deck.isEmpty()){
            return cards.remove(index);
        } else {
            Card poppedCard = cards.get(index);
            cards.set(index, deck.dealCard().setFaceUp(true)); // replace the picked up card with the card that was dealt without losing ordering of cards
            return poppedCard;
        }
    }

    public Card peekCard(int index) {
        return cards.get(index);
    }

    // used for Nobles
    public Card remove(int index) {
        return cards.remove(index);
    }

    // used for Nobles
    public ArrayList<Card> getCards() {
        return cards;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        if (row != 0)
            output.append("Level ").append(row).append(": ");
        else
            output.append("Nobles: ");
        for (Card card : cards) {
            output.append(card.toString()).append("     ");
        }
        if (row != 0)
            output.append("     Cards in deck: ").append(deck.numCards());
        output.append("\n");
        return output.toString();
    }
}
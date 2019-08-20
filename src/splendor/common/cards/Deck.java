package splendor.common.cards;

import splendor.common.util.PCG;

import java.util.ArrayList;

/**
 * Deck
 */
public class Deck {

    private ArrayList<Card> cards;
    private static final PCG rand = new PCG();

    // wild card generic allows for Card or Noble objects
    public Deck(ArrayList<? extends Card> cards) {
        this.cards = (ArrayList<Card>) cards; // must be casted for this to work
        shuffle();
    }

    //Fisher-Yates
    private void shuffle() {
        Card temp;
        int numCards = cards.size();

        for (int i = 0; i < numCards - 2; i++) {
            //j is a random integer such that i ≤ j < n, for our purposes this is always a positive int
            int j = (int) rand.nextInt(numCards);
            while (j < i)
                j = (int) rand.nextInt(numCards);
            temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    public Card dealCard() {
        return cards.remove(0);
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        int counter = 1;
        for (Card card : cards) {
            output.append("(").append(counter++).append(") ").append(card.toString()).append("\n");
        }

        return output.toString();
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }
}
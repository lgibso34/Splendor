package splendor.cards;

import java.util.ArrayList;
import java.util.Random;

/**
 * Deck
 */
public class Deck {

	ArrayList<Card> cards = new ArrayList<Card>();

	public Deck(ArrayList<Card> cards) {
		this.cards = cards;
	}

	private void shuffle(Integer amountOfCards) {
		Random numberGenerator = new Random();

		for (int i = 0; i < 1001; i++) {
			int rand1 = numberGenerator.nextInt(amountOfCards); // create a random number between 0-amountOfCards (for
																// index).
			int rand2 = numberGenerator.nextInt(amountOfCards);
			Card copy = cards.get(rand1); // store the card of the first random spot
			cards.set(rand1, cards.get(rand2)); // set the second card to the first cards spot
			cards.set(rand2, copy); // set the first card to the second cards spot
		}
	}

	public Card dealCard() {
		return cards.remove(cards.size() - 1);
	}

	public String toString() {
		String output = "";
		int counter = 1;
		for (Card card: cards){
			output += "Card " + counter++ + " " + card.toString() + "\n";
		}

		return output;
	}
}
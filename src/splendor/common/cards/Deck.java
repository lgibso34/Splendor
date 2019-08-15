package splendor.common.cards;

import splendor.common.util.PCG;

import java.util.ArrayList;

/**
 * Deck
 */
public class Deck {

	ArrayList<Card> cards;
	private static PCG rand = new PCG();

	public Deck(ArrayList<Card> cards) {
		this.cards = cards;
		shuffle();
	}

	//Fisher-Yates
	private void shuffle() {
		Card temp;
		int numCards = cards.size();

		for(int i = 0; i < numCards - 2; i++){
			//j is a random integer such that i ≤ j < n
			int j = rand.nextInt(numCards);
			while(j < i)
				j = rand.nextInt(numCards);
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
		for (Card card: cards){
			output.append("(").append(counter++).append(") ").append(card.toString()).append("\n");
		}

		return output.toString();
	}
}
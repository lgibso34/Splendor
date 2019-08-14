package splendor.common.cards;

import java.util.ArrayList;

/**
 * Deck
 */
public class Deck {

	ArrayList<Card> cards = new ArrayList<Card>();
	private static Pcg32 rand = new Pcg32();

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
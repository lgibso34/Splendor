package splendor.common.cards;

import splendor.common.util.PCG;

import java.util.ArrayList;

/**
 * Deck
 */
public class Deck {

	ArrayList<Card> cards = new ArrayList<>();
	private static PCG rand = new PCG();

	// wild card generic allows for Card or Noble objects
	public Deck(ArrayList<? extends Card> cards) {
		this.cards = (ArrayList<Card>) cards; // must be casted for this to work
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
		String output = "";
		int counter = 1;
		for (Card card: cards){
			output += "(" + counter++ + ") " + card.toString() + "\n";
		}

		return output;
	}
}
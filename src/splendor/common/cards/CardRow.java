package splendor.common.cards;

import java.util.ArrayList;

public class CardRow {

	ArrayList<Card> cards = new ArrayList<>(4); // 4 cards per row
	private int row;
	private Deck deck;

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
	//TODO add checking for end of deck, etc
	public Card removeAndReplace(int index, Card dealtCard) {
		Card poppedCard = cards.remove(index);
		if (dealtCard != null)
			cards.set(index, dealtCard); // replace the picked up card with the card that was dealt without losing ordering of cards
		return poppedCard;
	}

	public Card remove(int index, Card dealtCard) {
		return cards.remove(index);
	}

	public String toString(){
		StringBuilder output = new StringBuilder("Row #" + row + " \n");
		for(Card card : cards){
			output.append(card.toString()).append("\n");
		}
		return output.toString();
	}
}
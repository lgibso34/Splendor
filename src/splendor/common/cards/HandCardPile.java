package splendor.common.cards;

import java.util.ArrayList;

public class HandCardPile {

	int cardCount = 0;
	ArrayList<Card> cards = new ArrayList<Card>();

	
	public HandCardPile() {}
	
	public void add(Card card) {
		cards.add(card);
	}
	
	// used for reserved cards, remove and return the desired card
	public Card remove(Card card) {
		return cards.remove(cards.indexOf(card));
	}
}
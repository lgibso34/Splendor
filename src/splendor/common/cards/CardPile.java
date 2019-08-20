package splendor.common.cards;

import splendor.common.util.Constants.Color;

import java.util.ArrayList;

class CardPile {

	private ArrayList<Card> cards = new ArrayList<>();
	private final Color color;

	public CardPile(Color color) {
		this.color = color;
	}
	
	public void add(Card card) {
		cards.add(card);
	}

	public int getSize(){
	    return cards.size();
    }

	public Card peekCard(int index){
		return cards.get(index);
	}

	public Card buyReservedCard(int index){
		return cards.remove(index);
	}

	public String toString(){
		if(cards == null)
			return null;
		if(this.color == Color.Gold){
			return "Reserved cards: " + cards.size() + " |";
		}else {
			return cards.size() + "x Permanent " + color + " |";
		}
	}
}
package splendor.common.cards;

import splendor.common.util.Constants.Colors;

import java.util.ArrayList;

public class CardPile {

	private ArrayList<Card> cards = new ArrayList<Card>();
	private Colors color;

	public CardPile(Colors color) {
		this.color = color;
	}
	
	public void add(Card card) {
		cards.add(card);
	}
	
	// used for reserved cards, remove and return the desired card
	public Card remove(Card card) {
		return cards.remove(cards.indexOf(card));
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

	public String showCards(){
		String output = "";
		for(Card c : cards){
			output += c.toString() + "\n";
		}
		return output;
	}

	public String toString(){
		if(this.color == Colors.Gold){
			return "Reserved cards: " + cards.size() + " |";
		}else {
			return cards.size() + "x Permanent " + color + " |";
		}
	}
}
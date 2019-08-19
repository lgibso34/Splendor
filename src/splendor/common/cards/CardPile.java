package splendor.common.cards;

import splendor.common.util.Constants.Colors;

import java.util.ArrayList;

public class CardPile {

	private ArrayList<Card> cards;
	private final Colors color;

	public CardPile(Colors color) {
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

	public String showCards(){
		StringBuilder output = new StringBuilder();
		for(Card c : cards){
			output.append(c.toString()).append("\n");
		}
		return output.toString();
	}

	public String toString(){
		if(this.color == Colors.Gold){
			return "Reserved cards: " + cards.size() + " |";
		}else {
			return cards.size() + "x Permanent " + color + " |";
		}
	}
}
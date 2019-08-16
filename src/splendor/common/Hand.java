package splendor.common;

import splendor.common.cards.Card;
import splendor.common.cards.CardPile;
import splendor.common.coins.CoinPile;
import splendor.common.util.Constants;
import splendor.common.util.Constants.Colors;

public class Hand{

	private CoinPile[] coinPiles = new CoinPile[6]; // 5 colors + gold: [ white, blue, green, red, black, gold ]
	private CardPile[] cardPiles = new CardPile[6]; //5 colors + reserved: [ white, blue, green, red, black, reserveCards ]
	private int points = 0;
	
	public Hand() {
		//int i = 0;
		for(int i=0; i<Constants.colors.length; i++){
			coinPiles[i] = new CoinPile(Constants.colors[i]);
			cardPiles[i] = new CardPile(Constants.colors[i]);
		}
	}

	public boolean checkBalance(Card card){
		int [] cardCost = card.getColorCost();
		for(int i=0; i<coinPiles.length; i++){
			if(cardCost[i] > coinPiles[i].getSize()){
				return false;
			}
		}
		return true;
	}

	// [ white, blue, green, red, black, gold ]
	public void addCoin(int color){
		coinPiles[color].add();
	}

	public boolean removeCoin(int color){
		return coinPiles[color].remove();
	}

	public void addCard(int color, Card card){
		cardPiles[color].add(card);
	}

	public Card removeCard(int color, Card card){
		return cardPiles[color].remove(card);
	}

	public void reserveCard(Card card){
		cardPiles[5].add(card);
	}

	public int getPoints(){ return points; }

	public String toString(){
		String output = "Points: " + points + "\n";
		for(int i=0; i<cardPiles.length; i++){
			output += cardPiles[i].toString() + " " + coinPiles[i].toString() + "\n";
		}
		return output;
	}
}

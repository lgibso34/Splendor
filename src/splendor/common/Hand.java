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
		for(int i=0; i<cardCost.length; i++){
			if(cardCost[i] > coinPiles[i].getSize()){
				return false;
			}
		}
		return true;
	}

	public boolean checkReservedCardQuantitiy(){
		return cardPiles[5].getSize() <3;
	}

	// [ white, blue, green, red, black, gold ]
	public void addCoin(int color){
		coinPiles[color].add();
	}

	public int removeCoin(int color){
		return coinPiles[color].remove();
	}

	public void addCard(int color, Card card){
		if(card.getFaceUp()){
			cardPiles[color].add(card);
		}else{
			cardPiles[5].add(card);
		}
	}

	// used for ties
	public int totalPermanentCards(){
		int count = 0;
		for(CardPile c : cardPiles){
			count += c.getSize();
		}
		return count;
	}

	public void showReservedCards(){
		System.out.println(cardPiles[5].showCards());
	}

	public Card peekCard(int index){
		return cardPiles[5].peekCard(index);
	}

	public int[] buyReservedcard (int index) {
		// use removeCoin() to buy this reserved card
		int[] cardCost = cardPiles[5].buyReservedCard(index).getColorCost();
		for(int i=0; i<cardCost.length; i++){
			if(cardCost[i] > 0){
				for (int j=0; j<cardCost[i]; j++){
					removeCoin(i);
				}
			}
		}

		return cardCost;
	}

	public void reserveCard(Card card){
		cardPiles[5].add(card);
	}

	public int getPoints(){ return points; }

	public String toString(){
		String output = "Points: " + points + "\n";
		for(int i=0; i<cardPiles.length; i++) {
			output += cardPiles[i].toString() + " " + coinPiles[i].toString() + "\n";
		}
		return output;
	}
}

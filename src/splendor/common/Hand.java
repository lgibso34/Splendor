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
		for (int i = 0; i < Constants.colors.length; i++) {
			coinPiles[i] = new CoinPile(Constants.colors[i]);
			cardPiles[i] = new CardPile(Constants.colors[i]);
		}
	}

	// prompt usage of a gold coin if one will be used so that the user knows for sure they will lose one or many
	public int checkBalance(Card card){
		boolean playerCanBuy = true;
		boolean playerMustUseGoldCoin = false;

		int [] cardCost = card.getColorCost();
		for(int i=0; i<cardCost.length; i++){
			if(cardCost[i] > coinPiles[i].getSize()){
				if(coinPiles[5].getSize() > 0 && cardCost[i] >= (coinPiles[i].getSize() + coinPiles[5].getSize())) {
					playerMustUseGoldCoin = true;
				}else{
					playerCanBuy = false;
				}
			}
		}
		// 2: must use gold coin(s) 1: player can buy 0: player can't buy
		return playerCanBuy ? playerMustUseGoldCoin ? 2 : 1 : 0;
	}

	public boolean checkReservedCardQuantity(){
		return cardPiles[5].getSize() <3;
	}

	// [ white, blue, green, red, black, gold ]
	public void addCoin(int color){
		coinPiles[color].add();
	}

	// TODO
	// works but does not take into consideration of gold wildcard coins
	public void removeCoinsFromInventory(int[] cardCost){
		for(int i=0; i<cardCost.length; i++){
			if(cardCost[i] > 0){
				for (int j=0; j<cardCost[i]; j++){
					coinPiles[i].remove();
				}
			}
		}
	}

	public void addCard(Card card){
		if(card.getFaceUp()){
				int[] cardCost = card.getColorCost();
			removeCoinsFromInventory(cardCost);
				cardPiles[card.getColorIndex()].add(card);
		}else{
			cardPiles[5].add(card); // add to faceDown reserve pile
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

	public int[] buyReservedCard (int index) {
		// use removeCoin() to buy this reserved card
		Card reservedCardBought = cardPiles[5].buyReservedCard(index);
		reservedCardBought.setFaceUp(true);
		int[] cardCost = reservedCardBought.getColorCost();
		removeCoinsFromInventory(cardCost);
		addCard(reservedCardBought);
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

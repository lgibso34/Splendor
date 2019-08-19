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

	public int[] getCost(Card card){
		int [] cardCost = card.getColorCost(); // get the color cost for the wanted card
		int[] cardCostWithPermanentsAndGold = new int[cardCost.length+1];
		for(int i=0; i < cardCost.length; i++){
			if(cardPiles[i].getSize() > 0){ // if permanent cards exist for that color...
				int cost = cardCost[i] - cardPiles[i].getSize();
				if(cost < 0){ // if the cost becomes negative there are more permanent cards than the cost so it costs zero of that color
					cardCostWithPermanentsAndGold[i] = 0;
				}else{
					cardCostWithPermanentsAndGold[i] = cost; // otherwise, the extra coins required are assigned to that color spot
				}
			}else{
				cardCostWithPermanentsAndGold[i] = cardCost[i]; // cost stays the same
			}
		}

		int goldCoinsNeeded = 0;
		int goldCoinCount = coinPiles[5].getSize();
		boolean playerHasAtLeastOneGoldCoin = goldCoinCount > 0;

		for(int i=0; i<cardCostWithPermanentsAndGold.length; i++){ // iterate through each color
			if(cardCostWithPermanentsAndGold[i] > coinPiles[i].getSize()){ // and check if the card costs more than what the player has
				// if it does, see if the user has any gold coins that can be used to still buy the card
				if(playerHasAtLeastOneGoldCoin && cardCostWithPermanentsAndGold[i] <= (coinPiles[i].getSize() + goldCoinCount)) {
					int difference = cardCostWithPermanentsAndGold[i] - coinPiles[i].getSize(); // how many gold coins are needed for this color
					goldCoinsNeeded += difference; // add to total gold coins needed for this card
					cardCostWithPermanentsAndGold[i] = cardCostWithPermanentsAndGold[i] - difference; // subtract gold coin usage from color cost
				}
			}
		}

		cardCostWithPermanentsAndGold[5] = goldCoinsNeeded; // amount of gold coins needed for this card

		return cardCostWithPermanentsAndGold;
	}

	public int checkBalance(Card card){
		boolean playerCanBuy = true;
		boolean goldRequired = false;
		int goldCoinsNeeded = 0;
		int goldCoinCount = coinPiles[5].getSize();

		int[] cardCostWithPermanents = getCost(card);

		if(cardCostWithPermanents[5] > 0){
			goldRequired = true;
			goldCoinsNeeded = cardCostWithPermanents[5];
		}

		for(int i=0; i<cardCostWithPermanents.length; i++){ // iterate through each color
			if(cardCostWithPermanents[i] > coinPiles[i].getSize()){ // and check if the card costs more than what the player has
					playerCanBuy = false;
			}
		}

		if(goldCoinsNeeded > goldCoinCount){
			goldRequired = false;
		}
		// -1: player can't buy, 0: player can buy, 1 or greater: number of gold coins that it will cost the player
		return playerCanBuy ? goldRequired ? goldCoinsNeeded : 0 : -1;
	}

	public boolean checkReservedCardQuantity(){
		return cardPiles[5].getSize() < 3;
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

	public void addCard(Card card, int[] cardCost){
		if(card.getFaceUp()){
			int pointValue = card.getValue();
			removeCoinsFromInventory(cardCost);
			cardPiles[card.getColorIndex()].add(card);
			if(pointValue > 0){
				points += pointValue;
			}
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

	// cardCost must be passed in because gold coins are not in the colorCost property
	// of the Card, the parameter passed in may contain gold coins that need to be
	// subtracted from the user's hand
	public void buyReservedCard (int index, int[] cardCost) {
		Card reservedCardBought = cardPiles[5].buyReservedCard(index);
		reservedCardBought.setFaceUp(true);
		addCard(reservedCardBought, cardCost);
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

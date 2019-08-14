package splendor.common;

import java.util.ArrayList;

import splendor.common.coins.HandCoinPile;
import splendor.common.cards.HandCardPile;

public class Hand{
	// [ white, blue, green, red, black, gold ]
	ArrayList<HandCoinPile> handCoinPiles = new ArrayList<HandCoinPile>(6); // six coin piles
	// [ reserveCards, white, blue, green, red, black ]
	ArrayList<HandCardPile> cardPiles = new ArrayList<HandCardPile>(6); 
	int points = 0;
	
	public Hand() {
		HandCoinPile emptyHandCoinPile = new HandCoinPile();
		HandCardPile emptyCardPile = new HandCardPile();
		for (int i=0; i<cardPiles.size(); i++) {
			handCoinPiles.set(i, emptyHandCoinPile);
			cardPiles.set(i, emptyCardPile);
		}		
	}	
}

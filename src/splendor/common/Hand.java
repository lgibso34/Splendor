package splendor.common;

import java.util.ArrayList;

import splendor.common.coins.HandCoinPile;
import splendor.common.cards.HandCardPile;

public class Hand{
	// [ white, blue, green, red, black, gold ]
	private ArrayList<HandCoinPile> handCoinPiles = new ArrayList<>(6); // 5 colors + gold
	// [ white, blue, green, red, black, reserveCards ]
	private ArrayList<HandCardPile> cardPiles = new ArrayList<>(6); //5 colors + reserved
	int points = 0;
	
	public Hand() {

	}	
}

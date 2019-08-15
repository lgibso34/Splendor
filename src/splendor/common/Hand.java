package splendor.common;

import splendor.common.cards.CardPile;
import splendor.common.coins.CoinPile;
import splendor.common.util.Constants;
import splendor.common.util.Constants.Colors;

public class Hand{

	private CoinPile[] coinPiles = new CoinPile[6]; // 5 colors + gold: [ white, blue, green, red, black, gold ]
	private CardPile[] cardPiles = new CardPile[6]; //5 colors + reserved: [ white, blue, green, red, black, reserveCards ]
	private int points = 0;
	
	public Hand() {
		int i = 0;
		for(Colors color : Constants.colors) {
			coinPiles[i] = new CoinPile(color);
			cardPiles[i++] = new CardPile(color);
		}
	}

	public int getPoints(){ return points; }
}

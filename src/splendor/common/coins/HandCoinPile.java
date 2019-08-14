package splendor.common.coins;

import java.util.ArrayList;

public class HandCoinPile {
	
	ArrayList<Coin> coins = new ArrayList<Coin>();

	
	public HandCoinPile() {}
	
	public void add(Coin incoming) {
		coins.add(incoming);
	}
	
	public Coin remove(Coin outgoing) {
		return coins.remove(coins.size() - 1); // last index coin
	}
}

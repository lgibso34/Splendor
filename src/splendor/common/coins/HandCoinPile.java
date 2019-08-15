package splendor.common.coins;

import java.util.ArrayList;

public class HandCoinPile {
	
	private ArrayList<Coin> coins = new ArrayList<>();

	
	public HandCoinPile() {}
	
	public void add(Coin incoming) {
		coins.add(incoming);
	}
	
	public Coin remove(Coin outgoing) {
		return coins.remove(coins.size() - 1); // last index coin
	}
}

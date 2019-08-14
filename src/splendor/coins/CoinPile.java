package splendor.coins;

import java.util.ArrayList;

public class CoinPile {

	ArrayList<Coin> coins = new ArrayList<Coin>();

	private static final int MAX_GEMS_4PLAYER = 4;
	private static final int MAX_GEMS_5PLAYER = 5;
	private static final int MAX_GEMS_DEFAULT = 7;
	
	
	public CoinPile(Integer numberOfPlayers, String color) {		
		// gold pile is always five no mater the amount of players
		if (color.equals("gold")) {
			Coin goldCoin = new Coin("gold");
			for (var i = 0; i < 5; i++) {
				coins.add(goldCoin);
			}
		} else {
			switch (numberOfPlayers) {
			case 2:
				initializePile(MAX_GEMS_4PLAYER, color);
				break;
			case 3:
				initializePile(MAX_GEMS_5PLAYER, color);
				break;
			default:
				initializePile(MAX_GEMS_DEFAULT, color);
				break;
			}
		}
	}

	private void initializePile(int numberOfPlayers, String color){
		for (var i = 0; i < numberOfPlayers; i++) {
			coins.add(new Coin(color));
		}
	}
	
	public void add(Coin incoming) {
		coins.add(incoming);
	}
	
	public Coin remove(Coin outgoing) {
		return coins.remove(coins.size() - 1); // last index coin
	}

	public String toString() {
		String test = "";
		for(Coin coin : coins) {
			test += coin.value + " ";
		}
		return test;
	}
	
	public static void main(String[] args) {
		int numOfPlayers = 2;

		CoinPile goldPile = new CoinPile(numOfPlayers, "gold");
		CoinPile whitePile = new CoinPile(numOfPlayers, "white");
		CoinPile bluePile = new CoinPile(numOfPlayers, "blue");
		CoinPile greenPile = new CoinPile(numOfPlayers, "green");
		CoinPile redPile = new CoinPile(numOfPlayers, "red");
		CoinPile blackPile = new CoinPile(numOfPlayers, "black");

		CoinPile[] allCoins = { goldPile, whitePile, bluePile, greenPile, redPile, blackPile };

		for(CoinPile pile : allCoins) {
			System.out.println(pile.toString());
		}
	}
}
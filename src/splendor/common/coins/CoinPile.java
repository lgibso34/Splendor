package splendor.common.coins;

import splendor.common.util.Constants.Colors;

import java.util.ArrayList;

public class CoinPile {

	ArrayList<Coin> coins = new ArrayList<Coin>();

	private static final int MAX_GEMS_2PLAYER = 4;
	private static final int MAX_GEMS_3PLAYER = 5;
	private static final int MAX_GEMS_DEFAULT = 7;
	
	
	public CoinPile(Integer numberOfPlayers, Colors color) {
		// gold pile is always five no mater the amount of players
		if (color.equals(Colors.Gold)) {
			Coin goldCoin = new Coin(Colors.Gold);
			for (var i = 0; i < 5; i++) {
				coins.add(goldCoin);
			}
		} else {
			switch (numberOfPlayers) {
			case 2:
				initializePile(MAX_GEMS_2PLAYER, color);
				break;
			case 3:
				initializePile(MAX_GEMS_3PLAYER, color);
				break;
			default:
				initializePile(MAX_GEMS_DEFAULT, color);
				break;
			}
		}
	}

	private void initializePile(int numCoins, Colors color){
		for (var i = 0; i < numCoins; i++) {
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
			test += coin.color.toString() + " ";
		}
		return test;
	}
	
	public static void main(String[] args) {
		int numOfPlayers = 2;

		CoinPile goldPile = new CoinPile(numOfPlayers, Colors.Gold);
		CoinPile whitePile = new CoinPile(numOfPlayers, Colors.White);
		CoinPile bluePile = new CoinPile(numOfPlayers, Colors.Blue);
		CoinPile greenPile = new CoinPile(numOfPlayers, Colors.Green);
		CoinPile redPile = new CoinPile(numOfPlayers, Colors.Red);
		CoinPile blackPile = new CoinPile(numOfPlayers, Colors.Black);

		CoinPile[] allCoins = { goldPile, whitePile, bluePile, greenPile, redPile, blackPile };

		for(CoinPile pile : allCoins) {
			System.out.println(pile.toString());
		}
	}
}
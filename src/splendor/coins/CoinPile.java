package splendor.coins;

import java.util.ArrayList;

public class CoinPile {

	public Coin[] coins;
	public ArrayList<Coin> coins2 = new ArrayList<Coin>();

	private Integer FOUR = 4;
	private Integer FIVE = 5;
	private Integer SEVEN = 7;
	
	
	public CoinPile(int numberOfPlayers, String color) {

		// gold pile is always five no mater the amount of players
		if (color.equals("gold")) {
			Coin goldCoin = new Coin("gold");
//			this.coins = new Coin[FIVE];
			for (var i = 0; i < FIVE; i++) {
				this.coins2.add(goldCoin);
			}
			
		} else {

			switch (numberOfPlayers) {
			case 2:
				for (var i = 0; i < FOUR; i++) {
					this.coins2.add(new Coin(color));
				}
				break;
			case 3:
				this.coins = new Coin[FIVE];
				break;
			case 4:
				this.coins = new Coin[SEVEN];
				break;
			case 5:
				this.coins = new Coin[SEVEN];
				break;
			}
		}
	}

//	public CoinPile(int numberOfPlayers, String color) {
//
//		// gold pile is always five no mater the amount of players
//		if (color.equals("gold")) {
//			Coin goldCoin = new Coin("gold");
//			this.coins = new Coin[FIVE];
//			for (var i = 0; i < this.coins.length; i++) {
//				this.coins[i] = goldCoin;
//			}
//			
//		} else {
//
//			switch (numberOfPlayers) {
//			case 2:
//				this.coins = new Coin[FOUR];
//				for (var i = 0; i < this.coins.length; i++) {
//					this.coins[i] = new Coin(color);
//				}
//				break;
//			case 3:
//				this.coins = new Coin[FIVE];
//				break;
//			case 4:
//				this.coins = new Coin[SEVEN];
//				break;
//			case 5:
//				this.coins = new Coin[SEVEN];
//				break;
//			}
//		}
//	}

	public String toString() {
		String test = "";
		for (var i = 0; i < this.coins2.size(); i++) {
			test += " " + this.coins[i].value;
		}

		return test;
	}

	public String toStringArrList() {

		
		String test = "";
		for(Coin coin : this.coins2) {
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
//		for (var i = 0; i < 6; i++) {
//			System.out.println(allCoins[i].toString());
//		}
		for(CoinPile pile : allCoins) {
			System.out.println(pile.toStringArrList());
		}
	}
}
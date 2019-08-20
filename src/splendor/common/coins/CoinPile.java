package splendor.common.coins;

import splendor.common.util.Constants;
import splendor.common.util.Constants.Color;

class CoinPile {

	private final Color color;
	private int size;
	
	public CoinPile(int numPlayers, Color color) {
		this.color = color;
		if (color.equals(Color.Gold)) {
			size = Constants.MAX_GOLD_COINS;
		} else {
			switch (numPlayers) {
			case 2:
				size = Constants.MAX_GEMS_2PLAYER;
				break;
			case 3:
				size = Constants.MAX_GEMS_3PLAYER;
				break;
			default:
				size = Constants.MAX_GEMS_DEFAULT;
				break;
			}
		}
	}

	public CoinPile(Color color) {
		this.color = color;
		size = 0;
	}

	public void add(int amount) {
		if(amount > 0)
			size += amount;
	}

	public int getSize(){
		return size;
	}

	public boolean removeCoins(int amount) {
		if(amount < 0)
			return true;
		if (size >= amount) {
			size -= amount;
			return true;
		}
		return false;
	}

	public boolean canTake(int amount){
		if (amount == 0)
			return true;
		if (amount > 2)
			return false;
		if(amount == 2)
			return size >= 4;
		return size >= amount;
	}

	public String toString() {
		return size + "x " + color.toString() + "(" + color.getShortName() + ")";
	}

}
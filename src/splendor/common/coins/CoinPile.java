package splendor.common.coins;

import splendor.common.util.Constants;
import splendor.common.util.Constants.Colors;

public class CoinPile {

	private Colors color;
	private int size;
	
	public CoinPile(int numPlayers, Colors color) {
		this.color = color;
		if (color.equals(Colors.Gold)) {
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

	public CoinPile(Colors color) {
		this.color = color;
		size = 0;
	}
	
	public void add() {
		size++;
	}

	public int getSize(){
		return size;
	}
	
	public boolean remove() {
		if (size > 0) {
			size--;
			return true;
		}
		return false;
	}

	public String toString() {
		return size + "x " + color.toString();
	}

}
package splendor.cards;

/**
 * Card
 */
public class Card {

	private int white, blue, green, red, black, value = 0;
	private Boolean faceUp = false;

	public Card(int[] colors, int value) {
		white = colors[0];
		blue = colors[1];
		green = colors[2];
		red = colors[3];
		black = colors[4];
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public Boolean getFaceUp() {
		return faceUp;
	}

	public void setFaceUp(Boolean newBool) {
		faceUp = newBool;
	}
	
	public int getColorValue(String color) {
		switch(color) {
		case "white":
			return white;
		case "blue":
			return blue;
		case "green":
			return green;
		case "red":
			return red;
		case "black":
			return black;
		default:
			return -1;
		}
	}
}

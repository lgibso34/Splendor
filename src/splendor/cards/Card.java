package splendor.cards;

/**
 * Card
 */
public class Card {

	private int color, pointValue;
	private int[] colorCost;
	private Boolean faceUp = false;

	public Card(int[] colorCost, int pointValue, int color) {
		this.colorCost = colorCost;
		this.pointValue = pointValue;
		this.color = color;
	}

	public int getValue() {
		return pointValue;
	}

	public int getColor() {
		return color;
	}

	public Boolean getFaceUp() {
		return faceUp;
	}

	public void setFaceUp(Boolean b) {
		faceUp = b;
	}

	//public enum Colors
	//{
	//	WHITE, BLUE, GREEN, RED, BLACK, GOLD;
	//}
	public int getColorValue(int color) {
		return colorCost[color];
	}

	public String toString(){
		return "Card: " + white + " " + blue + " " + green + " " + red + " " + black + " | Value: " + value + " | FaceUp: " + faceUp;
	}
}

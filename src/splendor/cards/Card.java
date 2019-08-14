package splendor.cards;

/**
 * Card
 */
public class Card {

	private int color, pointValue = 0;
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
	
	public int getColorValue(String color) {
		switch(color) {
			case "white":
				return colorCost[0];
			case "blue":
				return colorCost[1];
			case "green":
				return colorCost[2];
			case "red":
				return colorCost[3];
			case "black":
				return colorCost[4];
			default:
				return -1;
		}
	}

	public String toString(){
		return "Card: " + white + " " + blue + " " + green + " " + red + " " + black + " | Value: " + value + " | FaceUp: " + faceUp;
	}
}

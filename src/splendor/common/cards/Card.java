package splendor.common.cards;

import splendor.common.util.Constants.Colors;

/**
 * Card
 */
public class Card {

	private int[] colorCost;
	private Colors color;
	private int pointValue;
	private Boolean faceUp = false;

	public Card(int[] colorCost, int pointValue, Colors color) {
		this.colorCost = colorCost;
		this.pointValue = pointValue;
		this.color = color;
	}

	public int getValue() {
		return pointValue;
	}

	public Colors getColor() {
		return color;
	}

	public int getColorIndex() {
		return color.ordinal();
	}

	public Boolean getFaceUp() {
		return faceUp;
	}

	public void setFaceUp(Boolean b) {
		faceUp = b;
	}

	public int getColorValue(int color) {
		return colorCost[color];
	}

	public String toString(){
		String output = "Card:: Cost: ";
		int i = 0;
		for(int cost : colorCost){
			if(cost > 0)
				output += color.toString() + ": " + cost + " ";
			i++;
		}
		output += " | Value: " + pointValue + " | FaceUp: " + faceUp;
		return output;
	}
}

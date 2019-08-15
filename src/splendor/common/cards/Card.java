package splendor.common.cards;

import splendor.common.util.Constants.Colors;
import splendor.common.util.Constants;

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
		for(int i = 0; i < colorCost.length; i++){
			int cost = colorCost[i];
			if(cost > 0)
				output += Constants.colors[i].toString() + ": " + cost + ", ";
		}
		output += "[" + pointValue + "] " + (faceUp == true ? "FaceUp" : "FaceDown");
		return output;
	}
}

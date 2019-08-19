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
	private boolean faceUp = false;

	public Card(int[] colorCost, int pointValue, Colors color) {
		this.colorCost = colorCost;
		this.pointValue = pointValue;
		this.color = color;
	}

	public Card(int[] colorCost, int pointValue) {
		this.colorCost = colorCost;
		this.pointValue = pointValue;
		this.color = null;
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

	public void setFaceUp(boolean b) {
		faceUp = b;
	}

	public int getColorValue(int color) {
		return colorCost[color];
	}

	public int[] getColorCost(){
		return this.colorCost;
	}

	public String toString(){
		StringBuilder output = new StringBuilder("Card: (");
		for(int i = 0; i < colorCost.length; i++){
			int cost = colorCost[i];
			if(cost > 0)
				output.append(cost).append(Constants.shortColors[i]).append(" ");
		}
		output = new StringBuilder(output.substring(0, output.length() - 1));
		output.append(") ").append(color).append(" [").append(pointValue).append("] ").append(faceUp ? "FaceUp" : "FaceDown");
		return output.toString();
	}
}

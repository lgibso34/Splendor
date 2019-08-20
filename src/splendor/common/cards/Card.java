package splendor.common.cards;

import splendor.common.util.Constants.Color;
import splendor.common.util.Constants;

/**
 * Card
 */
public class Card {

	private final int[] colorCost;
	private final Color color;
	private final int pointValue;
	private boolean faceUp = false;

	public Card(int[] colorCost, int pointValue, Color color) {
		this.colorCost = colorCost;
		this.pointValue = pointValue;
		this.color = color;
	}

	Card(int[] colorCost, int pointValue) {
		this.colorCost = colorCost;
		this.pointValue = pointValue;
		this.color = null;
	}

	public int getValue() {
		return pointValue;
	}

	public Color getColor() {
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

	public int[] getColorCost(){
		return this.colorCost;
	}

	public String toString(){
		StringBuilder output = new StringBuilder("Card: (");
		for(Color c : Color.colors){
			if(c == Color.Gold)
				continue;
			int cost = colorCost[c.ordinal()];
			if(cost > 0)
				output.append(cost).append(c.getShortName()).append(" ");
		}
		output = new StringBuilder(output.substring(0, output.length() - 1));
		output.append(") ").append(color != null ? color : "Noble").append(" [").append(pointValue).append("] "); //.append(faceUp ? "FaceUp" : "FaceDown")
		return output.toString();
	}
}

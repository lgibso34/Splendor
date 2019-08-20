package splendor.common.cards;

import splendor.common.util.Constants.Color;

/**
 * Card
 */
public class Card {

    private final Colors colorCost;
    private final Color color;
    private final int pointValue;
    private boolean faceUp = false;

    public Card(Colors colorCost, int pointValue, Color color) {
        this.colorCost = colorCost;
        this.pointValue = pointValue;
        this.color = color;
    }

    Card(Colors colorCost, int pointValue) {
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

    public Boolean getFaceUp() {
        return faceUp;
    }

    public Card setFaceUp(boolean b) {
        faceUp = b;
        return this;
    }

    public Colors getColorCost() {
        return this.colorCost;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("Card: (");
        for (Color c : Color.colors) {
            if (c == Color.Gold)
                continue;
            int cost = colorCost.getCost(c);
            if (cost > 0)
                output.append(cost).append(c.getShortName()).append(" ");
        }
        output = new StringBuilder(output.substring(0, output.length() - 1));
        output.append(") ").append(color != null ? color : "Noble").append(" [").append(pointValue).append("] "); //.append(faceUp ? "FaceUp" : "FaceDown")
        return output.toString();
    }
}

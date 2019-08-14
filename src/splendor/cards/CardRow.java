package splendor.cards;

import java.util.ArrayList;

public class CardRow {

	ArrayList<Card> cards = new ArrayList<Card>(4); // 4 cards per row
	Integer row = 0;
	private static Integer rowCount = 0;

	public CardRow(Integer row, Deck deck) {
		if (rowCount < 4) {
			this.row = row;
			// deal cards and place in corresponding spot in CardRow
			for (int i = 0; i < 4; i++) {
				cards.add(i, deck.dealCard());
				cards.get(i).setFaceUp(true);
			}
			rowCount++;
		}
	}
}
package splendor.cards;

import java.util.ArrayList;
import java.util.Random;

/**
 * Deck
 */
public class Deck {

	Integer row = 0; // 1,2,3. 1 being bottom row, 3 being top
	Integer cardsLeft = 0;
	ArrayList<Card> cards = new ArrayList<Card>();
	private static Integer deckCount = 0;
	private Integer TWENTY = 20;
	private Integer THRITY = 30;
	private Integer FORTY = 40;

	// row 3 (top) has 20 (4 dealt immediately)
	// row 2 has 30 "
	// row 1 has 40 "

	public Deck(Integer row) {
		if (deckCount < 4) {
			switch (row) {
			case 1:
				createDeck(row, TWENTY);
				break;
			case 2:
				createDeck(row, THRITY);
				break;
			case 3:
				createDeck(row, FORTY);
				break;
			}
		}
	}

	private void createDeck(Integer row, Integer amountOfCards) {
		cardsLeft = amountOfCards;
		switch (amountOfCards) {
		case 20:
			// grab 20 card deck from constants file
			// then shuffle
			shuffle(amountOfCards);
			break;
		case 30:
			// grab 30 card deck from constants file
			// then shuffle
			shuffle(amountOfCards);
			break;
		case 40:
			// grab 40 card deck from constants file
			// then shuffle
			shuffle(amountOfCards);
			break;
		}
	}

	private void shuffle(Integer amountOfCards) {
		Random numberGenerator = new Random();

		for (int i = 0; i < 1001; i++) {
			int rand1 = numberGenerator.nextInt(amountOfCards); // create a random number between 0-amountOfCards (for
																// index).
			int rand2 = numberGenerator.nextInt(amountOfCards);
			Card copy = cards.get(rand1); // store the card of the first random spot
			cards.set(rand1, cards.get(rand2)); // set the second card to the first cards spot
			cards.set(rand2, copy); // set the first card to the second cards spot
		}
	}

	public Card dealCard() {
		cardsLeft--;
		return cards.remove(cards.size() - 1); // may be able to use cardsleft
	}
}
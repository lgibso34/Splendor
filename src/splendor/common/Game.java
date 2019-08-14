package splendor.common;

import splendor.common.cards.Deck;
import splendor.common.util.DeckBuilder;

public class Game{
    public static void main(String[] args) {

         Deck[] decks = DeckBuilder.buildDecks();

        for(Deck d : decks){
            d.toString();
        }

    }
}

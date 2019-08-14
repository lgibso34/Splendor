package splendor;

import splendor.cards.Deck;
import splendor.util.DeckBuilder;

public class Game{
    public static void main(String[] args) {

         Deck[] decks = DeckBuilder.buildDecks();

        for(Deck d : decks){
            d.toString();
        }

    }
}

package splendor.common;

import splendor.common.cards.CardRow;
import splendor.common.cards.Deck;
import splendor.common.coins.CoinPile;
import splendor.common.util.DeckBuilder;
import splendor.common.util.Constants.Colors;


import java.util.ArrayList;

public class Game{
    public static void main(String[] args) {

        int numberOfPlayers = 5;
        boolean debug = true;

        Deck[] decks = DeckBuilder.buildDecks();
        ArrayList<CoinPile> coinPiles = new ArrayList<CoinPile>();
        ArrayList<CardRow> cardRows = new ArrayList<CardRow>();

        for(int i = 0; i < 6; i++){
            coinPiles.add(i, new CoinPile(numberOfPlayers, Colors.values()[i]));
        }

        for(int i = 0; i < decks.length; i++){
            cardRows.add(i, new CardRow(i, decks[i]));
        }

        if(debug) {
            for (Deck d : decks) {
                System.out.println(d.toString());
            }
            System.out.println();

            // show coin piles
            for (CoinPile pile : coinPiles) {
                System.out.println(pile.toString());
            }
            System.out.println();

            // show card row
            for (CardRow cr : cardRows) {
                System.out.println(cr.toString());
            }
        }
    }
}

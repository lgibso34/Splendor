package splendor.common;

import splendor.common.cards.CardRow;
import splendor.common.cards.Deck;
import splendor.common.coins.CoinPile;
import splendor.common.util.DeckBuilder;
import splendor.common.util.Constants;
import splendor.common.util.Constants.Colors;

public class Game{
    public static void main(String[] args) {

        int i = 0;
        int numPlayers = 5;
        boolean debug = true;

        Deck[] decks = DeckBuilder.buildDecks();
        CoinPile[] coinPiles = new CoinPile[6];
        CardRow[] cardRows = new CardRow[4];

        for(Colors color : Constants.colors){
            coinPiles[i++] = new CoinPile(numPlayers, color);
        }

        i = 0;
        for(Deck d : decks){
            int dealt = 4;
            if (i == 0)
                dealt = numPlayers + 1;
            cardRows[i] = new CardRow(i, decks[i], dealt);
            i++;
        }

        if(debug) {
            System.out.println("Number of Players: " + numPlayers);
            System.out.println();
            i = 0;
            for (Deck d : decks) {
                System.out.println("==========Deck " + i++ + " ==========");
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

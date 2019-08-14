package splendor.util;

import splendor.cards.Card;
import splendor.cards.Deck;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is solely responsible for reading the CSV file with the card information and constructing decks from it
 */
public class DeckBuilder {

    /**
     * this method builds all three decks at once, based off of the CSV file
     * @return an array containing all three levels of deck, where each level's index is the level number - 1
     */
    public static Deck[] getDecks() {
        ArrayList<Card> lv1 = new ArrayList<>(); //level 1
        ArrayList<Card> lv2 = new ArrayList<>(); // level 2
        ArrayList<Card> lv3 = new ArrayList<>(); // level 3

        Scanner reader;
        try {
            reader = new Scanner(new File("lib/CardList.csv"));
            reader.nextLine(); //skip the header
            while(reader.hasNextLine()){
                String cardInfo = reader.nextLine();
                int[] costs = new int[5];
                String[] info = cardInfo.split(",");
                // populate the costs array
                for(int i = 0; i < costs.length; i++){
                    costs[i] = Integer.parseInt(info[i+1]);
                }
                // I wrote the color as a string in the CSV but we're using numbers, so this fixes it
                int color;
                switch(info[6]){
                    case "WHITE":
                        color=1;
                        break;
                    case "BLUE":
                        color=2;
                        break;
                    case "GREEN":
                        color=3;
                        break;
                    case "RED":
                        color=4;
                        break;
                    default:
                        color=5;
                        break;
                }
                Card lnCard = new Card(costs, Integer.parseInt(info[7]), color);
                //figure out which deck to put the card in
                switch(Integer.parseInt(info[0])){
                    case 1:
                        lv1.add(lnCard);
                        break;
                    case 2:
                        lv2.add(lnCard);
                        break;
                    default:
                        lv3.add(lnCard);
                        break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Error reading card list.");
        }

        return new Deck[]{new Deck(lv1), new Deck(lv2), new Deck(lv3)};
    }

    /**
     * VERY inefficient method, but if you just want one of the decks, there you go
     * @param level the level you want. THERE IS NO BOUND CHECKING HERE, DO AT YOUR OWN RISK
     * @return the deck at the specified level
     */
    public static Deck getDeck(int level){
        return getDecks()[level];
    }
}

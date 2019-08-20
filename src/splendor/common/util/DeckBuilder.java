package splendor.common.util;

import splendor.common.cards.Card;
import splendor.common.cards.Deck;
import splendor.common.cards.Noble;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import splendor.common.util.Constants.Color;

/**
 * This class is solely responsible for reading the CSV file with the card information and constructing decks from it
 */
public class DeckBuilder {

    /**
     * this method builds all three decks at once, based off of the CSV file
     * @return an array containing all three levels of deck, where each level's index is the level number - 1
     */
    public static Deck[] buildDecks() {
        ArrayList<Card> lv1 = new ArrayList<>();
        ArrayList<Card> lv2 = new ArrayList<>();
        ArrayList<Card> lv3 = new ArrayList<>();
        ArrayList<Noble> patrons =  new ArrayList<>();

        Scanner reader;
        try {
            reader = new Scanner(new File("src/lib/CardList.csv"));
            reader.useDelimiter(Pattern.compile("[\\s,]+"));
            reader.nextLine(); //skip the header
            while(reader.hasNextLine()){
                int deck = reader.nextInt();
                int[] colorCost = new int[5];
                for(int i = 0; i < colorCost.length; i++){
                    colorCost[i] = reader.nextInt();
                }
                int colorInt = reader.nextInt();
                int pointValue = reader.nextInt();
                if(colorInt == -1) {
                    patrons.add(new Noble(colorCost, pointValue));
                } else {
                    Color color = Color.colors[colorInt];
                    switch (deck) {
                        case 1:
                            lv1.add(new Card(colorCost, pointValue, color));
                            break;
                        case 2:
                            lv2.add(new Card(colorCost, pointValue, color));
                            break;
                        case 3:
                            lv3.add(new Card(colorCost, pointValue, color));
                            break;
                    }
                }
                reader.nextLine();
            }
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Error reading card list.");
        }

        return new Deck[]{new Deck(patrons), new Deck(lv1), new Deck(lv2), new Deck(lv3)};
    }
}

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
    public static Deck[] buildDecks() {
        ArrayList<Card> lv1 = new ArrayList<>();
        ArrayList<Card> lv2 = new ArrayList<>();
        ArrayList<Card> lv3 = new ArrayList<>();
        ArrayList<Card> patrons =  new ArrayList<>();

        Scanner reader;
        try {
            reader = new Scanner(new File("lib/CardList.csv"));
            reader.useDelimiter(",");
            reader.nextLine(); //skip the header
            while(reader.hasNextLine()){
                if(!reader.hasNextInt())
                    break; // I don't know if this is necessary without testing it.
                int deck = reader.nextInt();
                int[] costs = new int[5];
                for(int i = 0; i < costs.length; i++){
                    costs[i] = reader.nextInt();
                }
                int color = reader.nextInt();
                switch(deck){
                    case 1:
                        lv1.add(new Card(costs, reader.nextInt(), color));
                        break;
                    case 2:
                        lv2.add(new Card(costs, reader.nextInt(), color));
                        break;
                    case 3:
                        lv3.add(new Card(costs, reader.nextInt(), color));
                        break;
                    default:
                        patrons.add(new Card(costs, reader.nextInt(), color));
                        break;
                }
                reader.nextLine();
            }
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Error reading card list.");
        }

        return new Deck[]{new Deck(lv1), new Deck(lv2), new Deck(lv3), new Deck(patrons)};
    }
}

package splendor.util;

import splendor.cards.Card;
import splendor.cards.Deck;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DeckBuilder {

    public static Deck[] GetDecks() {
        ArrayList<Card> lv1 = new ArrayList<>();
        ArrayList<Card> lv2 = new ArrayList<>();
        ArrayList<Card> lv3 = new ArrayList<>();

        Scanner reader;
        try {
            reader = new Scanner(new File("lib/CardList.csv"));
            reader.nextLine();
            while(reader.hasNextLine()){
                String cardInfo = reader.nextLine();
                int[] costs = new int[5];

            }
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Error reading card list.");
        }

        return null;
    }
}

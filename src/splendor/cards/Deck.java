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
    private Integer TWENTY = 20;
    private Integer THRITY = 30;
    private Integer FORTY = 40;

    // row 3 (top) has 20 (4 dealt immediately)
    // row 2 has 30 "
    // row 1 has 40 "
    
    public Deck(Integer row){
        

        switch(row){
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

    private void createDeck(Integer row, Integer amountOfCards){
        switch(amountOfCards){
            case 20:
            // grab 20 card deck from constants file
            break;
            case 30:
            // grab 30 card deck from constants file
            break;
            case 40:
            // grab 20 card deck from constants file
            break;
        }
    }

    // public Card dealCard()
    // {   numberLeft--;
    //     Card holder = this.cards[0]; 
    //     for(int i=1; i<52; i++ )
    //     {this.cards[i-1] = this.cards[i];
        
    //     }
    //     this.cards[51] = null;
        
    //     return holder;
    // }

    /*
    public void shuffle()
    {
        //create two random numbers 
        //use those numbers as index's for the Deck array and swap the positions
        //run this in a for loop
        //dont forget to store the value of the first item before swapping or else you lose it
        Random numberGenerator;//declare a reference for the random number
        numberGenerator = new Random(); //make it an object
        
        for(int i=0; i<1001; i++){
        int holder = numberGenerator.nextInt(52); //create a random number between 0-51 (for index).
        int holder2 = numberGenerator.nextInt(52);
        Card copy = cards[holder]; //store the card of the first random spot
        cards[holder] = cards[holder2]; // put the second card in the first card's spot
        cards[holder2] = copy;       //put the first card in the second card's spot
    }
    */
}
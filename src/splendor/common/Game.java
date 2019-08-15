package splendor.common;

import java.util.InputMismatchException;
import java.util.Scanner;

import splendor.common.cards.Card;
import splendor.common.cards.CardRow;
import splendor.common.cards.Deck;
import splendor.common.coins.CoinPile;
import splendor.common.util.DeckBuilder;
import splendor.common.util.Constants;
import splendor.common.util.Constants.Colors;

public class Game{

    private static int index = 0;
    private static int numPlayers = 5;
    private static boolean debug = true;

    private static Deck[] decks = new Deck[4];
    private static CoinPile[] coinPiles = new CoinPile[6];
    private static CardRow[] cardRows = new CardRow[4];

    private static void initializeGame(int numPlayers){

        decks = DeckBuilder.buildDecks();

        for(int i=0; i<Constants.colors.length; i++){
            coinPiles[i] = new CoinPile(numPlayers, Constants.colors[i]);
        }

        for(int i=0; i<decks.length; i++){
            int dealt = 4;
            if(i == 0){
                dealt = numPlayers + 1;
            }
            cardRows[i] = new CardRow(i, decks[i], dealt);
        }
    }

    public static void showDecks(){
        for(int i=0; i<decks.length; i++){
            System.out.println("========== Deck " + i + " ==========");
            System.out.println(decks[i].toString());
        }
    }

    public static void showCoinPiles(){
        // show coin piles
        for (CoinPile pile : coinPiles) {
            System.out.println(pile.toString());
        }
        System.out.println();
    }

    public static void showCardRows(){
        // show card row
        System.out.println(cardRows[0].toString());
        for(int i=3; i>0; i--){
            System.out.println(cardRows[i].toString());
        }
        System.out.println();

    }

    public static void showNumberOfPlayers(){
        System.out.println("Number of Players: " + numPlayers);
        System.out.println();
    }

    public static void main(String[] args) {


        if(debug) {
            Scanner scanner = new Scanner(System.in);

            int exitDo = 0;
            int temp = 0;
            do {
                try {
                    System.out.print("Enter number of players: ");
                    temp = scanner.nextInt();
                    if(temp > 1 && temp < 6){
                        numPlayers = temp;
                        exitDo = 1; // exit do loop
                    }else{
                        System.out.println("Must be between 2-5 players");
                    }
                }catch (InputMismatchException e){
                    System.out.println("Must enter an integer");
                    scanner.next();
                }

            }while (exitDo == 0);



            initializeGame(numPlayers);

            exitDo = 0;
            int choice = 0;
            do {
                try{
                    System.out.println("--------------------------------------------------------------");
                    System.out.println("0: Exit\n" +
                            "1. Show the number of Players\n" +
                            "2. Show Decks\n" +
                            "3. Show Coin Piles\n" +
                            "4. Show Card Rows\n" +
                            "5. Show Hands (not implemented)");
                    System.out.println("--------------------------------------------------------------");

                    temp = scanner.nextInt();
                    System.out.println();
                    if(temp >= 0 && temp < 6){
                        choice = temp;
                        switch (choice){
                            case 0:
                                exitDo = 1;
                                break;
                            case 1:
                                showNumberOfPlayers();
                                break;
                            case 2:
                                showDecks();
                                break;
                            case 3:
                                showCoinPiles();
                                break;
                            case 4:
                                showCardRows();
                                break;
                            case 5:
                                // showHands();
                                exitDo = 1;
                            default:
                                break;
                        }
                    }else{
                        System.out.println("Choose 1-4");
                    }
                }catch (InputMismatchException e){
                    System.out.println("Must enter an integer");
                    scanner.next();
                }
            }while (exitDo == 0);
        }else{
            initializeGame(numPlayers);
            showNumberOfPlayers();
            showDecks();
            showCoinPiles();
            // showHands();
        }
    }

}


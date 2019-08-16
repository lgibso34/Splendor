package splendor.common;

import java.util.InputMismatchException;
import java.util.Scanner;

import splendor.common.cards.Card;
import splendor.common.cards.CardRow;
import splendor.common.cards.Deck;
import splendor.common.coins.CoinPile;
import splendor.common.util.DeckBuilder;
import splendor.common.util.Constants;

public class Game{

    private static int index = 0;
    private static int numPlayers = 5;
    private static boolean debug = true;

    private static Deck[] decks = new Deck[4];
    private static CoinPile[] coinPiles = new CoinPile[6];
    private static CardRow[] cardRows = new CardRow[4];
    private static Hand[] hands = new Hand[0];

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

        hands = new Hand[numPlayers];
        for (int i=0; i<numPlayers; i++){
            hands[i] = new Hand();
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

    public static void showHands(){
        for(int i=0; i<numPlayers; i++){
            System.out.println("Hand " + (i+1) + ": " + hands[i].toString());
        }
    }

    public static int handlePlayChoice(Scanner scanner, int player, int choice, int exitDo){
        int row = 0;
        int cardSpot = 0;
        switch (choice){
            case 0:
                exitDo = 1;
                break;
            case 1:
                showCoinPiles();
                // pick up coins
                break;
            case 2:
                showCardRows();
                // pick up card
                System.out.println("Choose row (1-3): ");
                row = scanner.nextInt(); // not using try catch to verify that it is an integer since this is only debug mode,

                System.out.println("Choose card spot (0-2): ");
                cardSpot = scanner.nextInt();

                // checkBalance(player);
                if(hands[player].checkBalance(cardRows[row].peekCard(cardSpot))){
                    Card pickedUpCard = cardRows[row].removeAndReplace(cardSpot, decks[row].dealCard());
                    hands[player].addCard(pickedUpCard.getColorIndex(), pickedUpCard);
                }else{
                    System.out.println("You do not have the balance for this card.");
                    exitDo = -1;
                }
                break;
            case 3:
                showHands();
                break;
            case 4:
                // reserve card logic
                break;
            case 5:
                // pickup card from hand reserve pile

                break;
            default:
                break;
        }

        return exitDo;

    }

    public static void main(String[] args) {
        boolean play = false;

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
                            "5. Show Hands\n" +
                            "6. Play game");
                    System.out.println("--------------------------------------------------------------");

                    temp = scanner.nextInt();
                    System.out.println();
                    if(temp >= 0 && temp <= 6){
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
                                showHands();
                            case 6:
                                exitDo = 1;
                                play = true;
                                break;
                            default:
                                break;
                        }
                    }else{
                        System.out.println("Choose 0-6");
                    }
                }catch (InputMismatchException e){
                    System.out.println("Must enter an integer");
                    scanner.next();
                }
            }while (exitDo == 0);

            // if the programmer chooses to play ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(play){
                int player = 0;
                exitDo = 0;
                choice = 0;
                do {
                    try{
                        System.out.println("--------------------------------------------------------------");
                        System.out.println("Player " + (player+1));
                        System.out.println("0: Exit\n" +
                                "1. Pickup coins\n" +
                                "2. Pick up card\n" +
                                "3. Show Hands\n" +
                                "4. Reserve card\n" +
                                "5. Pickup reserve card");
                        System.out.println("--------------------------------------------------------------");

                        temp = scanner.nextInt();
                        System.out.println();
                        if(temp >= 0 && temp <= 5){
                            choice = temp;
                            exitDo = -1;
                            while(exitDo == -1){
                                exitDo = handlePlayChoice(scanner, player, choice, exitDo);
                            }

                            if(player == numPlayers){
                                player = 0;
                            }else{
                                player++;
                            }

                        }else{
                            System.out.println("Choose 0-5");
                        }
                    }catch (InputMismatchException e){
                        System.out.println("Must enter an integer");
                        scanner.next();
                    }
                }while (exitDo <= 0);
            }
        }else{
            initializeGame(numPlayers);
            showNumberOfPlayers();
            showDecks();
            showCoinPiles();
            // showHands();
        }
    }

}


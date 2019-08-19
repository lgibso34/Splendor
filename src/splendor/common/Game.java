package splendor.common;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import splendor.common.cards.*;
import splendor.common.coins.CoinPile;
import splendor.common.util.DeckBuilder;
import splendor.common.util.Constants;

class Game {

    //private static int index = 0;
    private static int numPlayers = 5;
    private static boolean lastRound = false;
    private static int playerWhoInitiatedLastRound = -1;
    private static final boolean debug = true;

    private static Deck[] decks = new Deck[4];
    private static CoinPile[] coinPiles = new CoinPile[6];
    private static CardRow[] cardRows = new CardRow[4];
    private static Hand[] hands = new Hand[0];

    /**
     * Initializes the game by creating decks and dealing cards to their respective rows.
     * Hands are created for each player
     * @param numPlayers - Number of players in the game
     */
    private static void initializeGame(int numPlayers) {

        decks = DeckBuilder.buildDecks();

        for (int i = 0; i < Constants.colors.length; i++) {
            coinPiles[i] = new CoinPile(numPlayers, Constants.colors[i]);
        }

        for (int i = 0; i < decks.length; i++) {
            int dealt = 4;
            if (i == 0) {
                dealt = numPlayers + 1;
            }
            cardRows[i] = new CardRow(i, decks[i], dealt);
        }

        hands = new Hand[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            hands[i] = new Hand();
        }
    }

    private static Noble removeNoble(int index) {
        return (Noble) cardRows[0].remove(index);
    }

    private static boolean[] checkForNobles(Hand player) {
        int[] permCards = player.getPermanentCardCount();
        ArrayList<Card> nobles = cardRows[0].getCards();
        boolean[] noblesThatCanBeBought = new boolean[nobles.size()];
        boolean playerCanBuy = true;


        for (int i = 0; i < nobles.size(); i++) {
            int[] cost = nobles.get(i).getColorCost();
            for (int j = 0; j < cost.length; j++) {
                if (permCards[j] < cost[j]) {
                    playerCanBuy = false;
                    j = cost.length; // break out of the loop
                }
            }
            noblesThatCanBeBought[i] = playerCanBuy;
        }
        return noblesThatCanBeBought;
    }

    private static void addCoinsToPiles(int[] cardCost) {
        for (int i = 0; i < cardCost.length; i++) {
            if (cardCost[i] > 0) {
                for (int j = 0; j < cardCost[i]; j++) {
                    coinPiles[i].add();
                }
            }
        }
    }

    private static int removeCoin(int color) {
        return coinPiles[color].remove();
    }

    private static void showDecks() {
        for (int i = 0; i < decks.length; i++) {
            System.out.println("========== Deck " + i + " ==========");
            System.out.println(decks[i].toString());
        }
    }

    private static void showCoinPiles() {
        // show coin piles
        System.out.println(coinPiles[coinPiles.length-1].toString()); // show just the gold coins

        for(int i=0; i<coinPiles.length-1; i++){
            System.out.println(i + ": " + coinPiles[i].toString());
        }
        System.out.println();
    }

    private static void showCardRows() {
        // show card row
        System.out.println(cardRows[0].toString());
        for (int i = 3; i > 0; i--) {
            System.out.println(cardRows[i].toString());
        }
        System.out.println();
    }

    private static void showNumberOfPlayers() {
        System.out.println("Number of Players: " + numPlayers);
        System.out.println();
    }

    private static void showHands() {
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Hand " + (i + 1) + ": " + hands[i].toString());
        }
    }

    //UNTESTED
    // TODO
    private static Hand handleWinner() {
        Hand winner = hands[playerWhoInitiatedLastRound];
        ArrayList<Hand> ties = new ArrayList<>();
        ties.add(winner);
        for (Hand player : hands) {
            if (player.getPoints() == winner.getPoints()) {
                ties.add(player);
            }
            winner = player.getPoints() > winner.getPoints() ? player : winner;
        }

        return (ties.size() <= 1) ? winner : handleTie(ties);
    }

    //UNTESTED
    // TODO
    private static Hand handleTie(ArrayList<Hand> tiedHands) {
        Hand winner = tiedHands.get(0); // start with the first hand
        for (Hand hand : tiedHands) {
            // check if the tiedHands have less total permanent cards than the current 'winner'
            winner = hand.totalPermanentCards() < winner.totalPermanentCards() ? hand : winner;
        }
        return winner;
    }

    private static int handlePlayChoice(Scanner scanner, int player, int choice, int exitDo) {
        int row;
        int cardSpot;
        switch (choice) {
            case 0:
                exitDo = 1;
                break;
            case 1:
                int playerCoinCount = hands[player].getCoinCount();
                showCoinPiles();
                // pick up coins
                System.out.println("Total coin count: " + playerCoinCount);
                System.out.println("0: Go back");
                System.out.println("--------------------------------------------------------------");
                System.out.println("Select coin color\nOnly enter colors on new lines\n");

                int coinChoice = scanner.nextInt() - 1; // not using try catch to verify that it is an integer since this is only debug mode,
                if (coinChoice == -1) {
                    exitDo = -2;
                    break;
                }

                int secondChoice = scanner.nextInt() - 1;
                if (secondChoice == -1) {
                    exitDo = -2;
                    break;
                }

                if (coinChoice != secondChoice) {
                    if (playerCoinCount < 8) {
                        coinPiles[coinChoice].remove();
                        hands[player].addCoin(coinChoice);

                        coinPiles[secondChoice].remove();
                        hands[player].addCoin(secondChoice);

                        int thirdChoice = scanner.nextInt() - 1;
                        coinPiles[thirdChoice].remove();
                        hands[player].addCoin(thirdChoice);

                    } else {
                        // this will contain a bug until this is cleaned up
                        // for instance if the user can only grab two coins of different colors
                        System.out.println("You would go over 10 coins.");
                        exitDo = -2;
                        break;
                    }
                } else if (coinPiles[coinChoice].getSize() >= 4) {
                    if (playerCoinCount < 9) {
                        coinPiles[coinChoice].remove();
                        hands[player].addCoin(coinChoice);

                        coinPiles[secondChoice].remove();
                        hands[player].addCoin(secondChoice);
                    } else {
                        System.out.println("There must be four coins available for you to grab two or you would go over 10 coins.");
                        exitDo = -2;
                        break;
                    }
                }
                exitDo = 0;
                break;
            case 2:
                // buy a card logic
                showHands();
                showCardRows();
                System.out.println("Choose row (1-3) (0 to go back): ");
                row = scanner.nextInt(); // not using try catch to verify that it is an integer since this is only debug mode,
                if (row == 0) {
                    exitDo = -2;
                    break;
                } else {
                    System.out.println("Choose card spot (1-4): ");
                    cardSpot = scanner.nextInt() - 1;

                    int playerCanBuy = hands[player].checkBalance(cardRows[row].peekCard(cardSpot));
                    int playerWillUseGoldCoin = 0;

                    if (playerCanBuy >= 0) {
                        if (playerCanBuy > 0) {
                            System.out.println("You must use " + playerCanBuy + " gold coin(s) to purchase this card. Proceed?\n0 for no\n1 for yes");
                            playerWillUseGoldCoin = scanner.nextInt();
                            if (playerWillUseGoldCoin == 0) {
                                exitDo = -2;
                                break;
                            }
                        }

                        int[] cardCost = hands[player].getCost(cardRows[row].peekCard(cardSpot));
                        if (playerWillUseGoldCoin == 1) {
                            int[] oldCardCost = new int[cardCost.length + 1];
                            System.arraycopy(cardCost, 0, oldCardCost, 0, cardCost.length);
                            oldCardCost[5] = playerCanBuy;

                            cardCost = new int[cardCost.length + 1];
                            System.arraycopy(oldCardCost, 0, cardCost, 0, cardCost.length);
                        }
                        Card pickedUpCard = cardRows[row].removeAndReplace(cardSpot, decks[row].dealCard());
                        addCoinsToPiles(cardCost);
                        hands[player].addCard(pickedUpCard, cardCost);
                        exitDo = 0;
                    } else {
                        System.out.println("You do not have the balance for this card.");
                        exitDo = -2;
                    }
                    break;
                }
            case 3:
                // show hands and card rows
                showHands();
                showCardRows();
                exitDo = -2;
                break;
            case 4:
                // reserve card logic
                System.out.println(coinPiles[5].toString());
                showCardRows();
                // pick up card
                System.out.println("Choose row (1-3) (0 to go back): ");
                row = scanner.nextInt(); // not using try catch to verify that it is an integer since this is only debug mode,
                if (row == 0) {
                    exitDo = -2;
                    break;
                } else {
                    System.out.println("Choose card spot (1-4): ");
                    cardSpot = scanner.nextInt() - 1;

                    if (hands[player].checkReservedCardQuantity()) {
                        Card pickedUpCard = cardRows[row].removeAndReplace(cardSpot, decks[row].dealCard());
                        pickedUpCard.setFaceUp(false);
                        hands[player].reserveCard(pickedUpCard);
                        hands[player].addCoin(removeCoin(5)); // add a gold coin while removing it from this game pile
                        exitDo = 0;
                    } else {
                        System.out.println("You already have 3 reserved cards.");
                        exitDo = -2;
                    }
                }
                break;
            case 5:
                // buy card from hand reserve pile
                showHands();
                hands[player].showReservedCards();

                System.out.println("Choose row (0 to go back): ");
                row = scanner.nextInt() - 1; // not using try catch to verify that it is an integer since this is only debug mode,
                if (row == -1) {
                    exitDo = -2;
                    break;
                } else {
                    int playerCanBuy = hands[player].checkBalance(hands[player].peekCard(row));
                    int playerWillUseGoldCoin = 0;

                    if (playerCanBuy >= 0) {
                        if (playerCanBuy > 0) {
                            System.out.println("You must use " + playerCanBuy + " gold coin(s) to purchase this card. Proceed?\n0 for no\n1 for yes");
                            playerWillUseGoldCoin = scanner.nextInt();
                            if (playerWillUseGoldCoin == 0) {
                                exitDo = -2;
                                break;
                            }
                        }
                        int[] cardCost = hands[player].getCost(hands[player].peekCard(row));

                        if (playerWillUseGoldCoin == 1) {
                            int[] oldCardCost = new int[cardCost.length + 1];
                            System.arraycopy(cardCost, 0, oldCardCost, 0, cardCost.length);
                            oldCardCost[5] = playerCanBuy;

                            cardCost = new int[cardCost.length + 1];
                            System.arraycopy(oldCardCost, 0, cardCost, 0, cardCost.length);
                        }
                        hands[player].buyReservedCard(row, cardCost);
                        addCoinsToPiles(cardCost);
                        exitDo = 0;
                        break;
                    } else {
                        System.out.println("you do not have the balance for that card");
                    }
                }
                break;
            default:
                break;
        }
        return exitDo;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public static void main(String[] args) {
        boolean play = false;

        if (debug) {
            Scanner scanner = new Scanner(System.in);

            int exitDo = 0;
            int temp;
            do {
                try {
                    System.out.print("Enter number of players: ");
                    temp = scanner.nextInt();
                    if (temp > 1 && temp < 6) {
                        numPlayers = temp;
                        exitDo = 1; // exit do loop
                    } else {
                        System.out.println("Must be between 2-5 players");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Must enter an integer");
                    scanner.next();
                }

            } while (exitDo == 0);

            initializeGame(numPlayers);

            exitDo = 0;
            int choice;
            do {
                try {
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
                    if (temp >= 0 && temp <= 6) {
                        choice = temp;
                        switch (choice) {
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
                    } else {
                        System.out.println("Choose 0-6");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Must enter an integer");
                    scanner.next();
                }
            } while (exitDo == 0);

            // if the programmer chooses to play ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if (play) {
                int player = 0;
                exitDo = 0;
                do {
                    if (lastRound) {
                        if ((player) == playerWhoInitiatedLastRound) {
                            break; //exit the doWhile loop to handleWinner()
                        }
                    }
                    try {
                        System.out.println("--------------------------------------------------------------");
                        System.out.println("Player " + (player + 1));
                        System.out.println("0: Exit\n" +
                                "1. Pickup coins\n" +
                                "2. Buy a card\n" +
                                "3. Show Hands\n" +
                                "4. Reserve card\n" +
                                "5. Buy one of your reserved cards");
                        System.out.println("--------------------------------------------------------------");

                        temp = scanner.nextInt();
                        System.out.println();
                        if (temp >= 0 && temp <= 5) {
                            choice = temp;
                            exitDo = -1;
                            while (exitDo == -1) {
                                exitDo = handlePlayChoice(scanner, player, choice, exitDo);
                            }

                            if (exitDo != -2) {
                                //if a user can buy a noble the index location will have a 1 in it. The user can pickup the noble at that index
                                boolean[] noblesPlayerCanBuy = checkForNobles(hands[player]);
                                boolean playerCantPickupNobles = true;
                                for (int i = 0; i < noblesPlayerCanBuy.length; i++) {
                                    if (noblesPlayerCanBuy[i]) {
                                        playerCantPickupNobles = false;
                                        System.out.println(cardRows[0].peekCard(i).toString());
                                    }
                                }

                                if (!playerCantPickupNobles) {
                                    System.out.println("Choose a noble to pickup: ");
                                    int nobleChoice = scanner.nextInt();
                                    hands[player].addNobleToHand(removeNoble(nobleChoice));
                                }

                                if (hands[player].getPoints() >= 15 && !lastRound) {
                                    lastRound = true;
                                    playerWhoInitiatedLastRound = player;
                                }

                                if (player == numPlayers - 1) {
                                    player = 0;
                                } else {
                                    player++;
                                }
                            }

                        } else {
                            System.out.println("Choose 0-5");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Must enter an integer");
                        scanner.next();
                    }
                } while (exitDo <= 0);

                handleWinner();
            }
        } else {
            initializeGame(numPlayers);
            showNumberOfPlayers();
            showDecks();
            showCoinPiles();
            // showHands();
        }
    }

}


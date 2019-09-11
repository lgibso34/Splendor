package splendor.common;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import splendor.common.cards.*;
import splendor.common.coins.CoinBank;
import splendor.common.util.Constants;
import splendor.common.util.DeckBuilder;
import splendor.common.util.Constants.Color;

class Game {

    private static int numPlayers = 0;
    private static boolean lastRound = false;
    private static int playerWhoInitiatedLastRound = -1;
    private static final boolean debug = true;
    private static int MAX_PLAYER_COINS;

    private static Deck[] decks = new Deck[4];
    private static CoinBank gameCoins;
    private static CardRow[] cardRows = new CardRow[4];
    private static Hand[] hands;

    /**
     * Initializes the game by creating decks and dealing cards to their respective rows.
     * Hands are created for each player
     *
     * @param numPlayers - Number of players in the game
     */
    private static void initializeGame(int numPlayers) {
        MAX_PLAYER_COINS = numPlayers == 5 ? 7 : 10;

        gameCoins = new CoinBank(numPlayers);
        decks = DeckBuilder.buildDecks();

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
        Colors permCards = player.getPermanentCardCount();
        ArrayList<Card> nobles = cardRows[0].getCards();
        boolean[] noblesThatCanBeBought = new boolean[nobles.size()];
        int i = 0;
        for (Card n : nobles) {
            noblesThatCanBeBought[i++] = permCards.greaterOrEqualTo(n.getColorCost());
        }
        return noblesThatCanBeBought;
    }

    private static void addCoinsToGameBank(Colors cardCost) {
        for (Color c : Color.colors) {
            if (cardCost.getCost(c) > 0) {
                gameCoins.addCoins(c, cardCost.getCost(c));
            }
        }
    }

    private static void showDecks() {
        for (int i = 0; i < decks.length; i++) {
            System.out.println("========== Deck " + i + " ==========");
            System.out.println(decks[i].toString());
        }
    }

    private static void showGameBank() {
        for (Color c : Color.colors) {
            System.out.println(gameCoins.pileToString(c));
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
        System.out.println("Number of Players: " + numPlayers + "\n");
    }

    private static void showHands() {
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Hand " + (i + 1) + ": " + hands[i].toString());
        }
    }

    private static Hand handleWinner() {
        Hand winner = hands[playerWhoInitiatedLastRound];
        ArrayList<Hand> ties = new ArrayList<>();

        // add the hand to the first index [0] of the ties array
        ties.add(winner); // handle ties uses the first index to determine the winner if the two or more people have the same number of permanent cards

        for (int i = 0; i < hands.length; i++) {
            if (i != playerWhoInitiatedLastRound && hands[i].getPoints() == winner.getPoints())
                ties.add(hands[i]);

            if (hands[i].getPoints() > winner.getPoints())
                winner = hands[i];
        }

        return (ties.size() <= 1) ? winner : handleTie(ties);
    }

    private static Hand handleTie(ArrayList<Hand> tiedHands) {
        Hand winner = tiedHands.get(0); // start with the first hand, if perm cards are equal between two players, the playerWhoInitiatedLastRound wins
        for (Hand hand : tiedHands) { // could possibly use for loop to prevent one iteration by starting at index 1 in tiedHands
            // check if the tiedHands have less total permanent cards than the current 'winner'
            if (hand.getTotalPermanentCount() < winner.getTotalPermanentCount())
                winner = hand;
        }
        return winner;
    }

    private static void checkCoinOverflow(Hand playerHand, Scanner scanner) {
        //check if player has too many gems
        if (playerHand.getCoinCount() > MAX_PLAYER_COINS) {
            System.out.println("You may not own more than 10 coins. You currently have " + playerHand.getCoinCount() + " coins.");
            System.out.println(playerHand.coinsToString());
            System.out.println("You must dispose of some until you only have 10.");
            System.out.println("--------------------------------------------------------------");
            System.out.println("Select coin color(s) and amount(s), ONE PER LINE, in the format: 3b\n");
            while (playerHand.getCoinCount() > MAX_PLAYER_COINS) {
                int amount = -1;
                Color color = null;
                while (0 > amount || color == null) {
                    color = null;
                    String input = scanner.next();
                    amount = Integer.parseInt(input.substring(0, 1));
                    if (input.length() > 1)
                        color = Color.fromShortName(input.substring(1, 2));
                    //check hand coins
                    if (playerHand.getCoinAmount(color) >= amount)
                        playerHand.removeCoins(color, amount);
                }
            }
        }
    }

    public boolean checkCoinOverflow(int playerNum) {
        Hand playerHand = hands[playerNum];
        while (playerHand.getCoinCount() > MAX_PLAYER_COINS) {
            //TODO need to have client send overflow packet.
//            int amount = -1;
//            Color color = null;
//            while (0 > amount || color == null) {
//                color = null;
//                String input = scanner.next();
//                amount = Integer.parseInt(input.substring(0, 1));
//                if (input.length() > 1)
//                    color = Color.fromShortName(input.substring(1, 2));
//                //check hand coins
//                if (playerHand.getCoinAmount(color) >= amount)
//                    playerHand.removeCoins(color, amount);
//            }
        }
        return true;
    }

    private static int handlePlayChoice(Scanner scanner, int player, int choice, int exitDo) {
        int row;
        int cardSpot;
        Hand playerHand = hands[player];
        switch (choice) {
            case 0:
                exitDo = 1;
                break;
            case 1:
                int playerCoinCount = playerHand.getCoinCount();
                // pick up coins
                System.out.println("Total coin count: " + playerCoinCount);
                System.out.println("0g: Go back");
                System.out.println("--------------------------------------------------------------");
                System.out.println("You currently have a total of " + playerHand.getCoinCount() + " coins consisting of:");
                System.out.println(playerHand.coinsToString());
                System.out.println("Available coins:");
                System.out.println(gameCoins.toString());
                System.out.println("Select coin color(s) and amount(s), in the format: 1b 1k 0\n");

                int amount1 = -1, amount2, amount3;
                Color color1 = null, color2 = null, color3 = null;

                while (0 > amount1 || amount1 > 2 || color1 == null) { //first amount can be 1 or 2, 0 to go back
                    color1 = null;
                    String input = scanner.next();
                    amount1 = Integer.parseInt(input.substring(0, 1));
                    if (input.length() > 1)
                        color1 = Color.fromShortName(input.substring(1, 2));
                }
                if (amount1 == 0) {
                    exitDo = -2;
                    break;
                }

                if (amount1 == 2) {
                    //check coin piles
                    if (gameCoins.canTake(color1, amount1)) {
                        gameCoins.removeCoins(color1, amount1);
                        playerHand.addCoins(color1, amount1);
                    } else {
                        System.out.println("You may not take 2 " + color1 + " coins right now.");
                        exitDo = -2;
                        break;
                    }
                } else {
                    amount2 = -1;
                    amount3 = -1;
                    while (0 > amount2 || amount2 > 1) { //second amount can be 0 or 1
                        color2 = null;
                        String input = scanner.next();
                        amount2 = Integer.parseInt(input.substring(0, 1));
                        if (input.length() > 1)
                            color2 = Color.fromShortName(input.substring(1, 2));
                    }

                    while (0 > amount3 || amount3 > 1) { //third amount can be 0 or 1
                        color3 = null;
                        String input = scanner.next();
                        amount3 = Integer.parseInt(input.substring(0, 1));
                        if (input.length() > 1)
                            color3 = Color.fromShortName(input.substring(1, 2));
                    }

                    //check coin piles
                    if (gameCoins.canTake(color1, amount1) && gameCoins.canTake(color2, amount2) && gameCoins.canTake(color3, amount3)) {
                        gameCoins.removeCoins(color1, amount1);
                        gameCoins.removeCoins(color2, amount2);
                        gameCoins.removeCoins(color3, amount3);
                        //add coins
                        playerHand.addCoins(color1, amount1);
                        playerHand.addCoins(color2, amount2);
                        playerHand.addCoins(color3, amount3);
                    } else {
                        System.out.println("That combination of coins is not a valid choice.");
                        exitDo = -2;
                        break;
                    }
                }
                checkCoinOverflow(playerHand, scanner);
                exitDo = 0;
                break;
            case 2:
                // buy a card logic
                showHands();
                showCardRows();
                System.out.println("Choose level (1-3) (0 to go back): ");
                row = scanner.nextInt(); // not using try catch to verify that it is an integer since this is only debug mode,
                if (row == 0) {
                    exitDo = -2;
                    break;
                } else {
                    System.out.println("Choose card spot (1-4): ");
                    cardSpot = scanner.nextInt() - 1;
                    Card card = cardRows[row].peekCard(cardSpot);
                    Colors modifiedCost = playerHand.getModifiedCost(card);

                    if (playerHand.canBuy(card, modifiedCost)) {
                        if (modifiedCost.getCost(Color.Gold) > 0) {
                            System.out.println("You must use " + modifiedCost.getCost(Color.Gold) + " gold coin(s) to purchase this card. Proceed?\n0 for no\n1 for yes");
                            int useGoldCoins = scanner.nextInt();
                            if (useGoldCoins == 0) {
                                exitDo = -2;
                                break;
                            }
                        }
                        Card pickedUpCard = cardRows[row].removeAndReplace(cardSpot);

                        addCoinsToGameBank(modifiedCost);
                        playerHand.addCard(pickedUpCard, modifiedCost);
                        exitDo = 0;
                    } else {
                        System.out.println("You do not have the balance for this card.");
                        exitDo = -2;
                    }
                    break;
                }
            case 3:
                // show hands and card rows
                showGameBank();
                showHands();
                showCardRows();
                exitDo = -2;
                break;
            case 4:
                // reserve card logic
                System.out.println(gameCoins.pileToString(Color.Gold));
                showCardRows();
                // pick up card
                System.out.println("Choose level (1-3) (0 to go back): ");
                row = scanner.nextInt(); // not using try catch to verify that it is an integer since this is only debug mode,
                if (row == 0) {
                    exitDo = -2;
                    break;
                } else {
                    System.out.println("Choose card spot (1-4): ");
                    cardSpot = scanner.nextInt() - 1;

                    if (playerHand.checkReservedCardQuantity()) {
                        Card pickedUpCard = cardRows[row].removeAndReplace(cardSpot);
                        pickedUpCard.setFaceUp(false);
                        playerHand.reserveCard(pickedUpCard);
                        if (gameCoins.removeCoins(Color.Gold)) {
                            playerHand.addCoin(Color.Gold); // add a gold coin if one was in the pile
                            checkCoinOverflow(playerHand, scanner);
                        }
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
                playerHand.showReservedCards();

                System.out.println("Choose card number (0 to go back): ");
                row = scanner.nextInt() - 1; // not using try catch to verify that it is an integer since this is only debug mode,
                if (row == -1) {
                    exitDo = -2;
                    break;
                } else {
                    Card card = cardRows[row].peekCard(row);
                    Colors modifiedCost = playerHand.getModifiedCost(card);
                    if (playerHand.canBuy(card, modifiedCost)) {
                        if (modifiedCost.getCost(Color.Gold) > 0) {
                            System.out.println("You must use " + modifiedCost.getCost(Color.Gold) + " gold coin(s) to purchase this card. Proceed?\n0 for no\n1 for yes");
                            int useGoldCoins = scanner.nextInt();
                            if (useGoldCoins == 0) {
                                exitDo = -2;
                                break;
                            }
                        }

                        addCoinsToGameBank(modifiedCost);
                        playerHand.buyReservedCard(row, modifiedCost);
                        exitDo = 0;
                    } else {
                        System.out.println("You do not have the balance for this card.");
                        exitDo = -2;
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
                                showGameBank();
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

                    try {
                        System.out.println("--------------------------------------------------------------");
                        System.out.println("Player " + (player + 1));
                        System.out.println("0: Exit\n" +
                                "1. Pickup coins\n" +
                                "2. Buy a card\n" +
                                "3. Show Game State\n" +
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
                                Hand playerHand = hands[player];
                                //if a user can buy a noble the index location will have a 1 in it. The user can pickup the noble at that index
                                boolean[] noblesPlayerCanBuy = checkForNobles(playerHand);
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
                                    playerHand.addNobleToHand(removeNoble(nobleChoice));
                                }

                                if (playerHand.getPoints() >= 15 && !lastRound) {
                                    lastRound = true;
                                    playerWhoInitiatedLastRound = player;
                                }

                                if (lastRound && player == numPlayers - 1)
                                    break; //exit the doWhile loop to handleWinner()


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

                if (playerWhoInitiatedLastRound != -1) {
                    Hand WINNER = handleWinner();
                    System.out.println("WINNER:");
                    System.out.println(WINNER.toString());
                }

            }
        } else {
            initializeGame(numPlayers);
            showNumberOfPlayers();
            showDecks();
            showGameBank();
        }
    }

    public int addClient(boolean player) {
        if (player && numPlayers < Constants.MAX_NUM_PLAYERS)
            return numPlayers++;
        else
            return -1;
    }

    public boolean validateWithdraw(Colors coins, int playerNum) {
        Hand playerHand = hands[playerNum];
        if (coins.isSaneWithdraw() && gameCoins.canPlayerTake(coins)) {
            playerHand.addCoins(gameCoins.removeCoins(coins));
            return true;
        }
        return false;
    }

    public boolean validateDeposit(Colors coins, int playerNum) {
        Hand playerHand = hands[playerNum];
        if (playerHand.canSpend(coins)) {
            playerHand.addCoins(gameCoins.removeCoins(coins));
            return true;
        }
        return false;
    }

}


package splendor.common;

import splendor.common.cards.Card;
import splendor.common.cards.CardPile;
import splendor.common.cards.Noble;
import splendor.common.coins.CoinBank;
import splendor.common.util.Constants.Color;

import java.util.ArrayList;

class Hand {

    //private CoinPile[] coinPiles = new CoinPile[6]; // 5 colors + gold: [ white, blue, green, red, black, gold ]
    private CoinBank playerBank = new CoinBank();
    private CardPile[] cardPiles = new CardPile[6]; // 5 colors + reserved: [ white, blue, green, red, black, reserveCards ]
    private ArrayList<Noble> noblePile;
    private int points = 0;

    public Hand() {
        for (Color c : Color.colors) {
            cardPiles[c.ordinal()] = new CardPile(c);
        }
    }

    public int[] getPermanentCardCount() {
        int[] retArr = new int[cardPiles.length];
        for (int i = 0; i < cardPiles.length; i++) {
            retArr[i] = cardPiles[i].getSize();
        }
        return retArr;
    }

    public int getCoinCount() {
        return playerBank.getCoinCount();
    }

    public int getCoinAmount(Color color) {
        return playerBank.numCoins(color);
    }

    public void removeCoins(Color color, int amount) {
        playerBank.removeCoins(color, amount);
    }

    public int[] getCost(Card card) {
        int[] cardCost = card.getColorCost(); // get the color cost for the wanted card
        int[] cardCostWithPermanentsAndGold = new int[Color.colors.length];
        for (int i = 0; i < cardCost.length; i++) {
            if (cardPiles[i].getSize() > 0) { // if permanent cards exist for that color...
                int cost = cardCost[i] - cardPiles[i].getSize();
                if (cost <= 0) { // if the cost becomes negative there are more permanent cards than the cost so it costs zero of that color
                    cardCostWithPermanentsAndGold[i] = 0;
                } else {
                    cardCostWithPermanentsAndGold[i] = cost; // otherwise, the extra coins required are assigned to that color spot
                }
            } else {
                cardCostWithPermanentsAndGold[i] = cardCost[i]; // cost stays the same
            }
        }

        int goldCoinsNeeded = 0;
        int goldCoinCount = playerBank.numCoins(Color.Gold);
        boolean playerHasAtLeastOneGoldCoin = goldCoinCount > 0;

        for (Color c : Color.colors) { // iterate through each color
            if (cardCostWithPermanentsAndGold[c.ordinal()] > playerBank.numCoins(c)) { // and check if the card costs more than what the player has
                // if it does, see if the user has any gold coins that can be used to still buy the card
                if (playerHasAtLeastOneGoldCoin && cardCostWithPermanentsAndGold[c.ordinal()] <= (playerBank.numCoins(c) + goldCoinCount)) {
                    int difference = cardCostWithPermanentsAndGold[c.ordinal()] - playerBank.numCoins(c); // how many gold coins are needed for this color
                    goldCoinsNeeded += difference; // add to total gold coins needed for this card
                    cardCostWithPermanentsAndGold[c.ordinal()] = cardCostWithPermanentsAndGold[c.ordinal()] - difference; // subtract gold coin usage from color cost
                }
            }
        }

        cardCostWithPermanentsAndGold[Color.Gold.ordinal()] = goldCoinsNeeded; // amount of gold coins needed for this card

        return cardCostWithPermanentsAndGold;
    }

    public int checkBalance(Card card) {
        boolean playerCanBuy = true;
        boolean goldRequired = false;
        int goldCoinsNeeded = 0;
        int goldCoinCount = playerBank.numCoins(Color.Gold);

        int[] cardCostWithPermanents = getCost(card);

        if (cardCostWithPermanents[Color.Gold.ordinal()] > 0) {
            goldRequired = true;
            goldCoinsNeeded = cardCostWithPermanents[Color.Gold.ordinal()];
        }

        for (Color c : Color.colors) { // iterate through each color
            if (cardCostWithPermanents[c.ordinal()] > playerBank.numCoins(c)) { // and check if the card costs more than what the player has
                playerCanBuy = false;
                break; // break out of the loop
            }
        }

        if (goldCoinsNeeded > goldCoinCount) {
            goldRequired = false;
        }
        // -1: player can't buy, 0: player can buy, 1 or greater: number of gold coins that it will cost the player
        return playerCanBuy ? goldRequired ? goldCoinsNeeded : 0 : -1;
    }

    public boolean checkReservedCardQuantity() {
        return cardPiles[Color.Gold.ordinal()].getSize() < 3;
    }

    // [ white, blue, green, red, black, gold ]
    public void addCoin(Color color) {
        playerBank.addCoins(color);
    }

    public void addCoins(Color color, int amount) {
        playerBank.addCoins(color, amount);
    }

    private void removeCoinsFromInventory(int[] cardCost) {
        for (Color c : Color.colors) {
            if (cardCost[c.ordinal()] > 0) {
                playerBank.removeCoins(c, cardCost[c.ordinal()]);
            }
        }
    }

    public void addCard(Card card, int[] cardCost) {
        if (card.getFaceUp()) {
            int pointValue = card.getValue();
            removeCoinsFromInventory(cardCost);
            cardPiles[card.getColorIndex()].add(card);
            if (pointValue > 0) {
                points += pointValue;
            }
        } else {
            cardPiles[Color.Gold.ordinal()].add(card); // add to faceDown reserve pile
        }
    }

    // used for pickup nobles
    public void addNobleToHand(Noble noble) {
        noblePile.add(noble);
        points += noble.getValue();
    }

    // used for ties
    public int totalPermanentCards() {
        int count = 0;
        for (CardPile c : cardPiles) {
            count += c.getSize();
        }
        return count;
    }

    public void showReservedCards() {
        System.out.println(cardPiles[Color.Gold.ordinal()].showCards());
    }

    public Card peekCard(int index) {
        return cardPiles[Color.Gold.ordinal()].peekCard(index);
    }

    // cardCost must be passed in because gold coins are not in the colorCost property
    // of the Card, the parameter passed in may contain gold coins that need to be
    // subtracted from the user's hand
    public void buyReservedCard(int index, int[] cardCost) {
        Card reservedCardBought = cardPiles[Color.Gold.ordinal()].buyReservedCard(index);
        reservedCardBought.setFaceUp(true);
        addCard(reservedCardBought, cardCost);
    }

    public void reserveCard(Card card) {
        cardPiles[Color.Gold.ordinal()].add(card);
    }

    public int getPoints() {
        return points;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("Points: " + points + "\n");
        for (Color c : Color.colors) {
            output.append(cardPiles[c.ordinal()].toString()).append(" ").append(playerBank.pileToString(c)).append("\n");
        }
        return output.toString();
    }

    public String coinsToString(){
        return playerBank.toString();
    }
}

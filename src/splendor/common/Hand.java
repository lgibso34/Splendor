package splendor.common;

import splendor.common.cards.Card;
import splendor.common.cards.CardBank;
import splendor.common.cards.Noble;
import splendor.common.coins.CoinBank;
import splendor.common.util.Constants.Color;

import java.util.ArrayList;

class Hand {

    private CoinBank playerCoins = new CoinBank();
    private CardBank playerCards = new CardBank();
    private ArrayList<Noble> noblePile = new ArrayList<>();
    private int points = 0;

    public Hand() {}

    public int[] getPermanentCardCount() {
        int[] retArr = new int[Color.colors.length - 1];
        for (Color c : Color.colors) {
            if(c == Color.Gold)
                continue;
            retArr[c.ordinal()] = playerCards.numCards(c);
        }
        return retArr;
    }

    public int getCoinCount() {
        return playerCoins.getCoinCount();
    }

    public int getCoinAmount(Color color) {
        return playerCoins.numCoins(color);
    }

    public void removeCoins(Color color, int amount) {
        playerCoins.removeCoins(color, amount);
    }

    public int[] getModifiedCost(Card card) {
        int[] cardCost = card.getColorCost(); // get the color cost for the wanted card
        int[] permanents = getPermanentCardCount();

        for(Color c : Color.colors){
            if(c == Color.Gold)
                continue;
            int diff = cardCost[c.ordinal()] - permanents[c.ordinal()];
            if(diff <= 0)
                cardCost[c.ordinal()] = 0;
            else {
                cardCost[c.ordinal()] = diff;
                cardCost[Color.Gold.ordinal()] += diff;
            }
        }

        return cardCost;
    }

    public int checkBalance(Card card) {
        boolean playerCanBuy = true;
        boolean goldRequired = false;
        int goldCoinsNeeded = 0;
        int goldCoinCount = playerCoins.numCoins(Color.Gold);

        int[] cardCostWithPermanents = getModifiedCost(card);

        if (cardCostWithPermanents[Color.Gold.ordinal()] > 0) {
            goldRequired = true;
            goldCoinsNeeded = cardCostWithPermanents[Color.Gold.ordinal()];
        }

        for (Color c : Color.colors) { // iterate through each color
            if (cardCostWithPermanents[c.ordinal()] > playerCoins.numCoins(c)) { // and check if the card costs more than what the player has
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
        return playerCards.numCards(Color.Gold) < 3;
    }

    // [ white, blue, green, red, black, gold ]
    public void addCoin(Color color) {
        playerCoins.addCoins(color);
    }

    public void addCoins(Color color, int amount) {
        playerCoins.addCoins(color, amount);
    }

    private void removeCoinsFromInventory(int[] cardCost) {
        for (Color c : Color.colors) {
            if (cardCost[c.ordinal()] > 0) {
                playerCoins.removeCoins(c, cardCost[c.ordinal()]);
            }
        }
    }

    public void addCard(Card card, int[] cardCost) {
        if (card.getFaceUp()) {
            int pointValue = card.getValue();
            removeCoinsFromInventory(cardCost);
            playerCards.add(card);
            if (pointValue > 0) {
                points += pointValue;
            }
        } else {
            playerCards.reserve(card); // add to faceDown reserve pile
        }
    }

    // used for pickup nobles
    public void addNobleToHand(Noble noble) {
        noblePile.add(noble);
        points += noble.getValue();
    }

    // used for ties
    public int getTotalPermanentCount() {
        return playerCards.getTotalPermanentCount();
    }

    public void showReservedCards() {
        System.out.println(playerCards.pileToString(Color.Gold));
    }

    public Card peekCard(int index) {
        return playerCards.peekCard(index);
    }

    // cardCost must be passed in because gold coins are not in the colorCost property
    // of the Card, the parameter passed in may contain gold coins that need to be
    // subtracted from the user's hand
    public void buyReservedCard(int index, int[] cardCost) {
        Card reservedCardBought = playerCards.buyReservedCard(index);
        reservedCardBought.setFaceUp(true);
        addCard(reservedCardBought, cardCost);
    }

    public void reserveCard(Card card) {
        playerCards.reserve(card);
    }

    public int getPoints() {
        return points;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("Points: " + points + "\n");
        for (Color c : Color.colors) {
            output.append(playerCards.pileToString(c)).append(" ").append(playerCoins.pileToString(c)).append("\n");
        }
        return output.toString();
    }

    public String coinsToString(){
        return playerCoins.toString();
    }
}

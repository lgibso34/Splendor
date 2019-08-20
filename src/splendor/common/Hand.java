package splendor.common;

import splendor.common.cards.Card;
import splendor.common.cards.CardPile;
import splendor.common.cards.Noble;
import splendor.common.coins.CoinPile;
import splendor.common.util.Constants.Color;

import java.util.ArrayList;

class Hand {

    private CoinPile[] coinPiles = new CoinPile[6]; // 5 colors + gold: [ white, blue, green, red, black, gold ]
    private CardPile[] cardPiles = new CardPile[6]; // 5 colors + reserved: [ white, blue, green, red, black, reserveCards ]
    private ArrayList<Noble> noblePile;
    private int points = 0;

    public Hand() {
        //int i = 0;
        for (int i = 0; i < Color.colors.length; i++) {
            coinPiles[i] = new CoinPile(Color.colors[i]);
            cardPiles[i] = new CardPile(Color.colors[i]);
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
        int counter = 0;
        for (CoinPile c : coinPiles) {
            counter += c.getSize();
        }
        return counter;
    }

    public int getCoinAmount(Color color) {
        return coinPiles[color.ordinal()].getSize();
    }

    public void removeCoins(Color color, int amount) {
        coinPiles[color.ordinal()].removeCoins(amount);
    }

    public int[] getCost(Card card) {
        int[] cardCost = card.getColorCost(); // get the color cost for the wanted card
        int[] cardCostWithPermanentsAndGold = new int[cardCost.length + 1];
        for (int i = 0; i < cardCost.length; i++) {
            if (cardPiles[i].getSize() > 0) { // if permanent cards exist for that color...
                int cost = cardCost[i] - cardPiles[i].getSize();
                if (cost < 0) { // if the cost becomes negative there are more permanent cards than the cost so it costs zero of that color
                    cardCostWithPermanentsAndGold[i] = 0;
                } else {
                    cardCostWithPermanentsAndGold[i] = cost; // otherwise, the extra coins required are assigned to that color spot
                }
            } else {
                cardCostWithPermanentsAndGold[i] = cardCost[i]; // cost stays the same
            }
        }

        int goldCoinsNeeded = 0;
        int goldCoinCount = coinPiles[Color.Gold.ordinal()].getSize();
        boolean playerHasAtLeastOneGoldCoin = goldCoinCount > 0;

        for (int i = 0; i < cardCostWithPermanentsAndGold.length; i++) { // iterate through each color
            if (cardCostWithPermanentsAndGold[i] > coinPiles[i].getSize()) { // and check if the card costs more than what the player has
                // if it does, see if the user has any gold coins that can be used to still buy the card
                if (playerHasAtLeastOneGoldCoin && cardCostWithPermanentsAndGold[i] <= (coinPiles[i].getSize() + goldCoinCount)) {
                    int difference = cardCostWithPermanentsAndGold[i] - coinPiles[i].getSize(); // how many gold coins are needed for this color
                    goldCoinsNeeded += difference; // add to total gold coins needed for this card
                    cardCostWithPermanentsAndGold[i] = cardCostWithPermanentsAndGold[i] - difference; // subtract gold coin usage from color cost
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
        int goldCoinCount = coinPiles[Color.Gold.ordinal()].getSize();

        int[] cardCostWithPermanents = getCost(card);

        if (cardCostWithPermanents[Color.Gold.ordinal()] > 0) {
            goldRequired = true;
            goldCoinsNeeded = cardCostWithPermanents[Color.Gold.ordinal()];
        }

        for (int i = 0; i < cardCostWithPermanents.length; i++) { // iterate through each color
            if (cardCostWithPermanents[i] > coinPiles[i].getSize()) { // and check if the card costs more than what the player has
                playerCanBuy = false;
                i = cardCostWithPermanents.length; // break out of the loop
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
        coinPiles[color.ordinal()].add();
    }

    public void addCoins(Color color, int amount) {
        coinPiles[color.ordinal()].add(amount);
    }

    private void removeCoinsFromInventory(int[] cardCost) {
        for (int i = 0; i < cardCost.length; i++) {
            if (cardCost[i] > 0) {
                for (int j = 0; j < cardCost[i]; j++) {
                    coinPiles[i].remove();
                }
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
        for (int i = 0; i < cardPiles.length; i++) {
            output.append(cardPiles[i].toString()).append(" ").append(coinPiles[i].toString()).append("\n");
        }
        return output.toString();
    }
}

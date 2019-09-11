package splendor.common;

import splendor.common.cards.Card;
import splendor.common.cards.CardBank;
import splendor.common.cards.Colors;
import splendor.common.cards.Noble;
import splendor.common.coins.CoinBank;
import splendor.common.util.Constants.Color;

import java.util.ArrayList;

class Hand {

    private CoinBank playerCoins = new CoinBank();
    private CardBank playerCards = new CardBank();
    private ArrayList<Noble> noblePile = new ArrayList<>();
    private int points = 0;

    public Hand() {
    }

    public Colors getPermanentCardCount() {
        return playerCards.getPermanentCounts();
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

    public Colors getModifiedCost(Card card) {
        Colors cardCost = card.getColorCost(); // get the color cost for the wanted card
        Colors permanents = getPermanentCardCount();

        for (Color c : Color.colors) {
            if (c == Color.Gold)
                continue;
            int totalCoinsNeeded = cardCost.getCost(c) - permanents.getCost(c);
            if (totalCoinsNeeded <= 0)
                cardCost.setCost(c, 0);
            else {
                int goldDiff = totalCoinsNeeded - playerCoins.numCoins(c);
                if (goldDiff <= 0) {
                    cardCost.setCost(c, totalCoinsNeeded);
                } else {
                    cardCost.setCost(c, totalCoinsNeeded - goldDiff);
                    cardCost.addCost(Color.Gold, goldDiff);
                }
            }
        }

        return cardCost;
    }

    public boolean canBuy(Card card, Colors modifiedCost) {
        return modifiedCost.greaterOrEqualTo(card.getColorCost());
    }

    public boolean canSpend(Colors spend) {
        return playerCoins.greaterOrEqualTo(spend);
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

    public void addCoins(Colors colors) {
        playerCoins.addCoins(colors);
    }

    private void removeCoinsFromInventory(Colors cardCost) {
        for (Color c : Color.colors) {
            if (cardCost.getCost(c) > 0) {
                playerCoins.removeCoins(c, cardCost.getCost(c));
            }
        }
    }

    public void addCard(Card card, Colors cardCost) {
        if (card.getFaceUp()) {
            int pointValue = card.getValue();
            removeCoinsFromInventory(cardCost);
            playerCards.add(card);
            if (pointValue > 0) {
                points += pointValue;
            }
        } else {
            card.setFaceUp(false);
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

    // cardCost must be passed in because gold coins are not in the colorCost property
    // of the Card, the parameter passed in may contain gold coins that need to be
    // subtracted from the user's hand
    public void buyReservedCard(int index, Colors cardCost) {
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
        for (Noble n : noblePile) {
            output.append(n.toString());
        }
        return output.toString();
    }

    public String coinsToString() {
        return playerCoins.toString();
    }
}

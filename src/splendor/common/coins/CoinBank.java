package splendor.common.coins;

import splendor.common.util.Constants.Color;

public class CoinBank {
    private CoinPile[] bank = new CoinPile[6];

    public CoinBank(int numPlayers) {
        for (Color c : Color.colors) {
            bank[c.ordinal()] = new CoinPile(numPlayers, c);
        }
    }

    public CoinBank() {
        for (Color c : Color.colors) {
            bank[c.ordinal()] = new CoinPile(c);
        }
    }

    public void addCoins(Color c) {
        addCoins(c, 1);
    }

    public void addCoins(Color c, int amount) {
        bank[c.ordinal()].add(amount);
    }

    public boolean removeCoins(Color c) {
        return removeCoins(c, 1);
    }

    public boolean removeCoins(Color c, int amount) {
        if (c == null)
            return true;
        return bank[c.ordinal()].removeCoins(amount);
    }

    public int numCoins(Color c) {
        return bank[c.ordinal()].getSize();
    }

    public int getCoinCount() {
        int sum = 0;
        for (Color c : Color.colors)
            sum += bank[c.ordinal()].getSize();
        return sum;
    }

    public boolean canTake(Color c, int amount) {
        if (c == null)
            return true;
        return bank[c.ordinal()].canTake(amount);
    }

    public String pileToString(Color c) {
        return bank[c.ordinal()].toString();
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Color c : Color.colors) {
            output.append(bank[c.ordinal()].toString()).append(", ");
        }
        return output.substring(0, output.length() - 2);
    }

}

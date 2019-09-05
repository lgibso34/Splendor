package splendor.common.cards;

import splendor.common.util.Constants.Color;

public class Colors {
    private final int[] colors = new int[Color.colors.length];

    public Colors(int[] colors) {
        for (Color c : Color.colors) {
            if (c == Color.Gold)
                continue;
            this.colors[c.ordinal()] = colors[c.ordinal()];
        }
    }

    public Colors(String s) {
        if (s.length() + 1 < Color.colors.length || Color.colors.length < s.length())
            return;
        for (Color c : Color.colors) {
            if (c == Color.Gold)
                continue;
            this.colors[c.ordinal()] = Integer.parseInt(s.substring(c.ordinal(), c.ordinal()));
        }
    }

    public int getCost(Color c) {
        return colors[c.ordinal()];
    }

    public void setCost(Color c, int amount) {
        colors[c.ordinal()] = amount;
    }

    public void addCost(Color c, int amount) {
        colors[c.ordinal()] += amount;
    }

    public boolean greaterOrEqualTo(Colors colors) {
        for (Color c : Color.colors) {
            if (colors.getCost(c) > this.getCost(c))
                return false;
        }
        return true;
    }

    public boolean isSaneWithdraw() {
        int count2 = 0;
        int totalCount = 0;
        for (Color c : Color.colors) {
            int amount = colors[c.ordinal()];
            if (c == Color.Gold && amount > 0)
                return false;
            if (amount > 2)
                return false;
            if (amount == 2)
                count2++;
            totalCount += amount;
        }
        if (totalCount > 3 || count2 > 0 && totalCount > 2)
            return false;
        return true;
    }

}

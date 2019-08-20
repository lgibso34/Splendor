package splendor.common.cards;

import splendor.common.util.Constants.Color;

public class Colors {
    private final int[] colors = new int[Color.colors.length];

    public Colors(int[] colors){
        for(Color c : Color.colors) {
            if(c == Color.Gold)
                continue;
            this.colors[c.ordinal()] = colors[c.ordinal()];
        }
    }

    public int getCost(Color c){
        return colors[c.ordinal()];
    }

    public void setCost(Color c, int amount){
        colors[c.ordinal()] = amount;
    }

    public void addCost(Color c, int amount){
        colors[c.ordinal()] += amount;
    }

    public boolean greaterOrEqualTo(Colors colors){
        for(Color c : Color.colors){
            if(colors.getCost(c) > this.getCost(c))
                return false;
        }
        return true;
    }

}

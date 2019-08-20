package splendor.common.cards;

import splendor.common.util.Constants.Color;

public class CardBank {
    private CardPile[] bank = new CardPile[6];

    public CardBank() {
        for (Color c : Color.colors) {
            bank[c.ordinal()] = new CardPile(c);
        }
    }

    public int numCards(Color c){
        return bank[c.ordinal()].getSize();
    }

    public void add(Card card){
        bank[card.getColor().ordinal()].add(card);
    }

    public void reserve(Card card){
        bank[Color.Gold.ordinal()].add(card);
    }

    public Card buyReservedCard(int index){
        return bank[Color.Gold.ordinal()].buyReservedCard(index);
    }

    public Card peekCard(int index){
        return bank[Color.Gold.ordinal()].peekCard(index);
    }

    public int getTotalPermanentCount(){
        int count = 0;
        for (Color c : Color.colors) {
            if(c == Color.Gold)
                continue;
            count += bank[c.ordinal()].getSize();
        }
        return count;
    }

    public Colors getPermanentCounts(){
        int[] counts = new int[Color.colors.length];
        for (Color c : Color.colors) {
            if(c == Color.Gold)
                continue;
            counts[c.ordinal()] = bank[c.ordinal()].getSize();
        }
        return new Colors(counts);
    }

    public String pileToString(Color c){
        return bank[c.ordinal()].toString();
    }

}

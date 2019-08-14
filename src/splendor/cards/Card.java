package splendor.cards;

/**
 * Card
 */
public class Card {

    Integer white = 0;
    Integer blue = 0;
    Integer green = 0;
    Integer red= 0;
    Integer black = 0;


    public Card(Integer[] colors){
        this.white = colors[0];
        this.blue = colors[1];
        this.green = colors[2];
        this.red = colors[3];
        this.black = colors[4];
    }

}

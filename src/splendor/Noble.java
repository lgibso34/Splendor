package splendor;

public class Noble{
	
    Integer white = 0;
    Integer blue = 0;
    Integer green = 0;
    Integer red= 0;
    Integer black = 0;
    Integer value = 0;
	
    public Noble(Integer[] colors, Integer value){
        this.white = colors[0];
        this.blue = colors[1];
        this.green = colors[2];
        this.red = colors[3];
        this.black = colors[4];
        this.value = value;
    }
}
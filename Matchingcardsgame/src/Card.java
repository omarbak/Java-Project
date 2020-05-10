
import javax.swing.JButton;

// Define getters and setters to update card information.

@SuppressWarnings("serial")
public class Card extends JButton{
	
	private int CardID; //initial condition for cards identification code to be used for the matching
    private boolean matched = false; // all cards are labeled to false when created.


    public void setId(int CardID){
        this.CardID = CardID;
    }

    public int getId(){ //int because ID is int
        return this.CardID;
    }


    public void setMatched(boolean matched){
        this.matched = matched;
    }

    public boolean getMatched(){
        return this.matched;
    }


}
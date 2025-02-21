import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;





@SuppressWarnings("serial")
public class Concentration2 extends JFrame   {

	
	private int randNum = ThreadLocalRandom.current().nextInt(1,8);
	String themes = MainMenu.themes;
	String namePlayer1 = MainMenu.namePlayer1.toUpperCase();
	String namePlayer2 = MainMenu.namePlayer2.toUpperCase();
	int timer1 = MainMenu.timer1;
	int rows = Integer.parseInt(MainMenu.rows);
	int columns = Integer.parseInt(MainMenu.columns);
	//private String imagepath = "C:\\Users\\user\\Desktop\\javapics\\";
	
	//all private ints//
	
	private  int randplayerstart = ThreadLocalRandom.current().nextInt(0,100);
	private int pairs = rows*columns;
	private int endGameCounter = pairs/2;
    private int scoreplayer1=0;
    private int scoreplayer2=0;
    private int playerturn; //will indicate which player is playing 0-1
    
	private List<Card> cards; // automatically assigns JButton
    private Card clickedCard;
    private Card firstcard; //will be considered the first card picked
    private Card secondcard; //will be considered the second card picked
    private Timer timer;
    
    //private String image0 = "C:\\Users\\user\\Desktop\\javapics\\11.png";
    private String sScore="0";
    private String playername;

    private JTextField turnplayer2;
    private JTextField ScoreScreen1;
    private JTextField ScoreScreen2;
   
    private JLabel turnplayer;
    private JLabel ScoreScreenLabelp1;
    private JLabel ScoreScreenLabelp2;
    private JPanel scorepanel;
    
    public Concentration2(){ 
    	
    	
    	//randomly initialize who starts the game first after player names input
    	nameturn1();
    	//creates the buttons for the board 
    	createbutton();
        //setting the board
    	panelcreation();    
   
    
    
    }

  //////////////////////////////////////*******ALL OTHER METHODS*************\\\\\\\\\\\\\\\\\\\\\\\\
    
    
    private void panelcreation() {
    	//playing board creation
    	Container mainpanel = getRootPane();
        mainpanel.setLayout(new BorderLayout());
        Container secondarypanel;
        secondarypanel = getContentPane();
        secondarypanel.setLayout(new GridLayout(rows+1, columns));
        for (int g = 0 ; g<cards.size(); g++){
        	//adding cards to the board//container
            secondarypanel.add(cards.get(g));}
        
        //panel for scoreboard
        scorepanel = new JPanel();
        //text to indicate player turn and score
        ScoreScreen1 = new JTextField(sScore);
        ScoreScreen2 = new JTextField(sScore);
        turnplayer2 = new JTextField(playername);
        //labels of previous texts
        turnplayer = new JLabel("Turn is for player : ");
        ScoreScreenLabelp1 = new JLabel(namePlayer1+"'s SCORE:");
        ScoreScreenLabelp2 = new JLabel(namePlayer2+"'s SCORE:");
        //texts are uneditable;
        ScoreScreen1.setEditable(false);        
        ScoreScreen2.setEditable(false);
        turnplayer2.setEditable(false);
        //size for text frame
        ScoreScreen1.setPreferredSize( new Dimension( 25,25 ) );
        ScoreScreen2.setPreferredSize( new Dimension( 25,25 ) );
        turnplayer2.setPreferredSize( new Dimension( 65,40 ) );
        //adding everything to the panel
        scorepanel.add(turnplayer);
        scorepanel.add(turnplayer2);
        scorepanel.add(ScoreScreenLabelp1);
        scorepanel.add(ScoreScreen1);
        scorepanel.add(ScoreScreenLabelp2);
        scorepanel.add(ScoreScreen2);
        //sticking the panel to the main panel/container
        mainpanel.add(secondarypanel,BorderLayout.CENTER);
        mainpanel.add(scorepanel,BorderLayout.SOUTH);
        //setting JFrame's settings
        setTitle("Concentration Game");
        setMinimumSize(new Dimension(600,600));
        setPreferredSize(new Dimension(900,900)); 
        setLocation(0,0);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    
    private void createbutton() {
    	
        List<Card> cardsList = new ArrayList<Card>();
        List<Integer> cardIDs = new ArrayList<Integer>();
        
        //creates a list with duplicate number [0,1,2,3,0,1,2,3]
        for (int twice = 0 ; twice <= 1 ; twice++) { 
        	 for (int i = 0; i < pairs/2; i++){
        		 cardIDs.add(i+randNum);}
        }
        //shuffling the future cards emplacement on the grid
        
        Collections.shuffle(cardIDs); 
        
        //checking if its working /cheating
        System.out.println(cardIDs);
        
        //check if random number is generated correctly
        System.out.println(randNum);
        
       //creation of cards/buttons with IDs that have been given to cardIDs
        for(int z = 0 ; z<cardIDs.size();z++) {
    	Card card = new Card();
    	card.setId(cardIDs.get(z));
        card.setMinimumSize(new Dimension(200, 200));
        card.setPreferredSize(new Dimension(200, 200));
        //setting blank standard image to buttons if not clicked
    	card.setIcon(new ImageIcon(getClass().getResource("gamepictures/11.png")));
    		card.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                	clickedCard = card;
                	System.out.println("You're pressing card"+ clickedCard.getId()); //check if prog is working correctly
                    assignCard(); //assigns the first click to the first card and second click to second card
                               }});
            cardsList.add(card); //adding the new cards to a list.
        }
        this.cards=cardsList;
    }
  //set up the timer
    //timer start once the second card is chosen
    private void timerstart() {
        timer = new Timer(timer1, new ActionListener(){ 
        	public void actionPerformed(ActionEvent ae){
        		checkMatch();
        	}});
        timer.start();
        timer.setRepeats(false);
    }
    
    //assigns first click to card1 and second click to card2 for matching procedures
    private void assignCard(){
    	if (firstcard == null && secondcard == null){
    		firstcard = clickedCard;
            String IDval1 = String.valueOf(firstcard.getId()); // assigns a picture from the saved pics
            firstcard.setIcon(new ImageIcon(getClass().getResource("gamepictures/"+themes+"/"+IDval1+".jpg")));
                       
    	}
    	
    		
    	

        else if (firstcard != null && firstcard != clickedCard && secondcard == null){
            secondcard = clickedCard;
            String IDval2 = String.valueOf(secondcard.getId());
            secondcard.setIcon(new ImageIcon(getClass().getResource("gamepictures/"+themes+"/"+IDval2+".jpg")));
            
            timerstart();

        }

    }
     
    private void checkMatch(){

        if (firstcard.getId() == secondcard.getId()){
            scoregain();
            firstcard.setEnabled(false); //disables the button
            secondcard.setEnabled(false);
            endGameCounter--;
            //after matching the cards, we check if it was the last pair of cards in the game
            ifgameends();

        	}
        else{
        	scoreloss();
            firstcard.setIcon(new ImageIcon(getClass().getResource("gamepictures/11.png")));
            secondcard.setIcon(new ImageIcon(getClass().getResource("gamepictures/11.png")));
            playerturn=playerturn+1;
            nameturn();
            System.out.println("value of playerturn :  "+ playerturn%2);
            
        }
 
        firstcard = secondcard = null; //reset c1 and c2 (empties them)
        ScoreScreen1.setText(Integer.toString(scoreplayer1));
        ScoreScreen2.setText(Integer.toString(scoreplayer2));

        }
    
    private void ifgameends() {
    	
    	if (this.GameEnds()){ScoreScreen1.setText(Integer.toString(scoreplayer1));
        ScoreScreen2.setText(Integer.toString(scoreplayer2));
        if(scoreplayer1>scoreplayer2) {
        int response =  JOptionPane.showConfirmDialog(this, "Congrats "+namePlayer1+" ! YOU WIN THE GAME!\n"+namePlayer1+" YOUR SCORE IS: "+Integer.toString(scoreplayer1)+"\n Start a Second Game?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
               dispose();
       } 
           else if (response == JOptionPane.YES_OPTION) {
        	   dispose();
        	   new Concentration2();
        	   randNum=0;      	   
       }}             	
        else if(scoreplayer2>scoreplayer1) {
                int response =  JOptionPane.showConfirmDialog(this, "Congrats "+namePlayer2+" ! YOU WIN THE GAME!\n"+namePlayer2+" YOUR SCORE IS: "+Integer.toString(scoreplayer2)+"\n Start a Second Game?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.NO_OPTION) {
                    dispose();
            } 
                else if (response == JOptionPane.YES_OPTION) {
             	   dispose();
             	   new Concentration2();
            	   randNum=0;
            	   }}
        else if(scoreplayer1==scoreplayer2) {
        	int response =  JOptionPane.showConfirmDialog(this, "IT'S A TIE \n Start a Second Game?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                dispose();
        } 
            else if (response == JOptionPane.YES_OPTION) {
         	   dispose();
         	   new Concentration2();
        	   randNum=0;
        	   }
        	
        	
        }
    	}}
    
    
  //after first run
    private void nameturn() {
    	if (playerturn%2==0) {
    		turnplayer2.setText(namePlayer1);
    	}
    	else { turnplayer2.setText(namePlayer2);}
    }
  //for the first run
    private void nameturn1() {
    	playerturn = randplayerstart%2;
    	System.out.println("random geneator:"+randplayerstart);
    	if (playerturn%2==0) {
    		playername=namePlayer1;
    	}
    	else { playername=namePlayer2;}
    }
    
  //method for the scoring loss
  	private void scoreloss() {
    	if (playerturn%2==0) {
        	scoreplayer1 = scoreplayer1-1;}
            if(playerturn%2==1) {
            	scoreplayer2 = scoreplayer2-1;
            }
    }
  	
  	//method for the scoring gain
    private void scoregain() {
    	if (playerturn%2==0) {
        	scoreplayer1 = scoreplayer1+2;}
            if(playerturn%2==1) {
            	scoreplayer2 = scoreplayer2+2;
            }
    }

    //when counter hits 0, game stops.
    private boolean GameEnds(){
    	if(endGameCounter!=0) {
    		return false;
    		
    	}
    	
    	return true;
    }
      
      
      
      
     
    
    
    public static void main(String[] args){
		new Concentration2();
    	
}}
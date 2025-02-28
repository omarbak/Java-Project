import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;





@SuppressWarnings("serial")
public class Concentration extends JFrame {

    /**
	 * 
	 */
	//random number to shift the pictures selected on each round to avoid playing with same pictures
	private int randNum = ThreadLocalRandom.current().nextInt(1,10);
	//name of the theme selected cats cars...
	String themes = MainMenu.themes;
	//name of the player playing
	String namePlayer = MainMenu.namePlayer.toUpperCase();
	//static value for the timer flip card, depends on difficulty level
	int timer1 = MainMenu.timer1;
	//number of rows of tiles
	int rows = Integer.parseInt(MainMenu.rows);
	//number of columns of tiles
	int columns = Integer.parseInt(MainMenu.columns);
	//score penalty depending on difficulty level from mainmenu.
	int scorePenalty = MainMenu.scorePenalty;
	//amount of time allowed per game to finish it
	int countDown=MainMenu.countDown;

	
	
	private List<Card> cards; // automatically assigns JButton
	private Card clickedCard;
    private Card firstCard; //will be considered the first card picked
    private Card secondCard; //will be considered the second card picked
    private Card temporaryCard; //saves firstcard in it to avoid reselecting it on the next turn
    private String sScore="0";
    private String initialTimer;
    //different initial values for variables
    private int score1=0;
    private int counter=0;
    private int shuffleCounter=0;
    //max score someone can get, if reached name will be saved in myscorefile
    private int highestScore = 3*rows*columns/2;
    //number of buttons to be created
    private int pairs = rows*columns;
    
    // score text
    private JTextField SCORESCREEN;
    // timer countdown text
    private JTextField COUNT_DOWN;
    // labels/titles for textfields.
    private JLabel SCORE_SCREEN_LABEL;
    private JLabel COUNT_DOWN12;
    //panel for the score
    private JPanel SCORE_PANEL;
 
    private Timer timer; //flipcard timer
    private Timer stopwatch; // stopwatch
    
    public Concentration(){ 
    	initialTimer=Integer.toString(countDown);
        //start by calling the createButton.
    	createButton();
      //creation of the guiframe with the buttons created previously.
    	panelCreation();
            
    
    }
    /////////////////****************METHODS CREATION*************************\\\\\\\\\\\\\\\\\\\ 
    
    
    /////////////////********************PANEL CREATION*************************\\\\\\\\\\\\\\\\\\\  

    private void panelCreation() {
    	

    	Container mainpanel = getRootPane();
        mainpanel.setLayout(new BorderLayout());
        
    	 //setting the board
        
        Container secondarypanel;
        secondarypanel = getContentPane();
        secondarypanel.setLayout(new GridLayout(rows, columns));
        
        //for loop to place the buttons saved in cards into the panel.
        
        for (int g = 0 ; g<cards.size(); g++){
            secondarypanel.add(cards.get(g));}
        
        //defining the new elements
        SCORE_PANEL = new JPanel();
        SCORE_SCREEN_LABEL = new JLabel(namePlayer+"'s SCORE:");
        COUNT_DOWN12 = new JLabel("TIMER");
        //each textfield gets assigned its value to show
        SCORESCREEN = new JTextField(sScore);
        COUNT_DOWN = new JTextField(initialTimer);
        //avoid direct editing of values, turn it from "dynamic" screen to static
        COUNT_DOWN.setEditable(false);
        SCORESCREEN.setEditable(false);
        //size of the score screen
        SCORESCREEN.setPreferredSize( new Dimension( 50,50 ) );
        // adding to elements panel
        SCORE_PANEL.add(SCORE_SCREEN_LABEL);
        SCORE_PANEL.add(SCORESCREEN);
        SCORE_PANEL.add(COUNT_DOWN12);
        SCORE_PANEL.add(COUNT_DOWN);
        //adding secondary panels to different locations in the main panel
        mainpanel.add(secondarypanel,BorderLayout.CENTER);
        mainpanel.add(SCORE_PANEL,BorderLayout.SOUTH);
        //frame title
        setTitle("Concentration Game");
        //size for frame
        setMinimumSize(new Dimension(600,600));
        setPreferredSize(new Dimension(900,900)); 
        //location on screen
        setLocation(0,0);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter(){ //in case the timer has started and we want to close
        	//otherwise the timer will keep going because dispose doesnt kill the timer.
        	public void windowClosing(WindowEvent e) {
        	try {
        		stopwatch.stop();
        		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        	}catch (Exception ge) {
        		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        	}
        		

        	}
        });
        pack();
        setVisible(true);
        
    }

    
    /////////////////********************CREATE BUTTON*************************\\\\\\\\\\\\\\\\\\\  
    
    private void createButton(){
    	
    	List<Card> cardsList = new ArrayList<Card>();
        List<Integer> cardIDs = new ArrayList<Integer>();
       
        for (int twice = 0 ; twice <= 1 ; twice++) { //creates a list with duplicate number [0,1,2,3,0,1,2,3],
        	 for (int i = 0; i < pairs/2; i++){//catching exception if pairs is N/A
        		 cardIDs.add(i+randNum); //IDs receive a shift due to random number to have different pictures each game
        }}
    		 
        
        Collections.shuffle(cardIDs); //shuffling the future cards emplacement on the grid
       System.out.println("random number generation :"+ randNum); //shows the random number chosen
        System.out.println("your cards IDs after shuffling is:"+cardIDs); //check/cheating
        
       
        for(int z = 0 ; z<cardIDs.size();z++) {
    	Card card = new Card(); //new element type card
    	card.setId(cardIDs.get(z)); //sets IDs to card
        card.setPreferredSize(new Dimension(500, 500)); // card dimension
    	card.setIcon(new ImageIcon(getClass().getResource("gamepictures/11.png"))); // sets picture of card when board starts
    		card.addActionListener(new ActionListener(){ //what happens when a card is clicked 
                public void actionPerformed(ActionEvent ae){
                	clickedCard = card;
                	System.out.println("You're pressing card"+ clickedCard.getId());
                    assignCard(); //assigns the first click to the first card and second click to second card
                    counter++;
                    penalty();
                    System.out.println("counter:  " + counter);    
                }
                
            });
            cardsList.add(card); //cards are collected into cardsList.
        }
        
        this.cards = cardsList;	
    }
    /////////////////********************TIMER STOPWATCH*************************\\\\\\\\\\\\\\\\\\\  

    private void startWatch() {
    	
    	stopwatch = new Timer(1000,new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				countDown--;
		        COUNT_DOWN.setText(Integer.toString(countDown));
        		if(countDown<=0) {
        			 int response =  JOptionPane.showConfirmDialog(null, "TIME IS UP YOU LOST!\n Wanna Play A New Game?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                     
        			 if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) {
                    	 stopwatch.stop();
                         dispose();
                 } 
                     else if (response == JOptionPane.YES_OPTION) {
                  	   dispose();
                  	   stopwatch.stop();
                  	   new Concentration();
                  	   randNum=0;}}
        		else if (countDown!=0) { gameEnds();}}});
    	stopwatch.start();


    }
    
    
    /////////////////********************FLIPPING TIME FOR CARDS*************************\\\\\\\\\\\\\\\\\\\  

    public void timeStart() {
        timer = new Timer(timer1, new ActionListener(){ //timer start once the second card is chosen flipping motion
        	public void actionPerformed(ActionEvent ae){
        		//activates the matching between cards
        		checkMatch();
        	}});
        timer.start();
        timer.setRepeats(false); //fires one time only
    }
   
    
    
    /////////////////******************** PENALTY *************************\\\\\\\\\\\\\\\\\\\  

    public void penalty() {
    	//general penalty once each game, once the player requires more than the click limit to finish the game
    	if(shuffleCounter == 0 && counter >= 2*pairs) {
    	score1= score1 - 5;
    	//message showing that the penalty has been activated
    	JOptionPane.showMessageDialog(null, 
				"YOU RECEIVED A PENALTY OF -5 \n Reason: Not being able to finish the game in limited clicks", 
				"Penalty",JOptionPane.WARNING_MESSAGE);
    	//once shufflecounter gets the new value, the penalty is deactivated 
    	shuffleCounter++;
    	}
    }
    
    
    /////////////////********************ASSIGNS CARDS*************************\\\\\\\\\\\\\\\\\\\  
    // assigns card clicked to a variable card1, and card2+flips the cards to see their values
    public void assignCard(){
    	if (counter==0 && firstCard == null && secondCard == null){
        	startWatch();
    		firstCard = clickedCard;
            String IDval1 = String.valueOf(firstCard.getId()); // assigns a picture from the saved pics
            try {
            	firstCard.setIcon(new ImageIcon(getClass().getResource("gamepictures/"+themes+"/"+IDval1+".jpg")));
            }catch (Exception e) {
            	JOptionPane.showMessageDialog(null, "PICTURE OR FOLDER NOT FOUND");
            }
                       
    	}
    	if (counter!=0 && firstCard == null && secondCard == null){  //if same first card is chosen twice in a row
    		firstCard = clickedCard;
    		if(temporaryCard==firstCard) { JOptionPane.showMessageDialog(null, 
    				"You can't select the same card twice in a row", 
    				"Warning",JOptionPane.WARNING_MESSAGE);
    		counter--; //doesnt take the card clicked twice to the counter.
    		firstCard=null;
    			
    		}

    		else {
    			String IDval1 = String.valueOf(firstCard.getId()); // assigns a picture from the saved pics
                firstCard.setIcon(new ImageIcon(getClass().getResource("gamepictures/"+themes+"/"+IDval1+".jpg")));
    		}
    		
    	}

        else if (firstCard != null && firstCard != clickedCard && secondCard == null){ //assigns value for the second card
            secondCard = clickedCard;
            String IDval2 = String.valueOf(secondCard.getId());
            secondCard.setIcon(new ImageIcon(getClass().getResource("gamepictures/"+themes+"/"+IDval2+".jpg")));
    		//once the second card is chosen the flipping time starts, also checkMatch() starts
            timeStart();


        }}
    
    
    /////////////////********************CHECK MATCH*************************\\\\\\\\\\\\\\\\\\\  

    
    public void checkMatch(){
    	//get the IDs set for each card, then we check if they are the same or not.
        if (firstCard.getId() == secondCard.getId()){//match condition
        	score1 = score1+3; //fixed score rate
        	countDown = countDown+5; // additional bonus on stopwatch every time you match a card
            firstCard.setEnabled(false); //disables the button
            secondCard.setEnabled(false);
            firstCard.setMatched(true); //sets the button to true value. 
            secondCard.setMatched(true);
            //checks if the game has ended
            if (this.gameEnds()){SCORESCREEN.setText(Integer.toString(score1));
            //different conditions if the game has ended
            	if (score1==highestScore) {
                    saveHighScore(); 
                    ////different options of the confirm dialog when the game ends.
            	    int response =  JOptionPane.showConfirmDialog(this, "Congrats! YOU ACHIEVED THE HIGHEST SCORE!\n"+namePlayer+" YOUR SCORE IS: "+Integer.toString(score1)+"\n Start a Second Game?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.NO_OPTION) {
                        dispose();
                } 
                    else if (response == JOptionPane.YES_OPTION) {
                 	   dispose();
                 	   new Concentration();
                 	   randNum=0;
                 	   
                }
            	
            	}
            	else if(score1>0) {
               int response =  JOptionPane.showConfirmDialog(this, "Congrats! YOU WIN THE GAME!\n"+namePlayer+" YOUR SCORE IS: "+Integer.toString(score1)+"\n Start a Second Game?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
               if (response == JOptionPane.NO_OPTION) {
                   dispose();
           } 
               else if (response == JOptionPane.YES_OPTION) {
            	   dispose();
            	   new Concentration();
            	   randNum=0;
            	   
           } 
            
           
            }
            	//what happens when you get a negative score 
            	else if (score1<=0) {
            		int response =  JOptionPane.showConfirmDialog(this, "YOU LOST THE GAME!\n"+"YOUR SCORE IS: "+Integer.toString(score1)+"\n Start a Second Game?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.NO_OPTION) {
                        dispose();
                } 
                    else if (response == JOptionPane.YES_OPTION) {
                 	   dispose();
                 	   new Concentration();
                 	   randNum=0;
                 	   
                } 
            		
            	}
            
            
            }
        }

        else{
        	//if no match, u get -1 on the timer as a penalty, and score penalty
        	countDown=countDown-1;
        	score1 = score1+scorePenalty;
        	//resets the opened cards to the main picture 
            firstCard.setIcon(new ImageIcon(getClass().getResource("gamepictures/11.png")));
            secondCard.setIcon(new ImageIcon(getClass().getResource("gamepictures/11.png")));
            
        }
        // save the first card opened to temporary card after each turn,
        temporaryCard = firstCard;
        firstCard = secondCard = null; //reset c1 and c2 (empties them)
        // scores are updated
        SCORESCREEN.setText(Integer.toString(score1));
        System.out.println("your score is:"+score1);
    }
    /////////////////********************SAVE HIGHSCORE*************************\\\\\\\\\\\\\\\\\\\  
//will save the highscore, if reached, to a file
    private void saveHighScore() {

		try {
			FileWriter fw;
			//opens the file and appends the new information
			fw = new FileWriter("src/myscore.txt",true);
	    	fw.write(namePlayer+" has achieved the highest score in the "+Integer.toString(rows)+"*"+Integer.toString(columns)+" of "+Integer.toString(highestScore)+".\n");
	    	fw.close();
	    	System.out.println("SAVED");
		} catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "High Score File Not Found");

			e.printStackTrace();
		}
    	

    }
    /////////////////********************GAME ENDS*************************\\\\\\\\\\\\\\\\\\\  
    //checks if all the cards have been set to true. if so, the game ends
    private boolean gameEnds(){
        for(int k = 0; k<this.cards.size(); k++){
        if((this.cards.get(k)).getMatched() == false){
        			return false;}
        }
        
        //stopwatch stops when the game is won.
        stopwatch.stop();
        return true;
        
    }
    
    
    public static void main(String[] args){
		new Concentration();
    	
}
}
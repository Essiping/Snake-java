package com.ascendant.snake;

import java.awt.Color;

/* Class used to handle all mechanism and other needed functions 
/*Import all neeeded packages and classes */

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/* To draw a gui frame */

import javax.swing.JFrame;
import javax.swing.Timer;

/* libraries and classes for playing music */

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public final class Snake implements ActionListener, KeyListener {
	
	/* Variables */
	protected int pixls = 0, direction = DOWN, score = 0, tailLength = 7, speed = 8, time, scor,
			tailLengthFirst = 7, tailLengthS = 7;
	protected Point head, cherry;
	
	public JFrame jframe; // create JFrame object
	public WindowRender windowRender; // object for window rendering from WindowRender class
	public Timer timer = new Timer(20, this); 
	public ArrayList<Point> snakeParts = new ArrayList<Point>(); // 
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3,
			SCALE = 10; // directions
	public Random random; // object to generate random cherry positions
	public boolean over = false, paused = false, win = false, show_credits = false,
			egg = false; // to hold game status
	public Dimension dimension; // object to hold component's width and height
	public String cg = "";
	public static Color snakeColor = Color.BLUE, snakeHeadColor = new Color(14705165); // blue, orange
	
	public Snake() { 
		dimension = Toolkit.getDefaultToolkit().getScreenSize(); // .sync(); for linux
		
		/* set JFrame object properties */
		
		jframe = new JFrame("Snake"); // parameter 1 is window name
		jframe.setVisible(true); // setting visibility
		jframe.setSize(805, 700); // window size
		jframe.setResizable(false); // to make it impossible to resize
		
		/* set window location at the middle of screen */
		
		jframe.setLocation(dimension.width / 2 - jframe.getWidth() / 2,
				dimension.height / 2 - jframe.getHeight() / 2);
		jframe.add(windowRender = new WindowRender());
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addKeyListener(this);
		
		startGame(); /* call for start game function */
	}

	public void startGame() { // function sets all variables
		/* this function sets all needed variables */
		over = false; // indicated if game finished or not
		paused = false; //indicates if game paused or not
		time = 0; // play time
		score = 0; // player score
		scor = 0; // player points
		tailLength = 7; // actual tail length; start from 7
		speed = 6; // Speed starting from 8
		// tailLengthFirst = tailLength;
		pixls = 0; // pixls
		direction = DOWN;//snake first direction; depends from start position
		
		random = new Random(); // new random class object
		cherry = (new Point(random.nextInt(79), random.nextInt(66))); 
		head = (new Point(0, -1)); // head starting point
		snakeParts.clear(); // clear snakeParts array; for sure
		// using random object to randomize starting cherry position
		timer.start(); // just timer start
		music(); // starts playing music
	}

	@Override // overriding method from superclass 
	public void actionPerformed(ActionEvent arg0) {
		
		windowRender.repaint(); // repaint window after each action
		pixls++; // with each next have to repaint window
		
		/* this if is checking if game is not over is not paused and 
		 * head didn't cross the frame border
		 */
		
		if(pixls % speed == 0 && head != null && !over && !paused) {
			
			//time++; 
			
			scor++;
			
			/* adds a new point at selected position */
			
			snakeParts.add(new Point(head.x, head.y));  

			/* next for if statements depend where to place next snake
			 * part depending on the current direction selected by the player.
			 * The only arguments that differs are noTailAt(int x, int y) function 
			 * arguments and information on where to place a new point. 
			 * If for example snake crossed window border over variable
			 * is being set to true then the game is over and player see a message
			 */
			
			if(direction == 0) 
				if(head.y - 1 >= 0 && noTailAt(head.x, head.y - 1))
					head = new Point(head.x, head.y - 1);
				else
					over = true;

			if (direction == 1)
				if(head.y + 1 < 67 && noTailAt(head.x, head.y + 1))				
					head = new Point(head.x, head.y + 1);
				else				
					over = true;				

			if(direction == 2)
				if (head.x - 1 >= 0 && noTailAt(head.x - 1, head.y))
					head = new Point(head.x - 1, head.y);				
				else
					over = true;

			if (direction == 3)
				if (head.x + 1 < 80 && noTailAt(head.x + 1, head.y))
					head = new Point(head.x + 1, head.y);
				else
					over = true;

			/* after adding a new snake parts to snakeParts array
			 * need to delete the last one 
			 */
			
			if (snakeParts.size() > tailLength) 
				snakeParts.remove(0);

			// if cherry exists
			
			if (cherry != null)
				// if snake head is in the same place cherry is
				if (head.equals(cherry)) 
				{
					
					musicCherryEaten();
					
					/* this section checks if a player has already eaten
					 *  2 (more?) cherries. If yes then increasing 
					 *  snake speed to make it harder.
					 *  If speed is equal 1 (the fastest) then it doesn't 
					 *  change anymore. Speed can't be under 0.
					 */
					
					if(tailLength - tailLengthFirst >= 2) {
						tailLengthFirst = tailLength;
						if(speed <= 1) // to never let app crash when speed is equal 0
							speed = 1;
						else speed--;
					} 
					
					/* this section sets bigger score after eating each 3 cherries */
					int avc = 10;//avc for keeping current player score with each cherry
					if(tailLength - tailLengthS >= 3) avc+=30;
					
					score += avc;
					tailLength++;
					
					/* checks if the player won. If tail has 93 parts more */
					
					if(tailLength >= 100) { win = true; over = true; }
					
					/* next cherry position */
					
					cherry.setLocation(random.nextInt(79), random.nextInt(66));  
				}
		}
}
	
	public boolean noTailAt(int x, int y) {
		
		/* for loop iterating on an array */
		
		for (Point point : snakeParts) {
			
			/* checks if tail crossed another snake part from snakeParts array */
			
			if (point.equals(new Point(x, y)))
				return false;
		}
		return true;
	}

	/* Determines pressed keys */
	
	@Override // overriding a function from superclass
	public void keyPressed(KeyEvent key) { 		
		//int KeyCode = key.getKeyCode();

		switch(key.getKeyCode()) {
			
			// controls with WASD
			case KeyEvent.VK_A: 
				if(getDirection() != 3) 
					setDirection(2); break;
			
			case KeyEvent.VK_D:
				if(getDirection() != 2) 
					setDirection(3); break;
				
			case KeyEvent.VK_W:
				if(getDirection() != 1)
					setDirection(0); break;
					
			case KeyEvent.VK_S:
				if(getDirection() != 0)
					setDirection(1); break;
			
			// controls with arrows
			case KeyEvent.VK_LEFT:
				if(getDirection() != 3) 
					setDirection(2); break;
			
			case KeyEvent.VK_RIGHT:
				if(getDirection() != 2) 
					setDirection(3); break;
					
			case KeyEvent.VK_UP:
				if(getDirection() != 1)
					setDirection(0); break;
					
			case KeyEvent.VK_DOWN:
				if(getDirection() != 0)
					setDirection(1); break;
			
			// other keys
			case KeyEvent.VK_C:
				paused = !paused;
				if(!show_credits) show_credits = true;
				else show_credits = false;	break;
				
			case KeyEvent.VK_Q:
				if(over) System.exit(0);
				else over = true; break;
				
			case KeyEvent.VK_SPACE:
				// or over == true
				if (over) startGame();  // starts a new game if space clicked
				else paused = !paused; // change from true -> false and false -> true
		} /// switch end
		
		// egg function. not calling if egg == true 
		if(!egg) checkOtherElements(key.getKeyChar()); 
	}
	
	
	@SuppressWarnings("restriction")
	private void music() {       
	        AudioPlayer MGP = AudioPlayer.player;
	        AudioStream BGM;
	        AudioData MD;

	        ContinuousAudioDataStream loop = null;

	        try {
	        	
	        	/* Getting input stream for AudioStream constructor by
	        	 * opening  a music file.
	        	 */
	        	
	            InputStream music = new FileInputStream("main.wav");
	            
	            /* Audio stream subclass extends InputStream. 
	             * Get the input stream as a parameter.
	             * Can throw an IOException so catching it.
	             */
	            
	            BGM = new AudioStream(music); 
	            AudioPlayer.player.start(BGM);
	            MD = BGM.getData();
	            
	            /* ** ContinuousAudioDataStream ** this constructor creates a
	             * continuous audio data stream from audio data. Here MD variable 
	             * holds it (data).   
	             */
	            
	            loop = new ContinuousAudioDataStream(MD);
	        }
	        
	        /* exception thrown if file does not exist; 
	         * if it is a directory or 
	         * if cannot be opened for reading for some reason
	         */
	        
	        catch(FileNotFoundException fileNotFoundExcept) { 
	            System.err.print(fileNotFoundExcept.toString()); // standard error out printing
	        }
	        
	        catch(IOException ioExcept) { 
	            System.err.print(ioExcept.toString()); // standard error out printing
	        }
	        
	        MGP.start(loop); // to start and play music in a loop
	    }
	
	@SuppressWarnings("restriction") // to hide warnings about methods not in default api 
	private void musicCherryEaten() { 
		
	        AudioPlayer MGP = AudioPlayer.player;
	        AudioStream BGM;
	        // AudioData MD; /* variable needed to loop music */

	        ContinuousAudioDataStream loop = null;

	        try {
	            InputStream music = new FileInputStream(
	            		"eat.wav");
	            BGM = new AudioStream(music);
	            AudioPlayer.player.start(BGM);
	            
	            /* this section to loop music */
	            
	            // MD = BGM.getData();
	            // loop = new ContinuousAudioDataStream(MD);
	        }
	        
	        catch(FileNotFoundException fileNotFoundExcept) {
	            System.err.print(fileNotFoundExcept.toString()); // standard error out print
	        }
	        
	        catch(IOException ioExcept) {
	            System.err.print(ioExcept.toString());
	        }
	        
	        MGP.start(loop);
	    }
	
	private void checkOtherElements(int k) {
		if(!cg.equals("")) { // checks if not null
			if(cg.equals("i") || cg.equals("in") || cg.equals("inf") ||
					cg.equals("infe") || cg.equals("infeg") || cg.equals("infegg"))
						cg = cg; // :D // to correct mistakes in typing
			
			// TODO check all parts of string
			else if(("i".equals(String.valueOf(cg.charAt(0)))) &&
					(String.valueOf(cg.charAt(1)) != "n")) cg = "";
			// else if(cg.toLowerCase().contains("in") && String.valueOf(cg.charAt(2)) != "f")

			
		}
		
		if(cg.equals("infegg") && cg.length() == 6) /* infegg */ {
			egg = true; // 
			tailLength += 40;
			speed = 1;
			snakeHeadColor = Color.black;
			snakeColor = new Color(59110);
		}
		else {
			if(k == 105 /*i*/ || k == 73 /*I*/) cg += String.valueOf((char) k); 
			if(k == 110 /*n*/ || k == 78 /*N*/) cg += String.valueOf((char) k); 
			if(k == 102 /*f*/ || k == 70 /*F*/ ) cg += String.valueOf((char) k); 
			if(k == 101 /*e*/ || k == 69 /*E*/) cg += String.valueOf((char) k); 
			if(k == 103 /*g*/ || k == 71 /*G*/) cg += String.valueOf((char) k); 
		}
	}
	
	// this methods not needed exist only cause required by KeyListener
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	// getters and setters

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getScore() {
		return score;
	}

	public int getTailLength() {
		return tailLength;
	}

	public int getScor() {
		return scor;
	}

	public Point getHead() {
		return head;
	}

	public Point getCherry() {
		return cherry;
	}
} /// END

/* Created by Ascendant
 * Real programmers don't comment their code cause if it was hard
 * to write it must be hard to read :D 
 */


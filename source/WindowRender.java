package com.ascendant.snake;

/* Class to render all elements and text 
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

/* suppress warnings for hiding warnings
 * serial to suppress warnings relative
 * to missing serialVersionUID field for a  class
 */

@SuppressWarnings("serial")
public final class WindowRender extends JPanel {

	final String win = "Congratulations! You won! :)",
		   crd1 = "Music by Kevin Macleod \"Cipher\"",
		   endInfo = "Game Over! Q to exit.",
		   egg = "Inf egg!",
		   pauseInfo = "Paused! Click space to continue.";
	
	final Color green = new Color(1666073); // background
	
	protected void cl(Graphics g, Color color) {
		g.setColor(color);
	}
	
	protected void ft(Graphics g, Font font) {
		g.setFont(font);
	}
	
	protected void WriteString(Graphics g, Color color, String stringToPrint, 
			Font font, int xPos, int yPos) {
		
		ft(g, font); // sets font
		cl(g, color); // sets color
		
		/* string String, int x, int y
		 *  drawString draws a string on the screen. first parameter
		 *  is the string to draw next 2 is position
		 *  the function takes int type parameters so have to implicitly convert
		 *  it: means (int) x
		 */
		
		g.drawString(stringToPrint, xPos, yPos);
	}
	
	@Override // overrides a method from superclass 
	protected void paintComponent(Graphics g) { 
		
		Snake snake = Main.getSnake(); // getting var from main class
		super.paintComponent(g);
		
		cl(g, green); // setting color green
		g.fillRect(0, 0, 800, 700); // fills the rectangle frame with green color
		
		cl(g, snake.snakeHeadColor); // printing snake head
		g.fillRect(snake.getHead().x * Snake.SCALE, snake.getHead().y * Snake.SCALE,
				Snake.SCALE, Snake.SCALE);
		
		// change color to blue to print the snake's part 
		cl(g, snake.snakeColor); // setting color blue for printing snake
		for (Point point : snake.snakeParts) // printing snake parts in loop iterating on an array of points
			g.fillRect(point.x * Snake.SCALE, point.y * Snake.SCALE,
					Snake.SCALE, Snake.SCALE); 
		
		/* change color to red to print new cherry then print a red point
		 * new cherry's location set in actionPerformed(ActionEvent arg0) function
		 */
		
		cl(g, Color.RED); // color red to print cherry
		g.fillRect(snake.getCherry().x * Snake.SCALE, snake.getCherry().y * Snake.SCALE,
				Snake.SCALE, Snake.SCALE);
		
		// strings to print at frame's top 
		
		String info = "Score: " + snake.getScore() + ", Length: " + snake.getTailLength() + 
				" Points: " + snake.getScor(), /* + ", String: " + snake.cg */ 
		
	            infoAfterEgg = "Score: Egg" + ", Length: " + snake.getTailLength() + 
				" Points: Egg";
		
		/* Font in which to print the info String 
		 * using arial cause other fonts may not be available in different OS 
		 */
		
		if(!snake.egg) WriteString(g, Color.white, info, new Font("Arial", 2, 22),
							(int) (getWidth() / 2.5 - info.length() * 2.5f), 15);
		
		else WriteString(g, Color.white, infoAfterEgg, new Font("Arial", 2, 22),
				  (int) (getWidth() / 2.5 - infoAfterEgg.length() * 2.5f), 15);
		
		if(snake.over && snake.win) 
			WriteString(g, Color.red, win, new Font("Arial", 1 , 40), 
					(int) (getWidth() / 4 - win.length() * 2.5f), 
					(int) snake.dimension.getHeight() / 5);
		
		if(snake.show_credits && snake.paused && !snake.over) 		
			WriteString(g, new Color(5963867), crd1, new Font("Arial", 1, 32),
					(int) (getWidth() / 4 - crd1.length() * 2.5f),
					(int) snake.dimension.getHeight() / 5);
		
		if (snake.over)  
			WriteString(g, Color.red, endInfo, new Font("Arial", 1, 34),
					(int) (getWidth() / 3 - endInfo.length() * 2.5f),
					(int) snake.dimension.getHeight() / 4);

		if (snake.paused && !snake.over) 
			WriteString(g, Color.orange, pauseInfo, new Font("Arial", 1, 25),
					(int) (getWidth() / 2.8 - pauseInfo.length() * 2.5f),
					(int) snake.dimension.getHeight() / 4);
		
		if(snake.egg) 		
			WriteString(g, Color.blue, egg, new Font("Arial", 1, 25),
					(int) (getWidth() / 2.2 - egg.length() * 2.5f),
					(int) snake.dimension.getHeight() / 3);
		
	}
} /// END

//Created by Ascendant


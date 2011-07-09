/*
 * File: Breakout.java
 * -------------------
 * Name: Carlos Folgar
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {
	


/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;
	
/** Diameter of the ball in pixels */
	private static final int BALL_DIAM = BALL_RADIUS * 2;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setup();
		play();
		
	}
	
	/** This method will only focus on setting up the game. */
	public void setup() {
		
		setBackground(Color.BLACK);
		// The following (2) nested loops will set up all of the colored bricks on the screen.
		for ( int j = 0; j < NBRICK_ROWS; ++j) {
			
					
			for ( int i = 0; i < NBRICKS_PER_ROW; ++i) {
				// Create a brick.
				GRect Brick = new GRect(BRICK_WIDTH*i + BRICK_SEP*i + (getWidth() -((NBRICKS_PER_ROW*BRICK_WIDTH) + (NBRICKS_PER_ROW-1)*BRICK_SEP))/2, BRICK_Y_OFFSET + (j*BRICK_HEIGHT)+ (j * BRICK_SEP), BRICK_WIDTH, BRICK_HEIGHT);
				// Set the color of the bricks according to their row - designated by the value of "j" in our loop.
				if ( j > NBRICK_ROWS - 3 ) 							brickColor = Color.RED;
				if ( j > NBRICK_ROWS - 5 && j < NBRICK_ROWS - 3 ) 	brickColor = Color.ORANGE;
				if ( j > NBRICK_ROWS - 7 && j < NBRICK_ROWS - 5) 	brickColor = Color.YELLOW;
				if ( j > NBRICK_ROWS - 9 && j < NBRICK_ROWS -7) 	brickColor = Color.GREEN;
				if ( j < NBRICK_ROWS - 9) 							brickColor = Color.CYAN;
				
				Brick.setColor(brickColor);
				Brick.setFilled(true);
				add(Brick);
				/* Records the x-coordinate of the first brick as "xStart" */
				if ( i == 0){
					xStart = (int)Brick.getX();
				}
				
				/* Records the x-coordinate of the last brick as "xFinish" */
				if ( i == NBRICKS_PER_ROW-1) {
					xFinish = (int)Brick.getX() + BRICK_WIDTH;
				}
				
			}
		}
		
		scoreKeeper = new GLabel("Breakout has started!");
		scoreKeeper.setFont("SansSeriff-18");
		scoreKeeper.setColor(Color.RED);
		add(scoreKeeper, 25,50);
		
		
		
		// The following code will setup the paddle on the screen.
		paddle = new GRect( getWidth()/2, APPLICATION_HEIGHT-PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setColor(Color.RED);
		paddle.setFilled(true);
		add(paddle);
		
		
		
	}	
	
	
	// This listeners allows us to move the paddle to wherever our cursor is - as long as it is withing the application width.
		public void mouseMoved(MouseEvent e) {
			
			if (  e.getX() <= getWidth()/2 + APPLICATION_WIDTH/2 - PADDLE_WIDTH && e.getX() >= getWidth()/2 - APPLICATION_WIDTH/2) {
				
				//if ( last == null) last = new GPoint(getWidth()/2 + PADDLE_WIDTH/2, APPLICATION_HEIGHT-PADDLE_Y_OFFSET);
				if (last == null){
					last = new GPoint(e.getX(),APPLICATION_HEIGHT-PADDLE_Y_OFFSET);
					paddle.setLocation(last);
				}
				paddle.move(e.getX() - (int)last.getX() , 0);
				pause(20);
				last = new GPoint(e.getPoint());
			}
		}
		 
		/** This methods provides the actual interactive playing of the game. */
		public void play() {
			
			gameOver = false;
			/* Create the playing ball */
			ball = new GOval(getWidth()/2, APPLICATION_HEIGHT/2, BALL_RADIUS*2,BALL_RADIUS*2 );
			ball.setColor(Color.WHITE);
			//ball.setFillColor(Color.WHITE);
			ball.setFilled(true);
			add(ball);
			
			// Draw walls that surround the gameboard
			GRect walls = new GRect((getWidth() -((NBRICKS_PER_ROW*BRICK_WIDTH) + (NBRICKS_PER_ROW-1)*BRICK_SEP))/2 - 2 ,  0, APPLICATION_WIDTH , APPLICATION_HEIGHT);
			walls.setColor(Color.WHITE);
			add(walls);
			addMouseListeners();
			
			
			
			// We initialize the velocity variables.
			vy = 3.0;
			vx = 3.0;
			//
			if (rgen.nextBoolean(.5)) vx = -vx;
			brickCounter = 0;
			
			 while (gameOver == false) {
				 
				 
				 ball.move(vx,vy);
				 
				 
				 if ( ball.getX() >= xFinish - BALL_DIAM  ) vx = -vx;
				 if ( ball.getX() <= xStart ) vx=-vx;
				 if ( ball.getY() >= APPLICATION_HEIGHT- BALL_DIAM) vy = -vy;
				 if ( ball.getY() <= 0) vy=-vy;
				 pause(12); 
				 
				 // Check upper left corner of the ball
				 collider = getCollidingObject(ball.getLocation());
				 if (collider != null) checkContact(collider);
				 
				 //Check upper right corner of the ball
				 
				 collider2 = getCollidingObject(new GPoint(ball.getX() + (double)BALL_DIAM , ball.getY()));
				 if ( collider2 != null ) checkContact(collider2);
				 
				 // Check lower right corner of the ball
				 collider3 = getCollidingObject(new GPoint(ball.getX() + (double)BALL_DIAM + 1, ball.getY()+(double)BALL_DIAM + 1));
				 if ( collider3 != null ) checkContact(collider3);
				 
				 // Check lower left corner of the ball
				 collider4 = getCollidingObject(new GPoint(ball.getX() + 1, ball.getY()+(double)BALL_DIAM +1));
				 if ( collider4 != null ) checkContact(collider4);
				 
				 // Check very bottom of the ball
				 collider5 = getCollidingObject(new GPoint(ball.getX() + 1 + (double)BALL_RADIUS, 1+ ball.getY()+(double)BALL_DIAM));
				 if ( collider5 != null ) checkContact(collider5);
				 
				 // Checks area between lower left corner and bottom center of the ball.
				 collider6 = getCollidingObject(new GPoint(ball.getX() + (double)BALL_RADIUS/2 + 1, ball.getY()+(double)BALL_DIAM + 1));
				 if ( collider6 != null ) checkContact(collider6);
				 
				 // Checks area between lower right corner and bottom center of the ball.
				 collider7 = getCollidingObject(new GPoint(ball.getX() + (double)BALL_DIAM*.75 + 1, ball.getY()+(double)BALL_DIAM + 1));
				 if ( collider7 != null ) checkContact(collider7);
				 
				 
				 
				 
				 
				 
				 
			 }
			
			 
			
		}
		
		
		
		/* public GPoint getUpperRight(GObject ball) {
			
			GPoint upperRightCorner = new GPoint(ball.getX() + (double)BALL_DIAM , ball.getY());
			return upperRightCorner;
		}
		*/
		
		public void checkContact( GObject collider) {
			
			
				 if ( collider == paddle){
					 vy=-vy;
					 
				 }
				 else if ( collider == ball){
					vy=vy; 
				 }
				 else if ( collider != null){
					 
					 remove (collider);
					 vy=-vy;
					 brickCounter+=1;
					 updateScore();
					 
					 // If all bricks are broken its game over
					 if (brickCounter == NBRICK_ROWS * NBRICKS_PER_ROW) {
						 scoreKeeper.setLabel("YOU HAVE REACHED THE MAXIMUM POINTS - YOU HAVE WON!");
						 //pause(3000);
						 gameOver = true;
						 
					 }
				 }
		}
			 
			
		
		public void updateScore() {
			scoreKeeper.setLabel("Your score is: " + brickCounter*10 + " points!");
			
			
		}
		
		
		public GObject getCollidingObject (GPoint ballLocation) {
			
			if ( ballLocation.getY() > APPLICATION_HEIGHT-PADDLE_Y_OFFSET + PADDLE_HEIGHT*2) {
				gameOver = true;
				scoreKeeper.setLabel("GAME OVER - YOU LOST!");
			}
			
			colliderX = getElementAt(ballLocation);
			
			
			
			return colliderX;
			
			
			
		}
		
		/* Private instance variables */
		private  int xStart;
		private int xFinish;
		private Color brickColor;
		private GRect paddle;
		private GPoint last;
		private double vx;
		private double vy;
		private RandomGenerator rgen = new RandomGenerator();
		private int brickCounter;
		private GObject collider;
		private GObject collider2;
		private GObject collider3;
		private GObject collider4;
		private GObject collider5;
		private GObject collider6;
		private GObject collider7;
		private GObject colliderX;
		private boolean gameOver;
		private GLabel scoreKeeper;
		private GOval ball; 
		//private GPoint upperRightCorner = new GPoint(ball.getX() + (double)BALL_DIAM + 1, ball.getY()+1);
		//= new GOval(getWidth()/2, APPLICATION_HEIGHT/2, BALL_RADIUS*2,BALL_RADIUS*2 );
		//private boolean insidePaddle = ( ball.getX()) 
}

		
			
	
	


	
		


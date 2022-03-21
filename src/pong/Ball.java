package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball {
	public double x, y, xInit, yInit;
	public int width, height;
	
	public double dx,dy;
	public double speed = 3;
	
	/*
	 * variable responsible to define which side the ball will go after a point is scored.
	 * if false, the ball goes to the left side
	 * if true, the ball goes to the right side
	 */
	private boolean player2Turn;
	
	// sets a direction angle to the ball based on player2Turn when the round starts
	private void setInitialDirection(boolean p2Turn) { 
		int angle;
		if (p2Turn) { // if player2Turn = true
			// generates an angle between 10 and 40 to the left side
			angle = new Random().nextInt(31) + 140; 
		} else { 	  // if player2Turn = false
			// generates an angle between 10 and 50 to the right side
			angle = new Random().nextInt(41) + 10; 
		}
		dx = Math.cos(Math.toRadians(angle));
		dy = Math.sin(Math.toRadians(angle));
	}
	
	/*
	 * setDirectionOnHit sets a reflection angle (between 0 and 45) based 
	 * on the distance between the ball and the center of the racket.
	 * If distance > 0, it means that the ball will be hit up
	 * If distance < 0, it means that the ball will be hit down
	 * If distance == 0, it means that the ball will be hit in a straight line
	 */	
	private void setDirectionOnHit(double dist, double xPos) { 
		double angleRatio = 45/12;
		double angle = dist * angleRatio;
		
		// if its the player1 paddle
		if (xPos > 75) 
			angle += 180;
		
		dx = Math.cos(Math.toRadians(angle));
		dy = Math.sin(Math.toRadians(angle));
		
		// if its the player2 paddle
		if (xPos < 75) 
			dy *= -1;	
	}
	
	// changes the ball speed if the player is moving the paddle
	private void detectMovement(boolean up, boolean down) {
		if (up == true || down == true) { 	// if movement is detected
			speed = 4; 
		} else { 							// if movement is not detected
			speed = 3;
		}
	}
	
	// put the ball at its initial position
	private void repositionBall() {
		x = xInit;
		y = yInit;
		setInitialDirection(player2Turn);	
		speed = 3;
	}
	
	public Ball(double x, double y) {
		xInit = x;
		yInit = y;
		this.x = xInit;
		this.y = yInit;
		width = 5;
		height = 5;
		setInitialDirection(player2Turn);
	}
	
	/**
	 * Responsible for the Ball logic.
	 */
	public void tick() {
		// verification of collision in the inferior and superior bound of the game
		if (y + (dy*speed) + height >= Game.HEIGHT) {
			Sound.play(1);					// play hit_obstacle sound		
			dy *= -1;
		} else if (y + (dy*speed) <= 0) {
			Sound.play(1);					// play hit_obstacle sound	
			dy *= -1; 
		}
		// verification if the ball passed the players vertical limits		
		if ((x <= -width && x >= -width-speed) || (x >= Game.WIDTH && x <= Game.WIDTH + speed)) {
			Sound.play(2);					// play point_marked
		}
		if (x - 10*width > Game.WIDTH) { // player 2 point
			Game.player2.score++;
			player2Turn = true; 			// the ball will restart going to the player2 side
			repositionBall();
		} else if (x + 10*width < 0) { // player 1 point
			Game.player.score++;
			player2Turn = false; 			// the ball will restart going to the player1 side
			repositionBall();
		}
		
		//specifying the bounds of the ball and the paddles
		Rectangle boundsBall = new Rectangle
				((int)(x + (dx*speed)), (int)(y + (dy*speed)), width, height);
		Rectangle boundsPlayer = new Rectangle
				((int)Game.player.x, (int)Game.player.y, Game.player.width, Game.player.height);
		Rectangle boundsPlayer2 = new Rectangle
				((int)Game.player2.x, (int)Game.player2.y, Game.player2.width, Game.player2.height);
		// verification of collision between the ball and a paddle
		if (boundsBall.intersects(boundsPlayer)) {
			Sound.play(0);					//
			setDirectionOnHit((Game.player.y + Game.player.height/2) - (y + height/2), x);
			detectMovement(Game.player.up, Game.player.down);
		} else if (boundsBall.intersects(boundsPlayer2)) {
			Sound.play(0);
			setDirectionOnHit((Game.player2.y + Game.player2.height/2) - (y + height/2), x);
			if (!Game.singleplayer)
				detectMovement(Game.player2.up, Game.player2.down);				
		}
		// change the x and y coordinates of the ball
		x += dx*speed;
		y += dy*speed;
	}
	
	/**
	 * Responsible for rendering the Ball.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect((int)x, (int)y, width, height);
	}
}

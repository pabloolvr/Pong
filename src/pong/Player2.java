package pong;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents Player2, that can be controlled by a real person or the computer.
 */
public class Player2 {
	public int width = 5; 
	public int height = 25;
	public int score;
	public double x, y;
	public boolean up, down;
	
	private int speed = 3;
	
	/*
	 * automatically controls the paddle if game Mode is 1-player
	 * the controlling system is based on the position of the ball
	 */
	private void automaticControl() {
		if (Game.gameDifficulty == 0) {         // if game difficulty is beginner
			y+= (Game.ball.y - 10 - y) * 0.15;
		} else if (Game.gameDifficulty == 1) {  // if game difficulty is intermediate
			y+= (Game.ball.y - 10 - y) * 0.16;
		} else{									// if game difficulty is expert
			y+= (Game.ball.y - 10 - y) * 0.17;
		}
	}
	
	public Player2(double x, double y) { // sets size and initial position of player2
		this.x = x;
		this.y = y;
		width = 5;
		height = 25;
		score = 0;
	}
	
	/**
	 * Responsible for the Player2 logic.
	 */
	public void tick() {
		if (Game.singleplayer) {
			automaticControl(); // control the player2 set automatically based on the position of the ball
		} else {
			// movement control
			if (up) {
				y-=speed;
			} else if (down) {
				y+=speed;
			}			
		}
		// detection of collision in the inferior and superior bound of the game
		if (y + height > Game.HEIGHT) {
			y = Game.HEIGHT - height;
		} else if (y < 0) {
			y = 0;
		}
	}
	
	/**
	 * Responsible for rendering Player2.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fillRect((int)x, (int)y, width, height);
	}
}

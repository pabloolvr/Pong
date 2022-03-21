package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Player {
	public int width = 5; 
	public int height = 25;
	public int score;
	public double x, y;
	public boolean up, down;
	
	private double speed = 3;
	
	public Player (double x, double y) { // sets size and initial position of player1
		this.x = x;
		this.y = y;
		score = 0;
		width = 5;
		height = 25;
	}
	
	/**
	 * Responsible for the Player1 logic.
	 */
	public void tick() {
		// movement control
		if (up) {
			y-=speed;
		} else if (down) {
			y+=speed;
		}
		// detection of collision in the inferior and superior bound of the game
		if (y + height > Game.HEIGHT) {
			y = Game.HEIGHT - height;
		} else if (y < 0) {
			y = 0;
		}
	}
	
	/**
	 * Responsible for rendering Player.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect((int)x, (int)y, width, height);
	}
	
}

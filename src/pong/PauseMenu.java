package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class PauseMenu {
	private String[] menuOptions = {"Resume","New Game","Main Menu","Quit"};
	
	private int currentOption = 0;
	private int maxOption = menuOptions.length - 1;
	
	/**
	 * Responsible for the Pause Menu logic.
	 */
	public void tick() {
		// options browsing system
		if (Game.up) {
			Game.up = false;
			currentOption--;
			if (currentOption < 0)
				currentOption = maxOption;
		}
		if (Game.down) {
			Game.down = false;
			currentOption++;
			if (currentOption > maxOption)
				currentOption = 0;
		}
		
		if (Game.enter) {
			Game.enter = false;
			if (menuOptions[currentOption] == "Resume") {
				Game.gameState = 0; 							// gameState = Game Running
			} else if (menuOptions[currentOption] == "New Game") {
				new Game();
				Game.gameState = 2;								// gameState = Selection Menu
				return;
			} else if (menuOptions[currentOption] == "Main Menu") {
				new Game();
				Game.gameState = 1;								// gameState = Main Menu
			} else {
				System.exit(1);									// if the selected option is Quit
			}
		}
	}
	
	/**
	 * Responsible for rendering the Pause Menu.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render(Graphics g) {
		// menu background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);		
		g.setColor(Color.white);
		// game title
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("PAUSED", Game.WIDTH/4, 35);
		// menu options
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString("     Resume", Game.WIDTH/3 + 1, 75);
		g.drawString("   New Game", Game.WIDTH/3, 85);
		g.drawString("   Main Menu", Game.WIDTH/3, 95);
		g.drawString("         Quit", Game.WIDTH/3, 105);
		
		g.setColor(Color.red);
		if (menuOptions[currentOption] == "Resume") {
			g.drawString("     Resume", Game.WIDTH/3 + 1, 75);
		} else if (menuOptions[currentOption] == "New Game") {
			g.drawString("   New Game", Game.WIDTH/3, 85);
		}else if (menuOptions[currentOption] == "Main Menu") {
			g.drawString("   Main Menu", Game.WIDTH/3, 95);
		} else { // if the selected option is Quit
			g.drawString("         Quit", Game.WIDTH/3, 105);		
		}
	}
	
}

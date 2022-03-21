package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class SelectionMenu {
	private String[] menuOptions = {"Singleplayer", "Multiplayer"};
	private String[] gameDifficultyOptions = {"Beginner", "Intermediate", "Expert"};
	
	private int currentOption = 0;
	private int maxOption = menuOptions.length - 1;
	
	private boolean gameModeSelection = true;
	private boolean difficultySelection;
	private boolean instructionScreen;
	
	/**
	 * Responsible for the Selection Menu logic.
	 */
	public void tick() {
		if (!instructionScreen) {
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
				if (gameModeSelection) {
					if (menuOptions[currentOption] == "Singleplayer") {
						gameModeSelection = false;
						Game.singleplayer = true;
						difficultySelection = true;
						currentOption = 0;
						maxOption = gameDifficultyOptions.length - 1;
					} else if (menuOptions[currentOption] == "Multiplayer") {
						gameModeSelection = false;
						Game.singleplayer = false;
						instructionScreen = true;
					}				
				} else if (difficultySelection) {
					difficultySelection = false;
					if (gameDifficultyOptions[currentOption] == "Beginner") {
						Game.gameDifficulty = 0;				// set game difficulty to beginner
					} else if (gameDifficultyOptions[currentOption] == "Intermediate") {
						Game.gameDifficulty = 1;				// set game difficulty to beginner
					} else {
						Game.gameDifficulty = 2;				// set game difficulty to expert
					}
					instructionScreen = true;
				}
			}
		} else { // if instructionScreen = true
			if (Game.enter) {
				Game.enter = false;
				Game.gameState = 0; 							// set gameState to Running
			}
		}			
	}
	
	/**
	 * Responsible for rendering the Selection Menu.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render(Graphics g) {
		// selection menu background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);		
		g.setColor(Color.white);
		
		if (gameModeSelection) { // shows the gameModeSelection screen
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("SELECT GAME MODE:", Game.WIDTH/5, Game.WIDTH/6);		
			// game Mode list
			g.drawString("1 PLAYER", Game.WIDTH/3, Game.HEIGHT/2 + 5);	
			g.drawString("2 PLAYER", Game.WIDTH/3, Game.HEIGHT/2 + 20);
			
			g.setColor(Color.red);
			if (menuOptions[currentOption] == "Singleplayer") {
				g.drawString("1 PLAYER", Game.WIDTH/3, Game.HEIGHT/2 + 5);
			} else {
				g.drawString("2 PLAYER", Game.WIDTH/3, Game.HEIGHT/2 + 20);				
			}			
		} else if (difficultySelection) { // shows the difficultySelection screen
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("   CHOOSE YOUR", Game.WIDTH/4, Game.HEIGHT/6);		
			g.drawString("DIFFICULTY LEVEL:", Game.WIDTH/4, Game.HEIGHT/6 + 12);	
			// difficulty list
			g.setFont(new Font("Arial", Font.BOLD, 10));
			g.drawString("BEGINNER", Game.WIDTH/3, Game.HEIGHT/2 + 10);	
			g.drawString("INTERMEDIATE", Game.WIDTH/3, Game.HEIGHT/2 + 20);
			g.drawString("EXPERT", Game.WIDTH/3, Game.HEIGHT/2 + 30);
			
			g.setColor(Color.red);
			if (gameDifficultyOptions[currentOption] == "Beginner") {
				g.drawString("BEGINNER", Game.WIDTH/3, Game.HEIGHT/2 + 10);
			} else if (gameDifficultyOptions[currentOption] == "Intermediate") {
				g.drawString("INTERMEDIATE", Game.WIDTH/3, Game.HEIGHT/2 + 20);				
			} else {
				g.drawString("EXPERT", Game.WIDTH/3, Game.HEIGHT/2 + 30);			
			}
		} else if (instructionScreen) {
			// instructions screen
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("INSTRUCTIONS", Game.WIDTH/4 + 10, 10);
			if(Game.singleplayer) {
				g.drawString("  ONE PLAYER:", Game.WIDTH/4 + 10, 22);
				// instructions text
				g.setFont(new Font("Arial", Font.PLAIN, 9));
				g.drawString("Control the paddle on the right side of the", 1, 40);
				g.drawString("screen using | K | to move the paddle up and", 1, 49);
				g.drawString("| M |  to move the paddle down.", 1, 58);
				g.drawString("Press Esc to pause the game.", 1, 75);
				g.drawString("First player to reach 9 points wins the game.", 1, 93);
				// final instruction to leave the instructionScreen 
				g.setFont(new Font("Arial", Font.BOLD, 11));
				g.drawString("PRESS ENTER TO PLAY!", Game.WIDTH/5, Game.HEIGHT - 8);
			} else {
				g.drawString(" TWO PLAYER:", Game.WIDTH/4 + 10, 22);
				// instructions text
				g.setFont(new Font("Arial", Font.PLAIN, 9));
				g.drawString("Player 1 controls the paddle on the right side", 1, 40);
				g.drawString("using | K | to move the paddle up and", 1,49);
				g.drawString("| M |  to move the paddle down.", 1, 58);
				g.drawString("Player 2 controls the paddle on the left side", 1, 74);
				g.drawString("using | A | to move the paddle up and", 1, 83);
				g.drawString("| Z |  to move the paddle down.", 1, 92);
				g.drawString("Press Esc to pause the game.", 1, 108);
				g.drawString("First player to reach 10 points wins the game.", 1, 124);
				// final instruction to leave the instructionScreen 
				g.setFont(new Font("Arial", Font.BOLD, 11));
				g.drawString("PRESS ENTER TO PLAY!", Game.WIDTH/5, Game.HEIGHT - 8);				
			}
		}
	}
}

package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class EndGameMenu {
	private String[] menuOptions = {"Play Again", "Save Score", "Return"};
	private String[] saveScoreOptions = {"Confirm", "Cancel"};
	
	private int currentOption1 = 0; 							// options control for menuOptions
	private int maxOption1 = menuOptions.length - 1;
	
	private int currentOption2 = 0;								// options control for saveScoreOptions
	private int maxOption2 = saveScoreOptions.length - 1;	
	
	/* 
	 * stores the name typed by the user during the saveScoreScreen
	 */
	private String inputName;									
	
	private boolean scoreSaved;
	public boolean saveScoreScreen;
	
	/**
	 * Responsible for getting input from the keyboard for inputName
	 * @param e Key pressed.
	 */
	public void writeName(KeyEvent e) {
		if (e.getKeyCode() >= 0x41 && e.getKeyCode() <= 0x5A) { // detects only letters from a/A to z/Z
			if (inputName.length() < 10)
				inputName += e.getKeyChar();
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && Game.endMenu.inputName.length() > 0) {
			inputName = inputName.substring(0, inputName.length() - 1);
		}
	}
	
	/**
	 * This method removes the oldest score saved from the scores file
	 * since the best scores list only shows the 7 most recent scores saved.
	 */
	private void removeOldScores(String path) {
		File file = new File(path);
		String data = null;	
		
		try { // read the current data
		      Scanner reader = new Scanner(file);
		      while (reader.hasNextLine()) {
		    	  data = reader.nextLine();
		      }
		      reader.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    }
		
		data = data.substring(9*15, data.length());				// stores the new data (6 most recent scores)
	    file.delete();											// deletes the old file
	    
		try {
			file.createNewFile();								// creates a new file
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	    try { // write the new data in the new file
	        FileWriter writer = new FileWriter(path);
	        writer.write(data);
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Writes score to a.txt file.
	 */
	private void writeScore(String path) {
		
		String absolutePath = Paths.get(path).toAbsolutePath().toString();
		
		System.out.println(absolutePath);
		
		File file = new File(absolutePath);
		
		int fileScores = (int)file.length() / 15;				// number of scores written
		// remove unused data from the file if there is 15 scores written
		if (fileScores == 15)
			removeOldScores(absolutePath);
		
		int nameLen = inputName.length();
		// makes a string of size 10
		if (nameLen < 10) {
			for (int i = 0; i < 10 - nameLen; i++) {
				inputName += ' ';
			}
		}
		
	    try {
	        FileWriter writer = new FileWriter(absolutePath, true);
	        writer.write(inputName);
	        writer.write("-");
	        writer.write(Game.player.score + "");
	        writer.write("-");
	        writer.write(Game.player2.score + "");
	        //writer.write(Game.player2.score + "\n");
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Responsible for the End Game Menu logic.
	 */
	public void tick() { // responsible for the logic
		if (!saveScoreScreen) {
			// options browsing system
			if (Game.up) {
				Game.up = false;
				currentOption1--;
				if ((scoreSaved || !Game.singleplayer) && currentOption1 == 1)
					currentOption1--;
				
				if (currentOption1 < 0)
					currentOption1 = maxOption1;
			}
			if (Game.down) {
				Game.down = false;
				currentOption1++;
				if ((scoreSaved || !Game.singleplayer) && currentOption1 == 1)
					currentOption1++;
				
				if (currentOption1 > maxOption1)
					currentOption1 = 0;
			}
			if (Game.enter) {
				Game.enter = false;
				if (menuOptions[currentOption1] == "Play Again") {
					scoreSaved = false;
					Game.player.score = 0;
					Game.player2.score = 0;
					Game.gameState = 0; 						// changes gameState to Game Running
				} else if (menuOptions[currentOption1] == "Save Score") {
					saveScoreScreen = true;
					currentOption2 = 0;
					inputName = "";
				} else if (menuOptions[currentOption1] == "Return") {
					new Game();
					Game.gameState = 1; 						// changes gameState to Main Menu
				}
			}
		} else {
			// options browsing system
			if (Game.up) {
				Game.up = false;
				currentOption2--;
				if (currentOption2 < 0)
					currentOption2 = maxOption2;
			}
			if (Game.down) {
				Game.down = false;
				currentOption2++;
				if (currentOption2 > maxOption2)
					currentOption2 = 0;
			}
			if (Game.enter) {
				Game.enter = false;
				if (saveScoreOptions[currentOption2] == "Confirm") {
					if (Game.gameDifficulty == 0) {   			// if difficulty is beginner
						writeScore("score_beginner.txt");
					} else if (Game.gameDifficulty == 1) { 		// if difficulty is intermediate
						writeScore("score_intermediate.txt");
					} else if (Game.gameDifficulty == 2) { 		// if difficulty is expert
						writeScore("score_expert.txt");
					}
					scoreSaved = true;		
				} else if (saveScoreOptions[currentOption2] == "Cancel") {
					scoreSaved = false;
				}
				saveScoreScreen = false;
				currentOption1 = 0;
			}			
		}
		
	}
	
	/**
	 * Responsible options in a defined side of the screen, based on who won.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	private void renderOptions(Graphics g, int x, int y) { 
		if (!saveScoreScreen) {
			// win text
			g.setColor(Color.yellow);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("  WIN", x, y);
			// options list
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 10));			
			g.drawString("> Play Again", x, y + 20);
			if (!scoreSaved && Game.singleplayer)
				if (Game.singleplayer)
					g.drawString("> Save Score", x, y + 35);
			
			if (scoreSaved)
				g.drawString("Score Saved!", x, y - 25);
				
			g.drawString("> Return to", x, y + 50);
			g.drawString("   Main Menu", x, y + 58);
			
			g.setColor(Color.red);
			if (menuOptions[currentOption1] == "Play Again") {
				g.drawString("> Play Again", x, y + 20);
			} else if (menuOptions[currentOption1] == "Save Score") {
				if (!scoreSaved && Game.singleplayer)
					g.drawString("> Save Score", x, y + 35);
			} else if (menuOptions[currentOption1] == "Return") {
				g.drawString("> Return to", x, y + 50);
				g.drawString("   Main Menu", x, y + 58);
			}			
		} else {
			// menu background
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setColor(Color.white);
			// name input background
			g.setColor(Color.GRAY);
			g.fillRect(Game.WIDTH/4 + 6, Game.HEIGHT/3 + 10, 90, 20);
			// game title
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 22));
			g.drawString("PONG", Game.WIDTH/3 + 3, 18);
			// subtitle
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString(" Save your score!", Game.WIDTH/4, Game.HEIGHT/5);
			// input name text
			g.setFont(new Font("Arial", Font.BOLD, 10));
			g.drawString("Enter your name:", Game.WIDTH/4 + 10, Game.HEIGHT/3 + 5);
			g.drawString(inputName, Game.WIDTH/4 + 20, Game.HEIGHT/3 + 23);
			// max characters warning
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.drawString("(Max 10 characters)", Game.WIDTH/4 + 8, Game.HEIGHT/2 + 15);
			// options list
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("CONFIRM", Game.WIDTH/3 + 10, Game.HEIGHT/2 + 40);
			g.drawString(" CANCEL", Game.WIDTH/3 + 10, Game.HEIGHT/2 + 55);			
			// currentOption warning
			g.setColor(Color.red);
			if (saveScoreOptions[currentOption2] == "Confirm") {
				g.drawString("CONFIRM", Game.WIDTH/3 + 10, Game.HEIGHT/2 + 40);
			} else if (saveScoreOptions[currentOption2] == "Cancel") {
				g.drawString(" CANCEL", Game.WIDTH/3 + 10, Game.HEIGHT/2 + 55);	
			} 
			
		}
	}
	
	/**
	 * Responsible for rendering End Game Menu.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render(Graphics g) {
		if (Game.player.score == 10) { // if player won
			renderOptions(g, Game.WIDTH*5/8 - 5, Game.HEIGHT/3 + 10);
		} else { // if player2 won
			renderOptions(g, Game.WIDTH/8, Game.HEIGHT/3 + 10);
		}
	}

}

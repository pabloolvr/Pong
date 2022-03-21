package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Manages the MainMenu screen logic.
 */
public class MainMenu {
	private String[] menuOptions = {"Start","Best Scores","Quit"};
	private String[] scoreOptions = {"Beginner", "Intermediate", "Expert", "Menu"};
	
	private int currentOption1 = 0;								// options control for menuOptions
	private int maxOption1 = menuOptions.length - 1;
	
	private int currentOption2 = 0;								// options control for ScoreOptions
	private int maxOption2 = scoreOptions.length - 1;

	private boolean bestScoreScreen;
	private String selectedDiff;           						// controls which difficulty score is being shown    
	
	private String[] playerName;                                // store player name read from score file
	private String[] Scores;                                    // store score value read from score file
	private int nScores;
	private File file;
	
	/**
	 * Reads the score list from a .txt file.
	 * 
	 * @param  path
     * 		The path of the file.
	 */
	private void readScoreFile(String path) {		
		String absolutePath = Paths.get(path).toAbsolutePath().toString();
		
		System.out.println(absolutePath);
		
		file = new File(absolutePath);
		
		System.out.println(file);
		
		int maxFileScores = 7;
		int maxPlayerNameLen = 10;
		int maxPlayerScoreLen = 4;
		
		String data = null;										// stores the player name and the score
		String numerics = null;									// stores the numeric part from data
		char[] numericsChar;
		int fileScores = (int)file.length() / (maxPlayerNameLen + maxPlayerScoreLen);				// number of scores written
		
		if (fileScores > maxFileScores)
			nScores = maxFileScores;
		else
			nScores = fileScores;
		
		playerName = new String[nScores];
		Scores = new String[nScores];
		
		try {
		      Scanner reader = new Scanner(file);
		      while (reader.hasNextLine()) {
		    	  data = reader.nextLine();
		      }
		      reader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		for (int i = 0; i < nScores; i++) {
			fileScores--;
			int fileIndex = fileScores * (maxPlayerNameLen + maxPlayerScoreLen);	
			playerName[i] = data.substring(fileIndex, fileIndex + maxPlayerNameLen);
			numerics = data.substring(fileIndex + maxPlayerNameLen + 1, fileIndex + maxPlayerNameLen + maxPlayerScoreLen);
			numericsChar = numerics.toCharArray();
			
			System.out.println(numericsChar);
			
			Scores[i] = "";
			for (int j = 0; j < maxPlayerScoreLen - 1; j++) {
				if (numericsChar[j] != '-') {
					Scores[i] += numericsChar[j];
				} else {
					Scores[i] += " x ";
				}
			}
		}	
	}
	
	/**
	 * Render Score list of a specific game mode difficulty.
	 * 
	 * @param g Graphics class to draw on the screen.
	 * @difficulty The game mode difficulty.
	 */
	private void renderScoreList(Graphics g, String difficulty) {
		// subtitle based on the difficulty selected
		g.setFont(new Font("Arial", Font.BOLD, 12));
		if (selectedDiff == "Beginner") {
			g.drawString("  Beginner Best Scores", Game.WIDTH/6, Game.HEIGHT/5);
			g.setFont(new Font("Arial", Font.BOLD, 10));
		} else if (selectedDiff == "Intermediate") {
			g.drawString("Intermediate Best Scores", Game.WIDTH/7, Game.HEIGHT/5);
			g.setFont(new Font("Arial", Font.BOLD, 10));
		} else if (selectedDiff == "Expert") {
			g.drawString("  Expert Best Scores", Game.WIDTH/6, Game.HEIGHT/5);
			g.setFont(new Font("Arial", Font.BOLD, 10));
		}		
		// score list
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString("Name", 10, Game.HEIGHT/4 + 10);
		g.drawString("_____", 10, Game.HEIGHT/4 + 10);
		g.drawString("Score", 100, Game.HEIGHT/4 + 10);
		g.drawString("_____", 100, Game.HEIGHT/4 + 10);		

		for (int i = 0; i < nScores; i++) {
			g.drawString(playerName[i], 10, Game.HEIGHT/4 + 25 + 11*i);
			g.drawString(Scores[i], 100, Game.HEIGHT/4 + 25 + 11*i);
		}
		
	}
	
	/**
	 * Responsible for the Main Menu logic.
	 */
	public void tick() {
		// options browsing system based on bestScoreScreen
		if (!bestScoreScreen) { // if the current screen is the main menu options
			if (Game.up) {
				Game.up = false;
				currentOption1--;
				if (currentOption1 < 0)
					currentOption1 = maxOption1;
			}
			if (Game.down) {
				Game.down = false;
				currentOption1++;
				if (currentOption1 > maxOption1)
					currentOption1 = 0;
			}
			if (Game.enter) {
				Game.enter = false;
				if (menuOptions[currentOption1] == "Start") {
					Game.gameState = 2; 						// changes gameState to selection menu
				} else if (menuOptions[currentOption1] == "Best Scores") {
					bestScoreScreen = true;
				} else if (menuOptions[currentOption1] == "Quit") {
					System.exit(1);
				}
			}
		} else { // if the current screen is the best scores list
			if (Game.left) {
				Game.left = false;
				currentOption2--;
				if (currentOption2 < 0)
					currentOption2 = maxOption2;
			}
			if (Game.right) {
				Game.right = false;
				currentOption2++;
				if (currentOption2 > maxOption2)
					currentOption2 = 0;
			}
			if (Game.enter) {
				Game.enter = false;
				if (scoreOptions[currentOption2] == "Beginner") {
					selectedDiff = "Beginner";
					readScoreFile("score_beginner.txt");
				} else if (scoreOptions[currentOption2] == "Intermediate") {
					selectedDiff = "Intermediate";
					readScoreFile("score_intermediate.txt");
				} else if (scoreOptions[currentOption2] == "Expert") {
					selectedDiff = "Expert";
					readScoreFile("score_expert.txt");
				} else if (scoreOptions[currentOption2] == "Menu") {
					bestScoreScreen = false;
				}
			}			
		}
	}
	
	/**
	 * Responsible for rendering the Main Menu screen.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render(Graphics g) {
		// menu background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		if (!bestScoreScreen) {
			// game title
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString("PONG", Game.WIDTH/4, 35);
			// menu options
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 10));
			g.drawString("Start", Game.WIDTH/2 - 12, 80);
			g.drawString("Best Scores", Game.WIDTH/3 + 5, 90);
			g.drawString("Quit", Game.WIDTH/2 - 10, 100);
			// currentOption warning
			g.setColor(Color.red);
			if (menuOptions[currentOption1] == "Start") {
				g.drawString("Start", Game.WIDTH/2 - 12, 80);	
			} else if (menuOptions[currentOption1] == "Best Scores") {
				g.drawString("Best Scores", Game.WIDTH/3 + 5, 90);
			} else if (menuOptions[currentOption1] == "Quit") {
				g.drawString("Quit", Game.WIDTH/2 - 10, 100);
			}	
		} else {		
			// game title
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 22));
			g.drawString("PONG", Game.WIDTH/3, 18);
			// list of best scores
			renderScoreList(g, selectedDiff);
			// difficulty scores options
			g.setFont(new Font("Arial", Font.BOLD, 10));
			g.drawString("Beginner", 10, Game.HEIGHT - 5);
			g.drawString("Intermediate", 59, Game.HEIGHT - 5);
			g.drawString("Expert", 124, Game.HEIGHT - 5);	
			g.drawString("Menu", 160, Game.HEIGHT - 5);	
			// currentOption warning
			g.setColor(Color.red);
			if (scoreOptions[currentOption2] == "Beginner") {
				g.drawString("Beginner", 10, Game.HEIGHT - 5);	
			} else if (scoreOptions[currentOption2] == "Intermediate") {
				g.drawString("Intermediate", 59, Game.HEIGHT - 5);
			} else if (scoreOptions[currentOption2] == "Expert") {
				g.drawString("Expert", 124, Game.HEIGHT - 5);
			} else if (scoreOptions[currentOption2] == "Menu") {
				g.drawString("Menu", 160, Game.HEIGHT - 5);				
			}
		}
	}
}

package pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {
	private static final long serialVersionUID = 692903652728389693L; // sets a generated serialVersionUID
	
	public static int WIDTH = 200;
	public static int HEIGHT = 150;
	public static int SCALE = 4;
	
	public BufferedImage layer = new BufferedImage
			(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); 		// layer where the graphics will be rendered
	public static Player player;
	public static Player2 player2;
	public static Ball ball;
	public static MainMenu mainMenu;
	public static SelectionMenu selecMenu;
	public static PauseMenu pauseMenu;
	public static EndGameMenu endMenu;
		
	/*
	 * variable gameState stores 5 possible values:
	 * 0 - Game Running
	 * 1 - Main Menu
	 * 2 - Selection Menu
	 * 3 - Pause Menu
	 * 4 - End Game Menu
	 */
	public static int gameState = 1;
	/*
	 * if game is 1-player mode, gameDifficulty stores 3 possible values:
	 * 0 - beginner
	 * 1 - intermediate
	 * 2 - expert
	 */
	public static int gameDifficulty;
	public static boolean singleplayer;							// defines if game is 1-player or 2-player
	public static boolean up, down, left, right, enter; 		// commands for the game menus browsing
	public static boolean pause;								// set gameState = Pause Menu when true
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	// responsible for typing and player controlling
	public void keyPressed(KeyEvent e) {						
		if (!Game.endMenu.saveScoreScreen) {					// if game is not in the score saving menu in End Game
			if (e.getKeyCode() == KeyEvent.VK_K) {				// if key pressed is K
				player.up = true;
			} else if (e.getKeyCode() == KeyEvent.VK_M) {		// if key pressed is M
				player.down = true;
			}		
			if (e.getKeyCode() == KeyEvent.VK_A) {				// if key pressed is A
				player2.up = true;
			} else if (e.getKeyCode() == KeyEvent.VK_Z) {		// if key pressed is Z
				player2.down = true;
			}
		} else {												// if game is in the score saving menu in End Game
			endMenu.writeName(e);								// method for writing the player name in the Save Score Screen
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {					// if key pressed is UP
			up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {		// if key pressed is DOWN
			down = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {				// if key pressed is LEFT
			left = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {		// if key pressed is RIGHT
			right = true;
		}
				
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {				// if key pressed is ENTER
			if (gameState != 0)									// if gameState is not Game Running
				enter = true;		
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {				// if key pressed is ESC
			if (gameState == 0)									// if gameState is Game Running
				gameState = 3;									// sets gameState to Pause Menu	
		}
	}
	
	// defines player controlling when a key is released
	public void keyReleased(KeyEvent e) {						
		if (e.getKeyCode() == KeyEvent.VK_UP) {					// if key released is UP
			up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {		// if key released is DOWN
			down = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {				// if key released is LEFT
			left = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {		// if key released is RIGHT
			right = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_K) {					// if key released is K
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_M) {			// if key released is M
			player.down = false;
		}	
		
		if (e.getKeyCode() == KeyEvent.VK_A) {					// if key released is A
			player2.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_Z) {			// if key released is Z
			player2.down = false;
		}		
	}
	
	/**
	 * Responsible for rendering the background of the game.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void renderBackground(Graphics g) {
		// set game background color
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		// loop for the line in the middle of the window
		for (int i = 0; i < HEIGHT; i += 5) { 					
			g.setColor(Color.white);
			g.fillRect(WIDTH/2, i, 1, 2);			
		}
		// text for the player2 score
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString(String.valueOf(player2.score), WIDTH/4 - 5, HEIGHT/10 + 5);
		// text for the player score
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString(String.valueOf(player.score), WIDTH*3/4, HEIGHT/10 + 5);			
	}
		
	public void tick() {
		if (gameState == 0) { 									// if gameState is Game Running
			player.tick();
			player2.tick();
			ball.tick();			
		} else if (gameState == 1) { 							// if gameState is Main Menu
			mainMenu.tick();
		} else if (gameState == 2) { 							// if gameState is Selection Running
			selecMenu.tick();
		} else if (gameState == 3) { 							// if gameState is Pause Menu 
			pauseMenu.tick();
		} else if (gameState == 4) {							// if gameState is End Game Menu
			endMenu.tick();
		}
	}
	
	/**
	 * Responsible for rendering every element of Game.
	 * 
	 * @param g Graphics class to draw on the screen.
	 */
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = layer.getGraphics(); 						// object for graphics manipulation
		
		if (gameState == 0) {									// if gameState is Game Running
			renderBackground(g); 								// render game background
			player.render(g); 									// render player graphics
			player2.render(g); 									// render player2 graphics
			if (player.score == 9 || player2.score == 9)		// if a player has reached 10 points
				gameState = 4;									// set gameState to End Game Menu
			ball.render(g); 									// render ball graphics			
		} else if (gameState == 1) {							// if gameState is Main Menu
			mainMenu.render(g);
		} else if (gameState == 2) {							// if gameState is Selection Menu
			selecMenu.render(g);
		} else if (gameState == 3) {							// if gameState is Pause Menu
			pauseMenu.render(g);
		} else if (gameState == 4) {							// if gameState is End Game
			renderBackground(g); 								// render game background
			player.render(g); 									// render player graphics
			player2.render(g); 									// render player2 graphics
			endMenu.render(g);
		}
		
		g = bs.getDrawGraphics();
		g.drawImage(layer, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null); // draws what was rendered
		bs.show(); 												   // shows what was rendered
	}
	
	/**
	 * Main Game class.
	 */
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		this.addKeyListener(this);	
						
		player = new Player(WIDTH - 5, HEIGHT/2 - 25/2); 		// initializes player at the desired position
		player2 = new Player2(0, HEIGHT/2 - 25/2); 				// initializes player2 at the desired position
		ball = new Ball(WIDTH/2, 0); 							// initializes ball at the desired position
		mainMenu = new MainMenu();
		selecMenu = new SelectionMenu();
		pauseMenu = new PauseMenu();
		endMenu = new EndGameMenu();
	}
	
	/**
	 * Responsible for the game loop.
	 */
	public void run() { // 
		while (true) {
			tick();
			render();
			try {
				Thread.sleep(1000/60);							// sets the refresh rate of the game to 60 updates per second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game(); 								// initializes game
		JFrame frame = new JFrame("Pong"); 						// creates game window
		frame.setResizable(false); 								// controls game window resizing
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	// set the quit option in window to terminate the execution
		frame.add(game); 										// add game as component to the frame
		frame.pack(); 											// calculate and shows the size of the game window
		frame.setLocationRelativeTo(null); 						// set window at the center of screen
		frame.setVisible(true); 								// set the window visible
		
		new Thread(game).start();								// starts the game thread
	}
}

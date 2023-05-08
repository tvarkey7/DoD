import java.io.*;
import java.util.*;

/**
 * Contains the main logic part of the game, as it processes.
 *
 */
public class GameLogic {
	
	/* Reference to the map being used */
	private Map map;
	
	/* The gold owned by the player; */
	private int gold;
	
	/*For bot's turns */
	private boolean searched = false;
	
	/*Direction for bots next move */
	private char botDir;
	/**
	 * Default constructor
	 */
	public GameLogic() {
		System.out.println("Welcome to Dungeon of Doom");
		getTheMap();
	}
	
	/* Bool to check if the game is running */
	private boolean gameRunning = false;
	
	/* Arrays that store the co-ordinates for player and bots */
	int[] playerSpawningCoords = new int[2];
	int[] currentPlayerCoords = new int[2];
	int[] botSpawningCoords = new int[2];
	int[] currentBotCoords = new int[2];

    /**
     * A function to set up the game.
     */
    protected void setup() {
    	//System.out.println("Welcome to Dungeon of Doom!");
    	//this function runs after getting the map
    	
    	//ready to begin
    	System.out.println("Your commands are: \n");
    	System.out.println("HELLO: Shows how much gold you need");
    	System.out.println("GOLD: Shows how much gold you own");
    	System.out.println("PICKUP: Picks up gold if you're on a gold tile");
    	System.out.println("MOVE <DIRECTION>: Moves the character. Directions are N,E,S,W.");
    	System.out.println("LOOK: Shows a 5x5 grid of the map around the player");
    	System.out.println("QUIT: Quit the game.\n");
    	//System.out.println("Spawning in your player");
    	gameRunning = true;
    	
    }


	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GameLogic logic = new GameLogic();
		logic.setup();
		logic.mainGame();
		}

	/* The function that will carry out the main functions of the game 
	 *
	 * Takes in the user's input and runs the appropriate command
	 */
	
	private void mainGame() {
    	while (true) {
    		System.out.println("Enter a command!");
    		/* Taking in the user's input */
	     	Scanner test1 = new Scanner(System.in);
	    	String response = test1.nextLine();
	    	if (response.toLowerCase().compareTo("look") == 0) {
	    		System.out.println(look(currentPlayerCoords));
	    	}
	    	else if (response.toLowerCase().compareTo("hello") == 0) {
	    		System.out.println(hello());

	    	}
	    	else if (response.toLowerCase().contains("move")) {
	    		String directionGetter = response.toLowerCase();
	    		String[] splitDir = directionGetter.split(" ");
	    		char dir = splitDir[1].charAt(0);
	    		System.out.println(move(dir, "p"));
	    		//return;
	    		
	    	}
	    	else if (response.toLowerCase().compareTo("quit") == 0) {
	    		System.out.println(quit(currentPlayerCoords));
	    		System.out.println("Quitting The game!");
	    		test1.close();
	    		System.exit(0);
	    		return;
	    	}   
	    	
	    	else if (response.toLowerCase().compareTo("pickup") == 0) {
	    		System.out.println(pickup(currentPlayerCoords));
	    	}
	    	
	    	else if (response.toLowerCase().compareTo("gold") == 0) {
	    		System.out.println(gold());
	    	}
	    	
	    	/*Bot's turn */
	    	
	    	searched = nextBotMove(currentBotCoords, searched);
	    	
	    	//if the bot moves to the player's tile
	    	if (Arrays.equals(currentBotCoords, currentPlayerCoords)) {
	    		System.out.println(caught());
	    		test1.close();
	    		System.exit(0);
	    		return;
	    	}
	    	
    	}
	}

	/**
	 * Getting the user's map choice.
	 * If the file is not found, the default map will be used.
	 * After getting the map, the map's details will be shown.
	 */
	protected void getTheMap() { 
		/*
		 * Taking the filename from the user
		 */
		System.out.println("Enter the name of the map you want to play");
		System.out.println("Example: small_example_map.txt");
		Scanner in1 = new Scanner(System.in);
		String filename = in1.nextLine();
		
		//System.out.println("FILENAME LEN: " + filename.length());
		/*
		 * If a filename isn't entered, the default map is used
		 */
		if (filename.length() == 0) {
			map = new Map();
			//System.out.println("Map name: " + map.getNameOfMap());
			map.displayDetails();
		}
		else {
			try {
				File f = new File(filename);
				if (f.exists()) {
					map = new Map(filename);
				}
				else {
					System.out.println("File not Found!");
					System.out.println("Using default map instead!");
					map = new Map();
				}
			} catch (Exception e){
				e.printStackTrace();
				System.out.println("Error getting your chosen map!");
				System.out.println("Using the default map");
				map = new Map();
			} finally {
				map.displayDetails();
				/*Generating The co ordinates to spawn in */
				playerSpawningCoords = generateCoords();
				currentPlayerCoords[0] = playerSpawningCoords[0];
				currentPlayerCoords[1] = playerSpawningCoords[1];
				//System.out.println("X is: " + playerSpawningCoords[0] + " Y is: " + playerSpawningCoords[1]);
				botSpawningCoords = generateCoords();
				currentBotCoords[0] = botSpawningCoords[0];
				currentBotCoords[1] = botSpawningCoords[1];
				
			}
		}
	}
	
	
	
	/**
	 * A function that generates valid spawning co-ordinates for the player.
	 * @return : spawning co ordinates as an integer array of size 2.
	 */
	protected int[] generateCoords() {
		int[] maxCoords = map.getRange();

		int[] coords = new int[2];
		
		while (true) {
			Random rd = new Random();
			int xCoord = rd.nextInt(maxCoords[1]);
			int yCoord = rd.nextInt(maxCoords[0]);
			coords[1] = xCoord;
			coords[0] = yCoord;
			
			
			if ((map.getMap()[coords[1]][coords[0]] != '#') && (map.getMap()[coords[1]][coords[0]] != 'G') && (map.getMap()[coords[1]][coords[0]] != 'P')) {
				
				return coords;
			}
		}
			
	}
	
	/*
	 * Game functions
	 */
	
    /**
	 * Returns a string which shows the gold required to win for the map.
	 * 
     * @return : Gold required to win.
     */
    protected String hello() {
        int gold1 = map.getGoldReq();
        return "Gold to win: " + gold1 +".";
    }
    
    /**
     * Displays the current gold owned by the player.
     * 
     * @return : a string that says how much gold is owned. 
     */
    protected String gold() {
    	return "Gold owned: " + gold + ".";
    }
    
    /**
     * A function that checks whether there is gold on a tile. 
     * Depending on if there is gold, it will display a message. 
     * 
     * @param coords : The co-ordinates of the player's current tile
     * @return : a string which indicates whether they picked up gold.
     */
    protected String pickup(int[] coords) {
    	if (map.getTile(coords) == 'G') {
    		map.pickupTile(coords);
    		gold++;
    		return "Success. Gold Owned: " + gold + ".";
    	}
    	else {
    		return "Fail. Gold Owned: " + gold + ".";
    	}	
    }
    protected void botMoveFunction (char dir) {
    	dir = Character.toLowerCase(dir);
    	//int[] setCoords = currentBotCoords;
    	
    	switch (dir) {
    	case 'n':
    		currentBotCoords[1] = currentBotCoords[1] - 1;
    		break;
    	
    	case 'e':
    		currentBotCoords[0] = currentBotCoords[0] + 1;
    		break;
    		
    	case 's':
    		currentBotCoords[1] = currentBotCoords[1] + 1;
    		break;
    		
    	case 'w':
    		currentBotCoords[0] = currentBotCoords[0] - 1;
    		break;
    	}
    	
    }
    /**
     * The function called when "MOVE" is entered by the user. 
     * If the player is able to move, they will move. If they are not, they won't.
     * A message will be displayed depending on if they were able to move or not. 
     * 
     * @param dir : the char of the direction that the player will be moving
     * @return : A string to indicate if the move occurred. 
     */
    protected String move(char dir, String plr) {
    	dir = Character.toLowerCase(dir);
    	/* An array to store the co ordinates */
		int[] testCoords = new int[2];
		int[] setCoords = new int[2];
		//instead of taking in char, we take in string and split in this function?
    	if (plr.compareTo("p") == 0) {
    		testCoords = currentPlayerCoords;
    		setCoords = currentPlayerCoords;
    	}
    	else if(plr.compareTo("b")==0) {
    		testCoords = currentBotCoords;
    		setCoords = currentBotCoords;
    	}
    	
    	switch(dir) {
    	/*checks if its a valid move, if it is, it changes the player's co ordinates */
    	case 'n':
    		if (validMove(testCoords, 'n') == true) {
    		
    			setCoords[1] = setCoords[1] - 1;
    			
    			if (plr.compareTo("p")==0) {
    				currentPlayerCoords = setCoords;
    			}
    			else if (plr.compareTo("b")==0) {
    				currentBotCoords = setCoords;
    			}
    			
    			return "Sucess";
    		}
    		else {
    			break;
    		}
    	case 'e':
    		if (validMove(testCoords, 'e') == true) {
    			
    			setCoords[0] = setCoords[0] + 1;
    			if (plr.compareTo("p")==0) {
    				currentPlayerCoords = setCoords;
    			}
    			else if (plr.compareTo("b")==0) {
    				currentBotCoords = setCoords;
    			}
    			
    			return "Success";
    		}
    		else {
    			break;
    		}
    	
    	case 's':
    		if (validMove(testCoords, 's')) {
    			
    			setCoords[1] = setCoords[1] + 1;
    			
    			if (plr.compareTo("p")==0) {
    				currentPlayerCoords = setCoords;
    			}
    			else if (plr.compareTo("b")==0) {
    				currentBotCoords = setCoords;
    			}
    			
    			return "Success";
    		}
    		else {
    			break;
    		}
    		
    	case 'w':
    		if (validMove(testCoords, 'w')) {
    			
    			setCoords[0] = setCoords[0] - 1;
    			if (plr.compareTo("p")==0) {
    				currentPlayerCoords = setCoords;
    			}
    			else if (plr.compareTo("b")==0) {
    				currentBotCoords = setCoords;
    			}
    			
    			return "Success";
    		}
    		else break;
    	}
    	return "Fail";
    }
    
    /**
     * Takes in the co-ordinates and the direction moved and returns 
     * whether its a valid move or not
     * 
     * @param coords : the co-ordinates of the current player
     * @param dir : direction being moved
     * @return : a boolean to say whether the move was valid or invalid
     */
    protected boolean validMove(int[] coords1, char dir) {
    	int[] coords = new int[2];
    	int xCord = coords1[0];
    	int yCord = coords1[1];
    	coords[0] = xCord;
    	coords[1] = yCord;
		//coords = coords1;
		
		char switchLetter = Character.toLowerCase(dir);
		char tile;
		switch (switchLetter) {
		/* Get the character for the direction and return true if its a valid move */
			case 'n':
				//coords = coords1;
				coords[1] = coords[1] - 1;
				
				tile = map.getTile(coords);
				
				if ((tile != 'B') && (tile != '#')) {
					return true;
				}
				else {
					return false;
				}
				
			case 'e':
				//coords = coords1;
				coords[0] = coords[0] + 1;
				
				tile = map.getTile(coords);
				
				if ((tile != 'B') && (tile != '#')) {
					return true;
				}
				else {
					return false;
				}
			
			case 's':
				//coords = coords1;
				coords[1] = coords[1] + 1;
				
				tile = map.getTile(coords);
			
				if ((tile != 'B') && (tile != '#')) {
					return true;
				}
				else {
					return false;
				}
			
			case 'w':
				coords[0] = coords[0] - 1;
				
				tile = map.getTile(coords);
				if ((tile != 'B') && (tile != '#')) {
					return true;
				}
				else {
					return false;
				}
				
		}
    	return false;
    	
    }
    
    /**
     * The function called when the user entered "LOOK". 
     * This will print a 5x5 grid of the map around the player.
     * 
     * @param coords : The coordinates of the player's current tile
     * @return : a 5x5 grid of the map around the player.
     */
    protected String look(int[] coords) {
    	/* Gets a copy of the map of its current state */
    	char[][] testMap = map.getMap();
    	
    	/* The 2d array that will be printed to the screen */
    	char[][] lookResult = new char[5][5];
    	
    	/* Loop through co-ords -2 to co-ords +2 and get item on each tile */
    	for (int i=0;i<5;i++) {
    		for (int j=0;j<5;j++) {
    			try {
    				/* If we reach the centre of the lookResult 2d array, print the P for the player
    				 * If the player is the one that searched. We check this by checking the co-ords
    				 */
    				
    			
    				if ((i==2) && (j==2) && (coords[0] == currentPlayerCoords[0]) && (coords[1] == currentPlayerCoords[1])) {
    					lookResult[i][j] = 'P';
    				}
    				/* If the bot is in the frame, we then also print out the Bot */
    				else if ((coords[0]- 2 + i == currentBotCoords[0]) && (coords[1] - 2 + j == currentBotCoords[1])) {
    					lookResult[i][j] = 'B';
    				}
    				/* If the bot is the one looking */
    				else if ((i==2)&&(j==2)&&(coords[0] == currentBotCoords[0])&& (coords[1] == currentBotCoords[1])) {
    					lookResult[i][j] = 'B';
    				}
    				/* Or we just print out everything else, '.', 'G', 'E' etc. */
    				else {
    					lookResult[i][j] = testMap[coords[1] - 2 + i][coords[0] - 2 + j];
    				}
    				/* If the i and j count goes out of bounds of the map, we will fill it in with a '#'. */
    			} catch (ArrayIndexOutOfBoundsException e) {
    				//System.out.println("ArrayIndexOutOfBoundsExcepton thrown");
    				//e.printStackTrace();
    				lookResult[i][j] = '#';
    				
    			} catch (Exception e) {
    				System.out.println("Something went wrong");
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	String lookString = "";
    	for (char[] row: lookResult) {
    		String adder = new String(row);
    		lookString = lookString + adder + '\n';
    	}
    	/*
    	 * String x = look(coords);
    	 * then print x and it should work;
    	 */
    	return lookString;
    }
    
    /**
     * The function called when "QUIT" is entered by the player. 
     * If the player is on an exit tile and has enough gold, they win the game.
     * Otherwise, they lose the game.
     * 
     * @param coords: The coords the player is currently on
     * @return : a string that will print to the screen. 
     */
    protected String quit(int[] coords) {
    	/* checks if the player has enough gold & is on an exit tile */
    	if ((map.getTile(coords) == 'E') && (gold >= map.getGoldReq())) {
    		/* If so, they win*/
    		return "WIN. You collected enough gold and escaped!";
    	}
    	else {
    		return "LOSE";
    	}
    }
    
    /*Function that lets the game know that the player has been caught */
    protected String caught() {
    	return "The bot has caught you before you could escape!";
    }
    
    /**
     * A function to get the bots next move.
     * It performs look and find outs which ways it can move on its next go.
     * If the player is in the vicinity, it will prioritise the player, otherwise moves randomly.
     * 
     * @param botCoords : the co ordinates of the bot
     * @param checked : the bool variable which decides whether this go is to scout the nearby area 
     * 					or to move the bot.
     *@return checked: it returns the 'checked' parameter.
     */
    protected boolean nextBotMove(int[] botCoords, boolean checked) {
    	if (checked == false) {
    		String pos = "";
    		char[] possible = new char[4];
	    	String lookR = look(botCoords);
	    	char[] nearbyMap = lookR.toCharArray();
	    	//System.out.println("NEARBY MAP: " + nearbyMap[14]);
	    	if (nearbyMap[8] != '#') {
	    		if (nearbyMap[8] == 'P') {
	    			pos = "n" + pos;
	    		}
	    		else {
	    			pos = pos + "n";
	    		}
	    	}
	    	
	    	if (nearbyMap[13] != '#') {
	    		if (nearbyMap[13] == 'P') {
	    			pos = "w" + pos;
	    		}
	    		else {
	    			pos = pos + "w";
	    		}
	    	}
	    	
	    	if (nearbyMap[15] != '#') {
	    		if (nearbyMap[15] == 'P') {
	    			pos = "e" + pos;
	    		}
	    		else {
	    			pos = pos + "e";
	    		}
	    	}
	    	
	    	if (nearbyMap[20] != '#') {
	    		if (nearbyMap[20] == 'P') {
	    			pos = "s" + pos;
	    		}
	    		else {
	    			pos = pos + "s";
	    		}
	    	}
	    	
	    	possible = pos.toCharArray();
	    	botDir = possible[0];
	    	/* changes the checked bool variable so for the next bot move, it can move */
	    	checked = true;
    	}
    	
    	else {
    		botMoveFunction(botDir);
    		/*changes the variable to false so next turn it can analyse its surroundings */
    		checked = false;
    	}
	
    	return checked;
    }

}

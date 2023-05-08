import java.io.*;

/**
 * Reads and contains in memory the map of the game.
 *
 */
public class Map {

	/* Number of rows in the map - will be used as y coordinate*/
	private int rows = 9;
	
	/* Number of columns in the map - will be used as x coordinate*/
	private int cols = 20;
	
	/* Representation of the map */
	private char[][] map;
	
	/* Map name */
	private String mapName;
	
	/* Gold required for the human player to win */
	private int goldRequired;
	
	/**
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 */
	public Map() {
		mapName = "Very small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][]{
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
		};
	}
	
	/**
	 * Constructor that accepts a map to read in from.
	 *
	 * @param : The filename of the map file.
	 * @throws IOException 
	 */
	public Map(String fileName) throws IOException {
		readMap(fileName);
	}

	/**
	 * Counts the number of lines in the file
	 * 
	 * @param : Name of the map's file
	 * @return : Number of lines - 2
	 * 			 Minus 2, because then we can find how many rows there are in the map
	 * @throws IOException 
	 */
	private static int numOfLines(String filename) throws IOException {
		int lines = 0;
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		while (reader.readLine() != null) {
			lines++;
		}
		//reader.close();
		
		return lines - 2;
	}

    /**
     * Reads the map file into the appropriate variables.
     *
     * @param : Name of the map's file.
     * @throws IOException 
     */
    public void readMap(String fileName) throws IOException {
    	try (BufferedReader mapFile = new BufferedReader(new FileReader(fileName))) {
			/* Getting the map name from file */
			mapName = (mapFile.readLine()).replaceFirst("name ", "");
			//System.out.println("Map Name: "+ mapName);
			
			/* Getting gold required from the file */
			String goldReq = (mapFile.readLine().replaceFirst("win ", ""));
			goldRequired = Integer.parseInt(goldReq);
			
			/* get the map into the 2d array */
			/* maybe check for rectangular grid? */
			rows = numOfLines(fileName);
			//System.out.println("Rows: " + rows);
			
			/* since the next line is the map, we can find the columns in the map */
			//cols = mapFile.readLine().length();
			//System.out.println("cols: " + cols);
			
			/* Reading in the file to the map 2d char array, one line at a time*/
			map = new char[rows][cols];
			for (int i=0; i<rows;i++) {
				map[i] = mapFile.readLine().toCharArray();
				//System.out.println("Row " + i + " added");
			}
			
			//mapFile.close();
			
		} catch (FileNotFoundException e){
			System.out.println("File Not Found");
		}
    	
    	catch (NumberFormatException e) {
			System.out.println("Error in Try catch for readMap()");
			e.printStackTrace();
		}
    	
    }
    /**
     * Returns the name of the map.
     * @return : The name of the map
     */
    public String getNameOfMap() {
		return mapName;
    }
    
    /**
     * Returns the amount of gold required for the player to win.
     * @return : Gold required to win
     */
    public int getGoldReq() {
		return goldRequired;

    }
    
    /**
     * Gets the map
     * @return : the map
     */
    public char[][] getMap() {
    	return map;
    }
    
    /**
     * A function that displays the details of the map to the user.
     * It displays:
     * The name of the map
     * The gold required to win the game
     * THe dimensions of the map
     */
    protected void displayDetails() {
    	System.out.println("\nMap: " + getNameOfMap());
    	System.out.println("Gold required: " + getGoldReq());
    	int[] sizeList = getSize();

    }
    
    /**
     * Gets the character that is on the tile of the co-ordinates.
     * @param coords : The co-ordinates of the tile
     * @return : the character of the tile referenced by the co-ordinates
     */
    protected char getTile(int[] coords) {
    	return map[coords[1]][coords[0]];
    }
    
    /**
     * Once the item on the tile has been picked up, it changes to an empty tile.
     * @param coords : the co-ordinates of the tile that PICKUP has been called on
     */
    protected void pickupTile(int[] coords) {
    	map[coords[1]][coords[0]] = '.';
    }
    /**
     * Gets the dimensions of the map. The width as the x co ordinate and the height as the y co ordinate.
     * @return : The size of the map in the format of {x co-ord, y co-ord}
     */
    protected int[] getSize() {
    	return new int[] {map[0].length, map.length};
    }
    
    protected int[] getRange() {
    	return new int[] {map[0].length -1, map.length -1};
    }
    /**
     * Prints the map out to view
     */
    protected void printMap() {
    	for (char[] row: map) {
    		System.out.println(row);
    	}
    }


}

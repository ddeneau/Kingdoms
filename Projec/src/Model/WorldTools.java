package Model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/* Various tools useful for organizing game data at the world level and below. */
public class WorldTools {
	protected static final int CITY_MIN_SPACING = 5;
	private static Random random = new Random();
	private static HashMap<Integer, String> peopleNames = new HashMap<Integer, String>();
	private static HashMap<Integer, String> placeNames = new HashMap<Integer, String>();
	protected static final int SMALL_POPULATION = 100, MEDIUM_POPULATION = 500, LARGE_POPULATION = 1000;
	protected static final int START_POPULATION = 20;
	protected final static Image GRASS_IMG = new Image("file:kng_grass.png");
	protected final static Image SAND_IMG = new Image("file:kng_desert.png");
	protected final static Image MTN_IMG = new Image("file:kng_mountain.png");
	protected final static Image WATER_IMG = new Image("file:kng_water.png");
	protected final static Image FLAG_IMG = new Image("file:kng_flag.png");
	protected final static Image ROAD_IMG = new Image("file:kng_road.png");
	protected final static Image GROUND_IMG = new Image("file:kng_ground.png");
	protected final static Image WALL_IMG = new Image("file:kng_wall.png");
	protected final static ImagePattern WALL = new ImagePattern(ROAD_IMG);
	protected final static ImagePattern GROUND = new ImagePattern(ROAD_IMG);
	protected final static ImagePattern ROAD = new ImagePattern(ROAD_IMG);
	protected final static ImagePattern FLAG = new ImagePattern(FLAG_IMG);
	protected final static ImagePattern GRASS = new ImagePattern(GRASS_IMG);
	protected final static ImagePattern SAND = new ImagePattern(SAND_IMG);
	protected final static ImagePattern MOUNTAIN = new ImagePattern(MTN_IMG);
	protected final static ImagePattern WATER = new ImagePattern(WATER_IMG);
	private static boolean lakeGenerator = false;
	private static int lakeGenInt = 10;
	private static boolean coastGenerator = false;
	private static int coastGenInt = 25;
	
	
	
	/* Checks if there are neighbors within a certain range, return true if valid settlement location
	 * and false if too close to other cities. . 
	 * 
	 * @param kingdomMap - Maps kingdoms to a coordinate on the grid. 
	 * @coordinates - Coordinate to process.  */
	protected static boolean  checkNeighbors(HashMap<Rectangle, Kingdom> kingdomMap, int[] coordinates, HashMap<Rectangle, int[]> nodeMap) {
		int distance = 0;
		
	
		
		for(Rectangle point : kingdomMap.keySet()) {
			distance = getEuclideanDistance(nodeMap.get(point), coordinates);
			
			if(distance < WorldTools.CITY_MIN_SPACING) {
				return false;
			}
		}
		return true;
	}
	
	protected static boolean checkInNodesInControls(int[] coordinates, int height) {
		if(coordinates[1] < (getBackgroundHeight(height)) - 10) {
			return false;
		}
		
		return true;
	}
	
	protected static int getBackgroundHeight(int height) {
		return height - (height / 6);
	}
	
	protected static String getKingdomName() {
		return placeNames.get(random.nextInt(placeNames.size()));
	}
	
	protected static String getPerson() {
		return peopleNames.get(random.nextInt(peopleNames.size()));
	}
	
	/* Fills in data fields. */
	public static void initialize() {
		String[] names = readFile("names.txt").split(" ");
		String[] places = readFile("places.txt").split("\n");
		
		for(int index = 0; index < names.length; index++) {
			peopleNames.put(index, names[index]);
		}
		
		for(int index = 0; index < places.length; index++) {
			placeNames.put(index, places[index]);
		}
		
	}
	
	private static String readFile(String fileName) {
		String itemData = "";
		
		try {
			FileReader orderReader = new FileReader(fileName);
			int textIndex = 0;
			while ((textIndex = orderReader.read()) != -1) {
				char charecter = (char) textIndex;
				itemData += charecter;
			}
			orderReader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return itemData;
	}
	
	protected static int getDefaultCitySizeGUI(int height) {
		return height / 100;
	}
	
	protected static ArrayList<String> settlePopulation() {
		ArrayList<String> settlers = new ArrayList<String>();
		int settled = 0;
		
		while(settled < START_POPULATION) {
			settlers.add(getPerson());
			settled++;
		}
		
		return settlers;
	}
	
	protected static int getEuclideanDistance(int[] pointA, int[] pointB) {
		double[] xyDelta = new double[2];
		double deltaX = 0; 
		double deltaY = 0;
		int distance;
		
		for(int index = 0; index < 2; index++) {
			double delta = 0;
			if(pointA[index] > pointB[index]) {
				delta = (Math.abs(pointA[index] - pointB[index]));
			} else {
				delta = (Math.abs(pointB[index] - pointA[index]));
			}
			xyDelta[index] = delta;
		}
		
		deltaX = (int) Math.pow(xyDelta[0], 2.0);
		deltaY = (int) Math.pow(xyDelta[1], 2.0);
	
		distance = (int) Math.pow((deltaX + deltaY), 0.5);
		
		return distance;
	}
	
	/* Adds the initial tile to the map. */
	public static Rectangle addTile(Rectangle tile) {
		Random randomIntGen = new Random();
		int randomInt = randomIntGen.nextInt(30);
		int randomLakeInt = randomIntGen.nextInt(2);
		
		if(coastGenerator && coastGenInt > 0) {
			tile = addIslands(tile);
			coastGenInt--;
			return tile;
		}
		
		if(lakeGenerator && lakeGenInt > 0) {
			tile.setFill(WATER);
			lakeGenInt--;
			return tile;
		}
		
		if(randomInt >= 0 && randomInt < 26) {
			tile.setFill(GRASS);
		}else if(randomInt >= 26 && randomInt < 27) {
			tile.setFill(MOUNTAIN);
			
			if(randomLakeInt == 2) {
				if(lakeGenInt <= 0) {
					tile.setFill(GRASS);
				} else if(lakeGenInt > 1) {
					tile.setFill(WATER);
				}
				lakeGenerator = true;
				
			
			}
			
			
		} else {
			if(coastGenInt <= 0) {
				tile.setFill(GRASS);
			} else if(coastGenInt > 1) {
				tile.setFill(WATER);
			}
			coastGenerator = true;
		}
		
		return tile;
	}
	
	private static Rectangle addIslands(Rectangle tile) {
		Random randomIntGen = new Random();
		int randomInt = randomIntGen.nextInt(10);
		int beachFactor = randomIntGen.nextInt(2);
		
		if(randomInt < 8 && coastGenInt < 300) {
			if(beachFactor == 1) {
				tile.setFill(GRASS);
			} else {
				tile.setFill(SAND);
			}
		} else {
			tile.setFill(WATER);
		}
		
		return tile;
	}
	
	public static void setSize(int sizeFactor) {
		lakeGenInt = lakeGenInt * sizeFactor;
		coastGenInt = coastGenInt * sizeFactor;
	}
	
}

package Model;


import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/* Kingdoms exist in a World. As do the main controls of the game.
 * 
 * - missing "wildness" field.  */
public class World extends Application implements Runnable {
	private static int tileSize, tileMaxWidth, tileMaxHeight, tileDivHor, tileDivVer; // tile settings. 
	private ArrayList<ArrayList<Rectangle>> background; // 2D Array of "tiles"
	private HashMap<Rectangle, Kingdom> kingdomMap; // Map of rectangle nodes to coordinates.
	private HashMap<Rectangle, int[]> nodeMap; // Graphical Component-to-location map
	private HashMap<String, int[]> coordinatesMap; // Name-to-location map. 
	private HashMap<String, Kingdom> kingdoms; // Name-to-Kingdom map. 
	private int width, height, turn, wealth; // variables for the world. 
	private Pane worldPane; // Main pane. 
	private TextField turnText, cityText, coordinatesText; // Texts fields. 
	private GridPane controls, innerControls, backgroundPane; // Control pane objects and background for graphics pane.
	private Button nodeButton,turnButton, roadButton, auxRoadButton; // Buttons. 
	private Rectangle src, dst; // Used for road building. 
	private int[] coordinates; // auxiliary array for use in adding cities. 
	private Rectangle selectedNode; //
	private int sizeFactor;
	
	/* Constructor. */
	public World(int width, int height, int size) throws Exception {
		worldPane = new Pane();
		this.width = width;
		this.height = height;
		wealth = 100;
		kingdoms = new HashMap<String, Kingdom>();
		nodeMap = new HashMap<Rectangle, int[]>();
		kingdomMap = new HashMap<Rectangle, Kingdom>();
		coordinatesMap = new HashMap<String, int[]>();
		turn = 0;
		turnText = new TextField("Turn : 0");
		cityText = new TextField("");
		coordinatesText = new TextField(0 + ", " + 0);
		controls = new GridPane();
		innerControls = new GridPane();
		nodeButton = new Button();
		turnButton = new Button();
		roadButton = new Button();
		auxRoadButton = new Button();
		turnText.setEditable(false);
		backgroundPane = new GridPane();
		coordinates = new int[2]; /* Construct for coordinates of node to be stored. */
		src = new Rectangle();
		dst = new Rectangle();
		tileMaxWidth = width - 100;
		tileMaxHeight = height - 100;
		sizeFactor = size;
		tileSize = tileMaxWidth / (10 * sizeFactor);
		tileDivHor = 11 * sizeFactor;
		tileDivVer = 9 * sizeFactor;
	}

	/* Adds a new city to the map. */
	private void addNode() {
		Rectangle node = new Rectangle();
		String name = WorldTools.getKingdomName();
		Kingdom kingdom = new Kingdom(name);
		Text nodeName = new Text(name);
		int[] xy = new int[2];

		for(int i = 0; i < xy.length; i++) {
			xy[i] = (coordinates[i]);
		}
		
		if(!(WorldTools.checkNeighbors(kingdomMap, coordinates, nodeMap))) {
			return;
		}
		
		if(xy[1] < 10 || xy[0] < 10) {
			return;
		} else if(xy[1] > tileMaxHeight || xy[0] > tileMaxWidth) {
			return;
		}
		
		wealth -= 10;
		
		/* Adds entry to kingdomMap. */
		kingdomMap.put(node, kingdom); 
		nodeMap.put(node, xy);
		coordinatesMap.put(name, xy);
		
		node.setTranslateY(coordinates[0]);
		node.setTranslateX(coordinates[1]);
		
		nodeName.setTranslateX(coordinates[1]);
		nodeName.setTranslateY(coordinates[0]);
		nodeName.resize(height / 100, width / 100);
		
		node.setId(name);
		node.setWidth(tileSize);
		node.setHeight(tileSize);
		node.setFill(WorldTools.FLAG);
	
		
		kingdoms.put(name, kingdom);
		
		
		worldPane.getChildren().addAll(node, nodeName);
	}
	
	/* Sets up stage. */
	public void setWithColor(Stage world, Color c) {
		int blockSize = tileSize;
		int x = 0, y = 0;
		background = new ArrayList<ArrayList<Rectangle>>();
		
	
		backgroundPane.setMaxHeight(tileMaxHeight);
		backgroundPane.setPadding(new Insets(10));
		
		
		for(int r = 0; r < tileDivVer; r++) {
			ArrayList<Rectangle> row = new ArrayList<Rectangle>();
			for(int cols = 0; cols < tileDivHor; cols++) {
			Rectangle newBlock = new Rectangle();
			
			newBlock = WorldTools.addTile(newBlock);
			newBlock.setWidth(blockSize);
			newBlock.setHeight(blockSize);
			newBlock.setTranslateX(x);
			newBlock.setTranslateY(y);
			row.add(newBlock);
			
			backgroundPane.getChildren().add(newBlock);
			
			x += blockSize;
			}
			
			background.add(row);
			y+= blockSize;
			x = 0;
		}
		y = 0;
		
		controls.setTranslateY(WorldTools.getBackgroundHeight(height));
		controls.resize(width, 25);
		
		
		worldPane.resize(width, height);
		worldPane.getChildren().addAll(backgroundPane, controls, innerControls);
		
		cityText.resize(150.0, 20.0);
		
		Scene scene = new Scene(worldPane, width, height);
		
		world.setScene(scene);
		world.show();
		world.setResizable(false);
	}
	
	@Override	/* JavaFX method for starting application. Calls run() to initiate the world. */
	public void start(Stage world) throws Exception {
		setWithColor(world, Color.AQUA);
		run();
	}

	@Override	/* Starts world. */
	public void run() {
		updateControlListeners();
		updateKingdomListeners();
	}

	/* Turn activities.  */
	private void update() {
		turn++;
		run();
		updateWealth();
		cityText.clear();
		updateKingdomListeners();
	}
	
	/* Updates wealths' of Kingdoms.  */
	private void updateWealth() {
		for(Kingdom k : kingdoms.values()) {
			k.cityAffairs();
			wealth += k.getGDP();
		}
	}
	
	/* Adds control features to stage.  */
	private void updateControlListeners() {
		
		cityText.setMinWidth(width / 4);
		controls.getChildren().clear();
		turnText.setText("Turn: " + Integer.toString(turn));

		nodeButton.setText("New City");
		nodeButton.autosize();
		nodeButton.setOnAction(e ->{
			if( wealth < 100) {
				cityText.setText("-$");
				return;
			} else {
				addNode();
			}
		});
		
		turnButton.setText("Next Turn");
		turnButton.autosize();
		turnButton.setOnAction(e ->{
			update();
		});
		
		roadButton.setText("Add a road");
		roadButton.autosize();
		roadButton.setOnAction(e ->{
			addRoad();
			
			auxRoadButton.setText("Confirm");
			auxRoadButton.autosize();
			controls.add(auxRoadButton, 0, 2);
		
		});
		
		
		auxRoadButton.setOnAction(e ->{
			finishAddRoad();
		});
	
		
		if(kingdoms.size() >= 2) {
			controls.add(nodeButton, 0, 0);
			controls.add(turnButton, 1, 0);
			controls.add(roadButton, 0, 1);
			controls.add(turnText, 2, 0);
			controls.add(cityText, 3, 0);
		} else {
			controls.add(nodeButton, 0, 0);
			controls.add(turnButton, 1, 0);
			controls.add(turnText, 2, 0);
			controls.add(cityText, 3, 0);
			controls.add(coordinatesText, 4, 0);
		}
		
	}
	
	/* Maintains consistency between controls and graphics.  */
	private void updateKingdomListeners() {
		if(sizeFactor == 1) {
			sizeFactor = sizeFactor * 10;
		}
		
		for(Node r : backgroundPane.getChildren()) {	
			r.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override 
				public void handle(MouseEvent event) {
				   coordinates[1] = (int) event.getSceneX();
	               coordinates[0] = (int) event.getSceneY();
	               coordinatesText.setText((event.getSceneX() / sizeFactor) + ", " + (event.getSceneY()/ sizeFactor));
	            }
			});
		
		}
		
		for(Rectangle circle : nodeMap.keySet()) {
			circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					selectedNode = (Rectangle) event.getSource();
					cityText.setText(kingdomMap.get(selectedNode).getName());
					
					try {
						kingdomMap.get(selectedNode).selectKingdom();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
		}
	
	}
	
	/* Updates control panel for road city selection.  */
	private void addRoad() {
		disableKingdomViewers();
		cityText.setText("Click Source");
		roadButton.setText("Next: ");
			
	
			
		roadButton.setText("Add new Road");
			
		roadButton.setOnAction(e1 ->{
				src = selectedNode;
				roadButton.setText("Final: ");
		});	
	}
	
	/* Updates control panel for road confirmation.  */
	private void finishAddRoad() {
			dst = selectedNode;
			roadButton.setText("Construct");
			
			roadButton.setOnAction(e -> {
				constructRoad();
				roadButton.setText("Add Road");
			});
	}
	
	/* Technical checks and state changes before beginning construction of a road. */
	private void constructRoad() {
		if(nodeMap.keySet().contains(src) && nodeMap.keySet().contains(dst)) {
	
			drawRoad(nodeMap.get(src)[1], nodeMap.get(src)[0], nodeMap.get(dst)[1], nodeMap.get(dst)[0]);	
		} else {
			cityText.setText("Invalid Road Parameters.");
		}
		
		cityText.setText("Road Created");
		wealth -= WorldTools.getEuclideanDistance(nodeMap.get(src), nodeMap.get(dst));
	}
	
	/* Graphically adds a road tile image to the world.  */
	private void constructRoadTile(int index, int constant, boolean vertical) {
		Rectangle roadTile = new Rectangle();
		roadTile.setFill(WorldTools.ROAD);
		roadTile.setWidth(tileSize);
		roadTile.setHeight(tileSize);
		
		
		backgroundPane.getChildren().add(roadTile);
		
		if(vertical) {
			roadTile.setTranslateY(index);
			roadTile.setTranslateX(constant);
		} else {
			roadTile.setTranslateX(index);
			roadTile.setTranslateY(constant);
		}
		
	}
	
	/* Repeats the drawing of a road tile between two given cities. */
	private void drawRoad(int sourceX, int sourceY, int destX, int destY) {
		int stop_x = Math.max(sourceX, destX), start_x = Math.min(sourceX, destX);
		int stop_y = Math.max(sourceY, destY), start_y = Math.min(sourceY, destY);
			
		for(int index = start_x; index <= stop_x; index++) {
				constructRoadTile(index, sourceY, false);
		}
			
				
		for(int index = start_y; index <= stop_y; index++) {
			constructRoadTile(index, sourceY, true);
		}
	}
	
	/* Prevents city windows from opening upon clicking cities. */
	private void disableKingdomViewers() {
		for(Rectangle circle : nodeMap.keySet()) {
			circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					selectedNode = (Rectangle) event.getSource();
					cityText.setText(kingdomMap.get(selectedNode).getName());
				}
				
			});
		}
	}

}
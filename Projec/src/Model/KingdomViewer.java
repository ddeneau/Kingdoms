package Model;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/* GUI Manager class for a kingdom. Relies on API of Kingdom to render
 * the kingdom passed in through the constructor. */
public class KingdomViewer extends Application {
	Kingdom kingdom;
	Pane kingdomPane;
	ArrayList<ArrayList<Rectangle>> cityBlocks;
	int width, height;
	
	
	public KingdomViewer(Kingdom kingdom) {
		this.kingdom = kingdom;
		kingdomPane = new Pane();
		cityBlocks = new ArrayList<ArrayList<Rectangle>>();
		width = 500;
		height = 500;
	}

	@Override
	public void start(Stage kingdom) throws Exception {
		HBox controls = new HBox();
		Scene scene = new Scene(kingdomPane, 500, 500);
		kingdom = new Stage();
		

		
		kingdomPane.getChildren().add(controls);
		
		
		
		controls.autosize();
		
		kingdom.setScene(scene);
		kingdom.show();
		initializeCity();
	
	}
	
	private void initializeCity() throws InterruptedException {		
		
		for(int rows = 0; rows < height; rows++) {
			ArrayList<Rectangle> row = new ArrayList<Rectangle>();
			for(int cols = 0; cols < width; cols++) {
			
				
				Rectangle tile = new Rectangle();
				
				tile.setWidth(10);
				tile.setHeight(10);
				
				tile.setFill(WorldTools.GROUND);
				
				if(rows == 0 || rows == (height - 1)) {
					tile.setFill(WorldTools.WALL);
				} else if(cols == 0 || cols < (width -1 )) {
					tile.setFill(WorldTools.WALL);
				}
				
				kingdomPane.getChildren().add(tile);
				tile.setTranslateX(cols);
				tile.setTranslateY(rows);
				row.add(tile);
			}
			cityBlocks.add(row);
		}
		
		
	}
		
}

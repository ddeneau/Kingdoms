package Utility;

import Model.World;
import Model.WorldTools;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Driver extends Application {
	private static int height, width;
	private static Pane startMenu;
	private static Text startText = new Text("Start");
	private static int size;
	private World world;

	public static void main(String[] args) {
		size = 0;
		width = 900;
		height = 900;
		WorldTools.initialize();
		Application.launch(args);
	}

	/* Populate */
	@Override
	public void start(Stage window) throws Exception {
		GridPane buttonPane = new GridPane();
		Button startButton = new Button();
		Slider sizeSlider = new Slider();

		startMenu = new Pane();
		
		startMenu = new GridPane();
		startMenu.resize(400, 400);
		startMenu.setVisible(true);
		
		sizeSlider.setShowTickMarks(true);
		sizeSlider.setShowTickLabels(true);
		sizeSlider.setBlockIncrement(4);
		sizeSlider.setMin(1);
		sizeSlider.setMax(10);
		sizeSlider.setMajorTickUnit(4);
		sizeSlider.setMinorTickCount(1);
		sizeSlider.setMinWidth(100);
		sizeSlider.setSnapToTicks(true);
		sizeSlider.setTranslateX(75);
		sizeSlider.setTranslateY(50);
	

		startButton.setText("");

		buttonPane.getChildren().addAll(sizeSlider,startButton);
		
		startButton.setPrefSize(80, 20);
		startButton.setGraphic(startText);
		startButton.setTranslateX(150);
		startButton.setTranslateY(100);
		startButton.setBackground((new Background(new BackgroundFill(
								   Color.LIGHTGOLDENRODYELLOW, new CornerRadii(2.0), new Insets(0)))));
		
		startMenu.setBackground(new Background(new BackgroundFill(
								Color.ANTIQUEWHITE, new CornerRadii(2.0), new Insets(0))));
		startMenu.getChildren().addAll(buttonPane);
		
		Scene scene = new Scene(startMenu, 400, 200);

		window.setScene(scene);
		window.show();

		sizeSlider.valueProperty().addListener(e -> {
			size = (int) sizeSlider.getValue();
			
			if(size == 0) {
				size = 5;
			}
		});
		
		startButton.setOnAction(e -> {
			WorldTools.setSize(size);
			
			try {
				world = new World(width, height, size);
				world.start(new Stage());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
;	
	}

}
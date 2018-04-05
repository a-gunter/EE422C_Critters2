package assignment5;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Insets;

import java.io.PrintStream;
import java.io.File;
import java.util.ArrayList;

import assignment5.Critter;




public class MainWindow {
	private static BorderPane bPane = new BorderPane();
	private static VBox sidePanel = new VBox();
	static VBox statsCheckBoxes = new VBox();
	protected static GridPane grid = new GridPane();
	private static Label controlsLabel;
	static TextField numOfSteps;
	static TextField numOfCritter;
	static TextField seed;
	static ComboBox<String> chooseCritter;
	static Button stepBtn;
	static Button makeBtn;
	static Button showBtn; //used for animations
	static Button runStatsBtn; //add checkboxes
	static Button seedBtn;
	static Button quitBtn;
	private static PrintStream ps;
	private static String myPackage;
	static int prefWidth = 125;
	static int diffHeight = 8;
	
	// Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
	
    /**
     * Sets up the mainwindow with the buttons, grid, and text output
     * @param primaryStage
     */
	public static void init(Stage primaryStage) {	
		//setup layout
		primaryStage.setTitle("Critters Simulation");
		grid.setGridLinesVisible(true);
		sidePanel.setStyle("-fx-border-color: black;-fx-font-family: \"Courier New\"");
		sidePanelInit();
		
		// test setup for grid
		double testWidth = 520;
		double testHeight = 520;
		grid.setMaxHeight(testHeight);
		grid.setMaxWidth(testWidth);
		for(int i = 0; i < Params.world_height; i++) {
			RowConstraints constr = new RowConstraints(testHeight / Params.world_width);
//			RowConstraints constr = new RowConstraints();
//			constr.setPercentHeight(Params.world_height);
			grid.getRowConstraints().add(constr);
		}
		for(int i = 0; i < Params.world_width; i++) {
			ColumnConstraints constr = new ColumnConstraints(testWidth / Params.world_width);
//			ColumnConstraints constr = new ColumnConstraints();
//			constr.setPercentWidth(Params.world_width);
			grid.getColumnConstraints().add(constr);
		}
		
		//text stuff
		TextArea console = new TextArea();
		Console os = new Console(console);
		ps = new PrintStream(os);
		console.setStyle("-fx-border-color: black;-fx-font-family: \"Courier New\"");
	    System.setOut(ps);
		
		//add panes to main pane
		bPane.setLeft(sidePanel);
		bPane.setCenter(grid);
		bPane.setBottom(console);
		
		//init scene
		Scene scene = new Scene(bPane, 850, 700);
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	/**
	 * Sets up the buttons and associated text fields/combo 
	 * boxes for the side panel
	 */
	private static void sidePanelInit() {
		Insets mainInsets = new Insets(diffHeight, 15, diffHeight, 15);
		//top label
		controlsLabel = new Label("Controls");
		sidePanel.getChildren().add(controlsLabel);
		sidePanel.getChildren().add(new Separator());
		VBox.setMargin(controlsLabel, new Insets(diffHeight*2, 15, diffHeight, 15));
		
		//step button
		stepBtn = new Button("Step");
		stepBtn.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(stepBtn);
		VBox.setMargin(stepBtn, new Insets(diffHeight, 15, diffHeight/2, 15));
		
		numOfSteps = new TextField("1");
		numOfSteps.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(numOfSteps);
		sidePanel.getChildren().add(new Separator());
		VBox.setMargin(numOfSteps, new Insets(diffHeight/2, 15, diffHeight, 15));
		
		//make button
		makeBtn = new Button("Make");
		makeBtn.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(makeBtn);
		VBox.setMargin(makeBtn, new Insets(diffHeight, 15, diffHeight/2, 15));
		
		chooseCritter = new ComboBox<String>();
		chooseCritter.setPrefWidth(prefWidth);
		chooseCritter.getItems().addAll(getCritterClasses());
		sidePanel.getChildren().add(chooseCritter);
		VBox.setMargin(chooseCritter, new Insets(diffHeight/2, 15, diffHeight/2, 15));
		
		numOfCritter = new TextField("1");
		numOfCritter.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(numOfCritter);
		sidePanel.getChildren().add(new Separator());
		VBox.setMargin(numOfCritter, new Insets(diffHeight/2, 15, diffHeight, 15));
		
		//show button
		showBtn = new Button("Animate");
		showBtn.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(showBtn);
		sidePanel.getChildren().add(new Separator());
		VBox.setMargin(showBtn, mainInsets);
		
		//run stats button
		runStatsBtn = new Button("Stats");
		runStatsBtn.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(runStatsBtn);
		VBox.setMargin(runStatsBtn, mainInsets);
		runStatsBtn.setDisable(true);
		
		sidePanel.getChildren().add(statsCheckBoxes); //for stats
		sidePanel.getChildren().add(new Separator());
		
		//seed button
		seedBtn = new Button("Seed");
		seedBtn.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(seedBtn);
		VBox.setMargin(seedBtn, new Insets(diffHeight, 15, diffHeight/2, 15));
		
		seed = new TextField("");
		seed.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(seed);
		sidePanel.getChildren().add(new Separator());
		VBox.setMargin(seed, new Insets(diffHeight/2, 15, diffHeight, 15));
		
		//quit button
		quitBtn = new Button("Quit");
		quitBtn.setPrefWidth(prefWidth);
		sidePanel.getChildren().add(quitBtn);
		VBox.setMargin(quitBtn, new Insets(diffHeight, 15, diffHeight*2, 15));
				
	}
	
	/**
	 * Gets the Critter subclasses in the directory
	 * @return Arraylist of critter subclasses
	 */
	private static ArrayList<String> getCritterClasses() {
		String[] paths;
		ArrayList<String> ret = new ArrayList<>();
		String dir = System.getProperty("user.dir") + "\\assignment5";
		paths = new File(dir).list();
		for(String s:paths) {
			try {
				s = s.substring(0, s.length()-6);
				Class<?> c = Class.forName(myPackage + "." + s);
				if(Critter.class.isAssignableFrom(c) && !s.equals("Critter")) {
					ret.add(s);
				}
			} catch (Exception e) {
				//do nothing it doesnt matter
			}
		}
		return ret;
	}
	
	
	/*
	 * Paints the border kind of couldnt really get it to work
	 */
	public static void paint() {
		grid.getChildren().clear(); // clean up grid.
		double vert = grid.getWidth()/(Params.world_width + 1);
		double hoz = grid.getHeight()/(Params.world_height + 1);
		
		for(int col = 1;col < Params.world_width + 1;col++) grid.add(new Rectangle(0, vert, 0, vert), col, 0); 
		for(int col = 1;col < Params.world_width + 1;col++) grid.add(new Line(0, vert, 0, vert), col, Params.world_height + 1);
		for(int row = 1;row < Params.world_height + 1;row++) grid.add(new Line(0, hoz, 0, hoz), 0, row); 
		for(int row = 1;row < Params.world_height + 1;row++) grid.add(new Line(0, hoz, 0, hoz), Params.world_width + 1, row);
		
		/*
		for (int i = 0; i < Params.; i++) {
			Shape s = getIcon(i);	// convert the index to an icon.
			grid.add(s, i, i); // add the shape to the grid.
		}
		*/
		
	}
}

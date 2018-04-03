package assignment5;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * <Matthew Davis>
 * <mqd224>
 * <15510>
 * <Austin Gunter>
 * <asg2523>
 * <15510>
 * Spring 2018
 */

import java.lang.reflect.Method;

//import java.util.ArrayList;

import assignment5.Critter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
	
	
	// Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main loop of program
     */
	@Override
	public void start(Stage primaryStage) {
		try {			
			//setup main window
			MainWindow.init(primaryStage);
			
			//setup buttons
			buttonInits();
			
		} catch(Exception e) {
			e.printStackTrace();		
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	
	/**
	 * Sets up the button functionalities
	 */
	private static void buttonInits() {
		//step button
		MainWindow.stepBtn.setOnAction(e->{
			stepButton();
		});
		
		//make button
		MainWindow.makeBtn.setOnAction(e->{
			makeButton();
		});
		
		//show button (animation stuff)
		MainWindow.showBtn.setOnAction(e->{
			//something random rn
			MainWindow.paint();
		});
		
		//run stats button
		MainWindow.runStatsBtn.setOnAction(e->{
			runStatsButton();
		});
		
		//seed button
		MainWindow.seedBtn.setOnAction(e->{
			seedButton();
		});
		
		//quit button
		MainWindow.quitBtn.setOnAction(e->{
			System.exit(0);
		});
	}
	
	/**
     * Prints error message for parsing error
     * @param input Input from keyboard
     */
    private static void errorProcessing(String input) {
    	System.out.print("error processing: " + input); //need to fix
    }
    
    /**
     * Provides functionality for step button, steps amount of times in the
     * num of steps textbox, prints error processing if parsing error
     */
    private static void stepButton() {
    	try {
			int steps = Integer.parseInt(MainWindow.numOfSteps.getText());
			if(steps > 0) {
				System.out.println(steps + " timesteps");
				for(int i = 0; i < steps; i++)
					Critter.worldTimeStep();
			} else 
				System.out.println("Input must be greater than 0");
		} catch(Exception ex) {
			errorProcessing(MainWindow.numOfSteps.getText());
			MainWindow.seed.setText("");
		}
    }
    
    /**
     * Provides functionality for make button, makes critter selected 
     * in the dropdown and makes the amount of them in the text field
     */
	@SuppressWarnings("null")
    private static void makeButton() {
    	try {
			String critter = MainWindow.chooseCritter.getValue().toString();
			if(critter != null || !critter.isEmpty()) {
				for(int i = 0;i < Integer.parseInt(MainWindow.numOfCritter.getText());i++) {
					Critter.makeCritter(critter);
					System.out.println("Critter Made!");
				}
				//add checkbox for runstats
				if(MainWindow.runStatsBtn.isDisabled()) MainWindow.runStatsBtn.setDisable(false);
				boolean containCheck = false;
				for(Node n: MainWindow.statsCheckBoxes.getChildren()) {
					CheckBox c = (CheckBox) n;
					if(c.getText().equals(critter)) {
						containCheck = true;
						break;
					}
				}
				if(!containCheck) {
					CheckBox newCheckBox = new CheckBox(critter);
					newCheckBox.setPrefWidth(MainWindow.prefWidth);
					MainWindow.statsCheckBoxes.getChildren().add(newCheckBox);
					VBox.setMargin(newCheckBox, new Insets(0, 15, MainWindow.diffHeight, 15));
				}
			}
			else
				System.out.println("Must select Critter");
		} catch (InvalidCritterException ex) {
			errorProcessing(MainWindow.chooseCritter.getValue().toString());
		} catch (Exception ex) {
			errorProcessing(MainWindow.numOfCritter.getText());
		}
    }
	
	/**
	 * Does runstats for the critters checked
	 */
	private static void runStatsButton() {
		try {
			for(Node n: MainWindow.statsCheckBoxes.getChildren()) {
				CheckBox ch = (CheckBox) n;
				if(ch.isSelected()) {	
					Class<?> c = Class.forName(myPackage + "." + ch.getText()); 
					Method method = c.getMethod("runStats", java.util.List.class);
					System.out.println(method.invoke(null, Critter.getInstances(ch.getText())));
				}
			}
		} catch (Exception ex) {
			errorProcessing("Something went wrong");
		}
	}
	
	/**
	 * Seeds the random function with the number provided
	 */
	private static void seedButton() {
		try {
			int seed = Integer.parseInt(MainWindow.seed.getText());
			if(seed > 0)
				Critter.setSeed(seed);
			else 
				System.out.println("Input must be greater than 0");
		} catch(Exception ex) {
			errorProcessing(MainWindow.seed.getText());
			MainWindow.seed.setText("");
		}
	}
    
}

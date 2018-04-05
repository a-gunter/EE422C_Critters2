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

import javafx.animation.AnimationTimer;

/**
 * Animation class used to animate the displayworld function from critter
 * @author mattqd97
 *
 */
public class Anim extends AnimationTimer{
	
	int steps;
	double initTime;
	double time = 0.5;
	
	/**
	 * Initalizes animator to number of steps and sets speed from slider
	 * @param steps
	 * @param slider
	 */
	public Anim(int steps, double slider) {
		this.steps = steps;
		initTime = time*1/slider;
		time = initTime;
	}
	
	@Override
	public void handle(long now) {
		handler();
	}
	
	@Override
	public void start() {
		super.start();
		MainWindow.setButtons(false);
	}
	
	/**
	 * Animates, displays world using timer
	 */
	private void handler() {
		time -= 0.01;
		
		if(time < 0) {
			Critter.worldTimeStep();
			Critter.displayWorld();
			steps--;
			time = initTime;
		}
		
		if(steps <= 0) {
			stop();
			MainWindow.setButtons(true);
		}
		
	}
	
}

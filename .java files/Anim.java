package assignment5;

import javafx.animation.AnimationTimer;

public class Anim extends AnimationTimer{
	
	int steps;
	double initTime;
	double time = 0.5;
	
	public Anim(int steps, double slider) {
		this.steps = steps;
		initTime = time*1/slider;
		time = initTime;
	}
	
	@Override
	public void handle(long now) {
		handler();
	}
	
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
		}
		
	}
}

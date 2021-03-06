package assignment5;
/* CRITTERS Critter4.java
 * EE422C Project 4 submission by
 * <Matthew Davis>
 * <mqd224>
 * <15510>
 * <Austin Gunter>
 * <asg2523>
 * <15510>
 * Spring 2018
 */


/**
 * Unique Critter 4
 * Doesn't reproduce in order to conserve energy
 * Likes to save energy for fights, so only move 50% of the time. Lets the other critters come to him.
 * Can't walk, can only run or stand still.
 * Always accepts a fight
 * @author Austin
 *
 */
public class Critter4 extends Critter{
	
	private int dir;
	private static int numFights = 0; // number of fights that have been picked by all Critter4s.
	
	/**
	 * Constructor which sets direction
	 */
	public Critter4() {
		dir = Critter.getRandomInt(8);
	}
	
	/**
	 * Only moves about half of the time.
	 * When Critter4 decides to move, checks to see if another Critter occupies the spot it would move to.
	 * Critter4 will only move whenever there's another Critter to fight at that spot.
	 */
	@Override
	public void doTimeStep() {
		int shouldRun = Critter.getRandomInt(2);
		if(shouldRun == 1) {
			String occupied = look(dir, true);
			if(occupied != null) {
				run(dir);
				dir = Critter.getRandomInt(8);
			} else {
				dir = Critter.getRandomInt(8);
			}
		}
	}

	/**
	 * Always accepts a fight
	 */
	@Override
	public boolean fight(String opponent) {
		numFights++;
		return true;
	}
	
	/**
	 * 
	 * @param critters List of Critter4s
	 */
	public static String runStats(java.util.List<Critter> critter4s) {
		return "" + critter4s.size() + " total Critter4s, and " 
				+ numFights + " fights have been picked";
	}
	
	/**
	 * Returns String "4" as specified in documentation
	 */
	public String toString() {
		return "4";
	}
	
	//you can change this i just added it to remove the error
	@Override
	public CritterShape viewShape() { return CritterShape.TRIANGLE; }
	
	@Override
	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.RED; }
	
}
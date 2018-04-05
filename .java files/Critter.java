package assignment5;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * <Matthew Davis>
 * <mqd224>
 * <15510>
 * <Austin Gunter>
 * <asg2523>
 * <15510>
 * Spring 2018
 */


import java.util.List;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	
	//Random functionality
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	private boolean hasMoved;	// used to check if a creature is able to move in an encounter
	private boolean isFighting;  // used to check if a creature can move into a spot where another creature already exists.
	private static final int WALK = 1;
	private static final int RUN = 2;
	
	/**
	 * Tries to move one space in given direction, if another Critter is in that space (while fighting) or it
	 *  has already moved it doesn't move but still deducts energy
	 * @param direction 0-7, with 0 being the right direction, 1 being up and right, etc
	 */
	protected final void walk(int direction) {
		if(canMove(direction, WALK)) {	// check if walking is a valid option for the critter
			move(direction);
			this.energy -= Params.walk_energy_cost;
			this.hasMoved = true;
		} else {
			this.energy -= Params.walk_energy_cost;  // if invalid option, reduce energy anyway.
		}
		
		if(this.energy < 0)
			this.energy = 0;	// change the energy to 0 here, in case of negative energy mid-encounter.
	}
	
	/**
	 * Tries to move two spaces in given direction, if another Critter is in that space (while fighting) or it
	 *  has already moved it doesn't move but still deducts energy
	 * @param direction 0-7, with 0 being the right direction, 1 being up and right, etc
	 */
	protected final void run(int direction) {
		if(canMove(direction, RUN)) {
			move(direction);
			move(direction);
			this.energy -= Params.run_energy_cost;
			this.hasMoved = true;
		} else {
			this.energy -= Params.run_energy_cost;
		}
		
		if(this.energy < 0)
			this.energy = 0;	// change the energy to 0 here, in case of negative energy mid-encounter.
	}
	
	/**
	 * Helper function for walk and run, moves Critter in given direction
	 * @param direction 0-7, with 0 being the right direction, 1 being up and right, etc
	 */
	private void move(int direction) {
		switch(direction) {
			case 0:
				moveRight();
				break;
			case 1:
				moveRight();
				moveUp();
				break;
			case 2:
				moveUp();
				break;
			case 3:
				moveUp();
				moveLeft();
				break;
			case 4:
				moveLeft();
				break;
			case 5:
				moveLeft();
				moveDown();
				break;
			case 6:
				moveDown();
				break;
			case 7:
				moveDown();
				moveRight();
				break;
			default:
		}
	}
	
	/**
	 * Helper function for move
	 */
	private void moveRight() {
		if(this.x_coord >= Params.world_width - 1)
			x_coord = 0;
		else
			x_coord++;
	}
	/**
	 * Helper function for move
	 */
	private void moveLeft() {
		if(this.x_coord <= 0)
			x_coord = Params.world_width - 1;
		else
			x_coord--;
	}
	/**
	 * Helper function for move
	 */
	private void moveUp() {
		if(this.y_coord <= 0)
			y_coord = Params.world_height - 1;
		else
			y_coord--;
	}
	/**
	 * Helper function for move
	 */
	private void moveDown() {
		if(this.y_coord >= Params.world_height - 1)
			y_coord = 0;
		else
			y_coord++;
	}
	
	
	/**
	 * If Critter has enough energy, gives offspring half energy and moves it one space away,
	 *  as well as halving its own energy
	 * @param offspring Hypothetical offspring given enough energy
	 * @param direction Direction for hypothetical offspring to move when birthed
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy) {
			return;
		} else {
			offspring.energy = this.energy / 2;
			offspring.x_coord = this.x_coord;
			offspring.y_coord = this.y_coord;
			offspring.move(direction);
			if(this.energy % 2 == 1)
				this.energy = (this.energy / 2) + 1;
			else
				this.energy = this.energy / 2;
			
			babies.add(offspring);
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String opponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			//make critter
			Class<?> c = Class.forName(myPackage + "." + critter_class_name); //must be assignment4.Class
			Critter crit = (Critter)c.newInstance();
			//initialize stats
			crit.x_coord = getRandomInt(Params.world_width);
			crit.y_coord = getRandomInt(Params.world_height);
			crit.energy = Params.start_energy;
			//add to pop
			population.add(crit); // I think it should be this not babies from the documentation
		} catch (Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		for(Critter c: population) {
			try {
				if(c.getClass() == Class.forName(myPackage + "." + critter_class_name)) {
					result.add(c);
				}
			} catch (Exception e){
				throw new InvalidCritterException(critter_class_name);
			}
		}
		return result;
	}
	
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		String ret = "" + critters.size() + " critters as follows -- ";
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			ret += prefix + s + ":" + critter_count.get(s);
			prefix = ", ";
		}
		return ret;		
	}
	
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		//little unclear on what exactly this should do but..
		population.clear();
		babies.clear();
	}
	
	/**
	 * Performs timestep, updating world
	 */
	public static void worldTimeStep() {
		// doTimeSteps for each critter
		for(Critter c: population) {
			c.doTimeStep();
		}
		
		// remove dead critters after each Critter has done their timestep
		removeDead();
		
		// do fights ie encounters
		doEncounters();

		//rest energy stuff (maybe?)
		for(Critter c: population) {
			c.energy -= Params.rest_energy_cost;
		}
		
		// generate algae genAlgae()
		for(int i = 0; i < Params.refresh_algae_count; i++) {
			Algae a = new Algae();
			a.setEnergy(Params.start_energy);
			a.setX_coord(Critter.getRandomInt(Params.world_width));
			a.setY_coord(Critter.getRandomInt(Params.world_height));
			population.add(a);
		}
		
		// move babies to general population
		population.addAll(babies);
		babies.clear();
		
		//remove dead critters
		removeDead();
		
		// reset all critters' hasMoved/isFighting to false at the end of worldTimeStep
		for(Critter c : population) {
			c.hasMoved = false;
			c.isFighting = false;
		}
	}
	
	private static void removeDead() {
		List<Critter> dead = new java.util.ArrayList<Critter>();
		for(Critter c: population) {
			if(c.energy <= 0) {
				dead.add(c);
			}
		}
		population.removeAll(dead); //other implementation was throwing an exception
	}
	
	/**
	 * NEEDS TO BE REMOVED
	 */
	
//	public static void displayWorld() {
//		String[][] grid = new String[Params.world_width + 2][Params.world_height + 2];
//		
//		//set corners
//		grid[0][0] = "+";
//		grid[0][Params.world_height + 1] = "+";
//		grid[Params.world_width + 1][0] = "+";
//		grid[Params.world_width + 1][Params.world_height + 1] = "+";
//		
//		//set border
//		for(int col = 1;col < Params.world_width + 1;col++) grid[col][0] = "-";
//		for(int col = 1;col < Params.world_width + 1;col++) grid[col][Params.world_height + 1] = "-";
//		for(int row = 1;row < Params.world_height + 1;row++) grid[0][row] = "|";
//		for(int row = 1;row < Params.world_height + 1;row++) grid[Params.world_width + 1][row] = "|";
//		
//		//put critters in
//		for(Critter c: population) {
//			try {
//				grid[c.x_coord + 1][c.y_coord + 1] = c.toString(); //compensate for border
//			} catch(Exception e) {
//				System.out.println(c.x_coord + " " + c.y_coord);
//				System.out.println(e);
//			}
//		}
//		
//		for(int row = 0;row < Params.world_height + 2;row++) {
//			for(int col = 0;col < Params.world_width + 2;col++) {
//				if(grid[col][row] == null) System.out.print( " ");
//				else System.out.print(grid[col][row]);
//			}
//			System.out.println();
//		}
//		
//	}
	
	public static void displayWorld() {
		//GridPane grid = new GridPane();
		MainWindow.grid.getChildren().clear();
		double gridCellSize = MainWindow.grid.getWidth() / Params.world_width;
		if (!population.isEmpty()) {
			for (Critter c : population) {
				if (c.energy > 0) {
					CritterShape cShape = c.viewShape();
					switch (cShape) {
					case CIRCLE:
						Circle circle = new Circle(gridCellSize / 2);
						circle.setStroke(c.viewOutlineColor());
						circle.setFill(c.viewFillColor());
						MainWindow.grid.add(circle, c.x_coord, c.y_coord);
						break;
					case SQUARE:
						Rectangle rect = new Rectangle(gridCellSize, gridCellSize);
						rect.setStroke(c.viewOutlineColor());
						rect.setFill(c.viewFillColor());
						MainWindow.grid.add(rect, c.x_coord, c.y_coord);
						break;
					case TRIANGLE:
						Polygon triangle = new Polygon();
						triangle.setStroke(c.viewOutlineColor());
						triangle.setFill(c.viewFillColor());
						Double[] verticesT = new Double[] {0.0, gridCellSize, gridCellSize, gridCellSize, gridCellSize / 2, 0.0};
						triangle.getPoints().addAll(verticesT);
						MainWindow.grid.add(triangle, c.x_coord, c.y_coord);
						break;
					case DIAMOND:
						Polygon diamond = new Polygon();
						diamond.setStroke(c.viewOutlineColor());
						diamond.setFill(c.viewFillColor());
						Double[] verticesD = new Double[] {gridCellSize / 2, 0.0, 0.0, gridCellSize / 2, gridCellSize / 2, gridCellSize, gridCellSize, gridCellSize / 2};
						diamond.getPoints().addAll(verticesD);
						MainWindow.grid.add(diamond, c.x_coord, c.y_coord);
						break;
					case STAR:
						Polygon star = new Polygon();
						star.setStroke(c.viewOutlineColor());
						star.setFill(c.viewFillColor());
						Double[] verticesS = new Double[] {0.5*gridCellSize, 0.0,
														   0.4*gridCellSize, 0.3*gridCellSize,
														   0.0, 0.3*gridCellSize,
														   0.3*gridCellSize, 0.55*gridCellSize,
														   0.2*gridCellSize, gridCellSize,
														   0.5*gridCellSize, 0.7*gridCellSize,
														   0.8*gridCellSize, gridCellSize,
														   0.7*gridCellSize, 0.55*gridCellSize,
														   gridCellSize, 0.3*gridCellSize,
														   0.6*gridCellSize, 0.3*gridCellSize
														   };
						star.getPoints().addAll(verticesS);
						MainWindow.grid.add(star, c.x_coord, c.y_coord);
						break;
					}
				}
			} 
		}
	}
	
	/**
	 * Checks if a critter can move or not, depending on its past actions. Critters can only move once per timestep,
	 * and critters cannot move into a pre-occupied space while fighting.
	 * @param direction The direction to move
	 * @param numSteps  1 for walk, 2 for run
	 * @return True if Critter can move, false otherwise.
	 */
	private boolean canMove(int direction, int numSteps) {
		if(this.hasMoved || this.energy <= 0) { // can only move once, and can't move if dead.
			return false;
		}  else if(this.isFighting) {  // if fighting, can't move where another critter exists
			if(numSteps == WALK) {
				if(search(direction, WALK) != null)
					return false;
				else
					return true;
			} else if(numSteps == RUN){
				if(search(direction, RUN) != null)
					return false;
				else
					return true;
			}
		}
		return true;
	}
	
	/**
	 * Simulates & resolves encounters for every living critter.
	 */
	private static void doEncounters() {
		for(int i = 0; i < population.size(); i++) {	// compare critters with every other critter to see if there are overlaps
			Critter A = population.get(i);
			if(A.energy > 0) {  // don't want encounters with already-dead critters
				for(int j = i + 1; j < population.size(); j++) { 
					Critter B = population.get(j);
					if(B.energy > 0) { // no dead critters
						if((A.x_coord == B.x_coord) && (A.y_coord == B.y_coord)) {
							A.isFighting = true;
							B.isFighting = true;
							boolean aWantsToFight = A.fight(B.toString()); // fight will call walk/run depending on the critter
							boolean bWantsToFight = B.fight(A.toString());
							if((A.x_coord == B.x_coord) && (A.y_coord == B.y_coord)) {
								int fightA, fightB;
								if(A.energy > 0 && aWantsToFight)
									fightA = Critter.getRandomInt(A.energy);  // getRandomInt(0) will throw an exception, so check for 0.
								else
									fightA = 0;
								if(B.energy > 0 && bWantsToFight)
									fightB = Critter.getRandomInt(B.energy);
								else
									fightB = 0;
								if((fightB > fightA) || (A instanceof Algae && !(B instanceof Algae))) {
									B.energy += (A.energy / 2);
									A.energy = 0;
								} else {
									A.energy += (B.energy / 2);
									B.energy = 0;
								}
							}
						}	
					}
				}				
			}
		}
	}
	
	/**
	 * "Searches" in the specified direction to see if a space is occupied.
	 * @param direction The direction the look towards
	 * @param numSteps  1 for walk, 2 for run
	 * @return toString() of the occupying Critter if the space is occupied, null otherwise.
	 */
	private String search(int direction, int numSteps) {
		if(numSteps == WALK) {
			switch(direction) {
			case 0:
				return isOccupied((this.x_coord + 1) % Params.world_width, this.y_coord);
			case 1:
				return isOccupied((this.x_coord + 1) % Params.world_width, (this.y_coord - 1) % Params.world_height);
			case 2:
				return isOccupied(this.x_coord, (this.y_coord - 1) % Params.world_height);
			case 3:
				return isOccupied((this.x_coord - 1) % Params.world_width, (this.y_coord - 1) % Params.world_height);
			case 4:
				return isOccupied((this.x_coord - 1) % Params.world_width, this.y_coord);
			case 5:
				return isOccupied((this.x_coord - 1) % Params.world_width, (this.y_coord + 1) % Params.world_height);
			case 6:
				return isOccupied(this.x_coord, (this.y_coord + 1) % Params.world_height);
			case 7:
				return isOccupied((this.x_coord + 1) % Params.world_width, (this.y_coord + 1) % Params.world_height);
			default:
			}
		} else if(numSteps == RUN) {
			switch(direction) {
			case 0:
				return isOccupied((this.x_coord + 2) % Params.world_width, this.y_coord);
			case 1:
				return isOccupied((this.x_coord + 2) % Params.world_width, (this.y_coord - 2) % Params.world_height);
			case 2:
				return isOccupied(this.x_coord, (this.y_coord - 2) % Params.world_height);
			case 3:
				return isOccupied((this.x_coord - 2) % Params.world_width, (this.y_coord - 2) % Params.world_height);
			case 4:
				return isOccupied((this.x_coord - 2) % Params.world_width, this.y_coord);
			case 5:
				return isOccupied((this.x_coord - 2) % Params.world_width, (this.y_coord + 2) % Params.world_height);
			case 6:
				return isOccupied(this.x_coord, (this.y_coord + 2) % Params.world_height);
			case 7:
				return isOccupied((this.x_coord + 2) % Params.world_width, (this.y_coord + 2) % Params.world_height);
			default:
			}
		}
		return null;
	}
	
	/**
	 * Allows a critter to look in the specified direction during their timestep for the purpose of decision making
	 * @param direction the direction to look
	 * @param numSteps false = 1 step (walk distance), true = 2 steps (run distance)
	 * @return toString() of the occupying Critter if the space is occupied, null otherwise.
	 */
	protected String look(int direction, boolean numSteps) {
		this.energy -= Params.look_energy_cost;
		
		if(numSteps == false) {  // false = walk
			return this.search(direction, WALK);
		} else if(numSteps == true) {  // true = run
			return this.search(direction, RUN);
		}
		return null;
	}
	
	/**
	 * Checks the list of critters to see if any of them occupy the space specified by x and y.
	 * Another alternative is to keep a grid data structure and iterate through that, instead of iterating through the Critters list.
	 * @param x The x coordinate to check
	 * @param y The y coordinate to check
	 * @return True if 1 or more critters occupies the space, false otherwise.
	 */
	private String isOccupied(int x, int y) {
		for(Critter c : population) {
			if(c.x_coord == x && c.y_coord == y && c.energy > 0) {
				// new rule that Critters all move simultaneously.
				// This makes look/search function based off of the old position of Critters, unless currently fighting.
				if(this.isFighting || !c.hasMoved) {
					return c.toString();
				}
			}
		}
		return null;
	}
}

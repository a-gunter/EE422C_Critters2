# EE422C_Critters2
Part 2 of Critters


Critter1 - 
Critter2 - 
Critter3 - Uses "look" inside of fight, but not in timeStep. Shape is a cyan circle.
Critter4 - Uses "look" inside of timeStep, but not in fight. Shape is a red triangle.

Controller/GUI Features
  -Includes buttons for stepping (which also displays the World), making Critters, animating, running stats, seed generation, and
   quitting.
  -Dropdown menu for Make, which finds all Critter classes and allows the user to choose which one they want to make.
  -Slider for animate, which adjusts the speed of animation.
  -Checkboxes for runStats, which allows the user to decide which Critters' stats they want to display.
  -Text boxes for inputs, e.g. running 20 timesteps or making 20 new Critters.
  -Includes a grid, dynamically sized to fit the panel. Grid looks best on a square map, but still functions otherwise. There's an issue
   where sometimes the Grid gets slightly resized, and you have to resize the window in order to keep the output text box showing
   correctly.
   
   
   
Team Plan:
Matt:

Austin:
  -Changed model
    -Look feature implemented, and replaced our previous "look" feature with one called "search".
    -Changed moving in timesteps such that Critters check other Critters' previous coordinates, instead of their current ones.
    -However, Critters still check up-to-date coordinates when fighting.
  -Changed Critter3 and Critter4 to comply with Critters part 2 requirements
  -Added displayWorld functionality
    -Created grid with specific row/column constraints in order to fit the screen
    -Created all required shapes, which are displayed and colored with their respective color whenever a Critter is made

/**
 * 
 */
package com.comp572.proj3;

import java.io.Serializable;
import java.util.Random;

/**
 * @author Sanjeev
 * 
 */
public class Individual implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4930729355833133477L;
    // fitness of the individual
    private Integer fitness;
    // total number of nodes in the tree
    private int size;
    // pointer to the root of the 'program' tree
    private Node theIndv;
    // Individual's X value;
    private int PosX;
    // Individual's Y value;
    private int PosY;
    // Individual's Orientation value;
    private Orientation orientation;
    // Individual's timeValue;
    private int timeUnits;
    // Food Eaten By Individual
    private int numFoodEaten;
    // Enable debug information
    public Boolean debug = false;
    // String to Capture Movement
    private static String movementString = "";
    // Random number Generator
    private static Random rdm = new Random();
    //Create a static board
    private static Board board= new Board();

    public Individual() {
	fitness = 0;
	size = 0;
	PosX = 0;
	PosY = 0;
	orientation = Orientation.EAST;
	timeUnits = Constants.NUMTIMEUNITS;
	numFoodEaten = 0;
    }

    /**
     * @return the fitness
     */
    public Integer getFitness() {
	return fitness;
    }

    /**
     * @param fitness
     *            the fitness to set
     */
    public void setFitness(Integer fitness) {
	this.fitness = fitness;
    }

    /**
     * @return the size
     */
    public int getSize() {
	return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(int size) {
	this.size = size;
    }

    /**
     * @return the theIndv
     */
    public Node getTheIndv() {
	return theIndv;
    }

    /**
     * @param theIndv
     *            the theIndv to set
     */
    public void setTheIndv(Node theIndv) {
	this.theIndv = theIndv;
    }

    /*
     * Calculates fitness. For the Santa Fe Trail problem it has to reevaluate
     * the expression tree for each of the X points. The square root of the sum
     * of the squared errors at each of those points is the fitness.
     */
    public void evaluate() {
	resetIndividual();
	while (timeUnits > 0) {
	    // evaluate the tree generated
	    // check the type of Node
	    // Make decisions accordingly
	    getEval(board, theIndv);
	    if (debug) {
		System.out.println("Value for Time Unit is " + timeUnits);
	    }
	}
	board.resetBoard();
	fitness = Constants.NUMFOODPELLETS - numFoodEaten;
	if (size >= 250) {
	    fitness += (int) ((Math.round(size * getFactor(250, size))));
	}
	// System.out.println("Raw Fitness :" + fitness);
    }

    private double getFactor(int initialStep, int size) {
	if (size >= initialStep && size < (initialStep + 10)) {
	    return 0.05;
	} else if (size >= (initialStep + 10) && size < (initialStep + 20)) {
	    return 0.07;
	} else if (size >= (initialStep + 20) && size < (initialStep + 30)) {
	    return 0.10;
	} else if (size >= (initialStep + 30) && size < (initialStep + 40)) {
	    return 0.30;
	} else if (size >= (initialStep + 40) && size < (initialStep + 50)) {
	    return 0.50;
	} else if (size >= (initialStep + 50) && size < (initialStep + 60)) {
	    return 0.70;
	} else {
	    return 0.80;
	}

    }

    public void getEval(Board board, Node root) {
	if (timeUnits > 0) {
	    switch (root.getType()) {
	    case Command.PROG2:
		getEval(board, root.getBranches()[0]);
		getEval(board, root.getBranches()[1]);
		break;
	    case Command.PROG3:
		getEval(board, root.getBranches()[0]);
		getEval(board, root.getBranches()[1]);
		getEval(board, root.getBranches()[2]);
		break;
	    case Command.IFFOODAHEAD:
		if (ifFoodAhead(board, this)) {
		    getEval(board, root.getBranches()[0]);
		} else {
		    getEval(board, root.getBranches()[1]);
		}
		break;
	    case Command.IFNOFOODAHEAD:
		if (ifFoodNotAhead(board, this)) {
		    getEval(board, root.getBranches()[0]);
		} else {
		    getEval(board, root.getBranches()[1]);
		}
		break;
	    case Command.LEFT:
		turnLeft(this);
		if (debug) {
		    movementString += "LEFT    :(" + PosX + " , " + PosY
			    + ")\n";
		}
		break;
	    case Command.RIGHT:
		turnRight(this);
		if (debug) {
		    movementString += "RIGHT   :(" + PosX + " , " + PosY
			    + ")\n";
		}
		break;
	    case Command.FORWARD:
		moveForward(board, this);
		eatFoodIfExists(board, this);
		if (debug) {
		    movementString += "FORWARD :(" + PosX + " , " + PosY
			    + ")\n";
		}
		break;
	    }
	} else {
	    return;
	}
    }

    public void printBestFitOutput(Individual indv) {
	resetIndividual();
	while (indv.timeUnits > 0) {
	    // evaluate the tree generated
	    // check the type of Node
	    // Make decisions accordingly
	    getEval(board, indv.theIndv);
	    // System.out.println("Value for Time Unit is "+timeUnits );
	}
	if(debug){
	    System.out.printf("Path By Best Ant\n%s",Individual.movementString);
	}
	board.resetBoard();
	fitness = Constants.NUMFOODPELLETS - numFoodEaten;
	System.out.println("Best Fitness : " + indv.getFitness());
    }

    /* Calculates the size of an individual's tree. */
    public void calcSize() {
	size = 0;
	size = theIndv.calcSize(theIndv);
    }

    /*
     * Generates a random full tree.
     */
    public void generate(int maxDepth) {
	theIndv = new Node();
	theIndv.full(0, maxDepth, null);
    }

    /*
     * Calls a function to erase/free the tree.
     */
    public void erase() {
	theIndv.erase();
    }

    public Node getRandomNode() {
	Node result = null;
	while (result == null) {
	    if (rdm.nextFloat() > 0.1) {
		result = theIndv.getRandomNonTerminalNode(theIndv);
	    } else {
		result = theIndv.getRandomTerminalNode(theIndv);
	    }
	}
	return result;
    }

    public String toString() {
	theIndv.setExpression("");
	theIndv.postorder(theIndv);
	return theIndv.getExpression();
	// return "";
    }

    public boolean ifFoodAhead(Board board, Individual indv) {
	// check for orientation
	switch (indv.orientation) {
	case EAST:
	    if (board.checkBoundsForBoard(indv.PosX, indv.PosY + 1)) {
		if (board.containsFood(indv.PosX, indv.PosY + 1)) {
		    return true;
		}
	    }
	    break;
	case WEST:
	    if (board.checkBoundsForBoard(indv.PosX, indv.PosY - 1)) {
		if (board.containsFood(indv.PosX, indv.PosY - 1)) {
		    return true;
		}
	    }
	    break;
	case NORTH:
	    if (board.checkBoundsForBoard(indv.PosX - 1, indv.PosY)) {
		if (board.containsFood(indv.PosX - 1, indv.PosY)) {
		    return true;
		}
	    }
	    break;
	case SOUTH:
	    if (board.checkBoundsForBoard(indv.PosX + 1, indv.PosY)) {
		if (board.containsFood(indv.PosX + 1, indv.PosY)) {
		    return true;
		}
	    }
	    break;
	default:
	    System.out.println("Default case reached something is wrong");
	    break;
	}
	return false;
    }

    public boolean ifFoodNotAhead(Board board, Individual indv) {
	return !ifFoodAhead(board, indv);
    }

    public void moveForward(Board board, Individual indv) {
	// check for orientation
	switch (indv.orientation) {
	case EAST:
	    // if bounds are OK Move forward EAST
	    if (board.checkBoundsForBoard(indv.PosX, indv.PosY + 1)) {
		indv.PosY += 1;
	    } else {
		//indv.PosY = 0;
		if (size >= 250) {
		    fitness += (int) ((Math.round(size * getFactor(250, size))));
		}
		else {
		    fitness +=8;
		}
	    }
	    break;
	case WEST:
	    // if bounds are OK Move forward WEST
	    if (board.checkBoundsForBoard(indv.PosX, indv.PosY - 1)) {
		indv.PosY -= 1;
	    } else {
		//indv.PosY = 31;
		if (size >= 250) {
		    fitness += (int) ((Math.round(size * getFactor(250, size))));
		}
		else {
		    fitness +=8;
		}
	    }
	    break;
	case NORTH:
	    // if bounds are OK Move forward NORTH
	    if (board.checkBoundsForBoard(indv.PosX - 1, indv.PosY)) {
		indv.PosX -= 1;
	    } else {
		//indv.PosX = 31;
		if (size >= 250) {
		    fitness += (int) ((Math.round(size * getFactor(250, size))));
		}
		else {
		    fitness +=8;
		}
	    }
	    break;
	case SOUTH:
	    // if bounds are OK Move forward SOUTH
	    if (board.checkBoundsForBoard(indv.PosX + 1, indv.PosY)) {
		indv.PosX += 1;
	    } else {
		//indv.PosX = 0;
		if (size >= 250) {
		    fitness += (int) ((Math.round(size * getFactor(250, size))));
		}
		else {
		    fitness +=8;
		}
	    }
	    break;
	default:
	    System.out.println("Default case reached something is wrong");
	    break;
	}
	// decrement time value by one unit
	timeUnits--;
    }

    public void turnLeft(Individual indv) {
	// check for orientation
	switch (indv.orientation) {
	case EAST:
	    indv.orientation = Orientation.NORTH;
	    break;
	case WEST:
	    indv.orientation = Orientation.SOUTH;
	    break;
	case NORTH:
	    indv.orientation = Orientation.WEST;
	    break;
	case SOUTH:
	    indv.orientation = Orientation.EAST;
	    break;
	default:
	    System.out
		    .println("Default case reached something is wrong for TurnLeft");
	    break;
	}
	// decrement time value by one unit
	timeUnits--;
    }

    public void turnRight(Individual indv) {

	// check for orientation
	switch (indv.orientation) {
	case EAST:
	    indv.orientation = Orientation.SOUTH;
	    break;
	case WEST:
	    indv.orientation = Orientation.NORTH;
	    break;
	case NORTH:
	    indv.orientation = Orientation.EAST;
	    break;
	case SOUTH:
	    indv.orientation = Orientation.WEST;
	    break;
	default:
	    System.out.println("Default case reached something is wrong");
	    break;
	}
	// decrement time value by one unit
	timeUnits--;
    }

    public void eatFoodIfExists(Board board, Individual indv) {
	if (board.checkBoundsForBoard(indv.PosX, indv.PosY)) {
	    if (board.containsFood(indv.PosX, indv.PosY)) {
		numFoodEaten += 1;
		board.removeFoodXY(indv.PosX, indv.PosY);
		if (debug) {
		    System.out.println("Food Eaten At Position X: " + indv.PosX
			    + " Position Y: " + indv.PosY);
		}
	    }
	}
    }

    public void resetIndividual() {
	fitness = 0;
	PosX = 0;
	PosY = 0;
	orientation = Orientation.EAST;
	timeUnits = Constants.NUMTIMEUNITS;
	numFoodEaten = 0;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	Individual i = new Individual();
	i.debug = true;
	i.generate(6);
	i.evaluate();
	// i.evaluatePrint();
	i.calcSize();
	System.out.println("Formula is " + i.toString());
	System.out.printf("%s", Individual.movementString);
	// Node node=i.getRandomNode();
	// System.out.println(node);
	System.out.println("\nSize = " + i.getSize());
    }
}

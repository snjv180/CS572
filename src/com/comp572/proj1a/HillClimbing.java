package com.comp572.proj1a;

import java.util.Arrays;

import com.comp572.functions.Schwefel;
import com.comp572.functions.Spherical;
import com.comp572.utilities.DoubleFormatter;
import com.comp572.utilities.RandomArrayGenerator;

public class HillClimbing {
    public static final int DIMENSIONS = 30;

    /**
     * Discrete Space Hill Climbing Algorithm currentNode = startNode; loop do L
     * = NEIGHBORS(currentNode); nextEval = -INF; nextNode = NULL; for all x in
     * L if (EVAL(x) > nextEval) nextNode = x; nextEval = EVAL(x); if nextEval
     * <= EVAL(currentNode) //Return current node since no better neighbors
     * exist return currentNode; currentNode = nextNode;
     * 
     ***/

    /**
     * 
     * @author Sanjeev
     * @return solutionSet
     * @param probArray
     *            [] Array of Random Integers
     * @param functionName
     *            Either "Spherical" or "Schwefel"
     * 
     */
    public double[] hillClimbing(double probArray[], String functionName) {
	// Create an empty Neighbors Array
	double[] neighbours = new double[DIMENSIONS];
	// Loop Condition if good solution not found
	for (int i = 0; i < 100000; i++) {
	    // Calculate the neighbor
	    neighbours = getNeighbour(probArray, functionName);
	    // if the current and Next Solution are same
	    // then move out of loop
	    if (Arrays.equals(probArray, neighbours)) {
		System.out.println("Test Cycles " + i);
		break;
	    }
	    // check the fitness value in between current node
	    // and next node
	    if (checkFitness(neighbours, probArray, functionName)) {
		// substitute current solution with next solution
		probArray = neighbours;
	    }

	}
	// return the solution set
	return probArray;
    }

    private double[] getNeighbour(double[] probArray, String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return getNeighbour4Sphere(probArray, functionName);
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return getNeighbour4Schwefel(probArray, functionName);
	} else {
	    return null;
	}
    }

    public boolean checkFitness(double neighbours[], double probArray[],
	    String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return Spherical.getfitnessValue(neighbours) <= Spherical
		    .getfitnessValue(probArray);
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return Schwefel.getfitnessValue(neighbours) <= Schwefel
		    .getfitnessValue(probArray);
	} else {
	    return false;
	}
    }

    public double[] getNeighbour4Schwefel(double[] probArray,
	    String functionName) {
	RandomArrayGenerator newRandomNumGen = new RandomArrayGenerator();
	double delta = newRandomNumGen.randomInRange(-512.03, 511.97);
	delta = DoubleFormatter.getFormattedDouble(delta, functionName);
	double[] neighbour = new double[DIMENSIONS];
	for (int i = 0; i < probArray.length; i++) {
	    if (checkLimitCondition(probArray[i] - delta, functionName)
		    && Schwefel.getSingleFitnessValue(probArray[i] - delta) < Schwefel
			    .getSingleFitnessValue(probArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(probArray[i]
			- delta, functionName);
	    } else if (checkLimitCondition(probArray[i] + delta, functionName)
		    && Schwefel.getSingleFitnessValue(probArray[i] + delta) < Schwefel
			    .getSingleFitnessValue(probArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(probArray[i]
			+ delta, functionName);
	    } else if (Schwefel.getSingleFitnessValue(delta) < Schwefel
		    .getSingleFitnessValue(probArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(delta,
			functionName);
	    }
	}
	return neighbour;

	/*
	 * RandomArrayGenerator rdm = new RandomArrayGenerator(); return
	 * rdm.getRandomArrayDouble(Schwefel.range, DIMENSIONS,
	 * Schwefel.FUNCTION_NAME);
	 */

    }

    public double[] getNeighbour4Sphere(double[] probArray, String functionName) {
	// small change delta
	double delta = 0.01;
	// initialize the neighbor solution
	double[] neighbour = new double[DIMENSIONS];
	// loop and change values of neighbor
	// solution based on problem set
	for (int i = 0; i < probArray.length; i++) {
	    if (probArray[i] < 0.00
		    && checkLimitCondition(probArray[i], functionName)) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(probArray[i]
			+ delta, functionName);
	    } else if (probArray[i] > 0.00
		    && checkLimitCondition(probArray[i], functionName)) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(probArray[i]
			- delta, functionName);
	    } else {
		neighbour[i] = DoubleFormatter.getFormattedDouble(probArray[i],
			functionName);
	    }
	}
	// return changed neighbor array
	return neighbour;
    }

    public boolean checkLimitCondition(double val, String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return val < Spherical.range.getMaximum()
		    && val > Spherical.range.getMinimum();
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return val < Schwefel.range.getMaximum()
		    && val > Schwefel.range.getMinimum();
	} else {
	    return false;
	}
    }

    public static void main(String[] args) {
	HillClimbing hillClimbing = new HillClimbing();
	RandomArrayGenerator rdm = new RandomArrayGenerator();
	double[] prob = rdm.getRandomArrayDouble(Spherical.range, DIMENSIONS,
		Spherical.FUNCTION_NAME);
	System.out
		.println("Problem Set for Hill Climbing for Sphere Function: ");
	System.out.println(Arrays.toString(prob));
	System.out.println("Fitness for Problem Set "
		+ Spherical.getfitnessValue(prob));
	long startTime = System.nanoTime();
	double[] sol = hillClimbing.hillClimbing(prob, Spherical.FUNCTION_NAME);
	long endTime = System.nanoTime();
	System.out.println("Took " + (double) (endTime - startTime)
		/ 1000000000.0 + " seconds ");
	System.out
		.println("Solution Set for Hill Climbing for Sphere Function: ");
	System.out.println(Arrays.toString(sol));
	System.out.println("Fitness for Solution Set "
		+ Spherical.getfitnessValue(sol));

	prob = rdm.getRandomArrayDouble(Schwefel.range, DIMENSIONS,
		Schwefel.FUNCTION_NAME);
	System.out
		.println("Problem Set for Hill Climbing for Schwefel Function: ");
	System.out.println(Arrays.toString(prob));
	System.out.println("Fitness for Solution Set "
		+ Schwefel.getfitnessValue(prob));
	startTime = System.nanoTime();
	sol = hillClimbing.hillClimbing(prob, Schwefel.FUNCTION_NAME);
	endTime = System.nanoTime();
	System.out.println("Took " + (double) (endTime - startTime)
		/ 1000000000.0 + " seconds ");
	System.out
		.println("Solution Set for Hill Climbing for Schwefel Function: ");
	System.out.println(Arrays.toString(sol));
	System.out.println("Fitness for Solution Set "
		+ Schwefel.getfitnessValue(sol));
    }

}

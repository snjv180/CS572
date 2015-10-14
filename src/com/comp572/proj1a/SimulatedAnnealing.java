package com.comp572.proj1a;

import java.util.Arrays;

import com.comp572.functions.Schwefel;
import com.comp572.functions.Spherical;
import com.comp572.utilities.DoubleFormatter;
import com.comp572.utilities.RandomArrayGenerator;

public class SimulatedAnnealing {

    public static final int DIMENSIONS = 30;

    public double[] simulatedAnnealing4Sphere(double[] s1) {
	double[] s2 = new double[30];
	for (double T = 100; T >= 0.01; T -= 0.01) {
	    s2 = getNeighbour(s1, Spherical.FUNCTION_NAME);

	    if (checkFitness(s2, s1, Spherical.FUNCTION_NAME)) {
		s1 = s2;
	    } else {
		if (Prob(Spherical.getfitnessValue(s1),
			Spherical.getfitnessValue(s2), T,
			Spherical.FUNCTION_NAME)) {
		    s1 = s2;
		}
	    }
	}
	return s1;
    }

    public double[] simulatedAnnealing4Schwefel(double[] s1) {
	double[] s2 = new double[30];
	for (double T = 100; T >= 0.01; T -= 0.01) {
	    s2 = getNeighbour(s1, Schwefel.FUNCTION_NAME);

	    if (checkFitness(s2, s1, Schwefel.FUNCTION_NAME)) {
		s1 = s2;
	    } else {
		if (Prob(Schwefel.getfitnessValue(s1),
			Schwefel.getfitnessValue(s2), T, Schwefel.FUNCTION_NAME)) {
		    s1 = s2;
		}
	    }
	}
	return s1;
    }

    private static boolean Prob(double e1, double e2, double T,
	    String functionName) {
	double P = 0.0;
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    P = 1 / (Math.exp((e1 - e2) / T));
	    return P > 0.5;
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    P = (Math.exp(((e1 - e2)) / T));
	    return P > (new RandomArrayGenerator().randomInRange(0, 1));
	}
	return false;
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

    private double[] getNeighbour(double[] probArray, String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return getNeighbour4Sphere(probArray, functionName);
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return getNeighbour4Schwefel(probArray, functionName);
	} else {
	    return null;
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
	double delta = 0.01;
	double[] neighbour = new double[DIMENSIONS];
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

	SimulatedAnnealing sm = new SimulatedAnnealing();
	RandomArrayGenerator rdm = new RandomArrayGenerator();
	double[] prob = new double[DIMENSIONS];
	double[] soln = new double[DIMENSIONS];
	prob = rdm.getRandomArrayDouble(Spherical.range, DIMENSIONS,
		Spherical.FUNCTION_NAME);
	System.out
		.println("Problem Set for Simulated Annealing for Sphere Function: ");
	System.out.println(Arrays.toString(prob));
	System.out.println("Fitness for Problem Set "
		+ Spherical.getfitnessValue(prob));
	long startTime = System.nanoTime();
	soln = sm.simulatedAnnealing4Sphere(prob);
	long endTime = System.nanoTime();
	System.out.println("Took " + (double) (endTime - startTime)
		/ 1000000000.0 + " seconds ");
	System.out
		.println("Solution Set for Simulated Annealing for Sphere Function: ");
	System.out.println(Arrays.toString(soln));
	System.out.println("Fitness for Solution Set "
		+ Spherical.getfitnessValue(soln));

	prob = rdm.getRandomArrayDouble(Schwefel.range, DIMENSIONS,
		Schwefel.FUNCTION_NAME);
	System.out
		.println("Problem Set for Simulated Annealing for Schwefel Function: ");
	System.out.println(Arrays.toString(prob));
	System.out.println("Fitness for Problem Set "
		+ Schwefel.getfitnessValue(prob));

	startTime = System.nanoTime();
	soln = sm.simulatedAnnealing4Schwefel(prob);
	endTime = System.nanoTime();
	System.out.println("Took " + (double) (endTime - startTime)
		/ 1000000000.0 + " seconds ");
	System.out
		.println("Solution Set for Simulated Annealing for Schwefel Function: ");
	System.out.println(Arrays.toString(soln));
	System.out.println("Fitness for Solution Set "
		+ Schwefel.getfitnessValue(soln));

    }
}

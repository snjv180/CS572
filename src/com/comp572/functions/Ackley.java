package com.comp572.functions;

import java.util.Arrays;

import org.apache.commons.lang3.Range;

import com.comp572.proj1b.Individual;
import com.comp572.utilities.Constants;
import com.comp572.utilities.DoubleFormatter;
import com.comp572.utilities.RandomArrayGenerator;

public class Ackley {
    public static final String FUNCTION_NAME = "Ackley";

    public static final Range<Double> range = Range.between(-30.0, 30.0);

    public static double getfitnessValue(double[] varArray) {
	double tempVar = 0.0;
	double s1 = 0.0;
	double s2 = 0.0;
	for (int i = 0; i < varArray.length; i++) {
	    s1 += Math.pow(varArray[i], 2);
	    s2 += Math.cos(2 * Constants.PI * varArray[i]);
	}
	tempVar = 20 + Math.exp(1) - 20
		* Math.exp(-0.2 * Math.sqrt(s1 / Constants.DIMENSIONS))
		- Math.exp(s2 / Constants.DIMENSIONS);
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static double getSingleFitnessValue(double var) {
	double tempVar = 0.0;
	tempVar = 20
		+ Math.exp(1)
		- 20
		* Math.exp(-0.2
			* Math.sqrt(Math.pow(var, 2) / Constants.DIMENSIONS))
		- Math.exp(Math.cos(2 * Constants.PI * var)
			/ Constants.DIMENSIONS);
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }
    
    public static Double getfitnessValueNP(Double[] individual) {
	Double tempVar = 0.0;
	Double s1 = 0.0;
	Double s2 = 0.0;
	for (int i = 0; i < individual.length; i++) {
	    s1 += Math.pow(individual[i], 2);
	    s2 += Math.cos(2 * Constants.PI * individual[i]);
	}
	tempVar = 20 + Math.exp(1) - 20
		* Math.exp(-0.2 * Math.sqrt(s1 / Constants.DIMENSIONS))
		- Math.exp(s2 / Constants.DIMENSIONS);
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static void main(String[] args) {
	Double[] varArray = new Double[30];
	for (int i = 0; i < varArray.length; i++) {
	    varArray[i] = 0.0;
	}
	if(DoubleFormatter.getFormattedSolution(getfitnessValueNP(varArray)).equals(-0.00) ){
		System.out.println("Solution Found");
	    }
	System.out.println(getfitnessValueNP(varArray));
    }

    

    public static Individual mutateIndividual(Individual individualtoMutate) {
	RandomArrayGenerator newRandomNumGen = new RandomArrayGenerator();
	double delta = newRandomNumGen.randomInRange(range.getMinimum(), range.getMaximum());
	delta = DoubleFormatter.getFormattedDouble(delta, FUNCTION_NAME);
	Double[] individualToMutateArray = individualtoMutate.getValues();
	Double[] neighbour = new Double[Constants.DIMENSIONS];
	Arrays.fill(neighbour, 0.0);
	Individual mutatedIndv = new Individual();
	for (int i = 0; i < individualToMutateArray.length; i++) {
	    if (checkLimitCondition(individualToMutateArray[i] - delta)
		    && Ackley
			    .getSingleFitnessValue(individualToMutateArray[i]
				    - delta) < Ackley
			    .getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] - delta, FUNCTION_NAME);
	    } else if (checkLimitCondition(individualToMutateArray[i] + delta)
		    && Ackley
			    .getSingleFitnessValue(individualToMutateArray[i]
				    + delta) < Ackley
			    .getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] + delta, FUNCTION_NAME);
	    } else if (Ackley.getSingleFitnessValue(delta) < Ackley
		    .getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(delta,
			FUNCTION_NAME);
	    }
	}
	mutatedIndv.setValues(neighbour);
	mutatedIndv.setFitnessValue(getfitnessValueNP(neighbour));
	// return changed neighbor array
	return mutatedIndv;
    }

    public static boolean checkLimitCondition(double val) {
	return val < Ackley.range.getMaximum()
		&& val > Ackley.range.getMinimum();
    }
}

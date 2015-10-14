package com.comp572.functions;

import java.util.Arrays;

import org.apache.commons.lang3.Range;

import com.comp572.proj1b.Individual;
import com.comp572.utilities.Constants;
import com.comp572.utilities.DoubleFormatter;
import com.comp572.utilities.RandomArrayGenerator;

public class Schwefel {
    public static final String FUNCTION_NAME = "Schwefel";

    public static final Range<Double> range = Range.between(-512.03, 511.97);

    public static double getfitnessValue(double[] varArray) {
	double tempVar = 418.9829 * Constants.DIMENSIONS;
	for (int i = 0; i < varArray.length; i++) {
	    tempVar += varArray[i] * Math.sin(Math.sqrt(Math.abs(varArray[i])));
	}
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static double getSingleFitnessValue(double var) {
	double tempVar = 418.9829 * 1;
	tempVar += var * Math.sin(Math.sqrt(Math.abs(var)));
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static void main(String[] args) {
	double[] varArray = new double[30];
	for (int i = 0; i < varArray.length; i++) {
	    varArray[i] = 10;
	}

	System.out.println(getfitnessValue(varArray));
    }

    public static Double getfitnessValueNP(Double[] individual) {
	Double tempVar = 418.9829 * Constants.DIMENSIONS;
	for (int i = 0; i < individual.length; i++) {
	    tempVar += individual[i]
		    * Math.sin(Math.sqrt(Math.abs(individual[i])));
	}
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static Individual mutateIndividual(Individual individualtoMutate) {
	RandomArrayGenerator newRandomNumGen = new RandomArrayGenerator();
	double delta = newRandomNumGen.randomInRange(-512.03, 511.97);
	delta = DoubleFormatter.getFormattedDouble(delta, FUNCTION_NAME);
	Double[] individualToMutateArray = individualtoMutate.getValues();
	Double[] neighbour = new Double[Constants.DIMENSIONS];
	Arrays.fill(neighbour, 0.0);
	Individual mutatedIndv = new Individual();
	for (int i = 0; i < individualToMutateArray.length; i++) {
	    if (checkLimitCondition(individualToMutateArray[i] - delta)
		    && Schwefel.getSingleFitnessValue(individualToMutateArray[i]
			    - delta) < Schwefel
				.getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] - delta, FUNCTION_NAME);
	    } else if (checkLimitCondition(individualToMutateArray[i] + delta)
		    && Schwefel.getSingleFitnessValue(individualToMutateArray[i]
			    + delta) < Schwefel
				.getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] + delta, FUNCTION_NAME);
	    } else if (Schwefel.getSingleFitnessValue(delta) < Schwefel
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
	return val < Schwefel.range.getMaximum()
		&& val > Schwefel.range.getMinimum();
    }
}

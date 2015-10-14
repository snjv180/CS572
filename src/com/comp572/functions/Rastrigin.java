package com.comp572.functions;

import java.util.Arrays;

import org.apache.commons.lang3.Range;

import com.comp572.proj1b.Individual;
import com.comp572.utilities.Constants;
import com.comp572.utilities.DoubleFormatter;
import com.comp572.utilities.RandomArrayGenerator;

public class Rastrigin {
    public static final String FUNCTION_NAME = "Rastrigin";

    public static final Range<Double> range = Range.between(-5.12, 5.12);

    public static double getfitnessValue(double[] varArray) {
	double tempVar = 0.0;
	double s1 = 0.0;
	for (int i = 0; i < varArray.length; i++) {
	    s1 += (Math.pow(varArray[i], 2) - 10 * Math.cos(2 * Constants.PI
		    * varArray[i]));
	}
	tempVar = 10 * Constants.DIMENSIONS + s1;
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static double getSingleFitnessValue(double var) {
	double tempVar = 0.0;
	tempVar = 10 * 1 + (Math.pow(var, 2) - 10 * Math.cos(2 * Constants.PI
		* var));
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static Double getfitnessValueNP(Double[] individual) {
	Double tempVar = 0.0;
	Double s1 = 0.0;
	for (int i = 0; i < individual.length; i++) {
	    s1 += (Math.pow(individual[i], 2) - 10 * Math.cos(2 * Constants.PI
		    * individual[i]));
	}
	tempVar = 10 * Constants.DIMENSIONS + s1;
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static void main(String[] args) {
	double[] varArray = new double[30];
	for (int i = 0; i < varArray.length; i++) {
	    varArray[i] = 0.0;
	}

	System.out.println(getfitnessValue(varArray));
    }

    public static Individual mutateIndividual(Individual individualtoMutate) {
	RandomArrayGenerator newRandomNumGen = new RandomArrayGenerator();
	double delta = newRandomNumGen.randomInRange(range.getMinimum(),
		range.getMaximum());
	delta = DoubleFormatter.getFormattedDouble(delta, FUNCTION_NAME);
	Double[] individualToMutateArray = individualtoMutate.getValues();
	Double[] neighbour = new Double[Constants.DIMENSIONS];
	Arrays.fill(neighbour, 0.0);
	Individual mutatedIndv = new Individual();
	for (int i = 0; i < individualToMutateArray.length; i++) {
	    if (checkLimitCondition(individualToMutateArray[i] - delta)
		    && Rastrigin
			    .getSingleFitnessValue(individualToMutateArray[i]
				    - delta) < Rastrigin
			    .getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] - delta, FUNCTION_NAME);
	    } else if (checkLimitCondition(individualToMutateArray[i] + delta)
		    && Rastrigin
			    .getSingleFitnessValue(individualToMutateArray[i]
				    + delta) < Rastrigin
			    .getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] + delta, FUNCTION_NAME);
	    } else if (Rastrigin.getSingleFitnessValue(delta) < Rastrigin
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
	return val < Rastrigin.range.getMaximum()
		&& val > Rastrigin.range.getMinimum();
    }
}

package com.comp572.functions;

import java.util.Arrays;

import org.apache.commons.lang3.Range;

import com.comp572.proj1b.Individual;
import com.comp572.utilities.Constants;
import com.comp572.utilities.DoubleFormatter;

public class Rosenbrock {
    public static final String FUNCTION_NAME = "Rosenbrock";

    public static final Range<Double> range = Range.between(-2.048, 2.048);

    public static double getfitnessValue(double[] varArray) {
	double tempVar = 0.0;
	for (int i = 0; i < (varArray.length - 1); i++) {
	    tempVar += (Math.pow((1 - varArray[i]), 2) + 100 * Math.pow(
		    (varArray[i + 1] - Math.pow(varArray[i], 2)), 2));
	}
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static Double getfitnessValueNP(Double[] individual) {
	Double tempVar = 0.0;
	for (int i = 0; i < (individual.length - 1); i++) {
	    tempVar += (Math.pow((1 - individual[i]), 2) + 100 * Math.pow(
		    (individual[i + 1] - Math.pow(individual[i], 2)), 2));
	}
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static void main(String[] args) {
	double[] varArray = new double[30];
	for (int i = 0; i < varArray.length; i++) {
	    varArray[i] = 1.0;
	}

	System.out.println(getfitnessValue(varArray));
    }

    public static Individual mutateIndividual(Individual individualtoMutate) {
	// small change delta
	double delta = 0.001;
	Double[] individualtoMutateArray = individualtoMutate.getValues();
	// initialize the neighbor solution
	Double[] mutatedIndividual = new Double[Constants.DIMENSIONS];
	Arrays.fill(mutatedIndividual, 0.0);
	Individual mutatedIndv = new Individual();
	// loop and change values of neighbor
	// solution based on problem set
	for (int i = 0; i < individualtoMutateArray.length; i++) {
	    if (individualtoMutateArray[i] < 1.00
		    && checkLimitCondition(individualtoMutateArray[i])) {
		mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			individualtoMutateArray[i] + delta, FUNCTION_NAME);
	    } else if (individualtoMutateArray[i] > 1.00
		    && checkLimitCondition(individualtoMutateArray[i])) {
		mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			individualtoMutateArray[i] - delta, FUNCTION_NAME);
	    } else {
		mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			individualtoMutateArray[i], FUNCTION_NAME);
	    }
	}
	mutatedIndv.setValues(mutatedIndividual);
	mutatedIndv.setFitnessValue(getfitnessValueNP(mutatedIndividual));
	// return changed neighbor array
	return mutatedIndv;
    }

    public static boolean checkLimitCondition(double val) {
	return val < Rosenbrock.range.getMaximum()
		&& val > Rosenbrock.range.getMinimum();
    }
}

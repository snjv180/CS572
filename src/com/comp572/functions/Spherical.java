package com.comp572.functions;

import org.apache.commons.lang3.Range;

import com.comp572.proj1b.Individual;
import com.comp572.utilities.Constants;
import com.comp572.utilities.DoubleFormatter;

public class Spherical {
    public static final String FUNCTION_NAME = "Spherical";

    public static final Range<Double> range = Range.between(-5.12, 5.12);

    public static double getfitnessValue(double[] varArray) {
	double tempVar = 0.0;
	for (int i = 0; i < varArray.length; i++) {
	    tempVar += Math.pow(varArray[i], 2);
	}
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static Double getfitnessValueNP(Double[] varArray) {
	Double tempVar = 0.0;
	for (int i = 0; i < varArray.length; i++) {
	    tempVar += Math.pow(varArray[i], 2);
	}
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }
    
    public static Double getSingleFitnessValue(Double var) {
	Double tempVar = 0.0;
	tempVar += Math.pow(var, 2);
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static Individual mutateIndividual(Individual individualtoMutate) {
	// small change delta
	double delta = 0.01;
	Double[] individualtoMutateArray = individualtoMutate.getValues();
	// initialize the neighbor solution
	Double[] mutatedIndividual = new Double[Constants.DIMENSIONS];
	Individual mutatedIndv = new Individual();
	// loop and change values of neighbor
	// solution based on problem set
	for (int i = 0; i < individualtoMutateArray.length; i++) {
	    if (individualtoMutateArray[i] < 0.00
		    && checkLimitCondition(individualtoMutateArray[i])) {
		mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			individualtoMutateArray[i] + delta, FUNCTION_NAME);
	    } else if (individualtoMutateArray[i] > 0.00
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
    
    /*public static Individual mutateIndividual(Individual individualtoMutate) {
	// small change delta
	double delta = 0.0;
	Double[] individualtoMutateArray = individualtoMutate.getValues();
	// initialize the neighbor solution
	Double[] mutatedIndividual = new Double[Constants.DIMENSIONS];
	Individual mutatedIndv = new Individual();
	Random rdm = new Random();
	// loop and change values of neighbor
	// solution based on problem set
	for (int i = 0; i < individualtoMutateArray.length; i++) {
	    if (rdm.nextDouble() > 0.2) {
		delta = 0.01;
		if (getSingleFitnessValue(individualtoMutateArray[i] + delta) < getSingleFitnessValue(individualtoMutateArray[i])
			&& checkLimitCondition(individualtoMutateArray[i]
				+ delta)) {
		    mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			    individualtoMutateArray[i] + delta, FUNCTION_NAME);
		} else if (getSingleFitnessValue(individualtoMutateArray[i]
			- delta) < getSingleFitnessValue(individualtoMutateArray[i])
			&& checkLimitCondition(individualtoMutateArray[i]
				- delta)) {
		    mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			    individualtoMutateArray[i] - delta, FUNCTION_NAME);
		} else {
		    mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			    individualtoMutateArray[i], FUNCTION_NAME);
		}
	    } else {
		mutatedIndividual[i] = DoubleFormatter.getFormattedDouble(
			individualtoMutateArray[i], FUNCTION_NAME);
	    }
	}
	mutatedIndv.setValues(mutatedIndividual);
	mutatedIndv.setFitnessValue(getfitnessValueNP(mutatedIndividual));
	// return changed neighbor array
	return mutatedIndv;
    }*/
    
    public static boolean checkLimitCondition(double val) {
	return val < Spherical.range.getMaximum()
		&& val > Spherical.range.getMinimum();
    }

}

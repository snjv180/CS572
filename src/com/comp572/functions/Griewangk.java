package com.comp572.functions;

import java.util.Arrays;

import org.apache.commons.lang3.Range;

import com.comp572.proj1b.Individual;
import com.comp572.utilities.Constants;
import com.comp572.utilities.DoubleFormatter;
import com.comp572.utilities.RandomArrayGenerator;

public class Griewangk {
    public static final String FUNCTION_NAME = "Griewangk";
    
    public static final Range<Double> range = Range.between(-600.0, 600.0);

    public static double getfitnessValue(double[] varArray) {
	double tempVar = 0.0;
	double s1 = 0.0;
	double s2 = 1.0;
	for (int i = 0; i < varArray.length; i++) {
	    s1 += (Math.pow(varArray[i], 2)/4000.0);
	    s2 = s2 * Math.cos(varArray[i]/Math.sqrt(i+1));
	}
	tempVar = 1 + s1 -s2;
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static double getSingleFitnessValue(double var) {
	double tempVar = 0.0;
	tempVar = 1 + (Math.pow(var, 2)/4000) -Math.cos(var/Math.sqrt(1));
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }
    
    public static Double getfitnessValueNP(Double[] individual) {
	Double tempVar = 0.0;
	Double s1 = 0.0;
	Double s2 = 1.0;
	for (int i = 0; i < individual.length; i++) {
	    s1 += (Math.pow(individual[i], 2)/4000);
	    s2 *= Math.cos(individual[i]/Math.sqrt(i+1));
	}
	tempVar = 1 + s1 -s2;
	return DoubleFormatter.getFormattedDouble(tempVar, FUNCTION_NAME);
    }

    public static void main(String[] args) {
	double[] varArray = new double[30];
	for (int i = 0; i < varArray.length; i++) {
	    varArray[i] = 600;
	}

	System.out.println(getfitnessValue(varArray));
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
		    && Griewangk
			    .getSingleFitnessValue(individualToMutateArray[i]
				    - delta) < Griewangk
			    .getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] - delta, FUNCTION_NAME);
	    } else if (checkLimitCondition(individualToMutateArray[i] + delta)
		    && Griewangk
			    .getSingleFitnessValue(individualToMutateArray[i]
				    + delta) < Griewangk
			    .getSingleFitnessValue(individualToMutateArray[i])) {
		neighbour[i] = DoubleFormatter.getFormattedDouble(
			individualToMutateArray[i] + delta, FUNCTION_NAME);
	    } else if (Griewangk.getSingleFitnessValue(delta) < Griewangk
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
	return val < Griewangk.range.getMaximum()
		&& val > Griewangk.range.getMinimum();
    }
}

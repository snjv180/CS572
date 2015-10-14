/**
 * 
 */
package com.comp572.proj1b;

import java.util.Arrays;

import org.apache.commons.lang3.Range;

import com.comp572.functions.Ackley;
import com.comp572.functions.Griewangk;
import com.comp572.functions.Rastrigin;
import com.comp572.functions.Rosenbrock;
import com.comp572.functions.Schwefel;
import com.comp572.functions.Spherical;
import com.comp572.utilities.Constants;
import com.comp572.utilities.RandomArrayGenerator;

/**
 * @author Sanjeev
 * 
 */
public class Individual {

    private Double[] values;
    private Double fitnessValue;
    private RandomArrayGenerator rdm = new RandomArrayGenerator();

    /**
     * 
     */
    public Individual() {
	this.values = new Double[30];
	for (int i = 0; i < this.values.length; i++) {
	    this.values[i] = 0.0;
	}
	this.fitnessValue = 0.0;
    }

    public Individual(Double[] values, Double fitnessValue) {
	this.values = values;
	this.fitnessValue = fitnessValue;
    }

    /**
     * @return the values
     */
    public Double[] getValues() {
	return values;
    }

    /**
     * @param values
     *            the values to set
     */
    public void setValues(Double[] values) {
	this.values = values;
    }

    /**
     * @return the fitnessValue
     */
    public Double getFitnessValue() {
	return fitnessValue;
    }

    /**
     * @param fitnessValue
     *            the fitnessValue to set
     */
    public void setFitnessValue(Double fitnessValue) {
	this.fitnessValue = fitnessValue;
    }

    public Individual generateIndividual(String functionName) {
	Individual indv = new Individual();
	indv.setValues(rdm.getRandomArrayDoubleNP(
		getFunctionRange(functionName), Constants.DIMENSIONS,
		functionName));
	indv.setFitnessValue(getFitnessOfIndividual(indv.getValues(),
		functionName));
	return indv;
    }

    private Range<Double> getFunctionRange(String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return Spherical.range;
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return Schwefel.range;
	} else if (functionName.equals(Ackley.FUNCTION_NAME)) {
	    return Ackley.range;
	} else if (functionName.equals(Griewangk.FUNCTION_NAME)) {
	    return Griewangk.range;
	} else if (functionName.equals(Rastrigin.FUNCTION_NAME)) {
	    return Rastrigin.range;
	} else if (functionName.equals(Rosenbrock.FUNCTION_NAME)) {
	    return Rosenbrock.range;
	} else {
	    return null;
	}
    }

    public Double getFitnessOfIndividual(Double[] individual,
	    String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return Spherical.getfitnessValueNP(individual);
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return Schwefel.getfitnessValueNP(individual);
	} else if (functionName.equals(Ackley.FUNCTION_NAME)) {
	    return Ackley.getfitnessValueNP(individual);
	} else if (functionName.equals(Griewangk.FUNCTION_NAME)) {
	    return Griewangk.getfitnessValueNP(individual);
	} else if (functionName.equals(Rastrigin.FUNCTION_NAME)) {
	    return Rastrigin.getfitnessValueNP(individual);
	} else if (functionName.equals(Rosenbrock.FUNCTION_NAME)) {
	    return Rosenbrock.getfitnessValueNP(individual);
	} else {
	    return 0.0;
	}
    }

    @Override
    public String toString() {
	return "The values in the Individual are \n"
		+ Arrays.toString(getValues()) + "\n"
		+ "The population has the fitness as " + getFitnessValue()
		+ "\n";
    }

    public Individual mutate(Individual indvidualToMutate, String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return Spherical.mutateIndividual(indvidualToMutate);
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return Schwefel.mutateIndividual(indvidualToMutate);
	} else if (functionName.equals(Ackley.FUNCTION_NAME)) {
	    return Ackley.mutateIndividual(indvidualToMutate);
	} else if (functionName.equals(Griewangk.FUNCTION_NAME)) {
	    return Griewangk.mutateIndividual(indvidualToMutate);
	} else if (functionName.equals(Rastrigin.FUNCTION_NAME)) {
	    return Rastrigin.mutateIndividual(indvidualToMutate);
	} else if (functionName.equals(Rosenbrock.FUNCTION_NAME)) {
	    return Rosenbrock.mutateIndividual(indvidualToMutate);
	} else {
	    return null;
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub

    }

}

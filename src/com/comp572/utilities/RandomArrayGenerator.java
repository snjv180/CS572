package com.comp572.utilities;

import java.util.Random;

import org.apache.commons.lang3.Range;

public class RandomArrayGenerator {

    private Random random = new Random();

    public double[] getRandomArrayDouble(Range<Double> range, int ArraySize,
	    String functionName) {
	double[] randomArray = new double[ArraySize];

	for (int i = 0; i < randomArray.length; i++) {
	    randomArray[i] = DoubleFormatter.getFormattedDouble(
		    randomInRange(range.getMinimum(), range.getMaximum()),
		    functionName);

	}
	return randomArray;
    }
    
    public Double[] getRandomArrayDoubleNP(Range<Double> range, int ArraySize,
	    String functionName) {
	Double[] randomArray = new Double[ArraySize];

	for (int i = 0; i < randomArray.length; i++) {
	    randomArray[i] = DoubleFormatter.getFormattedDouble(
		    randomInRange(range.getMinimum(), range.getMaximum()),
		    functionName);

	}
	return randomArray;
    }

    public double randomInRange(double min, double max) {
	double range = max - min;
	double scaled = random.nextDouble() * range;
	double shifted = scaled + min;
	return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	RandomArrayGenerator newRandomNumGen = new RandomArrayGenerator();
	Range<Double> range = Range.between(-5.12, 5.12);
	double[] randomArray = newRandomNumGen.getRandomArrayDouble(range, 30,"Spherical");
	for (int i = 0; i < randomArray.length; i++) {
	    System.out
		    .println("Array Value for " + i + " is " + randomArray[i]);
	}

    }
}

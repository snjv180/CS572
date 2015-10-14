/**
 * 
 */
package com.comp572.proj1b;

import com.comp572.functions.Ackley;
import com.comp572.functions.Griewangk;
import com.comp572.functions.Rastrigin;
import com.comp572.functions.Rosenbrock;
import com.comp572.functions.Schwefel;
import com.comp572.functions.Spherical;
import com.comp572.proj1.WriteExcel;
import com.comp572.utilities.Constants;

/**
 * @author Sanjeev
 * 
 */
public class FunctionTest {

    private String addOn = "case4";

    /**
     * @param args
     */
    public static void main(String[] args) {
	FunctionTest ft = new FunctionTest();
	ft.testForSphere();
	ft.testForSchwefel();
	ft.testForAckley();
	ft.testForGriewangk();
	ft.testForRastrigin();
	ft.testForRosenbrock();
    }

    private void testForSphere() {
	functionTester(Spherical.FUNCTION_NAME ,
		Constants.OFFSPRINGSIZE, Constants.POPULATION_SIZE,
		Constants.MAX_ITERATIONS,addOn);
    }

    private void testForSchwefel() {
	functionTester(Schwefel.FUNCTION_NAME , Constants.OFFSPRINGSIZE,
		Constants.POPULATION_SIZE, Constants.MAX_ITERATIONS,addOn);
    }

    private void testForRosenbrock() {
	functionTester(Rosenbrock.FUNCTION_NAME ,
		Constants.OFFSPRINGSIZE, Constants.POPULATION_SIZE,
		Constants.MAX_ITERATIONS,addOn);
    }

    private void testForRastrigin() {
	functionTester(Rastrigin.FUNCTION_NAME ,
		Constants.OFFSPRINGSIZE, Constants.POPULATION_SIZE,
		Constants.MAX_ITERATIONS,addOn);
    }

    private void testForAckley() {
	functionTester(Ackley.FUNCTION_NAME , Constants.OFFSPRINGSIZE,
		Constants.POPULATION_SIZE, Constants.MAX_ITERATIONS,addOn);
    }

    private void testForGriewangk() {
	functionTester(Griewangk.FUNCTION_NAME ,
		Constants.OFFSPRINGSIZE, Constants.POPULATION_SIZE,
		Constants.MAX_ITERATIONS,addOn);
    }

    private void functionTester(String functionName, int offSpringSize,
	    int populationSize, int maxIterations, String addon) {
	System.out.println("Test for " + functionName + " Function");
	Population population = new Population();
	WriteExcel wxl = new WriteExcel();
	population.setFunctionName(functionName);
	population.setOffSpringSize(offSpringSize);
	population.setPopulationSize(populationSize);
	population.setPopulation(population.generatePopulation(functionName));
	int i = 0;
	for (i = 0; !population.getSolutionFound() && i < maxIterations; i++) {
	    population.evolve(functionName, i);
	}
	wxl.createSheet(functionName +addOn+ " Test results",
		population.getPopulationStatsMap());
	wxl.closeExcel(functionName +addOn+ " Test results");
	System.out.println("Iteration for Generation : " + i);
	if (population.getSolutionFound()) {
	    System.out.println(population.getSolution().toString());
	}
	population.populationReset();
    }

}

/**
 * 
 */
package com.comp572.proj2;

/**
 * @author Sanjeev
 * 
 */
public class GPTestClass {

    /**
     * @param args
     */
    public static void main(String[] args) {
	Population population = new Population();
	WriteExcelGP wxl = new WriteExcelGP();
	String addon = "";
	String name = "GP"+addon+" Test results";
	population.setPopulationSize(100);
	population.setOffSpringSize(10);
	population.setTreeHeight(6);
	population.setPopulation(population.generatePopulation());
	
	int i = 0;
	for (i = 0; !population.getSolutionFound() && i < Constants.MAX_ITERATIONS; i++) {
	    population.evolve(i); 
	}

	wxl.createSheet(name, population.getPopulationStatsMap());
	wxl.closeExcel(name);

	/*for (Individual indv : population.getPopulation()) {
	    System.out.println(indv.toString());
	    System.out.println("\n");
	    indv.evaluatePrint();
	    System.out.println("\n");
	}*/

	System.out.println("Iteration for Generation : " + i);
	if (population.getSolutionFound()) {
	    System.out.println("The corresponding solution is : "
		    + population.getSolution().toString());
	}
	Individual.printBestFitOutput(population.getBestIndividualOutput());
	population.populationReset();
    }

}

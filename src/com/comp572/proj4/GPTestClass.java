/**
 * 
 */
package com.comp572.proj4;

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
	String name = "GP" + addon + " Test results";
	population.setPopulationSize(100);
	population.setOffSpringSize(10);
	population.setPopulation(population.generatePopulation());
	//population.printAll();

	int i = 0;
	for (i = 0; !population.getSolutionFound()
		&& i < Constants.MAX_ITERATIONS; i++) {
	    population.evolve(i);
	}

	wxl.createSheet(name, population.getPopulationStatsMap());
	wxl.closeExcel(name);
	

	System.out.println("Iteration for Generation : " + i);
	if (population.getSolutionFound()) {
	    System.out.println("The corresponding solution is : "
		    + population.getSolution().toString());
	}
	population.populationReset();
    }

}

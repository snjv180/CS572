/**
 * 
 */
package com.comp572.proj3;

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
	population.setOffSpringSize(6);
	population.setTreeHeight(7);
	population.setPopulation(population.generatePopulation());

	int i = 0;
	for (i = 0; !population.getSolutionFound()
	&& i < Constants.MAX_ITERATIONS; i++) {
	//while(!population.getSolutionFound()){
	    population.evolve(i++);
	    Individual bestIndividual = population.getBestIndividualOutput();
	    bestIndividual.printBestFitOutput(bestIndividual);
	}

	wxl.createSheet(name, population.getPopulationStatsMap());
	wxl.closeExcel(name);
	int count=1;
	for (Individual indv : population.getPopulation()) {
	    System.out.println("Individual " + count++);
	    System.out.println(indv.toString());
	    System.out.println("Fitness : "+indv.getFitness());
	}

	System.out.println("Iteration for Generation : " + i);
	if (population.getSolutionFound()) {
	    System.out.println("The corresponding solution is : "
		    + population.getSolution().toString());
	}
	Individual bestIndividual = population.getBestIndividualOutput();
	bestIndividual.debug=true;
	bestIndividual.printBestFitOutput(bestIndividual);
	population.populationReset();
    }

}

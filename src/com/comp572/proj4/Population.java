/**
 * 
 */
package com.comp572.proj4;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.comp572.utilities.DoubleFormatter;

/**
 * @author Sanjeev
 * 
 */
public class Population {

    private Set<Individual> population;
    private Set<Individual> populationSubSet;
    private Map<String, PopulationStatistics> populationStatsMap;
    private Double avgFitness;
    private int populationSize;
    private int offSpringSize;
    private Boolean solutionFound;
    private Individual solution;
    private static Random rdm = new Random();

    /**
     * 
     */
    public Population() {
	population = new HashSet<Individual>();
	setPopulationStatsMap(new TreeMap<String, PopulationStatistics>());
	avgFitness = 0.0;
	setPopulationSize(0);
	setOffSpringSize(0);
	solutionFound = false;
    }

    /**
     * @return the population
     */
    public Set<Individual> getPopulation() {
	return population;
    }

    /**
     * @return the populationStatsMap
     */
    public Map<String, PopulationStatistics> getPopulationStatsMap() {
	return populationStatsMap;
    }

    /**
     * @param populationStatsMap
     *            the populationStatsMap to set
     */
    public void setPopulationStatsMap(
	    Map<String, PopulationStatistics> populationStatsMap) {
	this.populationStatsMap = populationStatsMap;
    }

    /**
     * @param population
     *            the population to set
     */
    public void setPopulation(Set<Individual> population) {
	this.population = population;
    }

    /**
     * @return the populationSubSet
     */
    public Set<Individual> getPopulationSubSet() {
	return populationSubSet;
    }

    /**
     * @param populationSubSet
     *            the populationSubSet to set
     */
    public void setPopulationSubSet(Set<Individual> populationSubSet) {
	this.populationSubSet = populationSubSet;
    }

    /**
     * @return the avgFitness
     */
    public Double getAvgFitness() {
	return avgFitness;
    }

    /**
     * @param avgFitness
     *            the avgFitness to set
     */
    public void setAvgFitness(Double avgFitness) {
	this.avgFitness = avgFitness;
    }

    /**
     * @return the populationSize
     */
    public int getPopulationSize() {
	return populationSize;
    }

    /**
     * @param populationSize
     *            the populationSize to set
     */
    public void setPopulationSize(int populationSize) {
	this.populationSize = populationSize;
    }

    /**
     * @return the offSpringSize
     */
    public int getOffSpringSize() {
	return offSpringSize;
    }

    /**
     * @param offSpringSize
     *            the offSpringSize to set
     */
    public void setOffSpringSize(int offSpringSize) {
	this.offSpringSize = offSpringSize;
    }

    /**
     * @return the solutionFound
     */
    public Boolean getSolutionFound() {
	return solutionFound;
    }

    /**
     * @param solutionFound
     *            the solutionFound to set
     */
    public void setSolutionFound(Boolean solutionFound) {
	this.solutionFound = solutionFound;
    }

    /**
     * @return the solution
     */
    public Individual getSolution() {
	return solution;
    }

    /**
     * @param solution
     *            the solution to set
     */
    public void setSolution(Individual solution) {
	this.solution = solution;
    }

    public Set<Individual> generatePopulation() {
	Set<Individual> populationSet = new HashSet<Individual>();
	for (int i = 0; i < populationSize; i++) {
	    Individual indv = new Individual();
	    populationSet.add(indv);
	}
	return populationSet;
    }

    private void checkForSolution() {
	for (Individual indv : population) {
	    if (Math.abs(indv.getFitness()) <= 0.0) {
		solutionFound = true;
		solution = new Individual();
		solution = indv;
	    }
	}
    }

    public void gatherPopulationStats(Integer generation, Long timeToEvolve4Gen) {
	Individual bestIndv = new Individual();
	bestIndv.setFitness(50000.0);
	Double avgFitness = 0.0;
	Double avgSize = 0.0;
	for (Individual indv : getPopulation()) {
	    if (bestIndv.getFitness() > indv.getFitness()) {
		bestIndv = indv;
	    }
	    avgFitness += indv.getFitness();
	}
	avgFitness = DoubleFormatter.getFormattedSolution(avgFitness
		/ populationSize);
	avgSize = DoubleFormatter
		.getFormattedSolution(avgSize / populationSize);
	populationStatsMap.put(
		generation.toString(),
		new PopulationStatistics(generation, bestIndv.toString(),
			bestIndv.getFitness(), avgFitness, avgSize, Double
				.valueOf(timeToEvolve4Gen.doubleValue())));
    }

    public Individual getBestIndividualOutput() {
	Individual bestIndv = new Individual();
	bestIndv.setFitness(50000.0);
	for (Individual indv : getPopulation()) {
	    if (bestIndv.getFitness() > indv.getFitness()) {
		bestIndv = indv;
	    }
	}
	return bestIndv;
    }

    public void populationReset() {
	population.clear();
	// populationSubSet.clear();
	populationStatsMap.clear();
    }

    public void printAll() {
	int i = 1;
	for (Individual indv : population) {
	    System.out.println(i++ + ". " + " fitness " + indv.getFitness()
		    + " " + indv.toString());
	}
    }

    public Set<Individual> selectedIndividuals() {
	populationSubSet = new HashSet<Individual>();
	while (populationSubSet.size() != offSpringSize) {
	    populationSubSet.add(getTournamentWinner());
	}
	return populationSubSet;
    }

    private Individual getTournamentWinner() {
	Individual winner = new Individual();
	Individual temp = new Individual();
	winner = getRandomIndividual(getPopulation());
	for (int i = 0; i < offSpringSize; i++) {
	    temp = getRandomIndividual(getPopulation());
	    if ((temp.getFitness() < winner.getFitness())
		    && !populationSubSet.contains(winner)) {
		winner = temp;
		break;
	    }
	}
	return winner.getCopy();
    }

    private Individual getTournamentLoser() {
	Individual loser = new Individual();
	loser = getRandomIndividual(getPopulation());
	for (Individual indv : getPopulation()) {
	    if (indv.getFitness() >= loser.getFitness()) {
		loser = indv;
	    }
	}
	return loser;
    }

    private Individual getRandomIndividual(Set<Individual> population) {
	int size = population.size();
	int item = rdm.nextInt(size); // In real life, the Random
				      // object should be rather more
				      // shared than this
	int i = 0;
	for (Individual indv : population) {
	    if (i == item)
		return indv;
	    i = i + 1;
	}
	return null;
    }

    public void evolve(Integer generation) {
	long startTime = System.nanoTime();

	populationSubSet = selectedIndividuals();

	populationSubSet = crossOver(populationSubSet);

	populationSubSet = mutateOffSprings(populationSubSet);

	appendToPopulation(populationSubSet);

	removeExtraPopulation();

	printAll();

	checkForSolution();
	if (generation % 10 == 0 || getSolutionFound()) {
	    gatherPopulationStats(generation,
		    (System.nanoTime() - startTime) / 1000);
	}

	populationSubSet.clear();

	System.out.println("Generation " + generation + " complete. ");

    }
/*
    private void checkforDuplicate() {
	boolean checkflag = false;
	int count;
	for (Individual indv1 : getPopulation()) {
	    count=0;
	    for (Individual indv2 : getPopulation()) {
		if(indv1.toString().equalsIgnoreCase(indv2.toString())){
		    count++;
		    if (count>1){
			checkflag=true;
			System.out.println("Duplicate detected");
		    }
		}
	    }
	}
    }*/

    private Set<Individual> mutateOffSprings(Set<Individual> popSubSet) {
	for (Individual individual : popSubSet) {
	    // System.out.println("Before Mutation " +individual.toString());
	    mutate(individual);
	    individual.getFitness();
	    // System.out.println("After Mutation " +individual.toString());
	}
	return popSubSet;
    }

    private void mutate(Individual individual) {
	
	for (int gridId = 0; gridId < Constants.BOARDSIZE; gridId++) {
	    if (rdm.nextFloat() <= 0.10) {
		individual.mutate(gridId, individual);
	    }
	}
    }

    private Set<Individual> crossOver(Set<Individual> popSubSet) {
	Set<Individual> resultSet = new HashSet<Individual>();
	Individual[] indvArray = new Individual[popSubSet.size()];
	indvArray = popSubSet.toArray(indvArray);
	for (int i = 0; i < indvArray.length; i = i + 2) {
	    exchangeInfo(indvArray[i], indvArray[i + 1]);
	    resultSet.add(indvArray[i]);
	    resultSet.add(indvArray[i + 1]);
	}
	return resultSet;
    }

    private void exchangeInfo(Individual firstParent, Individual secondParent) {
	// exchange grids and u r done
	for (int gridId = 0; gridId < Constants.BOARDSIZE; gridId++) {
	    if (rdm.nextFloat() < 0.2) {
		firstParent.exchangeGridInfo(gridId, firstParent, secondParent);
		firstParent.getFitness();
		secondParent.getFitness();
	    }
	}
    }

    public void appendToPopulation(Set<Individual> popSubSet) {
	for (Individual individual : popSubSet) {
	    if (!checkIfDuplicate(individual)) {
		population.add(individual);
	    }
	}
    }

    private boolean checkIfDuplicate(Individual individual) {
	Boolean checkFlag = false;
	if (population.contains(individual)) {
	    checkFlag = true;
	}
	for (Individual popIndv : population) {
	    if ( popIndv.toString().equalsIgnoreCase(individual.toString())) {
		checkFlag = true;
		break;
	    }
	}
	return checkFlag;
    }

    public void removeExtraPopulation() {
	while (population.size() != populationSize) {
	    population.remove(getTournamentLoser());
	}
    }
}

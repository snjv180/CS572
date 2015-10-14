/**
 * 
 */
package com.comp572.proj1b;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.comp572.utilities.Constants;
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
    private String functionName;
    private int populationSize;
    private int offSpringSize;
    private Boolean solutionFound;
    private Individual solution;

    /**
     * 
     */
    public Population() {
	population = new HashSet<Individual>();
	setPopulationStatsMap(new TreeMap<String, PopulationStatistics>());
	avgFitness = 0.0;
	functionName = "";
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
     * @param population
     *            the population to set
     */
    public void setPopulation(Set<Individual> population) {
	this.population = population;
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
     * @return the functionName
     */
    public String getFunctionName() {
	return functionName;
    }

    /**
     * @param functionName
     *            the functionName to set
     */
    public void setFunctionName(String functionName) {
	this.functionName = functionName;
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

    public Set<Individual> generatePopulation(String functionName) {
	Set<Individual> populationSet = new HashSet<Individual>();
	for (int i = 0; i < populationSize; i++) {
	    Individual indv = new Individual();
	    populationSet.add(indv.generateIndividual(functionName));
	}
	return populationSet;
    }

    public void printAll() {
	int i = 1;
	for (Individual indv : population) {
	    System.out.println(i++ + ". " + indv.toString());
	}
    }

    public Set<Individual> selectedIndividuals() {
	populationSubSet = new HashSet<Individual>();
	while (populationSubSet.size() != offSpringSize) {
	    populationSubSet.add(getTournamentWinner());
	}
	return populationSubSet;
    }

    private void checkForSolution() {
	for (Individual indv : population) {
	    if (DoubleFormatter.getFormattedSolution(
		    Math.abs(indv.getFitnessValue())).equals(0.00)) {
		solutionFound = true;
		solution = new Individual();
		solution = indv;
	    }
	}
    }

    private Individual getTournamentWinner() {
	Individual winner = new Individual();
	Individual temp = new Individual();
	winner = getRandomIndividual(getPopulation());
	for (int i = 0; i < offSpringSize; i++) {
	    temp = getRandomIndividual(getPopulation());
	    if (temp.getFitnessValue() < winner.getFitnessValue()) {
		winner = temp;
	    }
	}
	return winner;
    }

    private Individual getRandomIndividual(Set<Individual> population) {
	int size = population.size();
	int item = new Random().nextInt(size); // In real life, the Random
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

    public HashSet<Individual> mutateAllOffSprings(
	    Set<Individual> individualsToMutate, String functionName) {
	HashSet<Individual> indvSubSet = new HashSet<Individual>();
	for (Individual individual : individualsToMutate) {
	    indvSubSet.add(individual.mutate(individual, functionName));
	}
	return indvSubSet;
    }

    public void appendToPopulation(Set<Individual> popSubSet) {
	for (Individual individual : popSubSet) {
	    population.add(individual);
	}
    }

    public void removeExtraPopulation() {
	while (population.size() != populationSize) {
	    Individual toRemove = new Individual();
	    toRemove.setFitnessValue(-50000.0);
	    for (Individual indv : population) {
		if (indv.getFitnessValue() > toRemove.getFitnessValue()) {
		    toRemove = indv;
		}
	    }
	    population.remove(toRemove);
	}
    }

    public void populationReset() {
	population.clear();
	populationSubSet.clear();
	populationStatsMap.clear();
    }

    public void evolve(String functionName, Integer generation) {
	long startTime = System.nanoTime();
	populationSubSet = selectedIndividuals();
	populationSubSet = crossOver(populationSubSet, functionName);
	populationSubSet = mutateAllOffSprings(populationSubSet, functionName);
	appendToPopulation(populationSubSet);
	removeExtraPopulation();
	//printAll();
	checkForSolution();
	gatherPopulationStats(functionName, generation,
		(System.nanoTime() - startTime) / 1000);
    }

    private Set<Individual> crossOver(Set<Individual> popSubSet,
	    String functionName) {
	Set<Individual> resultSet = new HashSet<Individual>();
	Individual child[] = new Individual[2];
	Individual[] indvArray = new Individual[popSubSet.size()];
	popSubSet.toArray(indvArray);
	for (int i = 0; i < indvArray.length; i = i + 2) {
	    child[0] = new Individual();
	    child[1] = new Individual();
	    child = exchangeInfo(indvArray[i], indvArray[i + 1], functionName);
	    resultSet.add(child[0]);
	    resultSet.add(child[1]);

	}
	return resultSet;
    }

    private Individual[] exchangeInfo(Individual firstParent,
	    Individual secondParent, String functionName) {
	Random rn = new Random();

	Individual[] resultArray = new Individual[2];

	Double[] firstParentVals = new Double[Constants.DIMENSIONS];
	firstParentVals = firstParent.getValues();

	Double[] secondParentVals = new Double[Constants.DIMENSIONS];
	secondParentVals = secondParent.getValues();

	Double[] tempArray = new Double[Constants.DIMENSIONS];

	for (int j = 0; j < Constants.DIMENSIONS; j++) {
	    if (rn.nextDouble() > 0.5) {
		tempArray[j] = firstParentVals[j];
		firstParentVals[j] = secondParentVals[j];
		secondParentVals[j] = tempArray[j];
	    }
	}

	Individual child = new Individual();
	child.setValues(firstParentVals);
	child.setFitnessValue(child.getFitnessOfIndividual(firstParentVals,
		functionName));
	resultArray[0] = new Individual(child.getValues(),
		child.getFitnessValue());

	child.setValues(secondParentVals);
	child.setFitnessValue(child.getFitnessOfIndividual(secondParentVals,
		functionName));
	resultArray[1] = new Individual(child.getValues(),
		child.getFitnessValue());

	return resultArray;
    }

    private void gatherPopulationStats(String functionName, Integer generation,
	    Long timeToEvolve4Gen) {
	Individual bestIndv = new Individual();
	bestIndv.setFitnessValue(50000.0);
	Double avgFitness = 0.0;
	for (Individual indv : getPopulation()) {
	    if (bestIndv.getFitnessValue() > indv.getFitnessValue()) {
		bestIndv = indv;
	    }
	    avgFitness += indv.getFitnessValue();
	}
	avgFitness = DoubleFormatter.getFormattedDouble(
		(avgFitness / populationSize), functionName);
	populationStatsMap.put(
		generation.toString(),
		new PopulationStatistics(generation, Arrays.toString(bestIndv
			.getValues()), bestIndv.getFitnessValue(), avgFitness,
			Double.valueOf(timeToEvolve4Gen.doubleValue())));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub

    }

}

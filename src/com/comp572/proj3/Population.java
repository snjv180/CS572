/**
 * 
 */
package com.comp572.proj3;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.SerializationUtils;

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
    private Double avgSize;
    private int populationSize;
    private int offSpringSize;
    private int treeHeight;
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
	avgSize = 0.0;
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
     * @return the avgSize
     */
    public Double getAvgSize() {
	return avgSize;
    }

    /**
     * @param avgSize
     *            the avgSize to set
     */
    public void setAvgSize(Double avgSize) {
	this.avgSize = avgSize;
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
     * @return the treeHeight
     */
    public int getTreeHeight() {
	return treeHeight;
    }

    /**
     * @param treeHeight
     *            the treeHeight to set
     */
    public void setTreeHeight(int treeHeight) {
	this.treeHeight = treeHeight;
    }

    public Set<Individual> generatePopulation() {
	Set<Individual> populationSet = new HashSet<Individual>();
	for (int i = 0; i < populationSize; i++) {
	    Individual indv = new Individual();
	    indv.generate(rdm.nextInt(getTreeHeight())+1);
	    indv.evaluate();
	    indv.calcSize();
	    populationSet.add(indv);
	}
	return populationSet;
    }

    private void checkForSolution() {
	for (Individual indv : population) {
	    if (Math.abs(indv.getFitness()) <= 20) {
		solutionFound = true;
		solution = new Individual();
		solution = indv;
	    }
	}
    }

    public void gatherPopulationStats(Integer generation, Long timeToEvolve4Gen) {
	Individual bestIndv = new Individual();
	bestIndv.setFitness(50000);
	Double avgFitness = 0.0;
	Double avgSize = 0.0;
	for (Individual indv : getPopulation()) {
	    if (bestIndv.getFitness() > indv.getFitness()) {
		bestIndv = indv;
	    }
	    avgFitness += indv.getFitness();
	    avgSize += indv.getSize();
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
	bestIndv.setFitness(50000);
	for (Individual indv : getPopulation()) {
	    if (bestIndv.getFitness() > indv.getFitness()) {
		bestIndv = indv;
	    }
	}
	return bestIndv;
    }

    public void populationReset() {
	population.clear();
	populationSubSet.clear();
	populationStatsMap.clear();
    }

    public void printAll() {
	int i = 1;
	for (Individual indv : population) {
	    System.out.println(i++ + ". " + " fitness " + indv.getFitness()
		    + indv.toString());
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
	    }
	}
	return SerializationUtils.clone(winner);
    }

    private Individual getTournamentLoser() {
	Individual loser = new Individual();
	loser = getRandomIndividual(getPopulation());
	for (Individual indv : getPopulation()) {
	    if (indv.getFitness() > loser.getFitness()) {
		loser = indv;
	    } else if (indv.getFitness() == loser.getFitness()) {
		if (indv.getSize() > loser.getSize()) {
		    loser = indv;
		}
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

	checkForSolution();
	if (generation % 10 == 0 || getSolutionFound()) {
	    gatherPopulationStats(generation,
		    (System.nanoTime() - startTime) / 1000);
	}

	populationSubSet.clear();

	System.out.println("Generation " + generation + " complete. ");

    }

    private Set<Individual> mutateOffSprings(Set<Individual> popSubSet) {
	for (Individual individual : popSubSet) {
	    // System.out.println("Before Mutation " +individual.toString());
	    mutate(individual.getTheIndv());
	    individual.calcSize();
	    individual.evaluate();
	    // System.out.println("After Mutation " +individual.toString());
	}
	return popSubSet;
    }

    private void mutate(Node root) {
	// if the root is null no need for mutation
	if (root == null) {
	    return;
	}
	if (rdm.nextFloat() < 0.3) {
	    // if the type of node is IFFOODAHEAD or IFNOFOODAHEAD
	    if (root.getType() == Command.IFFOODAHEAD
		    || root.getType() == Command.IFNOFOODAHEAD
		    || root.getType() == Command.PROG2) {
		// Change Node Type
		root.setType(rdm.nextInt(3) + 1);
		mutate(root.getBranches()[0]);
		mutate(root.getBranches()[1]);
	    }
	    // if the type of the node is LEFT or RIGHT or FORWARD
	    else if (root.getType() == Command.LEFT
		    || root.getType() == Command.RIGHT
		    || root.getType() == Command.FORWARD) {
		// Change Node Type
		root.setType(rdm.nextInt(3) + 4);
	    }
	}
    }

    private Set<Individual> crossOver(Set<Individual> popSubSet) {
	Set<Individual> resultSet = new HashSet<Individual>();
	Individual child[] = new Individual[2];
	Individual[] indvArray = new Individual[popSubSet.size()];
	popSubSet.toArray(indvArray);
	for (int i = 0; i < indvArray.length; i = i + 2) {
	    child[0] = new Individual();
	    child[1] = new Individual();
	    child = exchangeInfo(indvArray[i], indvArray[i + 1]);
	    resultSet.add(child[0]);
	    resultSet.add(child[1]);

	}
	return resultSet;
    }

    private Individual[] exchangeInfo(Individual firstParent,
	    Individual secondParent) {

	Individual[] resultArray = new Individual[2];

	// Create a temporary node
	Node temp = new Node();
	// Get a random node from firstParent
	Node firstParentRandNode = new Node();

	firstParentRandNode = firstParent.getRandomNode();
	// Get firstRandom Node's Parent
	Node firstRandNodeParent = new Node();
	firstRandNodeParent = firstParentRandNode.getParent();
	// Get a random node from secondParent
	Node secondParentRandNode = new Node();
	secondParentRandNode = secondParent.getRandomNode();
	// Get secondRandom Node's Parent
	Node secondRandNodeParent = new Node();
	secondRandNodeParent = secondParentRandNode.getParent();

	int firstRandNodeBranchLoc = 0, secondRandNodeBranchLoc = 0;

	for (int i = 0; i < Constants.MAX_ARITY; i++) {
	    if (firstRandNodeParent != null) {
		if (firstRandNodeParent.getBranches()[i] != null
			&& firstRandNodeParent.getBranches()[i]
				.equals(firstParentRandNode)) {
		    firstRandNodeBranchLoc = i;
		    break;
		}
	    }

	}

	for (int i = 0; i < Constants.MAX_ARITY; i++) {
	    if (secondRandNodeParent != null) {
		if (secondRandNodeParent.getBranches()[i] != null
			&& secondRandNodeParent.getBranches()[i]
				.equals(secondParentRandNode)) {
		    secondRandNodeBranchLoc = i;
		    break;
		}
	    }
	}

	/*
	 * System.out.println("\nStatus of Parents Before Exchange");
	 * System.out.println("First Parent: " + firstParent.toString());
	 * System.out.println("Second Parent: " + secondParent.toString());
	 */

	// Exchange the nodes
	// temp.setParent(firstParentRandNode.getParent());
	temp = firstParentRandNode;
	// firstParentRandNode.setParent(secondParentRandNode.getParent());
	firstParentRandNode = secondParentRandNode;
	// secondParentRandNode.setParent(temp.getParent());
	secondParentRandNode = temp;

	// Adjust the corresponding branches.
	if (firstRandNodeParent != null) {
	    firstRandNodeParent.getBranches()[firstRandNodeBranchLoc] = firstParentRandNode;
	} else {
	    firstParent.setTheIndv(firstParentRandNode);
	}

	if (secondRandNodeParent != null) {
	    secondRandNodeParent.getBranches()[secondRandNodeBranchLoc] = secondParentRandNode;
	} else {
	    secondParent.setTheIndv(secondParentRandNode);
	}

	/*
	 * System.out.println("Status of Parents After Exchange");
	 * System.out.println("First Parent: " + firstParent.toString());
	 * System.out.println("Second Parent: " + secondParent.toString() +
	 * "\n");
	 */
	firstParent.calcSize();
	secondParent.calcSize();

	firstParent.evaluate();
	secondParent.evaluate();

	resultArray[0] = firstParent;

	resultArray[1] = secondParent;

	return resultArray;
    }

    public Individual copyIndividual(Individual srcIndv) {
	Individual destIndv = new Individual();
	destIndv = SerializationUtils.clone(srcIndv);
	return destIndv;
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
	    if (popIndv.getFitness() == individual.getFitness()
		    && popIndv.getSize() == individual.getSize()) {
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

/**
 * 
 */
package com.comp572.proj4;

import java.util.Random;

/**
 * @author Sanjeev
 * 
 */
public class Individual {
    // fitness of the individual
    private Double fitness;
    // Random number Generator
    private static Random rdm = new Random();
    // Create a board
    private Board board;

    /**
     * 
     */
    public Individual() {
	setBoard(new Board());
	setFitness(getFitness());
    }

    /**
     * @return the fitness
     */
    public Double getFitness() {
	setFitness(board.getFitness());
	return fitness;
    }

    /**
     * @param fitness
     *            the fitness to set
     */
    public void setFitness(Double fitness) {
	this.fitness = fitness;
    }

    /**
     * @return the rdm
     */
    public static Random getRdm() {
	return rdm;
    }

    /**
     * @param rdm
     *            the rdm to set
     */
    public static void setRdm(Random rdm) {
	Individual.rdm = rdm;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
	return board;
    }

    /**
     * @param board
     *            the board to set
     */
    public void setBoard(Board board) {
	this.board = board;
    }

    public Individual getCopy() {
	Individual copy = new Individual();
	copy.setFitness(this.getFitness());
	copy.setBoard(this.getBoard().copyBoard());
	return copy;
    }

    public void exchangeGridInfo(int gridNum, Individual firstParent,
	    Individual secondParent) {
	board.exchangeGridInfo(gridNum, firstParent.board, secondParent.board);
    }

    @Override
    public String toString() {
	return this.board.toString();
    }

    public void mutate(int gridId, Individual individual) {
	board.changeGridValue(gridId,individual.getBoard());
    }

  
}

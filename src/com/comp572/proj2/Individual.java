/**
 * 
 */
package com.comp572.proj2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import com.comp572.utilities.DoubleFormatter;

/**
 * @author Sanjeev
 * 
 */
public class Individual implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4930729355833133477L;
    // fitness of the individual
    private Double fitness;
    // total number of nodes in the tree
    private int size;
    // pointer to the root of the 'program' tree
    private Node theIndv;
    //load fileInput values
    public static Double[] fileInputs;
    //load fileOutput values
    public static Double[] fileOutputs;

    /**
     * @return the fitness
     */
    public Double getFitness() {
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
     * @return the size
     */
    public int getSize() {
	return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(int size) {
	this.size = size;
    }

    /**
     * @return the theIndv
     */
    public Node getTheIndv() {
	return theIndv;
    }

    /**
     * @param theIndv
     *            the theIndv to set
     */
    public void setTheIndv(Node theIndv) {
	this.theIndv = theIndv;
    }

    /*
     * Calculates fitness. For the symbolic regression problem it has to
     * reevaluate the expression tree for each of the X points. The square root
     * of the sum of the squared errors at each of those points is the fitness.
     */
    /*public void evaluate() {
	fitness = 0.0;
	Double output;
	for (int j = 0; j < Constants.NUM_POINTS; j++) {
	    // evaluate function on each input point
	    output = theIndv.evaluate(Constants.inputs[j]);
	    fitness += (Math.pow((output - Constants.outputs[j]), 2));
	}
	fitness = DoubleFormatter.getFormattedSolution(Math.sqrt(fitness));
    }*/
    
    public void evaluate() {
	fitness = 0.0;
	Double output;
	for (int j = 0; j < fileInputs.length; j++) {
	    // evaluate function on each input point
	    output = theIndv.evaluate(fileInputs[j]);
	    fitness += (Math.pow((output - fileOutputs[j]), 2));
	}
	fitness = DoubleFormatter.getFormattedSolution(Math.sqrt(fitness));
    }
    
    public static void printBestFitOutput(Individual indv){
	Double output;
	
	try {
	    	File file = new File("tests\\GeneratedOutput.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for (int j = 0; j < fileInputs.length; j++) {
		    // evaluate function on each input point
		    output = indv.theIndv.evaluate(fileInputs[j]);
		    bw.write(output.toString()+"\n");
		}
		bw.flush();
		
		bw.close();

		System.out.println("Test Output Data Done");

	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
    }

    public void evaluatePrint() {
	fitness = 0.0;
	Double output;
	for (int j = 0; j < Constants.NUM_POINTS; j++) {
	    // evaluate function on each input point
	    output = theIndv.evaluate(Constants.inputs[j]);
	    fitness += (Math.pow((output - Constants.outputs[j]), 2));
	    // outputs array holds correct values
	    System.out.println(Constants.inputs[j] + " , " + output + " , "
		    + Constants.outputs[j] + " , ");
	}
	fitness = DoubleFormatter.getFormattedSolution(Math.sqrt(fitness));
	System.out.println("Fitness = " + fitness);
    }

    /* Calculates the size of an individual's tree. */
    public void calcSize() {
	size = 0;
	size = theIndv.calcSize(theIndv);
    }

    /*
     * Generates a random full tree.
     */
    public void generate(int maxDepth) {
	theIndv = new Node();
	theIndv.full(0, maxDepth, null);
    }

    /*
     * Calls a function to erase/free the tree.
     */
    public void erase() {
	theIndv.erase();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	Individual i = new Individual();
	i.generate(5);
	i.evaluate();
	i.evaluatePrint();
	i.calcSize();
	// i.getTheIndv().inorder(i.getTheIndv());
	System.out.println("Formula is "+ i.toString());
	//Node node=i.getRandomNode();
	//System.out.println(node);
	System.out.println("\nSize = " + i.getSize());
    }
    
    public Node getRandomNode(){
	return theIndv.getRandomNode1(theIndv, 0, new Random().nextInt(getSize()));
    }

    public String toString() {
	theIndv.setExpression("");
	theIndv.inorder(theIndv);
	return theIndv.getExpression();
	// return "";
    }
}

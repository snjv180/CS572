/**
 * 
 */
package com.comp572.proj3.gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sanjeev
 * 
 */
public class AntVectorReader {

    public static List<AntVector> readAntData() {
	List<AntVector> antVectors = new LinkedList<AntVector>();
	try (BufferedReader br = new BufferedReader(new FileReader(
		"BestAntTrail.txt"))) {

	    String sCurrentLine = "";

	    while ((sCurrentLine = br.readLine()) != null) {
		AntVector antVector = new AntVector();
		antVector.setDirection(sCurrentLine.substring(0,
			sCurrentLine.indexOf(' ')).trim());
		antVector.setAntXPos(Integer.parseInt(sCurrentLine.substring(
			sCurrentLine.indexOf('(') + 1,
			sCurrentLine.indexOf(',')).trim()));
		antVector.setAntYPos(Integer.parseInt(sCurrentLine.substring(
			sCurrentLine.indexOf(',') + 1,
			sCurrentLine.indexOf(')')).trim()));
		antVectors.add(antVector);
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return antVectors;
    }

    public static void main(String[] args) {

	try (BufferedReader br = new BufferedReader(new FileReader(
		"BestAntTrail.txt"))) {

	    String sCurrentLine = "";

	    while ((sCurrentLine = br.readLine()) != null) {
		System.out.println(sCurrentLine);
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

}

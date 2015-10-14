/**
 * 
 */
package com.comp572.proj3.gui;

/**
 * @author Sanjeev
 * 
 */
public class AntVector {
    private int antXPos;
    private int antYPos;
    private String direction;

    /**
     * Default Constructor
     */
    public AntVector() {
	setAntXPos(0);
	setAntYPos(0);
	setDirection("");
    }

    /**
     * @return the antXPos
     */
    public int getAntXPos() {
	return antXPos;
    }

    /**
     * @param antXPos
     *            the antXPos to set
     */
    public void setAntXPos(int antXPos) {
	this.antXPos = antXPos;
    }

    /**
     * @return the antYPos
     */
    public int getAntYPos() {
	return antYPos;
    }

    /**
     * @param antYPos
     *            the antYPos to set
     */
    public void setAntYPos(int antYPos) {
	this.antYPos = antYPos;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
	return direction;
    }

    /**
     * @param direction
     *            the direction to set
     */
    public void setDirection(String direction) {
	this.direction = direction;
    }

}

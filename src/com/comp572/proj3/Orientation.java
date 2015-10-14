/**
 * 
 */
package com.comp572.proj3;

/**
 * @author Sanjeev
 *
 */
public enum Orientation {
    EAST(0),WEST(1), NORTH(2), SOUTH(3);
    
    private int orientation;
    
    private Orientation(int orientation){
	this.setOrientation(orientation);
    }

    /**
     * @return the orientation
     */
    public int getOrientation() {
	return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(int orientation) {
	this.orientation = orientation;
    }
    
    
}

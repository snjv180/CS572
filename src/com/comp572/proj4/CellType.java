/**
 * 
 */
package com.comp572.proj4;

/**
 * @author Sanjeev
 * 
 */
public enum CellType {
    VALUE(1), GIVEN(2), PREDETERMINED(3);

    private int type;

    private CellType(int type) {
	this.setType(type);
    }

    /**
     * @return the type
     */
    public int getType() {
	return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type) {
	this.type = type;
    }

    public String toString() {
	if (type == 1) {
	    return "(V)";
	} else if (type == 2) {
	    return "(G)";
	} else {
	    return "(P)";
	}
    }
}

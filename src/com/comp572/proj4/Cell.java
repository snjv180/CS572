/**
 * 
 */
package com.comp572.proj4;

/**
 * @author Sanjeev
 *
 */
public class Cell {
    private Integer cellValue;
    
    private CellType cellType;

    /**
     * 
     */
    public Cell() {
	setCellValue(0);
	setCellType(CellType.VALUE);
    }

    /**
     * @return the cellValue
     */
    public Integer getCellValue() {
        return cellValue;
    }

    /**
     * @param cellValue the cellValue to set
     */
    public void setCellValue(Integer cellValue) {
        this.cellValue = cellValue;
    }

    /**
     * @return the cellType
     */
    public CellType getCellType() {
        return cellType;
    }

    /**
     * @param cellType the cellType to set
     */
    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }
    
    
    
}

/**
 * 
 */
package com.comp572.proj4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.ListUtils;

/**
 * @author Sanjeev
 * 
 */
public class Board {

    private Cell[][] board;
    private static Cell[][] backupBoard = new Cell[Constants.BOARDSIZE][Constants.BOARDSIZE];
    private static Boolean isDataLoaded = false;
    private static List<Integer> permuteList = new LinkedList<Integer>();
    public static Random rdm = new Random();

    public Board() {
	init();
    }

    /**
     * 
     */
    public void init() {
	// allocate memory for board
	board = new Cell[Constants.BOARDSIZE][Constants.BOARDSIZE];
	// createBoard
	createBoard(board);
	if (!isDataLoaded) {
	    // create permutation List
	    fillList(permuteList);
	    // fill with given values
	    fillBoardWithGivenValues();
	    // fill with predefined values
	    fillBoardWithPredefinedValues();
	    // fill the backUpBoard with
	    // the predefined values and given values
	    fillToBoard(board, backupBoard);
	    // load Data Values only once
	    isDataLoaded = true;
	} else {
	    fillToBoard(backupBoard, board);
	}

	fillInGrid();

    }

    /**
     * @return the board
     */
    public Cell[][] getBoard() {
	return board;
    }

    /**
     * @param board
     *            the board to set
     */
    public void setBoard(Cell[][] board) {
	this.board = board;
    }

    private void fillInGrid() {
	fillList(permuteList);
	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    for (int j = 0; j < Constants.BOARDSIZE; j++) {
		if (board[i][j].getCellType() == CellType.VALUE) {
		    checkInSubGrid(i, j, permuteList);
		    fillValueXY(i, j,
			    permuteList.get(rdm.nextInt(permuteList.size())),
			    CellType.VALUE);
		    fillList(permuteList);
		}
	    }
	}
    }

    private void fillBoardWithPredefinedValues() {
	for (int i = 0; i < Constants.MAXPREDEFINEDITERATIONS; i++) {
	    calulatePredefinedNumbers();
	}
    }

    private void calulatePredefinedNumbers() {
	fillList(permuteList);
	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    for (int j = 0; j < Constants.BOARDSIZE; j++) {
		if (board[i][j].getCellType() == CellType.VALUE) {
		    checkInRow(i, permuteList);
		    checkInColumn(j, permuteList);
		    checkInSubGrid(i, j, permuteList);
		    if (permuteList.size() == 1) {
			fillValueXY(i, j, permuteList.get(0),
				CellType.PREDETERMINED);
		    } else {
			checkForHiddenSingleRow(i, j, permuteList);
			checkForHiddenSingleColumn(i, j, permuteList);
			// checkForCrosshatch(i, j, permuteList);
		    }
		    fillList(permuteList);
		}
	    }
	}

    }

    @SuppressWarnings("unchecked")
    public void checkForHiddenSingleRow(int rowNum, int columnNum,
	    List<Integer> permuteList) {

	List<Integer> result = new LinkedList<Integer>();

	for (int j = 0; j < Constants.BOARDSIZE; j++) {
	    if (j == columnNum)
		continue;
	    List<Integer> temp = new LinkedList<Integer>();
	    if (board[rowNum][j].getCellType() == CellType.VALUE) {
		fillList(temp);
		checkInRow(rowNum, temp);
		checkInColumn(j, temp);
		checkInSubGrid(rowNum, j, temp);
		result = ListUtils.union(result, temp);
	    }

	}

	List<Integer> solution = new LinkedList<Integer>();

	solution = ListUtils.subtract(permuteList, result);

	if (solution.size() == 1) {
	    fillValueXY(rowNum, columnNum, solution.get(0),
		    CellType.PREDETERMINED);
	}
    }

    @SuppressWarnings("unchecked")
    public void checkForHiddenSingleColumn(int rowNum, int columnNum,
	    List<Integer> permuteList) {
	List<Integer> result = new LinkedList<Integer>();

	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    if (i == rowNum)
		continue;
	    List<Integer> temp = new LinkedList<Integer>();
	    if (board[i][columnNum].getCellType() == CellType.VALUE) {
		fillList(temp);
		checkInRow(i, temp);
		checkInColumn(columnNum, temp);
		checkInSubGrid(i, columnNum, temp);
		result = ListUtils.union(result, temp);
	    }
	}

	List<Integer> solution = new LinkedList<Integer>();

	solution = ListUtils.subtract(permuteList, result);

	if (solution.size() == 1) {
	    fillValueXY(rowNum, columnNum, solution.get(0),
		    CellType.PREDETERMINED);
	}
    }

    public void checkInSubGrid(int rowNum, int columnNum,
	    List<Integer> permuteList) {
	int rowIndex = 0, columnIndex = 0;

	for (int i = 0; i < Constants.SUBBOARDSIZE; i++) {
	    for (int j = 0; j < Constants.SUBBOARDSIZE; j++) {
		if (rowNum >= (i * Constants.SUBBOARDSIZE)
			&& rowNum <= (((i + 1) * Constants.SUBBOARDSIZE) - 1)
			&& columnNum >= (j * Constants.SUBBOARDSIZE)
			&& columnNum <= (((j + 1) * Constants.SUBBOARDSIZE) - 1)) {
		    rowIndex = ((i * Constants.SUBBOARDSIZE));
		    columnIndex = ((j * Constants.SUBBOARDSIZE));
		    break;
		}
	    }
	}

	/*
	 * System.out.println("Row Num: " + rowNum + " Column Num: " + columnNum
	 * + " Row Index: " + rowIndex + " Column Index: " + columnIndex +
	 * " Limit Row Index: " + (rowIndex + 3) + " Limit Column Index: " +
	 * (columnIndex + 3));
	 */

	for (int i = rowIndex; i < (rowIndex + Constants.SUBBOARDSIZE); i++) {
	    for (int j = columnIndex; j < (columnIndex + Constants.SUBBOARDSIZE); j++) {
		if (board[i][j].getCellValue() != 0) {
		    permuteList.remove(board[i][j].getCellValue());
		}
	    }
	}

    }

    private void checkInColumn(int columnNum, List<Integer> permuteList) {
	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    if (board[i][columnNum].getCellValue() != 0) {
		permuteList.remove(board[i][columnNum].getCellValue());
	    }
	}
    }

    private void checkInRow(int rowNum, List<Integer> permuteList) {
	for (int j = 0; j < Constants.BOARDSIZE; j++) {
	    if (board[rowNum][j].getCellValue() != 0) {
		permuteList.remove(board[rowNum][j].getCellValue());
	    }
	}
    }

    public void fillList(List<Integer> permuteList) {
	permuteList.clear();
	for (int i = 1; i < (Constants.BOARDSIZE + 1); i++) {
	    permuteList.add(i);
	}
    }

    public void fillToBoard(Cell[][] source, Cell[][] destination) {
	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    for (int j = 0; j < Constants.BOARDSIZE; j++) {
		Cell cell = new Cell();
		cell.setCellValue(source[i][j].getCellValue());
		cell.setCellType(source[i][j].getCellType());
		destination[i][j] = cell;
	    }
	}
    }

    private void createBoard(Cell[][] board) {
	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    for (int j = 0; j < Constants.BOARDSIZE; j++) {
		Cell cell = new Cell();
		board[i][j] = cell;
	    }
	}
    }

    public void loadValuePosFromFile() {
	try (BufferedReader br = new BufferedReader(new FileReader(
		System.getProperty("user.dir") + File.separator
			+ "sudokuMap.txt"))) {
	    String sCurrentLine;
	    String inputOutputArray[];
	    int i = 0;

	    while ((sCurrentLine = br.readLine()) != null) {
		inputOutputArray = sCurrentLine.split(" ");
		for (int j = 0; j < inputOutputArray.length; j++) {
		    if (Integer.parseInt(inputOutputArray[j]) == 0) {
			fillValueXY(i, j,
				Integer.parseInt(inputOutputArray[j]),
				CellType.VALUE);
		    } else {
			fillValueXY(i, j,
				Integer.parseInt(inputOutputArray[j]),
				CellType.GIVEN);
		    }
		}
		i++;
		if (i % Constants.BOARDSIZE == 0) {
		    i = 0;
		}

	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void fillValueXY(int xPos, int yPos, int value, CellType type) {
	board[xPos][yPos].setCellValue(value);
	board[xPos][yPos].setCellType(type);
    }

    private void fillBoardWithGivenValues() {
	loadValuePosFromFile();
    }

    public Boolean checkBoundsForBoard(int xPos, int yPos) {
	if ((xPos >= 0 && xPos < Constants.BOARDSIZE)
		&& ((yPos >= 0 && yPos < Constants.BOARDSIZE))) {
	    return true;
	} else {
	    return false;
	}
    }

    public Double getFitness() {
	Double fitness = 0.0;
	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    fitness += (10 * getSumRowFitness(i));

	    fitness += (10 * getSumColFitness(i));

	}

	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    for (int j = 0; j < Constants.BOARDSIZE; j++) {
		fitness += (10 * getRowColFitness(i, j));
	    }
	}

	return fitness;
    }

    private Double getSumColFitness(int colNum) {
	Double temp = 0.0;
	for (int row = 0; row < Constants.BOARDSIZE; row++) {
	    temp += board[row][colNum].getCellValue();
	}
	return Math.abs(45.0 - temp);
    }

    private Double getSumRowFitness(int rowNum) {
	Double temp = 0.0;
	for (int col = 0; col < Constants.BOARDSIZE; col++) {
	    temp += board[rowNum][col].getCellValue();
	}
	return Math.abs(45.0 - temp);
    }

    private Double getRowColFitness(int rowNum, int colNum) {
	Double temp = 0.0;
	for (int col = 0; col < Constants.BOARDSIZE; col++) {
	    if (col == colNum) {
		continue;
	    } else {
		if (board[rowNum][colNum].getCellValue() == board[rowNum][col]
			.getCellValue()) {
		    temp++;
		}
	    }

	}

	for (int row = 0; row < Constants.BOARDSIZE; row++) {
	    if (row == rowNum) {
		continue;
	    } else {
		if (board[rowNum][colNum].getCellValue() == board[row][colNum]
			.getCellValue()) {
		    temp++;
		}
	    }

	}

	return temp;
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < Constants.BOARDSIZE; i++) {
	    for (int j = 0; j < Constants.BOARDSIZE; j++) {

		/*
		 * sb.append(board[i][j].getCellValue() +
		 * board[i][j].getCellType().toString() + " ");
		 */

		sb.append(board[i][j].getCellValue());
	    }
	    // sb.append("\n");
	}

	return sb.toString();
    }

    public Board copyBoard() {
	Board copy = new Board();
	copy.board = new Cell[Constants.BOARDSIZE][Constants.BOARDSIZE];
	createBoard(copy.board);
	fillToBoard(board, copy.board);
	return copy;
    }

    public static void main(String[] args) {
	Board[] boardArr = new Board[5];
	Board board = new Board();
	Board board1 = new Board();
	boardArr[0] = board;
	boardArr[1] = board1;
	System.out.printf("%s", boardArr[0].toString());
	System.out.printf("%s", boardArr[1].toString());
    }

    public void exchangeGridInfo(int gridNum, Board firstBoard,
	    Board secondBoard) {
	int rowIndex = 0, columnIndex = 0, temp = 0;

	if (gridNum == 0) {
	    rowIndex = 0;
	    columnIndex = 0;
	} else if (gridNum == 1) {
	    rowIndex = 0;
	    columnIndex = 3;
	} else if (gridNum == 2) {
	    rowIndex = 0;
	    columnIndex = 6;
	} else if (gridNum == 3) {
	    rowIndex = 3;
	    columnIndex = 0;
	} else if (gridNum == 4) {
	    rowIndex = 3;
	    columnIndex = 3;
	} else if (gridNum == 5) {
	    rowIndex = 3;
	    columnIndex = 6;
	} else if (gridNum == 6) {
	    rowIndex = 6;
	    columnIndex = 0;
	} else if (gridNum == 7) {
	    rowIndex = 6;
	    columnIndex = 3;
	} else if (gridNum == 8) {
	    rowIndex = 6;
	    columnIndex = 6;
	}

	for (int i = rowIndex; i < (rowIndex + Constants.SUBBOARDSIZE); i++) {
	    for (int j = columnIndex; j < (columnIndex + Constants.SUBBOARDSIZE); j++) {
		temp = firstBoard.getBoard()[i][j].getCellValue();
		firstBoard.getBoard()[i][j]
			.setCellValue(secondBoard.getBoard()[i][j]
				.getCellValue());
		secondBoard.getBoard()[i][j].setCellValue(temp);
	    }
	}
    }

    public void changeGridValue(int gridNum, Board boardToMutate) {
	int rowIndex = 0, columnIndex = 0;

	if (gridNum == 0) {
	    rowIndex = 0;
	    columnIndex = 0;
	} else if (gridNum == 1) {
	    rowIndex = 0;
	    columnIndex = 3;
	} else if (gridNum == 2) {
	    rowIndex = 0;
	    columnIndex = 6;
	} else if (gridNum == 3) {
	    rowIndex = 3;
	    columnIndex = 0;
	} else if (gridNum == 4) {
	    rowIndex = 3;
	    columnIndex = 3;
	} else if (gridNum == 5) {
	    rowIndex = 3;
	    columnIndex = 6;
	} else if (gridNum == 6) {
	    rowIndex = 6;
	    columnIndex = 0;
	} else if (gridNum == 7) {
	    rowIndex = 6;
	    columnIndex = 3;
	} else if (gridNum == 8) {
	    rowIndex = 6;
	    columnIndex = 6;
	}
	List<Integer> valueList = new LinkedList<Integer>();
	for (int i = rowIndex; i < (rowIndex + Constants.SUBBOARDSIZE); i++) {
	    for (int j = columnIndex; j < (columnIndex + Constants.SUBBOARDSIZE); j++) {
		if (boardToMutate.getBoard()[i][j].getCellType() == CellType.VALUE) {
		    valueList
			    .add(boardToMutate.getBoard()[i][j].getCellValue());
		}
	    }
	}

	Collections.shuffle(valueList);

	for (int i = rowIndex; i < (rowIndex + Constants.SUBBOARDSIZE); i++) {
	    for (int j = columnIndex; j < (columnIndex + Constants.SUBBOARDSIZE); j++) {
		if (boardToMutate.getBoard()[i][j].getCellType() == CellType.VALUE) {
		    boardToMutate.getBoard()[i][j].setCellValue(valueList
			    .get(0));
		    valueList.remove(0);
		}
	    }
	}
    }
    
    public void changeAll(){
	
    }
    
    public void changeBlock(){
	
    }
    
    public void changeGrid(){
	
    }
}

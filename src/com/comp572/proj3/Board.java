/**
 * 
 */
package com.comp572.proj3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Sanjeev
 * 
 */
public class Board {

    private int BOARDSIZE = 32;
    private int[][] santaFeBoard = new int[BOARDSIZE][BOARDSIZE];
    private int[][] backupBoard = new int[BOARDSIZE][BOARDSIZE];

    public Board() {
	// createBoard
	createBoard();
	// fill with food pellets
	fillBoardWithFoodPellets();
	// copy to other board values
	fillToBoard(santaFeBoard, backupBoard);
    }

    private void fillToBoard(int[][] source, int[][] destination) {
	for (int i = 0; i < BOARDSIZE; i++) {
	    for (int j = 0; j < BOARDSIZE; j++) {
		destination[i][j] = source[i][j];
	    }
	}
    }

    private void createBoard() {
	for (int i = 0; i < BOARDSIZE; i++) {
	    for (int j = 0; j < BOARDSIZE; j++) {
		santaFeBoard[i][j] = Constants.NOFOOD;
	    }
	}
    }

    public void loadFoodPosFromFile() {
	try (BufferedReader br = new BufferedReader(new FileReader(
		System.getProperty("user.dir") + File.separator
			+ "santaFeTrailMap.txt"))) {
	    String sCurrentLine;
	    String inputOutputArray[];

	    while ((sCurrentLine = br.readLine()) != null) {
		inputOutputArray = sCurrentLine.split(" ");
		fillFoodXY(Integer.parseInt(inputOutputArray[0]),
			Integer.parseInt(inputOutputArray[1]));
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void fillFoodXY(int xPos, int yPos) {
	santaFeBoard[xPos][yPos] = Constants.FOOD;
    }

    private void fillBoardWithFoodPellets() {
	loadFoodPosFromFile();
    }

    public void removeFoodXY(int xPos, int yPos) {
	santaFeBoard[xPos][yPos] = Constants.NOFOOD;
    }

    public void removeFoodXYIfExists(int xPos, int yPos) {
	if (santaFeBoard[xPos][yPos] == Constants.FOOD) {
	    removeFoodXY(xPos, yPos);
	}
    }

    public void resetBoard() {
	fillToBoard(backupBoard, santaFeBoard);
    }

    public Boolean checkBoundsForBoard(int xPos, int yPos) {
	if ((xPos >= 0 && xPos < BOARDSIZE)
		&& ((yPos >= 0 && yPos < BOARDSIZE))) {
	    return true;
	} else {
	    return false;
	}
    }

    public Boolean containsFood(int xPos, int yPos) {
	if (santaFeBoard[xPos][yPos] == Constants.FOOD) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * @return the santaFeBoard
     */
    public int[][] getSantaFeBoard() {
	return santaFeBoard;
    }

    /**
     * @param santaFeBoard
     *            the santaFeBoard to set
     */
    public void setSantaFeBoard(int[][] santaFeBoard) {
	this.santaFeBoard = santaFeBoard;
    }

    /**
     * @return the backupBoard
     */
    public int[][] getBackupBoard() {
	return backupBoard;
    }

    /**
     * @param backupBoard
     *            the backupBoard to set
     */
    public void setBackupBoard(int[][] backupBoard) {
	this.backupBoard = backupBoard;
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("Board Current Instance\n");
	for (int i = 0; i < BOARDSIZE; i++) {
	    for (int j = 0; j < BOARDSIZE; j++) {
		sb.append(santaFeBoard[i][j] + " ");
	    }
	    sb.append("\n");
	}
	sb.append("\n");
	return sb.toString();
    }

    public static void main(String[] args) {
	Board board = new Board();
	System.out.printf("%s", board.toString());
	System.out.println("Removing Food ");
	board.removeFoodXYIfExists(0, 1);
	board.removeFoodXYIfExists(1, 3);
	System.out.println("After Removing Food ");
	System.out.printf("%s", board.toString());
	System.out.println("Reset Board Values");
	board.resetBoard();
	System.out.printf("%s", board.toString());
    }
}

/**
 * 
 */
package com.comp572.proj3.gui;

/**
 * @author Sanjeev
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.comp572.proj3.Board;
import com.comp572.proj3.Constants;
import com.comp572.proj3.Orientation;

public class Grid2JPanel extends JPanel implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 7291978086148054809L;

    private BufferedImage antEast;
    private BufferedImage antWest;
    private BufferedImage antNorth;
    private BufferedImage antSouth;
    private BufferedImage food;
    private Board board;
    private static final int DELAY = 100;
    private Timer timer;
    private int antXPos, antYPos;
    private Orientation antOrientation;
    private List<AntVector> antVectors;
    private int index;
    private int remainingFood;
    private Font trb = new Font("TimesRoman", Font.BOLD, 20);

    public Grid2JPanel() {
	loadImageFiles();
	initBoard();
    }

    private void loadImageFiles() {
	antEast = LoadImage.getImage("resources" + File.separator
		+ "antEast.png");
	antWest = LoadImage.getImage("resources" + File.separator
		+ "antWest.png");
	antNorth = LoadImage.getImage("resources" + File.separator
		+ "antNorth.png");
	antSouth = LoadImage.getImage("resources" + File.separator
		+ "antSouth.png");
	food = LoadImage.getImage("resources" + File.separator + "food.png");
    }

    private void initBoard() {
	board = new Board();
	setBackground(Color.WHITE);
	setPreferredSize(new Dimension(640, 640));

	setDoubleBuffered(true);

	antXPos = 20;
	antYPos = 20;

	antOrientation = Orientation.EAST;

	index = 0;

	remainingFood = Constants.NUMFOODPELLETS;

	antVectors = AntVectorReader.readAntData();

	timer = new Timer(DELAY, this);
	timer.start();
    }

    // draw grid
    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	Graphics2D g2d = (Graphics2D) g;
	
	g2d.setFont(trb);

	drawAll(g2d);

	Toolkit.getDefaultToolkit().sync();

	g.dispose();
    } // end method paintComponent

    private void drawAll(Graphics2D g2d) {
	drawBoard(g2d);
	drawFood(g2d);
	drawAnt(g2d);
	drawInfo(g2d);
    }

    private void drawInfo(Graphics2D g2d) {
	g2d.drawString("Time Units", 700, 280);
	g2d.drawString(String.valueOf(index), 720, 300);

	g2d.drawString("Command Processed", 690, 360);
	g2d.drawString(antVectors.get(index).getDirection(), 720, 380);
	
	g2d.drawString("Remaining Food", 700, 440);
	g2d.drawString(String.valueOf(remainingFood), 720, 460);

    }

    private void drawBoard(Graphics2D g2d) {
	for (int x = 20; x <= 640; x += 20) {
	    for (int y = 20; y <= 640; y += 20) {
		g2d.draw(new Rectangle2D.Double(x, y, 20, 20));
	    }
	}
    }

    private void drawFood(Graphics2D g2d) {
	for (int x = 20, i = 0; x <= 640; x += 20, i++) {
	    for (int y = 20, j = 0; y <= 640; y += 20, j++) {
		if (board.containsFood(j, i)) {
		    g2d.drawImage(food, x, y, null);
		}
	    }
	}
    }

    private void drawAnt(Graphics2D g2d) {
	if (antOrientation == Orientation.EAST) {
	    g2d.drawImage(antEast, antXPos, antYPos, null);
	} else if (antOrientation == Orientation.WEST) {
	    g2d.drawImage(antWest, antXPos, antYPos, null);
	} else if (antOrientation == Orientation.NORTH) {
	    g2d.drawImage(antNorth, antXPos, antYPos, null);
	} else if (antOrientation == Orientation.SOUTH) {
	    g2d.drawImage(antSouth, antXPos, antYPos, null);
	}

    }

    @Override
    public void actionPerformed(ActionEvent e) {

	/*
	 * x += 20; y += 20;
	 * 
	 * if (y > B_HEIGHT) {
	 * 
	 * y = INITIAL_Y; x = INITIAL_X; }
	 */
	// for (AntVector antVecor : antVectors) {
	// processAntVector(antVecor);
	processAntVector(antVectors.get(index++));
	if (index % antVectors.size() == 0) {
	    index = 0;
	    board.resetBoard();
	    remainingFood= Constants.NUMFOODPELLETS;
	}
	repaint();
	// }
    }

    private void processAntVector(AntVector antVecor) {
	if (antVecor.getDirection().equals("LEFT")) {
	    processTurnLeftCommand(antVecor);
	} else if (antVecor.getDirection().equals("RIGHT")) {
	    processTurnRightCommand(antVecor);
	} else if (antVecor.getDirection().equals("FORWARD")) {
	    processForwardCommand(antVecor);
	}
    }

    private void processForwardCommand(AntVector antVecor) {
	antXPos = (antVecor.getAntYPos() + 1) * 20;
	antYPos = (antVecor.getAntXPos() + 1) * 20;
	if (board.containsFood(antVecor.getAntXPos(), antVecor.getAntYPos())) {
	    remainingFood -= 1;
	}
	board.removeFoodXY(antVecor.getAntXPos(), antVecor.getAntYPos());
    }

    private void processTurnLeftCommand(AntVector antVecor) {
	switch (antOrientation) {
	case EAST:
	    antOrientation = Orientation.NORTH;
	    break;
	case WEST:
	    antOrientation = Orientation.SOUTH;
	    break;
	case NORTH:
	    antOrientation = Orientation.WEST;
	    break;
	case SOUTH:
	    antOrientation = Orientation.EAST;
	    break;
	default:
	    System.out
		    .println("Default case reached something is wrong for TurnLeft");
	    break;
	}
    }

    private void processTurnRightCommand(AntVector antVecor) {
	// check for orientation
	switch (antOrientation) {
	case EAST:
	    antOrientation = Orientation.SOUTH;
	    break;
	case WEST:
	    antOrientation = Orientation.NORTH;
	    break;
	case NORTH:
	    antOrientation = Orientation.EAST;
	    break;
	case SOUTH:
	    antOrientation = Orientation.WEST;
	    break;
	default:
	    System.out.println("Default case reached something is wrong");
	    break;
	}
    }

    /**
     * @return the antEast
     */
    public BufferedImage getAntEast() {
	return antEast;
    }

    /**
     * @param antEast
     *            the antEast to set
     */
    public void setAntEast(BufferedImage antEast) {
	this.antEast = antEast;
    }

    /**
     * @return the antWest
     */
    public BufferedImage getAntWest() {
	return antWest;
    }

    /**
     * @param antWest
     *            the antWest to set
     */
    public void setAntWest(BufferedImage antWest) {
	this.antWest = antWest;
    }

    /**
     * @return the antNorth
     */
    public BufferedImage getAntNorth() {
	return antNorth;
    }

    /**
     * @param antNorth
     *            the antNorth to set
     */
    public void setAntNorth(BufferedImage antNorth) {
	this.antNorth = antNorth;
    }

    /**
     * @return the antSouth
     */
    public BufferedImage getAntSouth() {
	return antSouth;
    }

    /**
     * @param antSouth
     *            the antSouth to set
     */
    public void setAntSouth(BufferedImage antSouth) {
	this.antSouth = antSouth;
    }

    /**
     * @return the food
     */
    public BufferedImage getFood() {
	return food;
    }

    /**
     * @param food
     *            the food to set
     */
    public void setFood(BufferedImage food) {
	this.food = food;
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
} //

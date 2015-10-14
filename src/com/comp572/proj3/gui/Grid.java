/**
 * 
 */
package com.comp572.proj3.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * @author Sanjeev
 * 
 */
public class Grid extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -8789000501467810502L;
    public Grid2JPanel gridJPanel = new Grid2JPanel();

    public Grid() {
	add(gridJPanel);
	setTitle("SantaFe Trail Animation");
	pack();
	setResizable(false);
	setSize(900, 720); // set frame size
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String args[]) {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame ex = new Grid();
		ex.setVisible(true);
	    }
	});
    } // end main
} // end class Grid
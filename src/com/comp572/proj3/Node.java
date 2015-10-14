package com.comp572.proj3;

import java.io.Serializable;
import java.util.Stack;

import com.comp572.utilities.MersenneTwister;

public class Node implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5382621713349328697L;
    private Node parent; // pointer to parent node
    private Node[] branches = new Node[4]; // pointers to branches
    private int type; // stores the terminal or non-terminal
    // expression generated
    private String expression;
    //Random number Generator
    private static MersenneTwister rdm= new MersenneTwister();

    /**
     * @return the parent
     */
    public Node getParent() {
	return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Node parent) {
	this.parent = parent;
    }

    /**
     * @return the branches
     */
    public Node[] getBranches() {
	return branches;
    }

    /**
     * @param branches
     *            the branches to set
     */
    public void setBranches(Node[] branches) {
	this.branches = branches;
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

    /**
     * @return the expression
     */
    public String getExpression() {
	return expression;
    }

    /**
     * @param expression
     *            the expression to set
     */
    public void setExpression(String expression) {
	this.expression = expression;
    }

    public void erase() {
	if (type < Constants.NUM_NON_TERMS) {
	    for (int i = 0; i < Constants.MAX_ARITY; i++)
		if (branches[i] != null)
		    branches[i] = null;
	}
    }

    public void full(int depth, int max, Node p) {
	/* creates full trees for the initial population */
	parent = p; // point to parent node

	if (depth >= max) // if at max depth put in terminal
	    type = Constants.NUM_NON_TERMS + rdm.nextInt(Constants.NUM_TERMS);
	else { // else add non-terminal
	    type = rdm.nextInt(Constants.NUM_NON_TERMS);
	    switch (type) {
	    case Command.PROG2:
	    case Command.IFFOODAHEAD:
	    case Command.IFNOFOODAHEAD:
		for (int i = 0; i < 2; i++) {
		    branches[i] = new Node();
		    branches[i].full(depth + 1, max, this);
		}
		for (int i = 2; i < Constants.MAX_ARITY; i++) {
		    // initially make all branches NULL
		    branches[i] = null;
		}
		break;
	    case Command.PROG3:
		for (int i = 0; i < 3; i++) {
		    branches[i] = new Node();
		    branches[i].full(depth + 1, max, this);
		}
		// initially make all branches NULL
		branches[3] = null;
		break;
	    default:
		System.out.println("Unknown case in full");

	    }
	}
    }

    /*
     * Evaluates a tree. Just a recursive function that evaluates branches and
     * adds, subtracts, etc. their values. For a more complex problem it may
     * need to manipulate some global values. E.g. for a 'forward' terminal the
     * position (stored as a global) of a robot is changed.
     */

    /* Recursively counts terminal and non-terminals. */
    public int calcSize(Node root) {
	if (root == null) {
	    return 0;
	} else if (root.branches[0] == null && root.branches[1] == null
		&& root.branches[2] == null && root.branches[3] == null) {
	    return 1;
	} else {
	    return (calcSize(root.branches[0]) + 1 + calcSize(root.branches[1])
		    + calcSize(root.branches[2]) + calcSize(root.branches[3]));
	}
    }

    // Needs a bit of checking and fixing
    public void postorder(Node node) {
	if (node == null) {
	    return;
	} else if (node.type == Command.PROG2
		|| node.type == Command.IFFOODAHEAD
		|| node.type == Command.IFNOFOODAHEAD) {
	    postorder(node.branches[0]);
	    postorder(node.branches[1]);
	    setExpression(getExpression() + getType(node));
	} else if (node.type == Command.PROG3) {
	    postorder(node.branches[0]);
	    postorder(node.branches[1]);
	    postorder(node.branches[2]);
	    setExpression(getExpression() + getType(node));
	    // setExpression(getExpression());
	} else if (node.type == Command.FORWARD || node.type == Command.LEFT
		|| node.type == Command.RIGHT) {
	    setExpression(getExpression() + getType(node));
	}
    }

    public String iterativeInorder(Node node) {
	String expression = new String("");
	Stack<Node> parentStack = new Stack<Node>();
	while (!parentStack.isEmpty() || node != null) {
	    if (node != null) {
		parentStack.push(node);
		node = node.branches[0];
	    } else {
		node = parentStack.pop();
		expression += getType(node);
		node = node.branches[1];
	    }
	}
	return expression;
    }

    public String postOrder(Node node) {
	if (node == null)
	    return "";
	postOrder(node.branches[0]);
	postOrder(node.branches[1]);
	return getType(node);
    }

    public String getType(Node node) {
	switch (node.type) {
	case Command.FORWARD:
	    return " FORWARD ";
	case Command.IFFOODAHEAD:
	    return " IFFOODAHEAD ";
	case Command.IFNOFOODAHEAD:
	    return " IFNOFOODAHEAD ";
	case Command.LEFT:
	    return " LEFT ";
	case Command.RIGHT:
	    return " RIGHT ";
	case Command.PROG2:
	    return " PROG2 ";
	case Command.PROG3:
	    return " PROG3 ";
	default:
	    return "";
	}
    }

    public Node getRandomNonTerminalNode(Node root) {

	if ((rdm.nextFloat()) >= 0.4 && !checkIfChild(root)
		&& !checkIfParent(root)) {
	    // System.out.println(" Type of Non-Terminal is "+getType(root));
	    return root;
	}

	if (root.branches[0] != null && rdm.nextFloat() >= 0.6) {
	    return getRandomNonTerminalNode(root.branches[0]);
	} else if (root.branches[1] != null && rdm.nextFloat() >= 0.5) {
	    return getRandomNonTerminalNode(root.branches[1]);
	} else if (root.branches[2] != null && rdm.nextFloat() >= 0.4) {
	    return getRandomNonTerminalNode(root.branches[2]);
	} else if (root.branches[3] != null && rdm.nextFloat() >= 0.3) {
	    return getRandomNonTerminalNode(root.branches[3]);
	}
	return null;

    }

    public Node getRandomTerminalNode(Node root) {

	if ((rdm.nextFloat()) >= 0.4 && checkIfChild(root)
		&& !checkIfParent(root)) {
	    // System.out.println(" Type of Terminal is "+getType(root));
	    return root;
	}

	if (root.branches[0] != null && rdm.nextFloat() >= 0.6) {
	    return getRandomTerminalNode(root.branches[0]);
	} else if (root.branches[1] != null && rdm.nextFloat() >= 0.5) {
	    return getRandomTerminalNode(root.branches[1]);
	} else if (root.branches[2] != null && rdm.nextFloat() >= 0.4) {
	    return getRandomTerminalNode(root.branches[2]);
	} else if (root.branches[3] != null && rdm.nextFloat() >= 0.3) {
	    return getRandomTerminalNode(root.branches[3]);
	}
	return null;
    }

    public Boolean checkIfChild(Node node) {
	if (node.branches[0] == null && node.branches[1] == null
		&& node.branches[2] == null && node.branches[3] == null) {
	    return true;
	} else {
	    return false;
	}
    }

    public Boolean checkIfParent(Node node) {
	if (node.parent == null) {
	    return true;
	} else {
	    return false;
	}
    }
}

package com.comp572.proj2;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import com.comp572.utilities.DoubleFormatter;

public class Node implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5382621713349328697L;
    private Node parent; // pointer to parent node
    private Node[] branches = new Node[4]; // pointers to branches
    private int type; // stores the terminal or non-terminal
    private Double constValue; // stores the constant, if any
    // expression generated
    private String expression;

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
     * @return the constValue
     */
    public double getConstValue() {
	return constValue;
    }

    /**
     * @param constValue
     *            the constValue to set
     */
    public void setConstValue(Double constValue) {
	this.constValue = constValue;
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

    public void copy(Node source) {
	type = source.type;
	constValue = source.constValue;
	if (type < Constants.NUM_NON_TERMS) {
	    for (int i = 0; i < Constants.MAX_ARITY; i++) {
		if (source.branches[i] != null) {
		    branches[i] = new Node();
		    branches[i].copy(source.branches[i]);
		} else {
		    branches[i] = null;
		}
	    }
	}
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
	Random rdm = new Random();

	if (depth >= max) // if at max depth put in terminal
	    type = Constants.NUM_NON_TERMS + rdm.nextInt(Constants.NUM_TERMS);
	else { // else add non-terminal
	    type = rdm.nextInt(Constants.NUM_NON_TERMS);
	    switch (type) {
	    case Command.ADD:
	    case Command.SUBTRACT:
	    case Command.MULTIPLY:
	    case Command.DIVIDE:
		for (int i = 0; i < 2; i++) {
		    branches[i] = new Node();
		    branches[i].full(depth + 1, max, this);
		}
		for (int i = 2; i < Constants.MAX_ARITY; i++) {
		    // initially make all branches NULL
		    branches[i] = null;
		}
		break;
	    case Command.IFGT:
	    case Command.IFLT:
		for (int i = 0; i < 4; i++) {
		    branches[i] = new Node();
		    branches[i].full(depth + 1, max, this);
		}
		break;
	    case Command.SINX:
	    case Command.COSX:
		for (int i = 0; i < 1; i++) {
		    branches[i] = new Node();
		    branches[i].full(depth + 1, max, this);
		}
		for (int i = 1; i < Constants.MAX_ARITY; i++) {
		    // initially make all branches NULL
		    branches[i] = null;
		}
		break;
	    default:
		System.out.println("Unknown case in full");

	    }
	}
	if (type == Command.CONSTANT) // if a constant include a value
	    constValue = DoubleFormatter.getFormattedSolution(Double
		    .valueOf(rdm.nextDouble() * 2 * Constants.CONST_LIMIT)
		    - (Constants.CONST_LIMIT / 2));
    }

    /*
     * Evaluates a tree. Just a recursive function that evaluates branches and
     * adds, subtracts, etc. their values. For a more complex problem it may
     * need to manipulate some global values. E.g. for a 'forward' terminal the
     * position (stored as a global) of a robot is changed.
     */
    public Double evaluate(Double X) {
	/* evaluates the tree , for a given X. */
	Double l, r;
	switch (type) {
	case Command.ADD: // +
	    l = branches[0].evaluate(X);
	    r = branches[1].evaluate(X);
	    return (l + r);
	case Command.SUBTRACT: // -
	    l = branches[0].evaluate(X);
	    r = branches[1].evaluate(X);
	    return (l - r);
	case Command.MULTIPLY: // -
	    l = branches[0].evaluate(X);
	    r = branches[1].evaluate(X);
	    return DoubleFormatter.getFormattedSolution((l * r));
	case Command.DIVIDE: // -
	    l = branches[0].evaluate(X);
	    r = branches[1].evaluate(X);
	    if (Double.isInfinite(l / r) || (l==0.0 && r==0.0)) {
		return 0.0;
	    } else {
		return DoubleFormatter.getFormattedSolution((l / r));
	    }
	case Command.IFGT: // -
	    l = branches[0].evaluate(X);
	    r = branches[1].evaluate(X);
	    if (l > r) {
		return branches[2].evaluate(X);
	    } else {
		return branches[3].evaluate(X);
	    }
	case Command.IFLT: // -
	    l = branches[0].evaluate(X);
	    r = branches[1].evaluate(X);
	    if (l < r) {
		return branches[2].evaluate(X);
	    } else {
		return branches[3].evaluate(X);
	    }
	case Command.SINX: // -
	    l = branches[0].evaluate(X);
	    return DoubleFormatter.getFormattedSolution(Math.sin(l));
	case Command.COSX: // -
	    l = branches[0].evaluate(X);
	    return DoubleFormatter.getFormattedSolution(Math.cos(l));
	case Command.INPUTX: // X
	    return (X);
	case Command.CONSTANT:
	    return (constValue);
	default:
	    System.out.println("Error, unknown instruction ");
	    return 0.0;
	}
    }

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

    public void inorder(Node node) {
	if (node == null) {
	    return;
	} else if (node.type == Command.ADD || node.type == Command.SUBTRACT
		|| node.type == Command.DIVIDE || node.type == Command.MULTIPLY) {
	    inorder(node.branches[0]);
	    setExpression(getExpression() + getType(node));
	    inorder(node.branches[1]);
	} else if (node.type == Command.IFGT || node.type == Command.IFLT) {
	    setExpression(getExpression() + getType(node) + " (");
	    inorder(node.branches[0]);
	    if (node.type == Command.IFGT) {
		setExpression(getExpression() + " > ");
	    } else {
		setExpression(getExpression() + " < ");
	    }
	    inorder(node.branches[1]);
	    setExpression(getExpression() + " ) { ");

	    inorder(node.branches[2]);
	    setExpression(getExpression() + " } else { ");
	    inorder(node.branches[3]);
	    setExpression(getExpression() + " } ");
	} else if (node.type == Command.SINX || node.type == Command.COSX) {
	    setExpression(getExpression() + getType(node) + " ( ");
	    inorder(node.branches[0]);
	    setExpression(getExpression() + " ) ");
	} else if (node.type == Command.INPUTX || node.type == Command.CONSTANT) {
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
	case Command.ADD:
	    return " + ";
	case Command.SUBTRACT:
	    return " - ";
	case Command.MULTIPLY:
	    return " * ";
	case Command.DIVIDE:
	    return " / ";
	case Command.IFGT:
	    return " IFGT ";
	case Command.IFLT:
	    return " IFLT ";
	case Command.SINX:
	    return " SIN ";
	case Command.COSX:
	    return " COS ";
	case Command.CONSTANT:
	    return String.valueOf(node.constValue);
	case Command.INPUTX:
	    return " X ";
	default:
	    return "";
	}
    }

    public Node getRandomNode(Individual indv) {
	int size = indv.getSize();
	int item = new Random().nextInt(size / 2); // In real life, the Random
						   // object should be rather
						   // more
						   // shared than this
	int i = 0;
	Queue<Node> nodequeue = new LinkedList<Node>();
	if (indv.getTheIndv() != null)
	    nodequeue.add(indv.getTheIndv());
	while (!nodequeue.isEmpty()) {
	    Node next = nodequeue.remove();
	    if (next.branches[0] != null) {
		nodequeue.add(next.branches[0]);
	    }
	    if (next.branches[1] != null) {
		nodequeue.add(next.branches[1]);
	    }
	    if (next.branches[2] != null) {
		nodequeue.add(next.branches[2]);
	    }
	    if (next.branches[3] != null) {
		nodequeue.add(next.branches[3]);
	    }

	    if (i++ == item) {
		return next;
	    }
	}
	if (nodequeue.isEmpty()) {
	    System.out.println("Queue is Empty!!!");
	}

	return null;
    }

    public Node getRandomNode1(Node root, int stepSize, int randNum) {
	if (stepSize == randNum) {
	    return root;
	}
	
	if (root.branches[0] != null) {
	    return getRandomNode1(root.branches[0], stepSize + 1, randNum);
	} else if (root.branches[1] != null) {
	    return getRandomNode1(root.branches[1], stepSize + 1, randNum);
	} else if (root.branches[2] != null) {
	    return getRandomNode1(root.branches[2], stepSize + 1, randNum);
	} else if (root.branches[3] != null) {
	    return getRandomNode1(root.branches[3], stepSize + 1, randNum);
	}
	
	
	return root; 

    }
}

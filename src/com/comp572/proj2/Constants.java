/**
 * 
 */
package com.comp572.proj2;


/**
 * @author Sanjeev
 * 
 */
public final class Constants {

    public static int NUM_POINTS = 5; // test points

    // inputs are x values, outputs are f(x) values
    public static Double[] inputs = { 1.0, 2.0, 3.0, 4.0, 5.0 };

    public static Double[] outputs = { 1.0, 4.0, 9.0, 16.0, 25.0 };

    public static final float CONST_LIMIT = 10;

    // non-terminals are add / subtract / divide / multiply
    public static int NUM_NON_TERMS = 8;

    // terminals are X and constants
    public static int NUM_TERMS = 2;

    // Total ops are the sum of the previous 2
    public static int TOTAL_OPS = 4;

    // max arity of any of the functions, can be changed
    // unused branches point to NULL
    // 4 in case you want to add conditionals
    public static int MAX_ARITY = 4;

    // Maximum number of Generations
    public static final int MAX_ITERATIONS = 10000;
}

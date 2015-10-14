/**
 * 
 */
package com.comp572.proj3;

/**
 * @author Sanjeev
 * 
 */
public final class Constants {

    // non-terminals are add / subtract / divide / multiply
    public static int NUM_NON_TERMS = 4;

    // terminals are X and constants
    public static int NUM_TERMS = 3;

    // max arity of any of the functions, can be changed
    // unused branches point to NULL
    // 4 in case you want to add conditionals
    public static int MAX_ARITY = 3;

    // Maximum number of Generations
    public static final int MAX_ITERATIONS = 10000;

    // Having No Food Value
    public static final int NOFOOD = 0;

    // Having Food Value
    public static final int FOOD = 1;
    
    //Number of Food Pellets
    public static final int NUMFOODPELLETS = 89;
    
    //Number of Time Units
    public static final int NUMTIMEUNITS = 600;

}

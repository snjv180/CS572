package com.comp572.utilities;

import java.text.DecimalFormat;

import com.comp572.functions.Ackley;
import com.comp572.functions.Griewangk;
import com.comp572.functions.Rastrigin;
import com.comp572.functions.Rosenbrock;
import com.comp572.functions.Schwefel;
import com.comp572.functions.Spherical;

public class DoubleFormatter {
    private static DecimalFormat dfForSphere = new DecimalFormat("###.##");
    private static DecimalFormat dfForAckley = new DecimalFormat("###.##");
    private static DecimalFormat dfForRosenbrock = new DecimalFormat("###.###");
    private static DecimalFormat dfForRastrigin = new DecimalFormat("###.##");
    private static DecimalFormat dfForGriewangk = new DecimalFormat("###.##");
    private static DecimalFormat dfForSchwefel = new DecimalFormat("###.####");
    private static DecimalFormat dfForSolution = new DecimalFormat("###.##");

    public static double getFormattedDouble(double value, String functionName) {
	if (functionName.equals(Spherical.FUNCTION_NAME)) {
	    return Double.parseDouble(dfForSphere.format(value));
	} else if (functionName.equals(Schwefel.FUNCTION_NAME)) {
	    return Double.parseDouble(dfForSchwefel.format(value));
	} else if (functionName.equals(Ackley.FUNCTION_NAME)) {
	    return Double.parseDouble(dfForAckley.format(value));
	} else if (functionName.equals(Griewangk.FUNCTION_NAME)) {
	    return Double.parseDouble(dfForGriewangk.format(value));
	} else if (functionName.equals(Rastrigin.FUNCTION_NAME)) {
	    return Double.parseDouble(dfForRastrigin.format(value));
	} else if (functionName.equals(Rosenbrock.FUNCTION_NAME)) {
	    return Double.parseDouble(dfForRosenbrock.format(value));
	}
	return 0.0;
    }

    public static Double getFormattedSolution(Double value) {
	return Double.parseDouble(dfForSolution.format(value));
    }

    public static Float getFormattedSolution(Float value) {
	return Float.parseFloat(dfForSolution.format(value));
    }
}

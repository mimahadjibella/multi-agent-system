package sma;
/**
 * Abstract representation of a mathematical function.
 * This class provides a framework for defining and evaluating mathematical functions.
 * Subclasses must implement the abstract method f(x) for the specific function.
 * 
 * @author Ryma HADJI
 */

public abstract class Function {
    /** The minimum value of the interval. */
    public final double min;
    /** The maximum value of the interval. */
    public final double max;
    /** The precision of the derivative, i.e., the distance between two points. */
    public final double delta;

    /**
     * Constructor for the Function class.
     * 
     * @param min   The minimum value of the interval.
     * @param max   The maximum value of the interval.
     * @param delta The precision of the derivative, i.e., the distance between two points.
     */
    public Function(double min, double max, double delta){
        this.min = min;
        this.max = max;
        this.delta = delta;
    }
    /**
     * Abstract method for evaluating the function at a given point.
     * Subclasses must implement this method for the specific function.
     * 
     * @param x The input value for the function.
     * @return The result of the function at the given point.
     */
    public abstract double f(double x);

    /**
     * Evaluates the integral of the function over the entire defined interval.
     * 
     * @return The integral of the function between min and max.
     */
    public double eval(){
        return eval(min, max);
    }
    /**
     * Evaluates the integral of the function over the specified range.
     * Uses the method of rectangles (midpoint method) for numerical integration.
     * 
     * @param lowerLimit The lower limit of the integration range.
     * @param upperLimit The upper limit of the integration range.
     * @return The integral of the function in the provided range.
     * @throws IllegalArgumentException if the range provided is outside the original function range.
     */ 
    public double eval(double lowerLimit, double upperLimit) {
        if (min > lowerLimit || max < upperLimit) {
            throw new IllegalArgumentException("Error : the range provided is outside the original function range.");
        }
        double integral = 0.0;
        for (double i = lowerLimit; i <= upperLimit; i+= delta) {
            // Accumulate the area of the rectangle (midpoint method)
            integral += delta * f(i + delta/2); 
        }
        return integral;
    }
     
  
}

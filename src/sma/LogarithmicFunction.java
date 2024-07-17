package sma;
/**
 * Represents a logarithmic function extending the abstract class Function.
 * This class provides an implementation of the logarithmic function.
 * The logarithmic function is defined as f(x) = ln(x).
 * 
 * @author Ryma HADJI
 */

public class LogarithmicFunction extends Function {
    /**
     * Constructor for the LogarithmicFunction class.
     * 
     * @param min   The minimum value of the interval.
     * @param max   The maximum value of the interval.
     * @param delta The precision of the derivative, i.e., the distance between two points.
     */
    public LogarithmicFunction(double min, double max, double delta) {
        super(min, max, delta);
    }

    /**
     * Evaluates the logarithmic function at the given point.
     * 
     * @param x The input value for the logarithmic function.
     * @return The result of the logarithmic function at the given point.
     */

    @Override
    public double f(double x) {
        return Math.log(x);
    }
}

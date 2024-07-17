package sma;
/**
 * Represents a sine function extending the abstract class Function.
 * This class provides an implementation of the sine function.
 * The sine function is defined as f(x) = sin(x).
 * 
 * @author Ryma HADJI
 */
public class SinFunction extends Function {
    /**
     * Constructor for the SinFunction class.
     * 
     * @param min   The minimum value of the interval.
     * @param max   The maximum value of the interval.
     * @param delta The precision of the derivative, i.e., the distance between two points.
     */
    public SinFunction(double min, double max, double delta) {
        super(min, max, delta);
    }
    /**
     * Evaluates the sine function at the given point.
     * 
     * @param x The input value for the sine function.
     * @return The result of the sine function at the given point.
     */
    @Override
    public double f(double x) {
        return Math.sin(x);
    }
}

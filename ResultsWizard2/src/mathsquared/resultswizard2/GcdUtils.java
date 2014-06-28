/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Contains methods for computing the greatest common divisor, least common multiple, and related quantities.
 * 
 * @author MathSquared
 * 
 */
public class GcdUtils {
    /**
     * Returns the greatest common divisor of two integers.
     * 
     * @param a the first integer
     * @param b the second integer
     * @return the GCD of a and b
     */
    public int gcd (int a, int b) {
        int t = 0;
        while (b != 0) {
            t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /**
     * Returns the least common multiple of two integers.
     * 
     * @param a the first integer
     * @param b the second integer
     * @return the LCM of a and b
     */
    public int lcm (int a, int b) {
        return a / gcd(a, b) * b; // reordered to avoid overflow; correctly, a*b / gcd(a, b)
    }
}

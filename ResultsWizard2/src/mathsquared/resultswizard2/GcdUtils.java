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
    public static int gcd (int a, int b) {
        int t = 0;
        while (b != 0) {
            t = b;
            b = mod(a, b);
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
    public static int lcm (int a, int b) {
        return a / gcd(a, b) * b; // reordered to avoid overflow; correctly, a*b / gcd(a, b)
    }

    /**
     * Computes the mathematical modulus of two numbers.
     * 
     * <p>
     * The Java <code>%</code> operator returns a result that adopts the sign of the dividend; however, a true mathematical modulus adopts the sign of the divisor. This method implements a mathematical modulus.
     * </p>
     * 
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    public static int mod (int a, int b) {
        return ((a % b) + b) % b;
    }
}

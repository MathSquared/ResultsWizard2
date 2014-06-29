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
public class GcdUtils { // TODO write unit tests
    /**
     * Returns the greatest common divisor of two integers. This method accepts negative arguments.
     * 
     * @param a the first integer
     * @param b the second integer
     * @return the GCD of a and b; always positive
     */
    public static int gcd (int a, int b) {
        // Fix signs
        a = (a < 0 ? -a : a);
        b = (b < 0 ? -b : b);
        int t = 0;
        while (b != 0) {
            t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /**
     * Returns the least common multiple of two integers. This method accepts negative arguments.
     * 
     * @param a the first integer
     * @param b the second integer
     * @return the LCM of a and b; always positive
     */
    public static int lcm (int a, int b) {
        int ret = a / gcd(a, b) * b; // reordered to avoid overflow; correctly, a*b / gcd(a, b)
        return (ret < 0 ? -ret : ret);
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
     * @return a mod b; that is, <code>(((a % b) + b) % b)</code>
     */
    public static int mod (int a, int b) {
        return ((a % b) + b) % b;
    }
}

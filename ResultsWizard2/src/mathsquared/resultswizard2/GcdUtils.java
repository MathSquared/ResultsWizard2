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

    /**
     * Computes an exponent.
     * 
     * <p>
     * The Math.pow method always returns a double--which is good for negative and rational exponents, but bad when we want an exact integral result that could be rendered void by floating-point round-off error. To correct for this, this method operates only on integers.
     * </p>
     * 
     * @param base the base of the power
     * @param exp the non-negative exponent to which to raise <code>base</code>
     * @return <code>base</code> raised to the power of <code>exp</code>
     * @throws ArithmeticException if <code>(exp &lt; 0)</code>
     */
    public static int pow (int base, int exp) {
        // Sanity and stupid tests
        if (exp < 0) { // then crazy things happen; we want integral results
            throw new ArithmeticException("exponent must be non-negative");
        }
        if (exp == 0) {
            return 1;
        }
        if (exp == 1) {
            return base;
        }
        if (exp == 2) {
            return base * base;
        }

        int ret = 1;
        while (exp != 0) {
            if ((exp & 1) != 0) {
                ret *= base;
            }
            exp >>= 1; // right-shift doesn't actually divide by 2 for negative numbers, but here it's OK due to sanity checks
            base *= base;
        }

        return ret;
    }
}

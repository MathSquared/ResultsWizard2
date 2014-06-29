/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author MathSquared
 * 
 */
public class GcdUtilsTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.GcdUtils#gcd(int, int)}.
     */
    @Test
    public void testGcd () {
        assertEquals("Relatively prime", 1, GcdUtils.gcd(17, 3));
        assertEquals("Two evens", 2, GcdUtils.gcd(18, 26));
        assertEquals("Negative", 1, GcdUtils.gcd(1, -1));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.GcdUtils#lcm(int, int)}.
     */
    @Test
    public void testLcm () {
        assertEquals("Relatively prime", 17 * 3, GcdUtils.lcm(17, 3));
        assertEquals("Multiples of 3", 45 / 3 * 57, GcdUtils.lcm(45, 57));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.GcdUtils#mod(int, int)}.
     */
    @Test
    public void testMod () {
        assertEquals("++", 3, GcdUtils.mod(13, 5));
        assertEquals("+-", -3, GcdUtils.mod(12, -5));

        assertEquals("-+", 3, GcdUtils.mod(-22, 5));
        assertEquals("--", -3, GcdUtils.mod(-23, -5));
    }

}

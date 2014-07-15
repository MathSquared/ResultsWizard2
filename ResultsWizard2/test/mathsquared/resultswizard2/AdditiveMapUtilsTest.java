/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author MathSquared
 * 
 */
public class AdditiveMapUtilsTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.AdditiveMapUtils#addNumber(java.util.Map, java.lang.Object, int)}.
     */
    @Test
    public void testAddNumberMapOfQsuperKIntegerKInt () {
        Map<String, Integer> one = new HashMap<String, Integer>();
        one.put("a", 1);
        one.put("b", 2);

        Map<String, Integer> first = new HashMap<String, Integer>();
        first.put("a", 1);
        first.put("b", 4);

        Map<String, Integer> second = new HashMap<String, Integer>();
        second.put("a", 1);
        second.put("b", 4);
        second.put("c", 3);

        AdditiveMapUtils.addNumber(one, "b", 2);
        assertEquals("Simple addition", one, first);

        AdditiveMapUtils.addNumber(one, "c", 3);
        assertEquals("Pre-existing null", one, second);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.AdditiveMapUtils#addNumber(java.util.Map, java.lang.Object, mathsquared.resultswizard2.Fraction)}.
     */
    @Test
    public void testAddNumberMapOfQsuperKFractionKFraction () {
        Map<String, Fraction> one = new HashMap<String, Fraction>();
        one.put("a", new Fraction(1, 2));
        one.put("b", new Fraction(5, 3));

        Map<String, Fraction> first = new HashMap<String, Fraction>();
        first.put("a", new Fraction(1, 2));
        first.put("b", new Fraction(10, 3));

        Map<String, Fraction> second = new HashMap<String, Fraction>();
        second.put("a", new Fraction(1, 2));
        second.put("b", new Fraction(10, 3));
        second.put("c", new Fraction(4, 7));

        AdditiveMapUtils.addNumber(one, "b", new Fraction(5, 3));
        assertEquals("Simple addition", one, first);

        AdditiveMapUtils.addNumber(one, "c", new Fraction(4, 7));
        assertEquals("Pre-existing null", one, second);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.AdditiveMapUtils#addAllNumbers(java.util.Map, java.util.Map)}.
     */
    @Test
    public void testAddAllNumbersMapOfQsuperKIntegerMapOfKInteger () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.AdditiveMapUtils#addAllNumbers(java.util.Map, java.util.Map, boolean)}.
     */
    @Test
    public void testAddAllNumbersMapOfQsuperKFractionMapOfKFractionBoolean () {
        fail("Not yet implemented");
    }

}

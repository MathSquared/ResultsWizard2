/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertEquals;

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
        Map<String, Integer> one = new HashMap<String, Integer>();
        one.put("a", 6);
        one.put("b", 3);

        Map<String, Integer> two = new HashMap<String, Integer>();
        two.put("a", -4);
        two.put("c", -7);

        Map<String, Integer> res = new HashMap<String, Integer>();
        res.put("a", 2);
        res.put("b", 3);
        res.put("c", -7);

        Map<String, Integer> oldTwo = new HashMap<String, Integer>(two); // used to check that two doesn't change

        AdditiveMapUtils.addAllNumbers(one, two);
        assertEquals("Addition", one, res);
        assertEquals("Two unchanged", two, oldTwo);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.AdditiveMapUtils#addAllNumbers(java.util.Map, java.util.Map, boolean)}.
     */
    @Test
    public void testAddAllNumbersMapOfQsuperKFractionMapOfKFractionBoolean () {
        Map<String, Fraction> one = new HashMap<String, Fraction>();
        one.put("a", new Fraction(5, 3));
        one.put("b", new Fraction(7, 4));

        Map<String, Fraction> two = new HashMap<String, Fraction>();
        two.put("a", new Fraction(-1, 3));
        two.put("c", new Fraction(-6, 5));

        Map<String, Fraction> res = new HashMap<String, Fraction>();
        res.put("a", new Fraction(4, 3));
        res.put("b", new Fraction(7, 4));
        res.put("c", new Fraction(-6, 5));

        Map<String, Fraction> oldTwo = new HashMap<String, Fraction>(two); // used to check that two doesn't change

        AdditiveMapUtils.addAllNumbers(one, two, false);
        assertEquals("Addition", one, res);
        assertEquals("Two unchanged", two, oldTwo);
    }

}

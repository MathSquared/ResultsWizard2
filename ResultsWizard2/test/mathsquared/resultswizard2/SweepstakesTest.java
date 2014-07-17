/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author MathSquared
 * 
 */
public class SweepstakesTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.Sweepstakes#assignPoints(int[], int[], mathsquared.resultswizard2.TiePlaceAssignment, mathsquared.resultswizard2.SweepstakesAssignment)}.
     */
    @Test
    public void testAssignPoints () {
        int[] spec = new int[]{243, 81, 27, 9, 3, 1}; // powers of 3 (averages of 2 or 3 will be whole, and sums of two elements are distinguished from lone elements)

        // CONDENSED LENGTH ARRAYS //

        // Simplest cases
        int[] simple = new int[]{1, 1, 1, 1, 1, 1}; // six competitors, no ties
        int[] overrun = new int[]{1, 1, 1, 1, 1, 1, 1}; // seven competitors, no ties

        // RESULTS ARRAYS //

        // Simplest cases
        Fraction[] simpleRes = new Fraction[]{new Fraction(243), new Fraction(81), new Fraction(27), new Fraction(9), new Fraction(3), new Fraction(1)};
        Fraction[] overrunRes = new Fraction[]{new Fraction(243), new Fraction(81), new Fraction(27), new Fraction(9), new Fraction(3), new Fraction(1), new Fraction(0)};

        // TEST CODE //

        TiePlaceAssignment tpaDefault = TiePlaceAssignment.TOP; // this is tested with SweepstakesAssignment.TIE_PLACE

        // Sanity check: SweepstakesAssignment.CUSTOM
        assertTrue("Returns null for SweepstakesAssignment.CUSTOM", null == Sweepstakes.assignPoints(simple, spec, tpaDefault, SweepstakesAssignment.CUSTOM));

        // Simple cases: simple, overrun
        assertArrayEquals("Simplest case", Sweepstakes.assignPoints(simple, spec, tpaDefault, SweepstakesAssignment.TOP), simpleRes);
        assertArrayEquals("Overrun by 1, no ties", Sweepstakes.assignPoints(overrun, spec, tpaDefault, SweepstakesAssignment.TOP), overrunRes);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Sweepstakes#linkSweepstakes(java.lang.String[][], mathsquared.resultswizard2.Fraction[])}.
     */
    @Test
    public void testLinkSweepstakes () {
        String[][] simple = new String[][]{{"a", "b"}, {"c"}, {"d", "e", "f"}};
        String[][] repeat = new String[][]{{"a", "b", "c"}, {"b", "d"}};
        String[][] selfRepeat = new String[][]{{"a", "a"}, {"b"}};

        Fraction[] toLink = new Fraction[]{new Fraction(4), new Fraction(2), new Fraction(1)};

        Map<String, Fraction> simpleRes = new HashMap<String, Fraction>();
        simpleRes.put("a", new Fraction(4));
        simpleRes.put("b", new Fraction(4));
        simpleRes.put("c", new Fraction(2));
        simpleRes.put("d", new Fraction(1));
        simpleRes.put("e", new Fraction(1));
        simpleRes.put("f", new Fraction(1));

        Map<String, Fraction> repeatRes = new HashMap<String, Fraction>();
        repeatRes.put("a", new Fraction(4));
        repeatRes.put("b", new Fraction(6));
        repeatRes.put("c", new Fraction(4));
        repeatRes.put("d", new Fraction(2));

        Map<String, Fraction> selfRepeatRes = new HashMap<String, Fraction>();
        selfRepeatRes.put("a", new Fraction(8));
        selfRepeatRes.put("b", new Fraction(2));

        assertEquals("Simple, no dupes", Sweepstakes.linkSweepstakes(simple, toLink), simpleRes);
        assertEquals("Repeated keys", Sweepstakes.linkSweepstakes(repeat, toLink), repeatRes);
        assertEquals("Repeated keys in the same sub-array", Sweepstakes.linkSweepstakes(selfRepeat, toLink), selfRepeatRes);
    }

}

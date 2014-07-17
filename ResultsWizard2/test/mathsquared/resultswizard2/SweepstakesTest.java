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
public class SweepstakesTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.Sweepstakes#assignPoints(int[], int[], mathsquared.resultswizard2.TiePlaceAssignment, mathsquared.resultswizard2.SweepstakesAssignment)}.
     */
    @Test
    public void testAssignPoints () {
        fail("Not yet implemented");
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

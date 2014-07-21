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
        int[] spec = new int[]{243, 81, 27, 9, 3, 1}; // powers of 3 (averages of 2 or 3 will be whole, and sums or averages of two elements are distinguished from lone elements)

        // CONDENSED LENGTH ARRAYS //

        // Simplest cases
        int[] simple = new int[]{1, 1, 1, 1, 1, 1}; // six competitors, no ties
        int[] overrun = new int[]{1, 1, 1, 1, 1, 1, 1}; // seven competitors, no ties

        // TiePlaceAssignment comparison testing
        int[] tpaTest = new int[]{4}; // Four places tied--each place in the range is selected by a different TPA

        // My First Tie: two-way tie for first place
        int[] twoWayTie = new int[]{2};

        // Test the different AVERAGEs (especially for off-the-end scenarios)
        int[] avgTest = new int[]{1, 1, 1, 1, 3}; // seven places, which is important for AVERAGE_IGNORE vs AVERAGE

        // Seven-way tie: final test of ridiculousness
        int[] sevenWay = new int[]{7};

        // RESULTS ARRAYS //

        // Simplest cases
        Fraction[] simpleRes = new Fraction[]{new Fraction(243), new Fraction(81), new Fraction(27), new Fraction(9), new Fraction(3), new Fraction(1)};
        Fraction[] overrunRes = new Fraction[]{new Fraction(243), new Fraction(81), new Fraction(27), new Fraction(9), new Fraction(3), new Fraction(1), new Fraction(0)};

        // TiePlaceAssignment comparison testing
        Fraction[] tpaTestResTop = new Fraction[]{new Fraction(243)}; // 1 to 4 >-- TOP --> 1
        Fraction[] tpaTestResBot = new Fraction[]{new Fraction(9)}; // 1 to 4 >-- BOT --> 4
        Fraction[] tpaTestResMrb = new Fraction[]{new Fraction(81)}; // 1 to 4 >-- MID_ROUND_BETTER --> 2
        Fraction[] tpaTestResMrw = new Fraction[]{new Fraction(27)}; // 1 to 4 >-- MID_ROUND_WORSE --> 3

        // My First Tie: two-way tie for first place
        Fraction[] twoWayTieResHigh = new Fraction[]{new Fraction(243)}; // for those that select the high place
        Fraction[] twoWayTieResLow = new Fraction[]{new Fraction(81)}; // for those that select the low place
        Fraction[] twoWayTieResAvg = new Fraction[]{new Fraction(243 + 81, 2)}; // for those that select the average of the assigned totals

        // Test the different AVERAGEs
        Fraction[] avgTestResNorm = new Fraction[]{new Fraction(243), new Fraction(81), new Fraction(27), new Fraction(9), new Fraction(3 + 1 + 0, 3)};
        Fraction[] avgTestResIgn = new Fraction[]{new Fraction(243), new Fraction(81), new Fraction(27), new Fraction(9), new Fraction(3 + 1, 2)}; // _IGNORE ignores the unspecified place
        Fraction[] avgTestResAdjMed = new Fraction[]{new Fraction(243), new Fraction(81), new Fraction(27), new Fraction(9), new Fraction(1)}; // also applies to MEDIAN

        // Seven-way tie
        Fraction[] sevenWayResTop = new Fraction[]{new Fraction(243)};
        Fraction[] sevenWayResBot = new Fraction[]{new Fraction(0)};
        Fraction[] sevenWayResMrb = new Fraction[]{new Fraction(9)};
        Fraction[] sevenWayResMrw = new Fraction[]{new Fraction(9)};
        Fraction[] sevenWayResAvg = new Fraction[]{new Fraction(243 + 81 + 27 + 9 + 3 + 1 + 0, 7)};
        Fraction[] sevenWayResAvi = new Fraction[]{new Fraction(243 + 81 + 27 + 9 + 3 + 1, 6)};
        Fraction[] sevenWayResAvj = new Fraction[]{new Fraction(81 + 27 + 9 + 3 + 1, 5)};
        Fraction[] sevenWayResMed = new Fraction[]{new Fraction(9)};

        // TEST CODE //

        TiePlaceAssignment tpaDefault = TiePlaceAssignment.TOP; // behavior of TPA is tested with SweepstakesAssignment.TIE_PLACE; otherwise, we can afford to make this more compact

        // Sanity check: SweepstakesAssignment.CUSTOM
        assertTrue("Returns null for SweepstakesAssignment.CUSTOM", null == Sweepstakes.assignPoints(simple, spec, tpaDefault, SweepstakesAssignment.CUSTOM));

        // Simple cases: simple, overrun
        assertArrayEquals("Simplest case", Sweepstakes.assignPoints(simple, spec, tpaDefault, SweepstakesAssignment.TOP), simpleRes);
        assertArrayEquals("Overrun by 1, no ties", Sweepstakes.assignPoints(overrun, spec, tpaDefault, SweepstakesAssignment.TOP), overrunRes);

        // TiePlaceAssignment comparison testing: stage 1, compare with actual TIE_PLACE results
        assertArrayEquals("TPA TOP stage 1", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.TOP, SweepstakesAssignment.TIE_PLACE), Sweepstakes.assignPoints(tpaTest, spec, tpaDefault, SweepstakesAssignment.TOP));
        assertArrayEquals("TPA BOT stage 1", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.BOTTOM, SweepstakesAssignment.TIE_PLACE), Sweepstakes.assignPoints(tpaTest, spec, tpaDefault, SweepstakesAssignment.BOTTOM));
        assertArrayEquals("TPA MRB stage 1", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.MID_ROUND_BETTER, SweepstakesAssignment.TIE_PLACE), Sweepstakes.assignPoints(tpaTest, spec, tpaDefault, SweepstakesAssignment.MID_ROUND_BETTER));
        assertArrayEquals("TPA MRW stage 1", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.MID_ROUND_WORSE, SweepstakesAssignment.TIE_PLACE), Sweepstakes.assignPoints(tpaTest, spec, tpaDefault, SweepstakesAssignment.MID_ROUND_WORSE));

        // TiePlaceAssignment comparison testing: stage 2, compare with expected by human computation
        assertArrayEquals("TPA TOP stage 2", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.TOP, SweepstakesAssignment.TIE_PLACE), tpaTestResTop);
        assertArrayEquals("TPA BOT stage 2", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.BOTTOM, SweepstakesAssignment.TIE_PLACE), tpaTestResBot);
        assertArrayEquals("TPA MRB stage 2", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.MID_ROUND_BETTER, SweepstakesAssignment.TIE_PLACE), tpaTestResMrb);
        assertArrayEquals("TPA MRW stage 2", Sweepstakes.assignPoints(tpaTest, spec, TiePlaceAssignment.MID_ROUND_WORSE, SweepstakesAssignment.TIE_PLACE), tpaTestResMrw);

        // My First Tie: two-way tie for first place
        assertArrayEquals("My First Tie TOP", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.TOP), twoWayTieResHigh);
        assertArrayEquals("My First Tie BOT", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.BOTTOM), twoWayTieResLow);
        assertArrayEquals("My First Tie MRB", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.MID_ROUND_BETTER), twoWayTieResHigh);
        assertArrayEquals("My First Tie MRW", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.MID_ROUND_WORSE), twoWayTieResLow);
        assertArrayEquals("My First Tie AVG", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.AVERAGE), twoWayTieResAvg);
        assertArrayEquals("My First Tie AVI", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.AVERAGE_IGNORE), twoWayTieResAvg);
        assertArrayEquals("My First Tie AVJ", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.AVERAGE_ADJUSTED), twoWayTieResAvg);
        assertArrayEquals("My First Tie MED", Sweepstakes.assignPoints(twoWayTie, spec, tpaDefault, SweepstakesAssignment.MEDIAN), twoWayTieResAvg);

        // Test the different AVERAGEs: check behavior of AVERAGE_* and MEDIAN
        assertArrayEquals("Average Behavior AVG", Sweepstakes.assignPoints(avgTest, spec, tpaDefault, SweepstakesAssignment.AVERAGE), avgTestResNorm);
        assertArrayEquals("Average Behavior AVI", Sweepstakes.assignPoints(avgTest, spec, tpaDefault, SweepstakesAssignment.AVERAGE_IGNORE), avgTestResIgn);
        assertArrayEquals("Average Behavior AVJ", Sweepstakes.assignPoints(avgTest, spec, tpaDefault, SweepstakesAssignment.AVERAGE_ADJUSTED), avgTestResAdjMed);
        assertArrayEquals("Average Behavior MED", Sweepstakes.assignPoints(avgTest, spec, tpaDefault, SweepstakesAssignment.MEDIAN), avgTestResAdjMed);

        // Seven-way tie: final test
        assertArrayEquals("Seven Way TOP", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.TOP), sevenWayResTop);
        assertArrayEquals("Seven Way BOT", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.BOTTOM), sevenWayResBot);
        assertArrayEquals("Seven Way MRB", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.MID_ROUND_BETTER), sevenWayResMrb);
        assertArrayEquals("Seven Way MRW", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.MID_ROUND_WORSE), sevenWayResMrw);
        assertArrayEquals("Seven Way AVG", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.AVERAGE), sevenWayResAvg);
        assertArrayEquals("Seven Way AVI", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.AVERAGE_IGNORE), sevenWayResAvi);
        assertArrayEquals("Seven Way AVJ", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.AVERAGE_ADJUSTED), sevenWayResAvj);
        assertArrayEquals("Seven Way MED", Sweepstakes.assignPoints(sevenWay, spec, tpaDefault, SweepstakesAssignment.MEDIAN), sevenWayResMed);
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

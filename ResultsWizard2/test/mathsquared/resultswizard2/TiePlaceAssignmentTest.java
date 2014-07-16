/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author MathSquared
 * 
 */
public class TiePlaceAssignmentTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.TiePlaceAssignment#assignPlace(int, int)}.
     */
    @Test
    public void testAssignPlace () {
        TiePlaceAssignment t = TiePlaceAssignment.TOP;
        assertTrue("Simple top", t.assignPlace(3, 5) == 3);
        assertTrue("Simple top reversed", t.assignPlace(5, 3) == 3);

        TiePlaceAssignment b = TiePlaceAssignment.BOTTOM;
        assertTrue("Simple bot", b.assignPlace(3, 5) == 5);
        assertTrue("Simple bot reversed", b.assignPlace(5, 3) == 5);

        TiePlaceAssignment m = TiePlaceAssignment.MID_ROUND_BETTER;
        assertTrue("Middle no rounding (MRB)", m.assignPlace(2, 6) == 4);
        assertTrue("Middle no rounding reversed (MRB)", m.assignPlace(6, 2) == 4);
        assertTrue("Middle rounding (MRB)", m.assignPlace(2, 5) == 3);
        assertTrue("Middle rounding reversed (MRB)", m.assignPlace(5, 2) == 3);

        TiePlaceAssignment w = TiePlaceAssignment.MID_ROUND_WORSE;
        assertTrue("Middle no rounding (MRW)", w.assignPlace(2, 6) == 4);
        assertTrue("Middle no rounding reversed (MRW)", w.assignPlace(6, 2) == 4);
        assertTrue("Middle rounding (MRW)", w.assignPlace(2, 5) == 4);
        assertTrue("Middle rounding reversed (MRW)", w.assignPlace(5, 2) == 4);
    }

}

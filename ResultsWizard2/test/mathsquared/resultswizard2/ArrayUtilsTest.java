/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author MathSquared
 * 
 */
public class ArrayUtilsTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.ArrayUtils#deepCopyOf(T[][])}.
     */
    // Timeout to detect infinite recursion with null
    @Test(timeout = 2000)
    public void testDeepCopyOf () {
        // Initialize some arrays

        String[][][] evil = new String[][][] { { {"a", "b"}, {"c"}}, {{"e", "f", "g"}}, { {"h", "i"}, {"j"}, {"k", "l", "m"}}};
        String[][][] result = ArrayUtils.deepCopyOf(evil);
        assertArrayEquals("3-D irregular String array", evil, result);

        Integer[][] easier = new Integer[][] { {1, 2}, {3}, {4, 5, 6}};
        Integer[][] easierResult = ArrayUtils.deepCopyOf(easier);
        assertArrayEquals("2-D irregular Integer array", easier, easierResult);

        Object[][] nul = null;
        assertArrayEquals("Null", ArrayUtils.deepCopyOf(nul), null); // infinite recursion detected by timeout
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.ArrayUtils#checkTies(String[][])}.
     */
    @Test
    public void testCheckTies () {
        String[][] res = new String[][] { {"First"}, {"Second A", "Second B"}, null, {"Fourth A", "Fourth B", "Fourth C"}, null, null, {"Seventh A", "Seventh B"}};
        assertTrue("Valid with several ties", ArrayUtils.checkTies(res));

        String[][] bad = new String[][] { {"First A", "First B"}, {"Second"}};
        assertFalse("Invalid--missing skip", ArrayUtils.checkTies(bad));

        String[][] nul = new String[][] { {"First A", "First B"}, null, null, {"Fourth A", "Fourth B"}};
        assertFalse("Invalid--extraneous null", ArrayUtils.checkTies(nul));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.ArrayUtils#checkStructureSame(Object[], Object[])}.
     */
    @Test
    public void testCheckStructureSame () {
        Integer[][] a = { {1, 2}, null, {}, {3}};
        Integer[][] b = { {4, 5}, null, {}, {6}};
        assertTrue("Same structure including both null and empty subarray", ArrayUtils.checkStructureSame(a, b));

        Integer[][] c = { {7, 8}, null, {}};
        assertFalse("Second array prefix of first", ArrayUtils.checkStructureSame(a, c));
        assertFalse("First array prefix of second", ArrayUtils.checkStructureSame(c, b));

        assertTrue("Two nulls", ArrayUtils.checkStructureSame(null, null));
        assertFalse("Null and non-null", ArrayUtils.checkStructureSame(null, a));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.ArrayUtils#lengthArray(Object[][])}.
     */
    @Test
    public void testLengthArray () {
        Object[][] test = new Object[][] {new Object[3], new Object[4], new Object[0]};
        int[] testRes = new int[] {3, 4, 0};
        Object[][] tes2 = new Object[][] {new Object[4], null, new Object[1]};
        int[] tes2Res = new int[] {4, 0, 1};

        assertArrayEquals("Simple", ArrayUtils.lengthArray(test), testRes);
        assertArrayEquals("With null", ArrayUtils.lengthArray(tes2), tes2Res);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.ArrayUtils#condensedLengthArray(int[])}.
     */
    @Test
    public void testCondensedLengthArray () {
        int[] simp = new int[] {1, 1, 1};
        int[] simpRes = new int[] {1, 1, 1};
        int[] skips = new int[] {1, 2, 0, 1, 1, 2, 0};
        int[] skipsRes = new int[] {1, 2, 1, 1, 2};
        int[] offEnd = new int[] {1, 1, 2, 0, 1, 4, 0, 0, 0, 3, 0};
        int[] offEndRes = new int[] {1, 1, 2, 1, 4, 3};
        int[] terminalOverOne = new int[] {1, 1, 3};
        int[] terminalOverOneRes = new int[] {1, 1, 3};

        assertArrayEquals("Ones", ArrayUtils.condensedLengthArray(simp), simpRes);
        assertArrayEquals("Skips", ArrayUtils.condensedLengthArray(skips), skipsRes);
        assertArrayEquals("Off the end", ArrayUtils.condensedLengthArray(offEnd), offEndRes);
        assertArrayEquals("Terminal over one", ArrayUtils.condensedLengthArray(terminalOverOne), terminalOverOneRes);

        // Check for erroneous arguments
        int[] neg = new int[] {1, -1, 2};
        boolean negCaught = false;
        int[] initZero = new int[] {0, 1, 2, 0, 1};
        boolean initZeroCaught = false;
        int[] unSkip = new int[] {1, 2, 1};
        boolean unSkipCaught = false;
        int[] overSkip = new int[] {2, 0, 1, 3, 0, 0, 0};
        boolean overSkipCaught = false;

        try {
            ArrayUtils.condensedLengthArray(neg);
        } catch (IllegalArgumentException e) {
            negCaught = true;
        }
        if (!negCaught) {
            fail("Negative not caught");
        }
        try {
            ArrayUtils.condensedLengthArray(initZero);
        } catch (IllegalArgumentException e) {
            initZeroCaught = true;
        }
        if (!initZeroCaught) {
            fail("Initial zero not caught");
        }
        try {
            ArrayUtils.condensedLengthArray(unSkip);
        } catch (IllegalArgumentException e) {
            unSkipCaught = true;
        }
        if (!unSkipCaught) {
            fail("Missing skip not caught");
        }
        try {
            ArrayUtils.condensedLengthArray(overSkip);
        } catch (IllegalArgumentException e) {
            overSkipCaught = true;
        }
        if (!overSkipCaught) {
            fail("Extraneous skip not caught");
        }
    }
}

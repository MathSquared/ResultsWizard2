/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        String[][][] evil = new String[][][]{{{"a", "b"}, {"c"}}, {{"e", "f", "g"}}, {{"h", "i"}, {"j"}, {"k", "l", "m"}}};
        String[][][] result = ArrayUtils.deepCopyOf(evil);
        assertArrayEquals("3-D irregular String array", evil, result);

        Integer[][] easier = new Integer[][]{{1, 2}, {3}, {4, 5, 6}};
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
        String[][] res = new String[][]{{"First"}, {"Second A", "Second B"}, null, {"Fourth A", "Fourth B", "Fourth C"}, null, null, {"Seventh A", "Seventh B"}};
        assertTrue("Valid with several ties", ArrayUtils.checkTies(res));

        String[][] bad = new String[][]{{"First A", "First B"}, {"Second"}};
        assertFalse("Invalid--missing skip", ArrayUtils.checkTies(bad));

        String[][] nul = new String[][]{{"First A", "First B"}, null, null, {"Fourth A", "Fourth B"}};
        assertFalse("Invalid--extraneous null", ArrayUtils.checkTies(nul));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.ArrayUtils#checkStructureSame(Object[], Object[])}.
     */
    @Test
    public void testCheckStructureSame () {
        Integer[][] a = {{1, 2}, null, {}, {3}};
        Integer[][] b = {{4, 5}, null, {}, {6}};
        assertTrue("Same structure including both null and empty subarray", ArrayUtils.checkStructureSame(a, b));

        Integer[][] c = {{7, 8}, null, {}};
        assertFalse("Second array prefix of first", ArrayUtils.checkStructureSame(a, c));
        assertFalse("First array prefix of second", ArrayUtils.checkStructureSame(c, b));

        assertTrue("Two nulls", ArrayUtils.checkStructureSame(null, null));
        assertFalse("Null and non-null", ArrayUtils.checkStructureSame(null, a));
    }

}

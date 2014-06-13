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
    @Test
    public void testDeepCopyOf () {
        // Initialize some arrays

        String[][][] evil = new String[][][] { { {"a", "b"}, {"c"}}, {{"e", "f", "g"}}, { {"h", "i"}, {"j"}, {"k", "l", "m"}}};
        String[][][] result = ArrayUtils.deepCopyOf(evil);
        assertArrayEquals("3-D irregular String array", evil, result);

        Integer[][] easier = new Integer[][] { {1, 2}, {3}, {4, 5, 6}};
        Integer[][] easierResult = ArrayUtils.deepCopyOf(easier);
        assertArrayEquals("2-D irregular Integer array", easier, easierResult);
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

}

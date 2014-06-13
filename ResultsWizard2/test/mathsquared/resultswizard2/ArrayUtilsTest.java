/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertArrayEquals;

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

}

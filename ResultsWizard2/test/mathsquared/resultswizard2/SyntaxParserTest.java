/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author MathSquared
 * 
 */
public class SyntaxParserTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.SyntaxParser#parseQuotedSyntax(java.lang.String)}.
     * 
     * Tests the String <code>"a" "b" "c""d"</code>.
     */
    @Test
    public void testParseQuotedSyntax () {
        String[] parsed = SyntaxParser.parseQuotedSyntax("\"a\" \"b\" \"c\"\"d\"");
        // assertTrue("quoted syntax: space-separated with one quote escape", Arrays.equals(parsed, new String[] {"a", "b", "c\"d"}));
        assertArrayEquals("quoted syntax: space-separated with one quote escape", new String[] {"a", "b", "c\"d"}, parsed);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.SyntaxParser#createPairwiseMap(T[])}.
     */
    @Test
    public void testCreatePairwiseMap () {
        Map<Integer, Integer> pairs = SyntaxParser.createPairwiseMap(new Integer[] {1, 2, 3, 4, 5});
        Map<Integer, Integer> expected = new HashMap<Integer, Integer>();
        expected.put(1, 2);
        expected.put(3, 4);
        assertTrue("pairwise: five integers", pairs.equals(expected));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.SyntaxParser#parseIntegerList(java.lang.String)}.
     */
    @Test
    public void testParseIntegerList () {
        int[] parsed = SyntaxParser.parseIntegerList("1 2,3 4.5$");
        int[] parsed2 = SyntaxParser.parseIntegerList("1, 2: 3, ");
        assertTrue("integer list: five integers, various separators, trailing", Arrays.equals(parsed, new int[] {1, 2, 3, 4, 5}));
        assertTrue("integer list: various multiple-character separators, trailing", Arrays.equals(parsed, new int[] {1, 2, 3}));
    }

}

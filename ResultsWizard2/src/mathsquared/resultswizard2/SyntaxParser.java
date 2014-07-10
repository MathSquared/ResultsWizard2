/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Contains methods for parsing a wide variety of data storage syntaxes.
 * 
 * @author MathSquared
 * 
 */
public class SyntaxParser {

    /**
     * Parses a String containing several input values into an array of those input values.
     * 
     * <p>
     * The format for a quoted-syntax value consists of several strings in double quotes separated by one or more non-double-quote characters. To include a literal double-quote in one of these strings, one puts two double-quotes in close succession. For example, the string
     * </p>
     * 
     * <code>"a" "b","c""d"</code>
     * 
     * <p>
     * represents three values: <code>a</code>, <code>b</code>, and <code>c"d</code>.
     * 
     * @param raw the String containing several input values
     * @return the given input values in the order given in <code>raw</code>
     */
    public static String[] parseQuotedSyntax (String raw) {
        char[] rawChars = raw.toCharArray();
        List<String> ret = new ArrayList<String>();

        boolean inQuotes = false; // marks whether the next character is within a quoted string or not
        StringBuilder nextAddition = new StringBuilder("");
        for (int i = 0; i < rawChars.length; i++) {
            char x = rawChars[i];
            char y = (i + 1 < rawChars.length) ? rawChars[i + 1] : 0; // Conditional to prevent ArrayIndexOutOfBounds

            if (inQuotes) {
                if (x == '"') {
                    // Check next character; if it's another quote, this is an escape sequence
                    if (y == '"') {
                        // Add in the escaped quote
                        nextAddition.append('"');

                        // Skip over the next quote too
                        i++; // skips one of the two quotes; other skipped by the for loop header
                    } else {
                        // End of the quoted string
                        inQuotes = false;
                        ret.add(nextAddition.toString());
                        nextAddition = new StringBuilder("");
                    }
                } else {
                    // Character within quoted string
                    nextAddition.append(x);
                }
            } else {
                if (x == '"') {
                    // Beginning of a quoted string, whether or not there is another double-quote immediately after it
                    inQuotes = true;
                } else {
                    // Ignoring these chars; do nothing
                }
            }
        }

        return ret.toArray(new String[] {});
    }

    /**
     * Converts an array into a {@link Map} where each pair of elements in the array is taken to be first a key, then a value.
     * 
     * <p>
     * For example, the array <code>["a", "b", "c", "d"]</code> is converted into a <code>Map</code> which maps <code>"a"</code> to <code>"b"</code>, and <code>"c"</code> to <code>"d"</code>. If the length of the input array is odd, the last element is ignored.
     * </p>
     * 
     * @param input the input array of key-value pairs
     * @return the array as a <code>Map</code> of these key-value pairs
     */
    public static <T> Map<T, T> createPairwiseMap (T[] input) {
        List<T> inputList = Arrays.asList(input);
        Iterator<T> iterator = inputList.iterator();
        Map<T, T> ret = new HashMap<T, T>();

        while (iterator.hasNext()) {
            T x = iterator.next();
            // Make sure there is actually a pair of elements
            if (!iterator.hasNext()) {
                break;
            }
            T y = iterator.next();
            ret.put(x, y);
        }

        return ret;
    }

    /**
     * Parses a String representing a list of integers into an array of those integers.
     * 
     * <p>
     * The integers are represented as base-10 strings separated by one or more characters that are not ASCII digits. If the first character of the given String is not an ASCII digit, the first element of the list is taken to be 0. However, any terminating non-digit characters are ignored.
     * </p>
     * 
     * @param raw the list of integers, as a String
     * @return an array representing the integers in the order that they are specified
     * @throws NumberFormatException if an integer in the list does not parse into a base-10 integer as the {@link Integer#parseInt(String)} method would parse it
     */
    public static int[] parseIntegerList (String raw) {
        String[] numeralStrings = raw.split("\\D+"); // split on non-digits, discarding trailing strings
        int[] ret = new int[numeralStrings.length];

        for (int i = 0; i < numeralStrings.length; i++) {
            ret[i] = (numeralStrings[i].equals("")) ? 0 : Integer.parseInt(numeralStrings[i]); // parseInt() verifiably chokes on ""
        }

        return ret; // whether we maintain a reference is irrelevant
    }

}

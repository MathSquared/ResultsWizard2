/**
 * 
 */
package mathsquared.resultswizard2;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author MathSquared
 * 
 */
public class ArrayUtils {
    /**
     * Creates a deep copy of a multi-dimensional array, copying all sub-arrays of the array (but not the elements themselves).
     * 
     * @param toCopy the multi-dimensional array to copy
     * @return a copy of the array
     */
    // HASHTAG UNCHECKED CASTS
    @SuppressWarnings("unchecked")
    public static <T> T[][] deepCopyOf (T[][] toCopy) {
        // Instantiate a generic array
        Class<T[]> type = (Class<T[]>) toCopy.getClass().getComponentType();
        T[][] copied = (T[][]) Array.newInstance(type, toCopy.length);

        // If a two-dimensional array, use Arrays.copyOf; otherwise, recurse
        if (!(toCopy instanceof Object[][][])) { // not 3-D; by parameter, must be 2-D
            for (int i = 0; i < toCopy.length; i++) {
                copied[i] = Arrays.copyOf(toCopy[i], toCopy[i].length);
            }
        } else { // 3-D or above
            for (int i = 0; i < toCopy.length; i++) {
                copied[i] = (T[]) deepCopyOf((Object[][]) toCopy[i]);
            }
        }

        return copied;
    }

    /**
     * Checks that all ties are represented validly.
     * 
     * <p>
     * For every sub-array of a result array (which represents a tie), if the sub-array contains more than one element, places should be skipped in accordance. This corresponds to normal practice; for example, if two people tie for first place, the next place awarded is third place (and the second-place array should be null or empty).
     * </p>
     * 
     * @param results the results array whose integrity to check
     * @return true if the array correctly skips places for ties; false otherwise
     */
    public static boolean checkTies (String[][] results) {
        /*
         * Implementation:
         * 
         * The algorithm keeps track of the amount of results that have been seen so far, where a "result" is one String.
         * 
         * For each sub-array of the input array, the algorithm adds subArr.length - 1 to an internal tracking variable.
         * 
         * If this variable is non-zero, it means that there was a tie previously, and the next few places should be skipped with null.
         * 
         * If this variable is zero, it means that there have been no ties--or all were accounted for--so the next array should have elements.
         */

        int num = 0; // tracking variable mentioned above
        for (String[] x : results) {
            // num never decreases by more than 1 each turn, and can never decrease if it is 0, so check for num < 0 is unnecessary
            assert (num >= 0);
            if (num == 0 && (results == null || results.length == 0)) {
                return false;
            }
            if (num > 0 && results != null && results.length != 0) {
                return false;
            }
            num += (x == null) ? -1 : x.length - 1;
        }

        // no problems; num does not need to be 0 in case of ties for last place
        return true;
    }
}

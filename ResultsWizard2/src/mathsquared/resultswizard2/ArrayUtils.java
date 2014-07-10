/**
 * 
 */
package mathsquared.resultswizard2;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
     * @return a copy of the array, or null if <code>toCopy</code> is null
     */
    // HASHTAG UNCHECKED CASTS
    @SuppressWarnings("unchecked")
    public static <T> T[][] deepCopyOf (T[][] toCopy) {
        // Null handling; null is a copy of null
        if (toCopy == null) {
            return null;
        }

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
     * <p>
     * For a parameter of null, this method returns true.
     * </p>
     * 
     * @param results the results array whose integrity to check
     * @return true if the array correctly skips places for ties; false otherwise
     */
    public static boolean checkTies (Object[][] results) {
        // An empty array is structured correctly, since there are no unstructured elements
        if (results == null) {
            return true;
        }

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
        for (Object[] x : results) {
            // num never decreases by more than 1 each turn, and can never decrease if it is 0, so check for num < 0 is unnecessary
            assert (num >= 0);
            if (num == 0 && (x == null || x.length == 0)) {
                return false;
            }
            if (num > 0 && x != null && x.length != 0) {
                return false;
            }
            num += (x == null) ? -1 : x.length - 1;
        }

        // no problems; num does not need to be 0 in case of ties for last place
        return true;
    }

    /**
     * Checks that the structure of two arrays is the same. That is:
     * 
     * <ul>
     * <li><code>a.length == b.length</code></li>
     * <li>For any integer <code>i</code>, <code>a[i]</code> is an array iff <code>b[i]</code> is an array</li>
     * <li>For any integer <code>i</code>, if <code>a[i]</code> and <code>b[i]</code> are arrays, the first two statements are recursively valid for them as well</li>
     * </ul>
     * 
     * <p>
     * If one of the arrays is null, this method returns true iff both are null.
     * </p>
     * 
     * @param a the first array to check
     * @param b the second array to check
     * @return true if the arrays have the same structure as defined above, false otherwise
     */
    public static boolean checkStructureSame (Object[] a, Object[] b) {
        // Null handling; two nulls are structured the same, but a null and non-null are not
        if (a == null && b == null) {
            return true;
        }
        if (a == null ^ b == null) {
            return false;
        }

        if (a.length != b.length)
            return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] instanceof Object[] && b[i] instanceof Object[]) {
                if (!checkStructureSame((Object[]) a[i], (Object[]) b[i])) {
                    return false;
                }
            }

            // Not generic, so no enforcement on same type...might have one be an array of arrays and the other not be
            if (a[i] instanceof Object[] ^ b[i] instanceof Object[]) {
                return false;
            }
        }

        // no problems
        return true;
    }

    /**
     * Returns an array of the lengths of the sub-arrays of a given array.
     * 
     * <p>
     * That is, returns an array <code>ret</code> such that:
     * </p>
     * 
     * <ul>
     * <li><code>ret.length == arr.length</code>, and</li>
     * <li>for all integers <code>i</code> where <code>0 &lt; i &lt; arr.length</code>, <code>ret[i] = arr[i].length</code>. The length of a null array is defined to be 0.</li>
     * </ul>
     * 
     * @param arr a two-or-more-dimensional array
     * @return a length array as described above, or null if <code>arr</code> is null
     */
    public static int[] lengthArray (Object[][] arr) {
        if (arr == null) {
            return null;
        }
        int[] ret = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ret[i] = (arr[i] != null) ? arr[i].length : 0; // null array has 0 length
        }
        return ret;
    }

    /**
     * Returns a condensed length array for a given array.
     * 
     * <p>
     * This method behaves identically to <code>{@link #condensedLengthArray(int[]) condensedLengthArray}({@link #lengthArray(Object[][]) lengthArray}(arr))</code>.
     * </p>
     * 
     * @param arr the input array
     * @return a condensed length array for <code>arr</code>
     * @throws IllegalArgumentException if <code>{@link #checkTies(Object[][]) checkTies}(arr)</code> returns false
     */
    public static int[] condensedLengthArray (Object[][] arr) {
        // Check ties
        if (!checkTies(arr)) {
            throw new IllegalArgumentException("Ties in array are incorrectly formatted");
        }

        return condensedLengthArray(lengthArray(arr));
    }

    /**
     * Condenses a {@linkplain #lengthArray(Object[][]) length array}.
     * 
     * <p>
     * Input to this method must be formatted as if it came from an array for which {@link #checkTies(Object[][])} returns true. That is:
     * </p>
     * 
     * <ul>
     * <li>the array must start with a non-zero integer</li>
     * <li>every non-zero integer <code>i</code> in the array must be followed by <code>i - 1</code> zeroes (if <code>i - 1</code> is greater than the number of remaining spaces in the array, all of these remaining spaces must be filled with zeroes</code></li>
     * </ul>
     * 
     * <p>
     * In addition, the input array must contain no negative entries.
     * </p>
     * 
     * <p>
     * A condensed length array is a length array with all zeroes removed. Due to the <code>checkTies()</code> stipulation, a condensed length array can still be converted into a full length array. (Terminal zeroes are the exception, since not all of them have to be present in input to this method.)
     * </p>
     * 
     * @param lengthArray a length array meeting the conditions above
     * @return a condensed version of the length array
     * @throws IllegalArgumentException if any entry in the length array is negative, the array contains a nonzero entry that should be zero, or a zero entry that should be nonzero
     */
    public static int[] condensedLengthArray (int[] lengthArray) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < lengthArray.length; i++) {
            int x = lengthArray[i];
            if (x == 0) { // there should be no zeroes except to skip places
                throw new IllegalArgumentException("Improperly formatted length array: zero at " + i);
            }
            if (x < 0) {
                throw new IllegalArgumentException("Invalid length array: negative at " + i);
            }
            ret.add(x);
            int initI = i;

            // Skip places, checking to ensure that each one is 0--intentionally updates the outer loop counter
            for (; i < initI + x - 1 && i < lengthArray.length; i++) {
                if (lengthArray[i] < 0) {
                    throw new IllegalArgumentException("Invalid length array: negative at " + i);
                }
                if (lengthArray[i] > 0) {
                    throw new IllegalArgumentException("Improperly formatted length array: nonzero at " + i);
                }
            }
        }

        // Unbox the ArrayList (it would be easier if it was automatic, but I understand the performance hit array autounboxing would cause)
        Integer[] pending = ret.toArray(new Integer[0]);
        int[] retArr = new int[pending.length];
        for (int i = 0; i < pending.length; i++) {
            retArr[i] = pending[i];
        }
        return retArr;
    }
}

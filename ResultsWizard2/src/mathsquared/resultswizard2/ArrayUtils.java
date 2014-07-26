/**
 * 
 */
package mathsquared.resultswizard2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Contains utility methods for working with arrays.
 * 
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
     * @return a condensed version of the length array, or null if <code>lengthArray</code> is null
     * @throws IllegalArgumentException if any entry in the length array is negative, the array contains a nonzero entry that should be zero, or a zero entry that should be nonzero
     */
    public static int[] condensedLengthArray (int[] lengthArray) {
        if (lengthArray == null) {
            return null;
        }
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < lengthArray.length;) { // incremented by final loop
            int x = lengthArray[i];
            if (x == 0) { // there should be no zeroes except to skip places
                throw new IllegalArgumentException("Improperly formatted length array: zero at " + i);
            }
            if (x < 0) {
                throw new IllegalArgumentException("Invalid length array: negative at " + i);
            }
            ret.add(x);
            int initI = i;

            i++; // prevents complaining about the current entry (which SHOULD be nonzero) by skipping it from loop below

            // Skip places, checking to ensure that each one is 0--intentionally updates the outer loop counter
            // The <= ensures that the final skip is checked--then increments i one more time. So the loop header does no incrementing.
            for (; i <= initI + x - 1 && i < lengthArray.length; i++) {
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

    /**
     * Indexes an array by another int array and returns the result.
     * 
     * <p>
     * Specifically, returns an array <code>ret</code> such that:
     * </p>
     * 
     * <ul>
     * <li><code>ret.length == indices.length</code>, and</li>
     * <li>for all integers <code>i</code> where <code>0 &lt; i &lt; ret.length</code>, <code>ret[i] == toIndex[indices[i]]</code></li>
     * </ul>
     * 
     * <p>
     * If <code>indices</code> is null, this method returns null. If <code>toIndex</code> is null, this method throws a <code>NullPointerException</code>.
     * </p>
     * 
     * @param toIndex the array to index
     * @param indices the indices within <code>toIndex</code> to return
     * @return <code>toIndex</code> indexed by <code>indices</code>, as described above
     * @throws ArrayIndexOutOfBoundsException if any entry in <code>indices</code> is negative or greater than <code>toIndex.length</code>
     * @throws NullPointerException if <code>toIndex == null</code>
     */
    // HASHTAG UNCHECKED CASTS
    @SuppressWarnings("unchecked")
    public static <T> T[] index (T[] toIndex, int[] indices) {
        // Null handling
        if (indices == null) {
            return null;
        }
        if (toIndex == null) {
            throw new NullPointerException("toIndex must not be null");
        }

        // Instantiate a generic array (equivalent to ret = new T[indices.length])
        Class<T> type = (Class<T>) toIndex.getClass().getComponentType();
        T[] ret = (T[]) Array.newInstance(type, indices.length);

        for (int i = 0; i < indices.length; i++) {
            if (indices[i] < 0 || indices[i] > toIndex.length) {
                throw new ArrayIndexOutOfBoundsException("Invalid index " + indices[i] + " at indices[" + i + "]");
            }
            ret[i] = toIndex[indices[i]];
        }

        return ret;
    }

    /**
     * Sorts multiple arrays by the quantities in one of them. This is similar to sorting a database, where the first column represents the sort key.
     * 
     * <p>
     * Note that this sort is not guaranteed to be stable; elements which compare as equal might be reordered relative to each other in the resulting array.
     * </p>
     * 
     * <p>
     * It is required that the lengths of all of the subarrays of <code>toSort</code> be equal. Failure to assure this throws an IllegalArgumentException.</code>
     * </p>
     * 
     * <p>
     * If <code>i</code> is a non-negative integer less than <code>toSort[0].length</code>, and <code>ret</code> is the result of calling <code>multiSort(toSort)</code>, then:
     * </p>
     * 
     * <ul>
     * <li>there exists a non-negative integer <code>j</code> less than <code>toSort[0].length</code> such that for all non-negative integers <code>k</code> less than <code>toSort.length</code>, <code>toSort[k][i] == ret[k][j]</code> (that is, all elements which share the same index in the subarrays of <code>toSort</code> will continue to share an index in the subarrays of <code>ret</code>, but the shared index might change)</li>
     * <li>if <code>i > 0</code>, <code>ret[0][i-1]</code> compares as less than <code>ret[0][i]</code> (that is, <code>ret[0]</code> will have its elements sorted in their natural order)</li>
     * </ul>
     * 
     * <p>
     * Additionally:
     * </p>
     * 
     * <ul>
     * <li><code>ret.length == toSort.length</code></li>
     * <li>for all non-negative integers <code>n</code> less than <code>toSort.length</code>, <code>ret[n].length == toSort[n].length</code>
     * </ul>
     * 
     * @param toSort the arrays to sort; all of the arrays will be sorted according to the values in <code>toSort[0]</code>
     * @return all of the arrays sorted by the keys in the first column (null if <code>toSort == null</code>, and a {@linkplain #deepCopyOf(Object[][]) deep copy} of <code>toSort</code> if its length is 0)
     * @throws IllegalArgumentException if the lengths of the subarrays of <code>toSort</code> do not all match
     */
    // HASHTAG UNCHECKED CASTS
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> T[][] multiSort (T[]... toSort) { // TODO unit test
        // Null handling
        if (toSort == null) {
            return null;
        }
        if (toSort.length == 0) {
            return deepCopyOf(toSort);
        }

        // Check lengths all the same
        int expLength = toSort[0].length;
        for (int i = 1; i < toSort.length; i++) {
            if (toSort[i].length != expLength) {
                throw new IllegalArgumentException("Array " + i + " (zero-based) does not match the length of array 0 (observed: " + toSort[i].length + ", expected: " + expLength + ")");
            }
        }

        // Logic below is HEAVILY based on a StackOverflow post by Jherico; http://stackoverflow.com/a/951910/1979005
        SortedMap<T, List<Integer>> sortedIndices = new TreeMap<T, List<Integer>>();
        for (int i = 0; i < toSort[0].length; i++) {
            T key = toSort[0][i];
            if (!sortedIndices.containsKey(key)) {
                sortedIndices.put(key, new ArrayList<Integer>());
            }
            sortedIndices.get(key).add(i);
        }
        Collection<List<Integer>> indices = sortedIndices.values(); // indices sorted by their corresponding keys (and thus in order of insertion into new array)

        // Instantiate a generic array
        Class<T[]> type = (Class<T[]>) toSort.getClass().getComponentType();
        T[][] ret = (T[][]) Array.newInstance(type, toSort.length);

        int arrayInsertionIndex = 0; // used to track where to insert array elements so we don't have to use Lists
        for (List<Integer> x : indices) {
            for (int y : x) {
                // Add to each of the arrays that we're sorting
                for (int i = 0; i < toSort.length; i++) {
                    ret[i][arrayInsertionIndex] = toSort[i][y];
                }
                arrayInsertionIndex++;
            }
        }

        return ret;
    }
}

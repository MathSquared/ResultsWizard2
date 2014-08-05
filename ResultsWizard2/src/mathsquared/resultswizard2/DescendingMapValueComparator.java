/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Comparator;
import java.util.Map;

/**
 * Compares two keys based on the values associated with them in the given Map. The values are sorted in descending order. Null values are ranked greater than everything else, placing them last on a sorted list.
 * 
 * @author MathSquared
 * @param <K> the keys of the Map that we are comparing
 * @param <V> the values of the Map that we are comparing
 * 
 */
public class DescendingMapValueComparator<K, V extends Comparable<V>> implements Comparator<K> {
    private Map<K, V> toCompare;

    /**
     * Constructs a new DescendingMapValueComparator pulling values from the given Map.
     * 
     * @param toCompare a Map from which to pull the values used in comparison
     */
    public DescendingMapValueComparator (Map<K, V> toCompare) {
        this.toCompare = toCompare;
    }

    /**
     * Compares two keys based on their values in the given Map. Note that this comparator uses the opposite of the natural ordering of the values.
     * 
     * <p>
     * Nulls are compared as greater than everything else. This places them at the end of a sorted list.
     * </p>
     * 
     * <p>
     * Formally: let <code>c</code> be a DescendingMapValueComparator, <code>fVal</code> be equal to <code>{@link #DescendingMapValueComparator(Map) toCompare}.get(first)</code>, and <code>sVal</code> be equal to <code>toCompare.get(second)</code>. Then:
     * </p>
     * 
     * <ul>
     * <li>If <code>fVal</code> and <code>sVal</code> are both null, this method returns 0.</li>
     * <li>Otherwise, if <code>fVal</code> is null but <code>sVal</code> is not, this method returns 1.</li>
     * <li>Otherwise, if <code>sVal</code> is null but <code>fVal</code> is not, this method returns -1.</li>
     * <li>Otherwise, this method returns <code>sVal.compareTo(fVal)</code>. If <code>V</code> implements {@link Comparable} correctly, this should be equal to <code>-fVal.compareTo(sVal)</code>.</li>
     * </ul>
     * 
     * @param first the first key to compare
     * @param second the second key to compare
     * @return the descending-order sort of the keys based on their values in <code>toCompare</code>, as described above
     */
    public int compare (K first, K second) {
        V fVal = toCompare.get(first);
        V sVal = toCompare.get(second);

        // Handle nulls
        if (fVal == null) {
            if (sVal == null) {
                return 0;
            } else { // compare(null, 0)
                return 1;
            }
        } else {
            if (sVal == null) { // compare(0, null)
                return -1;
            } else {
                return sVal.compareTo(fVal); // reverse for descending order
            }
        }
    }
}

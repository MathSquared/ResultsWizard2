/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Contains methods for working with maps and sorting them.
 * 
 * @author MathSquared
 * 
 */
public class MapSortingUtils {
    /**
     * Returns a version of the given Map sorted by its values.
     * 
     * <p>
     * Specifically, returns a new Map containing the same keys as <code>toSort</code> whose iteration order follows the natural ordering of its keys. If <code>descending</code> is true, this ordering is reversed.
     * </p>
     * 
     * <p>
     * Nulls compare as less than everything else. That is, they will sort before everything else in an ascending sort, and after everything else in a descending sort.
     * </p>
     * 
     * @param toSort the map to sort
     * @param descending true if the highest values should sort to the beginning
     * @param thenByKey true if values that compare as equal should then be sorted by the values of their keys; keys are always sorted ascending, and will compare as equal if they do not implement Comparable
     * @return a new Map with its keys sorted in the natural order of their associated values
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortValue (Map<K, V> toSort, boolean descending, final boolean thenByKey) {
        // Set up ascending or descending.
        final int mult = (descending) ? -1 : 1; // if descending, multiply all comparisons by -1

        // HEAVILY based on http://stackoverflow.com/a/2581754/1979005
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(toSort.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
            // Horrid code to cope with sorting by key
            @SuppressWarnings("unchecked")
            public int compare (Map.Entry<K, V> one, Map.Entry<K, V> two) {
                if (one == null) { // sort nulls less than everything else (before in ascending, after in descending)
                    if (two == null) {
                        return 0;
                    } else { // (null, *)
                        return -1 * mult;
                    }
                } else {
                    if (two == null) { // (*, null)
                        return 1 * mult;
                    } else {
                        int valComp = mult * one.getValue().compareTo(two.getValue());
                        if (!thenByKey) { // Detect if we should sort by key
                            return valComp;
                        } else if (valComp != 0) {
                            return valComp;
                        } else if (!(one.getKey() instanceof Comparable<?>) || !(two.getKey() instanceof Comparable<?>)) { // cannot be compared
                            return 0;
                        } else {
                            try {
                                return mult * ((Comparable) one.getKey()).compareTo(two.getKey());
                            } catch (ClassCastException e) {
                                // if we can't compare, return 0
                                return 0;
                            }
                        }
                    }
                }
            }
        });
        Map<K, V> ret = new LinkedHashMap<K, V>(); // preserve iteration order
        for (Map.Entry<K, V> x : entries) {
            ret.put(x.getKey(), x.getValue());
        }
        return ret;
    }
}

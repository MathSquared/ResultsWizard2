/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Map;

/**
 * Contains utility methods for working with maps, specifically those mapping to <code>Integer</code> or {@link Fraction}.
 * 
 * @author MathSquared
 * 
 */
public class AdditiveMapUtils {
    /**
     * Adds a number to the value stored in a map under a given key.
     * 
     * <p>
     * Specifically, adds <code>val</code> to the value mapped to <code>key</code> in <code>toAdd</code>. If <code>toAdd.get(key) == null</code>, this method is equivalent to <code>toAdd.put(key, val)</code>.
     * </p>
     * 
     * @param toAdd the Map on which this operation is taking place
     * @param key the key under consideration
     * @param val the value to add to <code>toAdd</code>'s mapping to <code>key</code>
     */
    public static <K> void addNumber (Map<? super K, Integer> toAdd, K key, int val) {
        if (toAdd.get(key) == null) {
            toAdd.put(key, val);
        } else {
            toAdd.put(key, toAdd.get(key) + val);
        }
    }

    /**
     * Adds a number to the value stored in a map under a given key.
     * 
     * <p>
     * Specifically, {@linkplain Fraction#add(Fraction) adds} <code>val</code> to the value mapped to <code>key</code> in <code>toAdd</code>. If <code>toAdd.get(key) == null</code>, this method is equivalent to <code>toAdd.put(key, val)</code>.
     * </p>
     * 
     * @param toAdd the Map on which this operation is taking place
     * @param key the key under consideration
     * @param val the value to add to <code>toAdd</code>'s mapping to <code>key</code>
     */
    public static <K> void addNumber (Map<? super K, Fraction> toAdd, K key, Fraction val) {
        if (toAdd.get(key) == null) {
            toAdd.put(key, val);
        } else {
            toAdd.put(key, toAdd.get(key).add(val));
        }
    }

    /**
     * Numerically adds two maps together. Specifically, for each key in <code>src</code>, {@linkplain #addNumber(Map, Object, int) numerically adds} its value to that stored under that key in <code>dest</code>.
     * 
     * @param dest the Map to which to add values
     * @param src the Map from which to add values
     */
    public static <K> void addAllNumbers (Map<? super K, Integer> dest, Map<K, Integer> src) {
        for (K x : src.keySet()) {
            addNumber(dest, x, src.get(x));
        }
    }

    /**
     * Numerically adds two maps together. Specifically, for each key in <code>src</code>, {@linkplain #addNumber(Map, Object, Fraction) numerically adds} its value to that stored under that key in <code>dest</code>.
     * 
     * @param dest the Map to which to add values
     * @param src the Map from which to add values
     * @param dummy ignored; used to differentiate type erasure from {@link #addAllNumbers(Map, Map)}
     */
    public static <K> void addAllNumbers (Map<? super K, Fraction> dest, Map<K, Fraction> src, boolean dummy) {
        for (K x : src.keySet()) {
            addNumber(dest, x, src.get(x));
        }
    }
}

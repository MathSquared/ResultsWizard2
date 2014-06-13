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
}

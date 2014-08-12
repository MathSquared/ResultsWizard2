/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Exposes a Slide based on certain criteria. The Slide that is exposed can change at any time.
 * 
 * @author MathSquared
 * 
 */
public interface Selector {
    /**
     * Obtains the Slide that this Selector is currently exposing.
     * 
     * @return a Slide
     */
    public Slide getCurrent ();
}

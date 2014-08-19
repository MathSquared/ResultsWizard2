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

    /**
     * Sets the size of slides that will be returned from this Selector.
     * 
     * <p>
     * Some implementations may silently impose their own limitations on the parameters to this method. Thus, client code must not assume that the size of the slides to be returned will be precisely equal to what is passed to this method.
     * </p>
     * 
     * @param width the width of the slides to return, in pixels
     * @param height the height of the slides to return, in pixels
     */
    public void setSize (int width, int height);
}

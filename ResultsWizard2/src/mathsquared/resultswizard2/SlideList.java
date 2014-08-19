/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a list of {@link Slide}s. Subinterfaces will carry different types of metadata (e.g. {@linkplain EventResults event results}) that are represented by the slides.
 * 
 * @author MathSquared
 * 
 */
public interface SlideList extends List<Slide>, Serializable {
    /**
     * Modifies the Slides carried by this SlideList to display using the given width and height. This method can change any aspect of the slides, including their number, amount, and relative ordering; it is as if the SlideList was reinitialized from its raw data.
     * 
     * <p>
     * Implementors may impose their own restrictions on the possible arguments to this method.
     * </p>
     * 
     * @param width the width of the slides, in pixels
     * @param height the height of the slides, in pixels
     */
    public void renderSlides (int width, int height);
}

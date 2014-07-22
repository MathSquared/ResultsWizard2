/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Represents a list of {@link Slide}s representing the {@linkplain EventResults results} of a given event.
 * 
 * @author MathSquared
 * 
 */
public interface EventResultsSlideList extends SlideList {
    /**
     * Obtains the {@link EventResults} represented by this list of slides.
     * 
     * @return the full <code>EventResults</code> being represented by this SlideList
     */
    public EventResults getEventResults ();
}

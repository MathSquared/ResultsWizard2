/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Generates {@link Slide}s from {@link EventResults} in a consistent fashion.
 * 
 * @author MathSquared
 * 
 */
public interface SlideFactory {
    /**
     * Generates an array of {@link Slide}s representing the given event results, in the order that they should be projected.
     * 
     * @param er the {@link EventResults} for which to generate <code>Slide</code>s
     * @param width the width that these <code>Slides</code> should use to draw themselves
     * @param height the height that these <code>Slides</code> should use to draw themselves
     * @return an array of <code>Slide</code>s that, when projected in the order given, represent the given <code>EventResults</code>
     */
    public Slide[] generate (EventResults er, int width, int height);
}

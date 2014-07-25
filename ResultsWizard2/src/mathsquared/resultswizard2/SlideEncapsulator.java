/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Graphics;

/**
 * Encapsulates a Slide, hiding its inner workings. Specifically, hides all methods of the Slide except those specified by the {@linkplain Slide <code>Slide</code> interface}.
 * 
 * <p>
 * Note that if all access to the encapsulated Slide is managed by a SlideEncapsulator, the encapsulated Slide is effectively immutable (unless the <code>draw</code> method changes the encapsulated Slide's state). This is useful for {@link SlideList} implementations, which may not want their Slides to be tampered with.
 * </p>
 * 
 * @author MathSquared
 * 
 */
public class SlideEncapsulator implements Slide {
    Slide innards;

    /**
     * Constructs a SlideEncapsulator that encapsulates the given Slide.
     * 
     * @param innards the Slide to be encapsulated
     */
    public SlideEncapsulator (Slide innards) {
        this.innards = innards;
    }

    public void draw (Graphics g) {
        innards.draw(g);
    }
}

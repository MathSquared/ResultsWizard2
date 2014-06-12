/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Graphics;

/**
 * Represents an object capable of drawing itself.
 * 
 * Normally, an implementation of this object would be obtained from another class that would parse client data into a series of Slides.
 * 
 * @author MathSquared
 * 
 */
public interface Slide {
    /**
     * Draws this Slide's data to the given <code>Graphics</code> object.
     * 
     * @param g the {@link Graphics} to which to draw this Slide
     */
    public void draw (Graphics g);
}

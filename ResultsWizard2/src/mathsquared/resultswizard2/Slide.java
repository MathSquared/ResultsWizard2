/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Graphics2D;
import java.io.Serializable;

/**
 * Represents an object capable of drawing itself.
 * 
 * Normally, an implementation of this object would be obtained from another class that would parse client data into a series of Slides.
 * 
 * @author MathSquared
 * 
 */
public interface Slide extends Serializable {
    /**
     * Draws this Slide's data to the given <code>Graphics2D</code> object.
     * 
     * @param g the {@link Graphics2D} to which to draw this Slide
     */
    public void draw (Graphics2D g);
}

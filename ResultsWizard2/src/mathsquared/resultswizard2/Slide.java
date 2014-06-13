/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Graphics;
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
    // Obtained from serialver
    public static final long serialVersionUID = 3343226026369768497L;

    /**
     * Draws this Slide's data to the given <code>Graphics</code> object.
     * 
     * @param g the {@link Graphics} to which to draw this Slide
     */
    public void draw (Graphics g);
}

/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Graphics;

/**
 * Represents a Slide that can be built up with several elements until it is full.
 * 
 * @author MathSquared
 * 
 */
public class BuildableStackedSlide implements Slide {
    private int width;
    private int height;
    private ColorScheme color;

    public BuildableStackedSlide (int width, int height, ColorScheme color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw (Graphics g) {
        // TODO
    }
}

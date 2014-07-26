/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Contains utility methods for working with fonts.
 * 
 * @author MathSquared
 * 
 */
public class FontUtils {
    /**
     * Finds the smallest font size that will fit a string within a given width.
     * 
     * <p>
     * Specifically, returns a {@link Font} <code>ret</code> with a size less than its current size such that <code>getMetrics.getFontMetrics(ret).stringWidth(text) &lt;= desiredWidth</code>.
     * </p>
     * 
     * @param base the base font which should be shrunk (not modified)
     * @param text the string to fit within the given width
     * @param desiredWidth the maximum possible width of the text, in pixels
     * @param getMetrics a {@link Graphics} object which should be used to obtain {@link FontMetrics} for the given font
     * @return a Font identical to <code>base</code> except possibly with a different font size as above
     */
    public static Font shrinkFontForWidth (Font base, String text, int desiredWidth, Graphics getMetrics) {
        FontMetrics metrics = getMetrics.getFontMetrics(base);
        while (metrics.stringWidth(text) > desiredWidth) {
            base = base.deriveFont((float) base.getSize() - 1);
        }
        return base;
    }
}

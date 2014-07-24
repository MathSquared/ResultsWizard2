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
    public static Font shrinkFontForWidth (Font base, String text, int desiredWidth, Graphics getMetrics) {
        FontMetrics metrics = getMetrics.getFontMetrics(base);
        while (metrics.stringWidth(text) > desiredWidth) {
            base = base.deriveFont((float) base.getSize() - 1);
        }
        return base;
    }
}

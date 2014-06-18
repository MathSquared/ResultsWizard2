/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

import javax.swing.JOptionPane;

/**
 * Contains methods that allow a user to select a {@link GraphicsDevice} from several alternatives.
 * 
 * @author MathSquared
 * 
 */
public class GraphicsDeviceSelector {
    public static GraphicsDevice selectGraphicsDevice (GraphicsDevice[] gds) {
        // Create Strings
        String[] gdStrings = new String[gds.length];
        for (int i = 0; i < gds.length; i++) {
            // We use the index to convert the String from JOptionPane back into an index into gds in the future, and to make the user's choices unique
            gdStrings[i] = i + ": " + graphicsDeviceToString(gds[i]);
        }

        String selection = null;
        while (selection == null) {
            selection = (String) JOptionPane.showInputDialog(null, "Select a screen on which to display the projection", "Screen Selection", JOptionPane.PLAIN_MESSAGE, null, gdStrings, gdStrings[0]);
        }
        int index = Integer.parseInt(selection.split(":")[0]);
        return gds[index];
    }

    /**
     * Creates a String representation of a <code>GraphicsDevice</code>, including information about its current <code>DisplayMode</code>.
     * 
     * @param gd the {@link GraphicsDevice} to examine
     * @return a String in the format <code>"[ &lt;IDstring> ] &lt;width>x&lt;height>, &lt;bitDepth>-bit @ &lt;refreshRate> Hz"</code>, where <code>IDstring = gd.getIDstring()</code>, <code>width = gd.getDisplayMode().getWidth()</code>, etc. (All values are obtained from the {@link DisplayMode} except for the <code>IDstring</code>.)
     */
    public static String graphicsDeviceToString (GraphicsDevice gd) {
        DisplayMode dm = gd.getDisplayMode();
        return "[ " + gd.getIDstring() + " ] " + dm.getWidth() + "x" + dm.getHeight() + ", " + dm.getBitDepth() + "-bit @ " + dm.getRefreshRate() + " Hz";
    }
}

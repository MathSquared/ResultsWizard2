/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Represents a color scheme, containing several {@link Color} objects referenced by name.
 * 
 * <p>
 * Note that this object is immutable: any attempt to modify it, whether directly or through any of its views, will throw an {@link UnsupportedOperationException}.
 * </p>
 * 
 * @author MathSquared
 * 
 */
public class ColorScheme extends AbstractMap<String, Color> {
    private Set<Entry<String, Color>> ent;

    /**
     * Reads in details of a color scheme from a line-oriented text description.
     * 
     * <p>
     * The format for a color scheme file is exactly as specified in {@link Properties#load(Reader)}. The keys must be names of colors, and the values must be hexadecimal integers representing valid RGB colors, in the format <code>#RRGGBB</code> or <code>RRGGBB</code>. Color names are not currently supported.
     * </p>
     * 
     * @param read the {@link Reader} containing the color scheme description
     * @throws IOException if an I/O error occurs when reading the description
     * @throws NumberFormatException if a value is not a correctly formatted hexadecimal integer
     */
    public ColorScheme (Reader read) throws IOException, NumberFormatException {
        // Read in the file
        Properties prop = new Properties();
        prop.load(read);

        // Set up an entry set
        Set<Entry<String, Color>> ent2 = new HashSet<Entry<String, Color>>();
        for (String x : prop.stringPropertyNames()) {
            String color = prop.getProperty(x);
            if (color.charAt(0) == '#') { // Accommodate the #RRGGBB syntax as well as the RRGGBB syntax
                color = color.substring(1);
            }
            int col = Integer.parseInt(color, 16);
            ent2.add(new SimpleEntry<String, Color>(x, new Color(col)));
        }

        // Assign to the actual entry set--but unmodifiable
        ent = Collections.unmodifiableSet(ent2);
    }

    // Inherit documentation
    public Set<Entry<String, Color>> entrySet () {
        return ent;
    }
}

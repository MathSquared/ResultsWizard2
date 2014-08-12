/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Handles communication between client and server, including slide selection.
 * 
 * @author MathSquared
 * 
 */
public class ProtocolSelector implements Selector {
    private int width;
    private int height;

    private LinkedHashMap<String, Slide[]> slides; // stores the slides to display
    private String currentTag; // the current key in the map where to find the current slide
    private int currentIndex; // the index in the array given by currentTag

    public ProtocolSelector (int width, int height) {
        this.width = width;
        this.height = height;

        slides = new LinkedHashMap<String, Slide[]>();
    }

    public Slide getCurrent () {
        if (currentTag == null) {
            return null;
        }
        if (!slides.containsKey(currentTag)) {
            return null;
        }
        if (slides.size() == 0) {
            return null; // this also checks that locateNextString won't IOOBE due to no entries in the key set
        }

        // Create duplicate field values to mess with (to canonicalize what we return)
        String tag = currentTag;
        int idx = currentIndex;

        // Ensure idx is in bounds
        if (idx < 0) {
            idx = 0;
        }
        if (idx > slides.get(tag).length) {
            // Move to the next tag; continue while there are no slides in the current one
            String origTag = tag; // ensure we don't wrap around the keySet
            do {
                tag = locateNextString(tag);
            } while (slides.get(tag).length == 0 && origTag != tag);
            idx = 0;
        }

        return slides.get(tag)[idx];
    }

    private String locateNextString (String current) {
        LinkedList<String> keys = new LinkedList<String>();
        keys.addAll(slides.keySet());
        Iterator<String> iter = keys.iterator();

        // Iterate while we have entries until we skip over an entry equal to current
        while (iter.hasNext() && !iter.next().equals(current));

        // If we're out of entries, return the first one
        if (!iter.hasNext()) {
            return keys.get(0);
        }

        // Return the next string
        return iter.next();
    }
}

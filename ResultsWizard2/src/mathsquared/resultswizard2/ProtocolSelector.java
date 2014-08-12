/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Handles communication between client and server, including slide selection.
 * 
 * @author MathSquared
 * 
 */
public class ProtocolSelector implements Selector {
    private int width;
    private int height;
    private long cycleDelay; // amount of milliseconds to wait between cycles

    private LinkedHashMap<String, SlideList> slides; // stores the slides to display
    private String currentTag; // the current key in the map where to find the current slide
    private int currentIndex; // the index in the array given by currentTag
    private long lastCycle = 0;
    private boolean getCurrentDone; // whether we've done a getCurrent

    public ProtocolSelector (int width, int height, long cycleDelay) {
        this.width = width;
        this.height = height;
        this.cycleDelay = cycleDelay;

        slides = new LinkedHashMap<String, SlideList>();
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

        // Figure out when to cycle
        if (getCurrentDone) { // previous getCurent
            if (System.currentTimeMillis() - lastCycle > cycleDelay) { // enough time passed
                // Cycle
                currentIndex++;
                lastCycle = System.currentTimeMillis();
            } else { // not enough time passed
                // do nothing
            }
        } else { // our first getCurrent
            lastCycle = System.currentTimeMillis();
        }

        // Create duplicate field values to mess with (to canonicalize what we return)
        String tag = currentTag;
        int idx = currentIndex;

        // Ensure idx is in bounds
        if (idx < 0) {
            idx = 0;
        }
        if (idx > slides.get(tag).size()) {
            // Move to the next tag; continue while there are no slides in the current one
            String origTag = tag; // ensure we don't wrap around the keySet
            do {
                tag = locateNextString(tag);
            } while (slides.get(tag).size() == 0 && origTag != tag);
            idx = 0;
        }

        return slides.get(tag).get(idx);
    }

    public Command processMessage (Command msg) {
        switch (msg.getType()) {
        case POISON:
        case XMIT_ERROR_RESTART:
            currentTag = null;
            break;
        case ADD:
            slides.putAll(msg.getStringSlideListPayload());
            break;
        case REMOVE:
            slides.remove(msg.getStringPayload());
            break;
        case RETR_SLIDES:
            return new Command() {
                // Carry the slides along with the command
                private LinkedHashMap<String, SlideList> carriedSlides = new LinkedHashMap<String, SlideList>(slides);

                public Message getType () {
                    return Message.RESP_SLIDES;
                }

                public String getStringPayload () {
                    throw new UnsupportedOperationException("RESP_SLIDES does not carry a String payload");
                }

                public Map<String, SlideList> getStringSlideListPayload () {
                    return new LinkedHashMap<String, SlideList>(carriedSlides);
                }
            };
            // break; (unreachable)
        case TICKER:
            // TODO ticker not supported
            break;
        case RETR_TICKER:
            return new Command() {
                public Message getType () {
                    return Message.RESP_TICKER;
                }

                public String getStringPayload () {
                    return "";
                }

                public Map<String, SlideList> getStringSlideListPayload () {
                    throw new UnsupportedOperationException("RESP_TICKER does not carry a Map<String, SlideList> payload");
                }
            };
            // break; (unreachable)
        case RESP_SLIDES: // we don't expect these cases
        case RESP_TICKER:
            break;
        }

        // if we haven't already returned, nothing to send to the client
        return null;
    }

    private String locateNextString (String current) {
        LinkedList<String> keys = new LinkedList<String>();
        keys.addAll(slides.keySet());
        Iterator<String> iter = keys.iterator();

        // Check if currentTag is null while there are actual entries
        if (currentTag == null) {
            if (keys.size() == 0) {
                return null;
            } else { // there are keys
                return keys.get(0);
            }
        }

        // Iterate while we have entries until we skip over an entry equal to current
        while (iter.hasNext() && !iter.next().equals(current));

        // If we're out of entries, return the first one
        if (!iter.hasNext()) {
            return keys.get(0);
        }

        // Return the next string
        return iter.next();
    }

    private void canonicalizeIndexing () {
        // Ensure currentIndex is in bounds
        if (currentIndex < 0) {
            currentIndex = 0;
        }
        if (currentIndex > slides.get(currentTag).size()) {
            // Move to the next tag; continue while there are no slides in the current one
            String origcurrentTag = currentTag; // ensure we don't wrap around the keySet
            do {
                currentTag = locateNextString(currentTag);
            } while (slides.get(currentTag).size() == 0 && origcurrentTag != currentTag);
            currentIndex = 0;
        }
    }
}

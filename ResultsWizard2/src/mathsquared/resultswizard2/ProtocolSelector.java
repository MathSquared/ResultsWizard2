/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Collection;
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
public class ProtocolSelector implements Selector, CommandProcessor {
    private int width;
    private int height;
    private long cycleDelay; // amount of milliseconds to wait between cycles

    private LinkedHashMap<String, SlideList> slides; // stores the slides to display
    private String currentTag; // the current key in the map where to find the current slide
    private int currentIndex; // the index in the array given by currentTag
    private long lastCycle = 0; // millisecond time of the last cycle
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
                canonicalizeIndexing();

                // Only update lastCycle if we actually cycled to a slide as opposed to emptiness
                if (currentTag != null) {
                    lastCycle = System.currentTimeMillis();
                }
            } else { // not enough time passed
                // do nothing
            }
        } else { // our first getCurrent
            // Start cycling when we first retrieve a slide
            lastCycle = System.currentTimeMillis();
            getCurrentDone = true;
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

    public void setSize (int w, int h) {
        width = w;
        height = h;
        slides = renderAllToSize(w, h, slides);

        // Because rendering messes with slide ordering within SlideLists, return to the beginning of this tag
        currentIndex = 0;
    }

    public Command processMessage (Command msg) {
        switch (msg.getType()) {
        case POISON:
        case XMIT_ERROR_RESTART:
            currentTag = null;
            break;
        case ADD:
            slides.putAll(renderAllToSize(width, height, msg.getStringSlideListPayload()));
            break;
        case REMOVE:
            slides.remove(msg.getStringPayload());
            break;
        case RETR_SLIDES:
            // return new Command() {
            // // Carry the slides along with the command
            // private LinkedHashMap<String, SlideList> carriedSlides = new LinkedHashMap<String, SlideList>(slides);
            //
            // public Message getType () {
            // return Message.RESP_SLIDES;
            // }
            //
            // public String getStringPayload () {
            // throw new UnsupportedOperationException("RESP_SLIDES does not carry a String payload");
            // }
            //
            // public Map<String, SlideList> getStringSlideListPayload () {
            // return new LinkedHashMap<String, SlideList>(carriedSlides);
            // }
            // };
            return new SimpleCommand(Message.RESP_SLIDES, null, slides);
            // break; (unreachable)
        case TICKER:
            // TODO ticker not supported
            break;
        case RETR_TICKER:
            // return new Command() {
            // public Message getType () {
            // return Message.RESP_TICKER;
            // }
            //
            // public String getStringPayload () {
            // return "";
            // }
            //
            // public Map<String, SlideList> getStringSlideListPayload () {
            // throw new UnsupportedOperationException("RESP_TICKER does not carry a Map<String, SlideList> payload");
            // }
            // };
            return new SimpleCommand(Message.RESP_TICKER, "", null);
            // break; (unreachable)
        case RESP_SLIDES: // we don't expect these cases
        case RESP_TICKER:
            break;
        }

        // if we haven't already returned, nothing to send to the client
        return null;
    }

    /**
     * Returns the String key in <code>slides</code> next in iteration order after the given one. If we're at the last entry, or <code>slides</code> does not contain the given key, returns the first key. If there are no keys, returns null.
     * 
     * @param current the current String
     * @return the String after it in <code>slides</code>'s iteration order
     */
    private String locateNextString (String current) {
        LinkedList<String> keys = new LinkedList<String>();
        keys.addAll(slides.keySet());
        Iterator<String> iter = keys.iterator();

        // Null sanity check
        if (keys.size() == 0) {
            return null;
        }

        // Check if current is null while there are actual entries
        if (current == null) {
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

    /**
     * Ensure that currentTag and currentIndex point to a valid entry. If they don't, manipulate them so that they are so.
     * 
     * <p>
     * Specifically, ensures that <code>currentIndex</code> points to a valid index within <code>slides.get(currentTag)</code>. If the index is negative, makes it zero; if the index is positive, sets it to 0 and sets <code>currentTag</code> to the next entry with a non-zero amount of slides.
     * </p>
     */
    private void canonicalizeIndexing () {
        // If no slides, set to null and 0 (not needed in getCurrent because of size check earlier in the method)
        if (slides.size() == 0) {
            currentTag = null;
            currentIndex = 0;
        }

        // Handle keys that don't exist in slides by setting to first slide
        if (!slides.containsKey(currentTag)) {
            currentTag = locateNextString(currentTag); // this sets to the first string, or null if there are no strings
            currentIndex = 0;
        }

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

    /**
     * Renders a collection of SlideLists such that all of their Slides display using the given width and height. Note that this method modifies the SlideLists referenced by <code>sl</code>.
     * 
     * @param w the width at which to display the slides, in pixels
     * @param h the height at which to display the slides, in pixels
     * @param sl a collection of the SlideLists on which to operate
     * @see SlideList#renderSlides(int, int)
     */
    private void renderAllToSize (int w, int h, Collection<SlideList> sl) {
        for (SlideList x : sl) {
            x.renderSlides(w, h);
        }
    }

    /**
     * Returns a new Map whose SlideLists have been manipulated so as to render using the given width and height. Note that this method modifies the SlideLists referenced by <code>sl</code>.
     * 
     * @param w the width at which to display the slides, in pixels
     * @param h the height at which to display the slides, in pixels
     * @param sl a map of the SlideLists on which to operate; the keys are irrelevent
     * @return a new Map, containing the same elements as <code>sl</code> in the same order and containing the SlideLists rendered to size
     */
    private LinkedHashMap<String, SlideList> renderAllToSize (int w, int h, Map<String, SlideList> sl) {
        LinkedHashMap<String, SlideList> ret = new LinkedHashMap<String, SlideList>(sl);
        renderAllToSize(w, h, ret.values());
        return ret;
    }
}

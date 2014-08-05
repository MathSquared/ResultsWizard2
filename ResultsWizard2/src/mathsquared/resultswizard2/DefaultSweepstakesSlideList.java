/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * A simple implementation of {@link SweepstakesSlideList}.
 * 
 * <p>
 * The {@link ColorScheme} keys used by this Slide are:
 * </p>
 * 
 * <ul>
 * </ul>
 * 
 * @author MathSquared
 * 
 */
public class DefaultSweepstakesSlideList implements SweepstakesSlideList {
    private int width;
    private int height;
    private ColorScheme color;
    private int baseFont; // the base font size
    private HashMap<String, Fraction> sweeps; // raw; this is sorted in the rendering method
    private int displayCap; // maximum amount of sweeps winners to display

    private ArrayList<Slide> slides;

    // Layout constants
    public static final int TOP_MARGIN = 20;
    public static final int BEFORE_RES_TYPE = 10; // before/after the "INDIVIDUAL RESULTS" etc. headers
    public static final int AFTER_RES_TYPE = 10; // also used after a special honor name
    public static final int BETWEEN_TIES = 2;

    public static final Color transparent = new Color(0, 0, 0, 0);

    public static final String DATE_FORMAT = "E M/d 'at' h:mm a z"; // Definitely not intended for long-term usage.
    private String date; // initialized in the constructor to the time of generation, formatted according to DATE_FORMAT

    // Define the fonts
    public static final String FONT_FACE = "SansSerif";
    private Font base; // used for most text--competitor names, etc.
    private Font head; // used for the heading
    public static final int HEAD_MULT = 3; // evaluate baseFont * HEAD_MULT / HEAD_DIV for the head font size
    public static final int HEAD_DIV = 2;
    public static final int HEAD_STYLE = Font.BOLD;
    private Font subhead; // used for subheadings, e.g. subsections within the larger results
    public static final int SUBHEAD_MULT = 1;
    public static final int SUBHEAD_DIV = 1;
    public static final int SUBHEAD_STYLE = Font.BOLD;
    private Font number; // used for numbers (places, sweeps)
    public static final int NUMBER_MULT = 1;
    public static final int NUMBER_DIV = 1;
    public static final int NUMBER_STYLE = Font.BOLD | Font.ITALIC;
    private Font smalltext; // used for things like "page _ of _ for this event"
    public static final int SMALLTEXT_MULT = 1;
    public static final int SMALLTEXT_DIV = 2;
    public static final int SMALLTEXT_STYLE = Font.PLAIN;

    /**
     * Initializes a new DefaultSweepstakesSlideList with the given data. The sweepstakes will be displayed in descending order by point total.
     * 
     * @param width the width of the slides, in pixels
     * @param height the height of the slides, in pixels
     * @param color a {@link ColorScheme} with the colors to use (see the class description for which keys are used)
     * @param baseFont the base font size, in points (this is the size of normal text; some fonts multiply this base size by fixed constants)
     * @param sweeps the sweepstakes data that this DefaultSweepstakesSlideList should represent
     * @param displayCap the maximum amount of sweepstakes winners to display; the actual amount displayed will be the size of <code>sweeps</code> or the value of this parameter, whichever is lesser
     */
    public DefaultSweepstakesSlideList (int width, int height, ColorScheme color, int baseFont, Map<String, Fraction> sweeps, int displayCap) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.baseFont = baseFont;
        this.sweeps = new HashMap<String, Fraction>(sweeps); // descending--highest schools at the top
        this.displayCap = displayCap;

        slides = new ArrayList<Slide>();

        // Initialize the fonts
        base = new Font(FONT_FACE, baseFont, Font.PLAIN);
        head = new Font(FONT_FACE, baseFont * HEAD_MULT / HEAD_DIV, HEAD_STYLE);
        subhead = new Font(FONT_FACE, baseFont * SUBHEAD_MULT / SUBHEAD_DIV, SUBHEAD_STYLE);
        number = new Font(FONT_FACE, baseFont * NUMBER_MULT / NUMBER_DIV, NUMBER_STYLE);
        smalltext = new Font(FONT_FACE, baseFont * SMALLTEXT_MULT / SMALLTEXT_DIV, SMALLTEXT_STYLE);

        // Get the update timestamp
        Calendar rightNow = Calendar.getInstance(); // default timezone
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        fmt.setCalendar(rightNow); // overwrites values such as time zone
        date = fmt.format(rightNow.getTime()); // getTime converts to Date, since SDF doesn't recognize Calendar properly

        renderSlides(width, height);
    }

    // public in case the screen size changes
    /**
     * Changes this DefaultSweepstakesSlideList to expose Slides that display using the given width and height.
     * 
     * <p>
     * Note that the slides represented by this DefaultSweepstakesSlideList may completely change after calling this method. In particular, this implementation completely regenerates all of its Slides according to the new dimensions.
     * </p>
     * 
     * @param width the new width of the slides, in pixels
     * @param height the new height of the slides, in pixels
     */
    public void renderSlides (int width, int height) {
        // Update instance variables
        this.width = width;
        this.height = height;

        // Discard old slides
        slides.clear();

        // Since slides is an ArrayList<Slide> and not <BuildableStackedSlide>...
        // (this is so we can make the slides returned to the client immutable with SlideEncapsulator)
        ArrayList<BuildableStackedSlide> workingSlides = new ArrayList<BuildableStackedSlide>();

        BuildableStackedSlide sl = createNewSkeletalSlide();
        workingSlides.add(sl);
        List<BuildableStackedSlide> surplus;
    }

    public Map<String, Fraction> getSweeps () {
        return new LinkedHashMap<String, Fraction>(sweeps);
    }

    // SLIDE GENERATION METHODS //

    /**
     * Creates a new {@link BuildableStackedSlide} that carries the event title and a default timestamp in the headers. The default timestamp is assigned {@linkplain BuildableStackedSlide#updatable() update} ID 0.
     * 
     * <p>
     * The headers are committed and pushed.
     * </p>
     * 
     * @return the newly generated slide
     */
    private BuildableStackedSlide createNewSkeletalSlide () {
        BuildableStackedSlide ret = new BuildableStackedSlide(width, height);
        ret.addSpacer(TOP_MARGIN);

        Color evtTitleColor = (color.containsKey("evtTitle")) ? color.get("evtTitle") : Color.black;
        ret.addText(">> CURRENT SWEEPSTAKES", head, evtTitleColor, false);

        Color timestampColor = (color.containsKey("timestamp")) ? color.get("timestamp") : new Color(0x666666);
        ret.addText("estimated sweepstakes totals", smalltext, timestampColor, false);
        ret.updatable(); // this will always be 0; see updatable Javadoc

        // Add a spacer, since we don't use result types like DERSL
        ret.addSpacer(AFTER_RES_TYPE);

        ret.commit(); // Propagate to top buffer
        ret.push();

        return ret;
    }

    private List<BuildableStackedSlide> forceAddTie (BuildableStackedSlide sl, Map<String, Fraction> toAdd, int index) {
        // this holds a list of all slides used
        ArrayList<BuildableStackedSlide> ret = new ArrayList<BuildableStackedSlide>();
        ret.add(sl);

        Color placeNumColor = (color.containsKey("placeNum")) ? color.get("placeNum") : new Color(0x444444);
        Color honoreeColor = (color.containsKey("honoree")) ? color.get("honoree") : new Color(0x222222);
        Color sweepsColor = (color.containsKey("sweeps")) ? color.get("sweeps") : new Color(0x666666);

        String plStr = Integer.toString(index + 1);

        Color placeNumColorCur = placeNumColor; // assigned to transparent if we want the place num. to be invisible on a certain line
        // we don't simply overwrite placeNumColor as in tryAddTie because we might need to reenable the place number if we start a new slide

        // Find the length of this tie
        int tieLength = checkTieLength(toAdd, index);
        if (tieLength == 0) { // nothing to add
            return ret;
        }

        List<Map.Entry<String, Fraction>> entries = new LinkedList<Map.Entry<String, Fraction>>(toAdd.entrySet());
        ListIterator<Map.Entry<String, Fraction>> iter = entries.listIterator(index);

        for (int i = 0; i < tieLength; i++) {
            Map.Entry<String, Fraction> cur = iter.next();

            // Generate a sweeps string for this school
            String swStr = String.format("%.2f", cur.getValue().toDouble()); // Two decimals
            if (swStr.endsWith(".00")) { // Chop off the last two digits if they're .00
                swStr = swStr.substring(0, swStr.length() - ".00".length());
            }

            boolean addSucceeded = sl.addThreeText(plStr, number, placeNumColorCur, cur.getKey(), base, honoreeColor, swStr, number, sweepsColor);

            if (!addSucceeded) {
                // Undo and restart on a new slide
                sl.undo();
                sl.commit();
                sl.push(); // ensure that the partial sequence actually renders on the previous slide
                sl = createNewSkeletalSlide();
                ret.add(sl);

                // Add it again (placeNumColor instead of placeNumColorCur because this is the first row of the new slide)
                sl.addThreeText(plStr, number, placeNumColor, cur.getKey(), base, honoreeColor, swStr, number, sweepsColor);
            }

            // Only add the place number once; overwrite the color for subsequent iterations (transparent so that subsequent entries still line up)
            // (if we just started a new slide, it would have been taken care of above)
            placeNumColorCur = transparent;
        }

        // All is well in the universe
        sl.addSpacer(BETWEEN_TIES); // we don't really care if this works
        sl.commit();
        sl.push(); // we know for certain we want what we just authored on the slide
        return ret;
    }

    private List<BuildableStackedSlide> forceAddList (BuildableStackedSlide sl, Map<String, Fraction> toAdd) {
        List<BuildableStackedSlide> ret = new ArrayList<BuildableStackedSlide>();
        ret.add(sl);

        int index = 0;
        index = checkTieLength(toAdd, index);
        List<BuildableStackedSlide> surplus = forceAddTie(sl, toAdd, 0);

        // Add the surplus
        if (surplus.size() > 1) {
            ret.addAll(surplus.subList(1, surplus.size()));
            sl = surplus.get(surplus.size() - 1);
        }

        // TODO add the rest

        return ret;
    }

    /**
     * Finds the length of a run of identical list values. This checks the Map in iteration order and returns the number of entries, starting at <code>index</code>, with the same value as the entry at <code>index</code>.
     * 
     * <p>
     * The entry at a given index <code>i</code> is defined as the entry found by obtaining the iterator into the map's entry set and calling <code>next</code> <code>i + 1</code> times, saving the result of the last call.
     * </p>
     * 
     * @param toCheck the Map to check for ties
     * @param index the index at which to start looking for ties
     * @return the number of elements at or after <code>index</code> whose values compare as equal; 0 if we have reached the end of the entry set
     * @throws IndexOutOfBoundsException if <code>index</code> is less than 0 or greater than <code>toCheck.size()</code>
     */
    private int checkTieLength (Map<String, Fraction> toCheck, int index) {
        List<Map.Entry<String, Fraction>> entries = new LinkedList<Map.Entry<String, Fraction>>(toCheck.entrySet());
        ListIterator<Map.Entry<String, Fraction>> iter = entries.listIterator(index);
        if (!iter.hasNext()) {
            return 0; // no next elements -> no ties
        }

        int currentLength = 1;
        Fraction firstMember = iter.next().getValue();
        while (iter.hasNext() && iter.next().equals(firstMember)) {
            currentLength++;
        }

        return currentLength;
    }

    // IMPLEMENT LIST //

    // These methods make this an immutable List<Slide> backed by slides.

    @Override
    public int size () {
        return slides.size();
    }

    @Override
    public boolean isEmpty () {
        return slides.isEmpty();
    }

    @Override
    public boolean contains (Object o) {
        return slides.contains(o);
    }

    @Override
    public Iterator<Slide> iterator () {
        final Iterator<Slide> it = slides.iterator();
        return new Iterator<Slide>() {
            public Slide next () {
                return it.next();
            }

            public boolean hasNext () {
                return it.hasNext();
            }

            public void remove () {
                throw new UnsupportedOperationException("This List is immutable; operation iterator().remove not supported");
            }
        };
    }

    @Override
    public Object[] toArray () {
        return slides.toArray();
    }

    @Override
    public <T> T[] toArray (T[] a) {
        return slides.toArray(a);
    }

    @Override
    public boolean add (Slide e) {
        throw new UnsupportedOperationException("This List is immutable; operation add not supported");
    }

    @Override
    public boolean remove (Object o) {
        throw new UnsupportedOperationException("This List is immutable; operation remove not supported");
    }

    @Override
    public boolean containsAll (Collection<?> c) {
        return slides.containsAll(c);
    }

    @Override
    public boolean addAll (Collection<? extends Slide> c) {
        throw new UnsupportedOperationException("This List is immutable; operation addAll not supported");
    }

    @Override
    public boolean addAll (int index, Collection<? extends Slide> c) {
        throw new UnsupportedOperationException("This List is immutable; operation addAll not supported");
    }

    @Override
    public boolean removeAll (Collection<?> c) {
        throw new UnsupportedOperationException("This List is immutable; operation removeAll not supported");
    }

    @Override
    public boolean retainAll (Collection<?> c) {
        throw new UnsupportedOperationException("This List is immutable; operation retainAll not supported");
    }

    @Override
    public void clear () {
        throw new UnsupportedOperationException("This List is immutable; operation clear not supported");
    }

    @Override
    public Slide get (int index) {
        return slides.get(index);
    }

    @Override
    public Slide set (int index, Slide element) {
        throw new UnsupportedOperationException("This List is immutable; operation set not supported");
    }

    @Override
    public void add (int index, Slide element) {
        throw new UnsupportedOperationException("This List is immutable; operation add not supported");
    }

    @Override
    public Slide remove (int index) {
        throw new UnsupportedOperationException("This List is immutable; operation remove not supported");
    }

    @Override
    public int indexOf (Object o) {
        return slides.indexOf(o);
    }

    @Override
    public int lastIndexOf (Object o) {
        return slides.lastIndexOf(o);
    }

    @Override
    public ListIterator<Slide> listIterator () {
        final ListIterator<Slide> it = slides.listIterator();
        return new ListIterator<Slide>() {

            @Override
            public boolean hasNext () {
                return it.hasNext();
            }

            @Override
            public Slide next () {
                return it.next();
            }

            @Override
            public boolean hasPrevious () {
                return it.hasPrevious();
            }

            @Override
            public Slide previous () {
                return it.previous();
            }

            @Override
            public int nextIndex () {
                return it.nextIndex();
            }

            @Override
            public int previousIndex () {
                return it.previousIndex();
            }

            @Override
            public void remove () {
                throw new UnsupportedOperationException("This List is immutable; operation listIterator().remove not supported");
            }

            @Override
            public void set (Slide e) {
                throw new UnsupportedOperationException("This List is immutable; operation listIterator().set not supported");
            }

            @Override
            public void add (Slide e) {
                throw new UnsupportedOperationException("This List is immutable; operation listIterator().add not supported");
            }

        };
    }

    @Override
    public ListIterator<Slide> listIterator (int index) {
        final ListIterator<Slide> it = slides.listIterator(index);
        return new ListIterator<Slide>() {

            @Override
            public boolean hasNext () {
                return it.hasNext();
            }

            @Override
            public Slide next () {
                return it.next();
            }

            @Override
            public boolean hasPrevious () {
                return it.hasPrevious();
            }

            @Override
            public Slide previous () {
                return it.previous();
            }

            @Override
            public int nextIndex () {
                return it.nextIndex();
            }

            @Override
            public int previousIndex () {
                return it.previousIndex();
            }

            @Override
            public void remove () {
                throw new UnsupportedOperationException("This List is immutable; operation listIterator(int).remove not supported");
            }

            @Override
            public void set (Slide e) {
                throw new UnsupportedOperationException("This List is immutable; operation listIterator(int).set not supported");
            }

            @Override
            public void add (Slide e) {
                throw new UnsupportedOperationException("This List is immutable; operation listIterator(int).add not supported");
            }

        };
    }

    @Override
    public List<Slide> subList (int fromIndex, int toIndex) {
        List<Slide> rawSub = slides.subList(fromIndex, toIndex);
        return Collections.unmodifiableList(rawSub);
    }
}

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

        // TODO magic
    }

    public Map<String, Fraction> getSweeps () {
        return new LinkedHashMap<String, Fraction>(sweeps);
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

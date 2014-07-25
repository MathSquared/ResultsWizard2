/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple implementation of {@link EventResultsSlideList}.
 * 
 * <p>
 * The {@link ColorScheme} keys used by this Slide are:
 * </p>
 * 
 * <ul>
 * <li><code>evtTitle</code> for the title of the event (default: black)</li>
 * <li><code>resType</code> for the type of results (e.g. "INDIVIDUAL RESULTS") (default: black)</li>
 * </ul>
 * 
 * @author MathSquared
 * 
 */
public class DefaultEventResultsSlideList implements EventResultsSlideList {
    private Graphics g;
    private int width;
    private int height;
    private ColorScheme color;
    private int baseFont; // the base font size
    private EventResults evr;

    private ArrayList<Slide> slides; // holds the slides

    // Layout constants
    public static final int TOP_MARGIN = 20;
    public static final int BEFORE_RES_TYPE = 10; // before/after the "INDIVIDUAL RESULTS" etc. headers
    public static final int AFTER_RES_TYPE = 10;

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

    public DefaultEventResultsSlideList (Graphics g, int width, int height, ColorScheme color, int baseFont, EventResults evr) {
        this.g = g;
        this.width = width;
        this.height = height;
        this.color = color;
        this.baseFont = baseFont;
        this.evr = evr;

        slides = new ArrayList<Slide>();

        // Initialize the fonts
        base = new Font(FONT_FACE, baseFont, Font.PLAIN);
        head = new Font(FONT_FACE, baseFont * HEAD_MULT / HEAD_DIV, HEAD_STYLE);
        subhead = new Font(FONT_FACE, baseFont * SUBHEAD_MULT / SUBHEAD_DIV, SUBHEAD_STYLE);
        number = new Font(FONT_FACE, baseFont * NUMBER_MULT / NUMBER_DIV, NUMBER_STYLE);
        smalltext = new Font(FONT_FACE, baseFont * SMALLTEXT_MULT / SMALLTEXT_DIV, SMALLTEXT_STYLE);
    }

    public EventResults getEventResults () {
        return new EventResults(evr);
    }

    // SLIDE GENERATION METHODS //

    private BuildableStackedSlide createNewSkeletalSlide () {
        BuildableStackedSlide ret = new BuildableStackedSlide(width, height);
        ret.addSpacer(TOP_MARGIN);

        Color evtTitleColor = (color.containsKey("evtTitle")) ? color.get("evtTitle") : Color.black;
        ret.addText(evr.getEvent().getPrimaryName().toUpperCase(), head, evtTitleColor, false);

        ret.commit(); // Propagate to top buffer
        ret.push();

        return ret;
    }

    private boolean addResType (BuildableStackedSlide sl, String resType) {
        boolean ret = sl.addSpacer(BEFORE_RES_TYPE);

        Color resTypeColor = (color.containsKey("resType")) ? color.get("resType") : Color.black;
        ret &= sl.addText(resType.toUpperCase(), subhead, resTypeColor, true);

        ret &= sl.addSpacer(AFTER_RES_TYPE);

        sl.commit();
        sl.push();
        
        return ret;
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

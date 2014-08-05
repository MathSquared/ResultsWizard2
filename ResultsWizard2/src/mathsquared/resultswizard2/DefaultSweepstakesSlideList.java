/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.ArrayList;
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

        // TODO render
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

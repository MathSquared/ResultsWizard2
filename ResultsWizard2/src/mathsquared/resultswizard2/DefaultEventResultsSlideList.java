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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A simple implementation of {@link EventResultsSlideList}.
 * 
 * <p>
 * The {@link ColorScheme} keys used by this Slide are:
 * </p>
 * 
 * <ul>
 * <li><code>evtTitle</code> for the title of the event (default: black)</li>
 * <li><code>timestamp</code> for the line containing the page number and last updated time (default: #666666)</li>
 * <li><code>resType</code> for the type of results (e.g. "INDIVIDUAL RESULTS") (default: black)</li>
 * <li><code>honorName</code> for the names of special honors (default: #222222)</li>
 * <li><code>placeNum</code> for the place number (default: #444444)</li>
 * <li><code>honoree</code> for the names of recognized individuals (for team awards, schools get this color as well) (default: #222222)</li>
 * <li><code>school</code> for the names of the schools of individual award winners (default: #333333)</li>
 * <li><code>sweeps</code> for the amount of sweepstakes points earned for a given placing (default: #666666)</li>
 * </ul>
 * 
 * @author MathSquared
 * 
 */
public class DefaultEventResultsSlideList implements EventResultsSlideList {
    private int width;
    private int height;
    private ColorScheme color;
    private int baseFont; // the base font size
    private EventResults evr;

    private ArrayList<Slide> slides; // holds the slides

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
     * Initializes a new DefaultEventResultsSlideList with the given data.
     * 
     * <p>
     * After calling this method, the slides are initialized as if by calling <code>{@link #renderSlides(int, int) renderSlides}(width, height)</code>.
     * </p>
     * 
     * <p>
     * This implementation exposes Slides that carry a timestamp. This timestamp is set by this constructor.
     * </p>
     * 
     * @param width the width of the slides, in pixels
     * @param height the height of the slides, in pixels
     * @param color a {@link ColorScheme} with the colors to use (see the class description for which keys are used)
     * @param baseFont the base font size, in points (this is the size of normal text; some fonts multiply this base size by fixed constants)
     * @param evr the {@link EventResults} that this DefaultEventResultsSlideList should represent
     */
    public DefaultEventResultsSlideList (int width, int height, ColorScheme color, int baseFont, EventResults evr) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.baseFont = baseFont;
        this.evr = new EventResults(evr);

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
     * Changes this DefaultEventResultsSlideList to expose Slides that display using the given width and height.
     * 
     * <p>
     * Note that the slides represented by this DefaultEventResultsSlideList may completely change after calling this method. In particular, this implementation completely regenerates all of its Slides according to the new dimensions.
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

        // For each slide, we only generate it if there are actually results to display.
        // This avoids generating empty slides that waste projection time.

        // Define these upfront (we can't define them at first use in the individual results block, lest they go out of scope)
        BuildableStackedSlide sl;
        List<BuildableStackedSlide> surplus;

        if (evr.getEvent().getIndivPlaces() > 0) {
            // Generate the first slide and initialize it for individual results
            sl = createNewSkeletalSlide();
            workingSlides.add(sl);
            addResType(sl, "INDIVIDUAL RESULTS");
            // Populate the slide (and possibly generate new ones)
            surplus = forceAddList(sl, "INDIVIDUAL RESULTS CONT.", null, evr.getIndivHonorees(), evr.getIndivSchools(), evr.computeIndivSweeps(true));
            if (surplus.size() > 1) { // extra slides generated
                workingSlides.addAll(surplus.subList(1, surplus.size()));
            }
        }

        if (evr.getEvent().getTeamPlaces() > 0) {
            // Generate a team results slide
            sl = createNewSkeletalSlide();
            workingSlides.add(sl);
            addResType(sl, "TEAM RESULTS");
            // Populate
            surplus = forceAddList(sl, "TEAM RESULTS CONT.", null, evr.getTeamHonorees(), null, evr.computeTeamSweeps());
            if (surplus.size() > 1) { // extra slides generated
                workingSlides.addAll(surplus.subList(1, surplus.size()));
            }
        }

        // Special honors will be more complicated.

        if (evr.getEvent().getSpecialHonors().size() > 0) {
            // Generate a special honors slide
            sl = createNewSkeletalSlide();
            workingSlides.add(sl);
            addResType(sl, "SPECIAL HONORS");
            // Find a sorted set of special honors
            SortedSet<String> honors = new TreeSet<String>(evr.getEvent().getSpecialHonors().keySet());
            // Iterate through the special honors and add them
            for (String hon : honors) {
                String[][] honorees = evr.getSpecialHonorees().get(hon);
                String[][] schools = evr.getSpecialSchools().get(hon);

                // Add an honor name, and commit but don't push (that way, the header will be reverted by tryAddList if it doesn't fit)
                addHonorName(sl, hon);
                sl.commit();

                // Try; if that doesn't work, commit, push, create new, and force
                boolean addSucceeded = tryAddList(sl, honorees, schools, evr.computeSpecialSweeps(hon, true));
                if (!addSucceeded) {
                    // Already reverted; just commit/push and then make a new slide
                    sl.commit();
                    sl.push();

                    sl = createNewSkeletalSlide();
                    workingSlides.add(sl);
                    addResType(sl, "SPECIAL HONORS CONT.");
                    addHonorName(sl, hon + " cont.");
                    sl.commit();

                    // Add the new stuff
                    surplus = forceAddList(sl, "SPECIAL HONORS CONT.", hon + " cont.", honorees, schools, evr.computeSpecialSweeps(hon, true));
                    if (surplus.size() > 1) { // extra slides generated
                        workingSlides.addAll(surplus.subList(1, surplus.size()));

                        // Ensure sl always points to the most recent slide
                        sl = surplus.get(surplus.size() - 1);
                    }
                }
            }
        }
        // Update the slides
        int totNumSlides = workingSlides.size();

        for (int i = 0; i < workingSlides.size(); i++) {
            String toUpdate = String.format("page %d of %d for this event; last updated %s", i + 1, totNumSlides, date);
            workingSlides.get(i).update(0, toUpdate); // first updatable request for each slide, so we know it's 0 (see Javadoc)
        }

        // Update the slides list; make the contained slides immutable (this is where SlideEncapsulator comes in)
        // slides is already cleared
        for (Slide x : workingSlides) {
            slides.add(new SlideEncapsulator(x));
        }
    }

    public EventResults getEventResults () {
        return new EventResults(evr);
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
        ret.addText(evr.getEvent().getPrimaryName().toUpperCase(), head, evtTitleColor, false);

        Color timestampColor = (color.containsKey("timestamp")) ? color.get("timestamp") : new Color(0x666666);
        ret.addText("partial event results", smalltext, timestampColor, false);
        ret.updatable(); // this will always be 0; see updatable Javadoc

        ret.commit(); // Propagate to top buffer
        ret.push();

        return ret;
    }

    /**
     * Adds a block of text representing the type of results represented on a given slide.
     * 
     * <p>
     * The header is committed and pushed.
     * </p>
     * 
     * @param sl the {@link BuildableStackedSlide} to which the header should be added
     * @param resType the header that should be added
     * @return false if the addition overflowed the slide (the headers are still added)
     */
    private boolean addResType (BuildableStackedSlide sl, String resType) {
        boolean ret = sl.addSpacer(BEFORE_RES_TYPE);

        Color resTypeColor = (color.containsKey("resType")) ? color.get("resType") : Color.black;
        ret &= sl.addText(resType.toUpperCase(), subhead, resTypeColor, true);

        ret &= sl.addSpacer(AFTER_RES_TYPE);

        sl.commit();
        sl.push();

        return ret;
    }

    /**
     * Adds a block of text representing the name of a special honor to a given slide.
     * 
     * <p>
     * The header is committed.
     * </p>
     * 
     * @param sl the {@link BuildableStackedSlide} to which the header should be added
     * @param honorName the header that should be added
     * @return false if the addition overflowed the slide (the header is still added)
     */
    private boolean addHonorName (BuildableStackedSlide sl, String honorName) {
        Color honorNameColor = (color.containsKey("honorName")) ? color.get("honorName") : new Color(0x222222);
        boolean ret = sl.addText(honorName, subhead, honorNameColor, false);

        ret &= sl.addSpacer(AFTER_RES_TYPE);

        sl.commit();

        return ret;
    }

    /**
     * Attempts to add a series of lines representing multiple individuals tied for the same place to a given slide.
     * 
     * <p>
     * The text is committed. If an addition overflows the slide, the slide is reset (this affects the first buffer).
     * </p>
     * 
     * @param sl the {@link BuildableStackedSlide} to which the text should be added
     * @param placeNum the numeric place assigned to each tied competitor
     * @param tiedHonorees the names of those tied for the given place
     * @param tiedSchools the names of the schools tied for the given place (if this is a school award, this array should be null)
     * @param sweeps the amount of sweepstakes points assigned to each tied competitor
     * @return false if the operation failed
     * @throws NullPointerException if tiedHonorees is null
     * @throws IllegalArgumentException if tiedSchools is not null and <code>tiedHonorees.length != tiedSchools.length</code>
     */
    private boolean tryAddTie (BuildableStackedSlide sl, int placeNum, String[] tiedHonorees, String[] tiedSchools, Fraction sweeps) {
        if (tiedHonorees == null) {
            throw new NullPointerException("tiedHonorees must not be null");
        }
        if (tiedSchools != null && tiedHonorees.length != tiedSchools.length) { // tiedSchools can be null for team awards
            throw new IllegalArgumentException("Length of tiedHonorees (" + tiedHonorees.length + ") must match length of tiedSchools (" + tiedSchools.length + ")");
        }

        boolean threeCol = (tiedSchools == null); // whether to use three columns instead of four (tiedSchools is the fourth column)

        Color placeNumColor = (color.containsKey("placeNum")) ? color.get("placeNum") : new Color(0x444444);
        Color honoreeColor = (color.containsKey("honoree")) ? color.get("honoree") : new Color(0x222222);
        Color schoolColor = (color.containsKey("school")) ? color.get("school") : new Color(0x333333);
        Color sweepsColor = (color.containsKey("sweeps")) ? color.get("sweeps") : new Color(0x666666);

        String plStr = Integer.toString(placeNum);
        String swStr = String.format("%.2f", sweeps.toDouble()); // Two decimals
        if (swStr.endsWith(".00")) { // Chop off the last two digits if they're .00
            swStr = swStr.substring(0, swStr.length() - ".00".length());
        }

        // Sort the honorees and schools (keeping honorees associated with their schools) TODO: sort by last name
        String[][] multiSortBundle = ArrayUtils.multiSort(tiedHonorees, tiedSchools);
        String[] sortedHonorees = multiSortBundle[0];
        String[] sortedSchools = multiSortBundle[1];

        for (int i = 0; i < tiedHonorees.length; i++) {
            boolean addSucceeded = false;
            if (threeCol) {
                addSucceeded = sl.addThreeText(plStr, number, placeNumColor, sortedHonorees[i], base, honoreeColor, swStr, number, sweepsColor);
            } else if (sortedSchools[i] == null) {
                addSucceeded = sl.addThreeText(plStr, number, placeNumColor, processStudentName(sortedHonorees[i]), base, honoreeColor, swStr, number, sweepsColor);
            } else {
                addSucceeded = sl.addFourText(plStr, number, placeNumColor, processStudentName(sortedHonorees[i]), base, honoreeColor, sortedSchools[i], base, schoolColor, swStr, number, sweepsColor);
            }

            if (!addSucceeded) {
                sl.reset();
                return false;
            }

            // Only add the place number once; overwrite the color for subsequent iterations (transparent so that subsequent entries still line up)
            placeNumColor = transparent;
        }

        // All is well in the universe
        sl.addSpacer(BETWEEN_TIES); // we don't really care if this works
        sl.commit();
        return true;
    }

    /**
     * Adds a series of lines representing multiple individuals tied for the same place to a given slide, overflowing onto new slides if necessary.
     * 
     * <p>
     * All generated slides are committed and pushed.
     * </p>
     * 
     * @param sl the first {@link BuildableStackedSlide} to which the text should be added
     * @param newSlideResType the {@linkplain #addResType(BuildableStackedSlide, String) result type} for any new slides
     * @param newSlideHonorName the {@linkplain #addHonorName(BuildableStackedSlide, String) honor name} for any new slides
     * @param placeNum the numeric place assigned to each tied competitor
     * @param tiedHonorees the names of those tied for the given place
     * @param tiedSchools the names of the schools tied for the given place (if this is a school award, this array should be null)
     * @param sweeps the amount of sweepstakes points assigned to each tied competitor
     * @return all of the slides to which elements were added, including <code>sl</code>
     * @throws NullPointerException if tiedHonorees is null
     * @throws IllegalArgumentException if tiedSchools is not null and <code>tiedHonorees.length != tiedSchools.length</code>
     */
    private List<BuildableStackedSlide> forceAddTie (BuildableStackedSlide sl, String newSlideResType, String newSlideHonorName, int placeNum, String[] tiedHonorees, String[] tiedSchools, Fraction sweeps) {
        if (tiedHonorees == null) {
            throw new NullPointerException("tiedHonorees must not be null");
        }
        if (tiedSchools != null && tiedHonorees.length != tiedSchools.length) { // tiedSchools can be null for team awards
            throw new IllegalArgumentException("Length of tiedHonorees (" + tiedHonorees.length + ") must match length of tiedSchools (" + tiedSchools.length + ")");
        }

        // this holds a list of all slides used
        ArrayList<BuildableStackedSlide> ret = new ArrayList<BuildableStackedSlide>();
        ret.add(sl);

        boolean threeCol = (tiedSchools == null); // whether to use three columns instead of four (tiedSchools is the fourth column)

        Color placeNumColor = (color.containsKey("placeNum")) ? color.get("placeNum") : new Color(0x444444);
        Color honoreeColor = (color.containsKey("honoree")) ? color.get("honoree") : new Color(0x222222);
        Color schoolColor = (color.containsKey("school")) ? color.get("school") : new Color(0x333333);
        Color sweepsColor = (color.containsKey("sweeps")) ? color.get("sweeps") : new Color(0x666666);

        String plStr = Integer.toString(placeNum);
        String swStr = String.format("%.2f", sweeps.toDouble()); // Two decimals
        if (swStr.endsWith(".00")) { // Chop off the last two digits if they're .00
            swStr = swStr.substring(0, swStr.length() - ".00".length());
        }

        // Sort the honorees and schools (keeping honorees associated with their schools) TODO: sort by last name
        String[][] multiSortBundle = ArrayUtils.multiSort(tiedHonorees, tiedSchools);
        String[] sortedHonorees = multiSortBundle[0];
        String[] sortedSchools = multiSortBundle[1];

        Color placeNumColorCur = placeNumColor; // assigned to transparent if we want the place num. to be invisible on a certain line
        // we don't simply overwrite placeNumColor as in tryAddTie because we might need to reenable the place number if we start a new slide

        for (int i = 0; i < tiedHonorees.length; i++) {
            boolean addSucceeded = false;
            if (threeCol) {
                addSucceeded = sl.addThreeText(plStr, number, placeNumColorCur, sortedHonorees[i], base, honoreeColor, swStr, number, sweepsColor);
            } else if (sortedSchools[i] == null) {
                addSucceeded = sl.addThreeText(plStr, number, placeNumColorCur, processStudentName(sortedHonorees[i]), base, honoreeColor, swStr, number, sweepsColor);
            } else {
                addSucceeded = sl.addFourText(plStr, number, placeNumColorCur, processStudentName(sortedHonorees[i]), base, honoreeColor, sortedSchools[i], base, schoolColor, swStr, number, sweepsColor);
            }

            if (!addSucceeded) {
                // Undo and restart on a new slide
                sl.undo();
                sl.commit();
                sl.push(); // ensure that the partial sequence actually renders on the previous slide
                sl = createNewSkeletalSlide();
                ret.add(sl);
                if (newSlideResType != null) {
                    addResType(sl, newSlideResType);
                }
                if (newSlideHonorName != null) {
                    addHonorName(sl, newSlideHonorName);
                }

                // Add it again (placeNumColor instead of placeNumColorCur because this is the first row of the new slide)
                if (threeCol) {
                    sl.addThreeText(plStr, number, placeNumColor, sortedHonorees[i], base, honoreeColor, swStr, number, sweepsColor);
                } else if (sortedSchools[i] == null) {
                    sl.addThreeText(plStr, number, placeNumColor, processStudentName(sortedHonorees[i]), base, honoreeColor, swStr, number, sweepsColor);
                } else {
                    sl.addFourText(plStr, number, placeNumColor, processStudentName(sortedHonorees[i]), base, honoreeColor, sortedSchools[i], base, schoolColor, swStr, number, sweepsColor);
                }
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

    /**
     * Attempts to add a series of lines representing a complete set of results to a given slide.
     * 
     * <p>
     * The text is committed and pushed. If an addition overflows the slide, the slide is reverted (this affects the second buffer).
     * </p>
     * 
     * <p>
     * For each integer <code>i</code> where <code>0 &lt;= i &lt; honorees.length</code>, the place assigned to the competitors in subarray <code>honorees[i]</code> is that given by <code>tieAssign.assignPlace(i + 1, i + honorees[i].length)</code>, where <code>tieAssign</code> is the {@link TiePlaceAssignment} assigned to the {@link Event} assigned to the <code>EventResults</code> represented by this DefaultEventResultsSlideList.
     * </p>
     * 
     * @param sl the {@link BuildableStackedSlide} to which the text should be added
     * @param honorees the results of the event, as if returned by {@link EventResults#getIndivHonorees()} or similar; should, but need not, correctly skip places for ties; also, each String entry should only appear once in the array
     * @param schools the schools corresponding to each element of <code>honorees</code>
     * @param sweeps a mapping from names of honorees to the amount of sweepstakes points earned by each
     * @return false if the operation failed
     * @throws NullPointerException if <code>honorees</code> or <code>schools</code> are null
     * @throws IllegalArgumentException of the lengths of <code>honorees</code> and <code>schools</code> do not match, or <code>honorees</code> and <code>schools</code> do not have the same {@linkplain ArrayUtils#checkStructureSame(Object[], Object[]) structure}
     */
    private boolean tryAddList (BuildableStackedSlide sl, String[][] honorees, String[][] schools, Map<String, Fraction> sweeps) {
        if (honorees == null) {
            throw new NullPointerException("honorees must not be null");
        }
        if (schools == null) {
            throw new NullPointerException("schools must not be null");
        }
        if (honorees.length != schools.length) {
            throw new IllegalArgumentException("Length of honorees (" + honorees.length + ") must match length of schools (" + schools.length + ")");
        }
        if (!ArrayUtils.checkStructureSame(honorees, schools)) {
            throw new IllegalArgumentException("Structure of honorees and schools must match");
        }

        for (int i = 0; i < honorees.length; i++) {
            if (honorees[i] != null && honorees[i].length != 0) { // null or empty is a place skipped for ties
                int placeOne = i + 1;
                int placeTwo = i + honorees[i].length;
                int placeAssign = evr.getEvent().getTieAssign().assignPlace(placeOne, placeTwo);
                boolean addSucceeded = tryAddTie(sl, placeAssign, honorees[i], schools[i], sweeps.get(honorees[i][0]));
                if (!addSucceeded) {
                    sl.revert();
                    return false;
                }
            }
        }

        // All is well in the universe
        sl.commit();
        sl.push();
        return true;
    }

    /**
     * Adds a series of lines representing a complete set of results to a given slide, overflowing onto new slides if necessary.
     * 
     * <p>
     * All generated slides are committed and pushed.
     * </p>
     * 
     * <p>
     * For each integer <code>i</code> where <code>0 &lt;= i &lt; honorees.length</code>, the place assigned to the competitors in subarray <code>honorees[i]</code> is that given by <code>tieAssign.assignPlace(i + 1, i + honorees[i].length)</code>, where <code>tieAssign</code> is the {@link TiePlaceAssignment} assigned to the {@link Event} assigned to the <code>EventResults</code> represented by this DefaultEventResultsSlideList.
     * </p>
     * 
     * @param sl the first {@link BuildableStackedSlide} to which the text should be added
     * @param newSlideResType the {@linkplain #addResType(BuildableStackedSlide, String) result type} for any new slides
     * @param newSlideHonorName the {@linkplain #addHonorName(BuildableStackedSlide, String) honor name} for any new slides
     * @param honorees the results of the event, as if returned by {@link EventResults#getIndivHonorees()} or similar; should, but need not, correctly skip places for ties; also, each String entry should only appear once in the array
     * @param schools the schools corresponding to each element of <code>honorees</code>
     * @param sweeps a mapping from names of honorees to the amount of sweepstakes points earned by each
     * @return all of the slides to which elements were added, including <code>sl</code>
     * @throws NullPointerException if <code>honorees</code> or <code>schools</code> are null
     * @throws IllegalArgumentException of the lengths of <code>honorees</code> and <code>schools</code> do not match, or <code>honorees</code> and <code>schools</code> do not have the same {@linkplain ArrayUtils#checkStructureSame(Object[], Object[]) structure}
     */
    private List<BuildableStackedSlide> forceAddList (BuildableStackedSlide sl, String newSlideResType, String newSlideHonorName, String[][] honorees, String[][] schools, Map<String, Fraction> sweeps) {
        if (honorees == null) {
            throw new NullPointerException("honorees must not be null");
        }
        if (schools == null) {
            throw new NullPointerException("schools must not be null");
        }
        if (honorees.length != schools.length) {
            throw new IllegalArgumentException("Length of honorees (" + honorees.length + ") must match length of schools (" + schools.length + ")");
        }
        if (!ArrayUtils.checkStructureSame(honorees, schools)) {
            throw new IllegalArgumentException("Structure of honorees and schools must match");
        }

        // this holds a list of all slides used
        ArrayList<BuildableStackedSlide> ret = new ArrayList<BuildableStackedSlide>();
        ret.add(sl);

        for (int i = 0; i < honorees.length; i++) {
            if (honorees[i] != null && honorees[i].length != 0) { // null or empty is a place skipped for ties
                int placeOne = i + 1;
                int placeTwo = i + honorees[i].length;
                int placeAssign = evr.getEvent().getTieAssign().assignPlace(placeOne, placeTwo);
                boolean addSucceeded = tryAddTie(sl, placeAssign, honorees[i], schools[i], sweeps.get(honorees[i][0]));
                if (!addSucceeded) {
                    // The tryAddTie method leaves no trace if it fails, so we start a new slide
                    sl.commit();
                    sl.push();
                    sl = createNewSkeletalSlide();
                    ret.add(sl);
                    if (newSlideResType != null) {
                        addResType(sl, newSlideResType);
                    }
                    if (newSlideHonorName != null) {
                        addHonorName(sl, newSlideHonorName);
                    }

                    // Force the add this time
                    int placeOneAgain = i + 1;
                    int placeTwoAgain = i + honorees[i].length;
                    int placeAssignAgain = evr.getEvent().getTieAssign().assignPlace(placeOneAgain, placeTwoAgain);
                    List<BuildableStackedSlide> forced = forceAddTie(sl, newSlideResType, newSlideHonorName, placeAssignAgain, honorees[i], schools[i], sweeps.get(honorees[i][0]));

                    // Mess with sl so that it represents the most recent slide
                    if (forced.size() > 1) { // if forceAddTie generated new slides
                        ret.addAll(forced.subList(1, forced.size())); // add the generated slides to ret
                        sl = forced.get(forced.size() - 1); // the most recent slide is what we're now working on
                    }
                }
            }
        }

        // All is well in the universe
        sl.commit();
        sl.push();
        return ret;
    }

    /**
     * Processes a student name for display by removing any characters after a ` character and trimming whitespace.
     * 
     * @param raw the unprocessed name of the student
     * @return the processed name of the student, with all characters after the first ` removed and whitespace on either end of the truncated string removed
     */
    private String processStudentName (String raw) {
        // Splice off anything that occurs after a grave character and trim whitespace
        if (raw.indexOf("`") >= 0) {
            raw = raw.substring(0, raw.indexOf("`"));
        }
        return raw.trim();
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

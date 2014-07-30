/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
 * TODO:
 * 
 * - check for nulls in indivHonorees/teamHonorees/specialHonorees (indivSchools/specialSchools should allow null for independent competitors--and nulls should be allowed if there are ties in the preceding place)
 */

/**
 * Represents a given competition event and its results.
 * 
 * @author MathSquared
 * 
 */
public class EventResults implements Serializable {
    private Event ev;

    // May be null if there are zero honorees in the corresponding category
    private String[][] indivHonorees;
    private String[][] indivSchools;
    private String[][] teamHonorees;
    private HashMap<String, String[][]> specialHonorees;
    private HashMap<String, String[][]> specialSchools;

    // Cached raw sweeps results; TODO: allow different sweeps for different honorees in the same tie
    private Fraction[] indivSweeps;
    private Fraction[] teamSweeps;
    private Fraction[] specialSweeps;

    /**
     * Constructs a new object representing results in the given event.
     * 
     * <p>
     * The {@link Event} passed into this method determines the amount of data that this object can store. For instance, if <code>evt.getTeamPlaces() == 0</code>, this object will not support specifying team places (the {@link #getIndivHonorees()} method will return null, and the {@link #setIndivHonorees(String[])} method will throw an {@link UnsupportedOperationException}).
     * </p>
     * 
     * <p>
     * This constructor is private so as to avoid EventResult objects in incomplete states (that don't represent fully available event results). To instantiate an EventResults object, use the {@link EventResults#EventResults(Event, String[], String[], Map)} constructor.
     * </p>
     * 
     * @param evt the Event object represented
     */
    private EventResults (Event evt) {
        ev = evt;

        // Set up the data structures based on the event spec
        if (ev.getIndivPlaces() > 0) {
            indivHonorees = new String[ev.getIndivPlaces()][];
            indivSchools = new String[ev.getIndivPlaces()][];
        }
        if (ev.getTeamPlaces() > 0) {
            teamHonorees = new String[ev.getTeamPlaces()][];
        }
        if (ev.getSpecialHonors().size() > 0) {
            specialHonorees = new HashMap<String, String[][]>();

            // Initialize each array in the Map to the proper length
            for (String x : ev.getSpecialHonors().keySet()) {
                specialHonorees.put(x, new String[ev.getSpecialHonors().get(x)][]);
            }

            // Now, the schools
            specialSchools = new HashMap<String, String[][]>();

            // Initialize each array in the Map to the proper length
            for (String x : ev.getSpecialHonors().keySet()) {
                specialSchools.put(x, new String[ev.getSpecialHonors().get(x)][]);
            }
        }
    }

    /**
     * Constructs a new object representing results in the given event, and initializes it with data about these results.
     * 
     * <p>
     * The {@link Event} passed into this method determines the amount of data that this object can store. For instance, if <code>evt.getTeamPlaces() == 0</code>, this object will not support specifying team places (the {@link #getIndivHonorees()} method will return null, and the {@link #setIndivHonorees(String[])} method will throw an {@link UnsupportedOperationException}).
     * </p>
     * 
     * <p>
     * This constructor will fail if the passed-in results do not match the event specifications; for details on exceptional conditions, see {@link #setIndivHonorees()}, {@link #setTeamHonorees(String[])}, and {@link #setSpecialHonorees(Map)}.
     * </p>
     * 
     * @param evt the Event object represented
     * @param indivHonorees an array of those placing individually, in rank order where index 0 is first place
     * @param indivSchools the schools of those with the same index in indivHonorees
     * @param teamHonorees an array of those placing as teams, in rank order where index 0 is first place
     * @param specialHonorees a mapping from names of special honors to arrays of the names of those placing in them, in rank order where index 0 is first place
     * @param specialSchools a mapping from names of special honors to arrays of the schools of those placing in them, in rank order
     * @throws UnsupportedOperationException if this Event does not support a particular result type, and the corresponding parameter(s) was/were not null
     * @throws NullPointerException if this Event supports a particular result type, and the corresponding parameter(s) was/were null
     * @throws IllegalArgumentException if a parameter is inconsistent with the Event specification or is otherwise formatted incorrectly; see {@link #setIndivResults(String[][], String[][])}, {@link #setTeamHonorees(String[][])}, and {@link #setSpecialResults(Map, Map)} for details on correct and incorrect formatting
     */
    public EventResults (Event evt, String[][] indivHonorees, String[][] indivSchools, String[][] teamHonorees, Map<String, String[][]> specialHonorees, Map<String, String[][]> specialSchools) {
        this(evt); // Initializes data structures, so we can work with their lengths when deciding whether or not to throw UOE or NPE

        // Only call these methods if the event is supported; if not, UOE if the parameter was not null
        if (this.indivHonorees != null) { // event supported, since we bothered to instantiate a data structure in the constructor
            setIndivResults(indivHonorees, indivSchools); // will NPE if parameter was null
        } else { // unsupported
            if (indivHonorees != null) { // unsupported, but passed in an object anyway
                throw new UnsupportedOperationException("Event " + evt.getPrimaryName() + " does not support individual places; parameter must be null");
            }
            if (indivSchools != null) { // unsupported, but passed in an object anyway
                throw new UnsupportedOperationException("Event " + evt.getPrimaryName() + " does not support individual places; schools parameter must be null");
            }
        }
        if (this.teamHonorees != null) { // event supported, since we bothered to instantiate a data structure in the constructor
            setTeamHonorees(teamHonorees); // will NPE if parameter was null
        } else { // unsupported
            if (teamHonorees != null) { // unsupported, but passed in an object anyway
                throw new UnsupportedOperationException("Event " + evt.getPrimaryName() + " does not support team places; parameter must be null");
            }
        }
        if (this.specialHonorees != null) { // event supported, since we bothered to instantiate a data structure in the constructor
            setSpecialResults(specialHonorees, specialSchools); // will NPE if parameter was null
        } else { // unsupported
            if (specialHonorees != null) { // unsupported, but passed in an object anyway
                throw new UnsupportedOperationException("Event " + evt.getPrimaryName() + " does not support special honors; parameter must be null");
            }
            if (specialSchools != null) { // unsupported, but passed in an object anyway
                throw new UnsupportedOperationException("Event " + evt.getPrimaryName() + " does not support special honors; schools parameter must be null");
            }
        }
    }

    /**
     * Constructs a new object representing the same results as the parameter. The constructed EventResults will be semantically equivalent to the passed-in EventResults (that is, all of its methods will return the same results, with the exception that the references will be different but will represent the same data), but will be independent of the parameter EventResults.
     * 
     * @param cp the EventResults whose data to copy
     */
    public EventResults (EventResults cp) {
        this(cp.getEvent(), cp.getIndivHonorees(), cp.getIndivSchools(), cp.getTeamHonorees(), cp.getSpecialHonorees(), cp.getSpecialSchools());
    }

    /**
     * Returns the sweepstakes points for individual winners in this event. Specifically, returns a Map from names of honorees to amount of sweeps points received.
     * 
     * @param studentNames if true, the returned Map has student names as keys; if false, school names are used as keys
     * @return a Map from student or school names to amount of points earned (entries earning 0 points may or may not be included)
     */
    public Map<String, Fraction> computeIndivSweeps (boolean studentNames) {
        String[][] workingArray = studentNames ? indivHonorees : indivSchools;
        int[] workingLengths = ArrayUtils.condensedLengthArray(workingArray);
        if (indivSweeps == null) { // sweeps not yet initialized
            indivSweeps = Sweepstakes.assignPoints(workingLengths, ev.getIndivSweeps(), ev.getTieAssign(), ev.getSweepsAssign());
        }
        return Sweepstakes.linkSweepstakes(workingArray, indivSweeps);
    }

    /**
     * Returns the sweepstakes points for team winners in this event. Specifically, returns a Map from names of honored schools to amount of sweeps points received.
     * 
     * @return a Map from school names to amount of points earned (entries earning 0 points may or may not be included)
     */
    public Map<String, Fraction> computeTeamSweeps () {
        int[] workingLengths = ArrayUtils.condensedLengthArray(teamHonorees);
        if (teamSweeps == null) {
            teamSweeps = Sweepstakes.assignPoints(workingLengths, ev.getTeamSweeps(), ev.getTieAssign(), ev.getSweepsAssign());
        }
        return Sweepstakes.linkSweepstakes(teamHonorees, teamSweeps);
    }

    /**
     * Returns the sweepstakes points for winners in the given special honor. Specifically, returns a Map from names of honorees to amount of sweeps points received.
     * 
     * @param honorName the name of the special honor for which to compute sweeps
     * @param studentNames if true, the returned Map has student names as keys; if false, school names are used as keys
     * @return a Map from student or school names to amount of points earned in the honor given by <code>honorName</code> (entries earning 0 points may or may not be included)
     * @throws NullPointerException if no results are available for <code>honorName</code> (equivalently, if <code>(studentNames ? specialHonorees : specialSchools).get(honorName) == null</code>)
     */
    public Map<String, Fraction> computeSpecialSweeps (String honorName, boolean studentNames) {
        String[][] results = (studentNames ? specialHonorees : specialSchools).get(honorName);
        if (results == null) {
            throw new NullPointerException("No results for honor " + honorName);
        }
        int[] workingLengths = ArrayUtils.condensedLengthArray(results);
        if (specialSweeps == null) {
            specialSweeps = Sweepstakes.assignPoints(workingLengths, ev.getSpecialSweeps().get(honorName), ev.getTieAssign(), ev.getSweepsAssign());
        }
        return Sweepstakes.linkSweepstakes(results, specialSweeps);
    }

    /**
     * Computes the sum total amount of sweepstakes points to award to all schools for this event.
     * 
     * <p>
     * This is equivalent to {@linkplain AdditiveMapUtils#addAllNumbers(Map, Map, boolean) numerically adding} the results of {@link #computeIndivSweeps(boolean) computeIndivSweeps(false)}, {@link #computeTeamSweeps()}, and {@link #computeSpecialSweeps(String, boolean) computeSpecialSweeps(x, false)} where <code>x</code> takes on all of the values of <code>getSpecialHonorees().keySet()</code> (equivalently, the names of all of the special honors in this event).
     * </p>
     * 
     * @return a Map from school names to amount of points earned in this event (entries earning 0 points may or may not be included)
     */
    public Map<String, Fraction> computeTotalSweeps () {
        Map<String, Fraction> ret = computeIndivSweeps(false); // must use schools because of team sweeps
        AdditiveMapUtils.addAllNumbers(ret, computeTeamSweeps(), false);

        // Add all special honors
        for (String x : specialHonorees.keySet()) {
            AdditiveMapUtils.addAllNumbers(ret, computeSpecialSweeps(x, false), false);
        }

        return ret;
    }

    /**
     * Returns the event whose results are represented by this EventResults.
     * 
     * @return the {@link Event} assigned to this EventResults object
     */
    public Event getEvent () {
        return ev;
    }

    /**
     * Returns an array of the names of those placing individually, in rank order where index 0 is first place.
     * 
     * @return the indivHonorees, or null if this {@linkplain Event event} does not award individual results
     */
    public String[][] getIndivHonorees () {
        if (indivHonorees == null) {
            return null;
        }
        return ArrayUtils.deepCopyOf(indivHonorees);
    }

    /**
     * Sets the names of those placing individually, in rank order where index 0 is first place.
     * 
     * @param indivHonorees the indivHonorees to set
     * @throws UnsupportedOperationException if this Event does not support individual results (equivalently, if {@link #getIndivHonorees()} returns null)
     * @throws NullPointerException if the parameter is null
     * @throws IllegalArgumentException if the length of <code>indivHonorees</code> does not match the {@linkplain Event#getIndivPlaces() amount of individual places} specified by the Event, or <code>indivHonorees</code> does not properly {@linkplain ArrayUtils#checkTies(String[][]) skip places for ties}
     */
    private void setIndivHonorees (String[][] indivHonorees) {
        if (this.indivHonorees == null) {
            throw new UnsupportedOperationException("Event " + ev.getPrimaryName() + " does not support individual results");
        }
        if (indivHonorees == null) {
            throw new NullPointerException("indivHonorees must not be null");
        }
        if (indivHonorees.length != ev.getIndivPlaces()) {
            throw new IllegalArgumentException("Length of indivHonorees must match specification in Event (here, " + ev.getIndivPlaces() + " for " + ev.getPrimaryName());
        }
        if (!ArrayUtils.checkTies(indivHonorees)) {
            throw new IllegalArgumentException("indivHonorees must correctly skip places for ties and must not contain extraneous nulls");
        }
        this.indivHonorees = ArrayUtils.deepCopyOf(indivHonorees);
    }

    /**
     * Returns an array of the schools of those placing individually, in rank order where index 0 is first place.
     * 
     * @return the indivSchools, or null if this {@linkplain Event event} does not award individual results
     */
    public String[][] getIndivSchools () {
        if (indivSchools == null) {
            return null;
        }
        return ArrayUtils.deepCopyOf(indivSchools);
    }

    /**
     * Sets the schools of those placing individually, in rank order where index 0 is first place.
     * 
     * @param indivSchools the indivSchools to set
     * @throws UnsupportedOperationException if this Event does not support individual results (equivalently, if {@link #getIndivHonorees()} returns null)
     * @throws NullPointerException if the parameter is null
     * @throws IllegalArgumentException if the length of <code>indivSchools</code> does not match the {@linkplain Event#getIndivPlaces() amount of individual places} specified by the Event, or <code>indivSchools</code> does not properly {@linkplain ArrayUtils#checkTies(String[][]) skip places for ties}
     */
    private void setIndivSchools (String[][] indivSchools) {
        if (this.indivSchools == null) {
            throw new UnsupportedOperationException("Event " + ev.getPrimaryName() + " does not support individual results");
        }
        if (indivSchools == null) {
            throw new NullPointerException("indivSchools must not be null");
        }
        if (indivSchools.length != ev.getIndivPlaces()) {
            throw new IllegalArgumentException("Length of indivSchools must match specification in Event (here, " + ev.getIndivPlaces() + " for " + ev.getPrimaryName());
        }
        if (!ArrayUtils.checkTies(indivSchools)) {
            throw new IllegalArgumentException("indivSchools must correctly skip places for ties and must not contain extraneous nulls");
        }
        this.indivSchools = ArrayUtils.deepCopyOf(indivSchools);
    }

    /**
     * Sets the names and schools of those placing individually, in rank order where index 0 is first place.
     * 
     * @param indivHonorees the indivHonorees to set
     * @param indivSchools the indivSchools to set
     * @throws UnsupportedOperationException if this Event does not support individual results (equivalently, if {@link #getIndivHonorees()} returns null)
     * @throws NullPointerException if the parameter is null
     * @throws IllegalArgumentException if the length of <code>indivHonorees</code> or <code>indivSchools</code> does not match the {@linkplain Event#getIndivPlaces() amount of individual places} specified by the Event, <code>indivHonorees</code> and <code>indivSchools</code> do not have the same {@linkplain ArrayUtils#checkStructureSame(Object[], Object[]) tied structure}, or <code>indivHonorees</code> or <code>indivSchools</code> does not properly {@linkplain ArrayUtils#checkTies(String[][]) skip places for ties}
     */
    public void setIndivResults (String[][] indivHonorees, String[][] indivSchools) {
        if (!ArrayUtils.checkStructureSame(indivHonorees, indivSchools)) {
            throw new IllegalArgumentException("Structure of honorees and schools must match");
        }

        // Reset sweeps
        indivSweeps = null;

        setIndivHonorees(indivHonorees);
        setIndivSchools(indivSchools);
    }

    /**
     * Returns an array of the names of those placing as teams, in rank order where index 0 is first place.
     * 
     * @return the teamHonorees, or null if this {@linkplain Event event} does not award team results
     */
    public String[][] getTeamHonorees () {
        if (teamHonorees == null) {
            return null;
        }
        return ArrayUtils.deepCopyOf(teamHonorees);
    }

    /**
     * Sets the names of those placing as teams, in rank order where index 0 is first place.
     * 
     * @param teamHonorees the teamHonorees to set
     * @throws UnsupportedOperationException if this Event does not support team results (equivalently, if {@link #getTeamHonorees()} returns null)
     * @throws NullPointerException if the parameter is null
     * @throws IllegalArgumentException if the length of <code>teamHonorees</code> does not match the {@linkplain Event#getTeamPlaces() amount of team places} specified by the Event, or <code>teamHonorees</code> does not properly {@linkplain ArrayUtils#checkTies(String[][]) skip places for ties}
     */
    public void setTeamHonorees (String[][] teamHonorees) {
        if (this.teamHonorees == null) {
            throw new UnsupportedOperationException("Event " + ev.getPrimaryName() + " does not support team results");
        }
        if (teamHonorees == null) {
            throw new NullPointerException("teamHonorees must not be null");
        }
        if (teamHonorees.length != ev.getTeamPlaces()) {
            throw new IllegalArgumentException("Length of teamHonorees must match specification in Event (here, " + ev.getTeamPlaces() + " for " + ev.getPrimaryName());
        }
        if (!ArrayUtils.checkTies(teamHonorees)) {
            throw new IllegalArgumentException("teamHonorees must correctly skip places for ties and must not contain extraneous nulls");
        }

        // Reset sweeps
        teamSweeps = null;

        this.teamHonorees = ArrayUtils.deepCopyOf(teamHonorees);
    }

    /**
     * Returns a mapping from names of special honors to arrays of the names of those placing in them, in rank order where index 0 is first place.
     * 
     * @return the specialHonorees, or null if this {@linkplain Event event} does not award special honors
     */
    public Map<String, String[][]> getSpecialHonorees () {
        if (specialHonorees == null) {
            return null;
        }

        // Copy everything
        Map<String, String[][]> ret = new HashMap<String, String[][]>();
        for (Map.Entry<String, String[][]> x : specialHonorees.entrySet()) {
            String k = x.getKey();
            String[][] v = x.getValue();
            ret.put(k, ArrayUtils.deepCopyOf(v));
        }

        return ret;
    }

    /**
     * Sets the names of those placing in special honors.
     * 
     * <p>
     * The parameter is a mapping from names of special honors to arrays of the names of those placing in them, in rank order where index 0 is first place.
     * </p>
     * 
     * @param specialHonorees the specialHonorees to set
     * @throws UnsupportedOperationException if this Event does not support special honors (equivalently, if {@link #getSpecialHonorees()} returns null)
     * @throws NullPointerException if the parameter is null
     * @throws IllegalArgumentException if the keys in <code>specialHonorees</code> do not match the {@linkplain Event#getSpecialHonors() special honors} specified by the Event, the length of one of the arrays does not match the amount of places specified for that special honor, or one of the arrays does not properly {@linkplain ArrayUtils#checkTies(String[][]) skip places for ties}
     */
    private void setSpecialHonorees (Map<String, String[][]> specialHonorees) {
        if (this.specialHonorees == null) {
            throw new UnsupportedOperationException("Event " + ev.getPrimaryName() + " does not support special honors");
        }
        if (specialHonorees == null) {
            throw new NullPointerException("specialHonorees must not be null");
        }

        // Check that the passed-in Map has same honors and same places in each and skips places correctly
        if (!specialHonorees.keySet().equals(ev.getSpecialHonors().keySet())) {
            throw new IllegalArgumentException("Keys in specialHonorees must match those specified in the Event");
        }
        for (String x : specialHonorees.keySet()) {
            // Check amount of places

            // How many places there should be for this special honor
            int expected = ev.getSpecialHonors().get(x);

            // How many there actually are
            int observed = specialHonorees.get(x).length;

            if (expected != observed) {
                throw new IllegalArgumentException("Places passed in for honor " + x + " in event " + ev.getPrimaryName() + " must match those specified in the Event (expected: " + expected + "; observed: " + observed + ")");
            }

            // Check place skips
            if (!ArrayUtils.checkTies(specialHonorees.get(x))) {
                throw new IllegalArgumentException("Places passed in for honor " + x + " do not properly skip places for ties");
            }
        }

        // Copy everything
        HashMap<String, String[][]> ret = new HashMap<String, String[][]>();
        for (Map.Entry<String, String[][]> x : specialHonorees.entrySet()) {
            String k = x.getKey();
            String[][] v = x.getValue();
            ret.put(k, ArrayUtils.deepCopyOf(v));
        }

        this.specialHonorees = ret;
    }

    /**
     * Returns a mapping from names of special honors to arrays of the schools of the competitors placing in them, in rank order where index 0 is first place.
     * 
     * @return the specialSchools, or null if this {@linkplain Event event} does not award special honors
     */
    public Map<String, String[][]> getSpecialSchools () {
        if (specialSchools == null) {
            return null;
        }

        // Copy everything
        Map<String, String[][]> ret = new HashMap<String, String[][]>();
        for (Map.Entry<String, String[][]> x : specialSchools.entrySet()) {
            String k = x.getKey();
            String[][] v = x.getValue();
            ret.put(k, ArrayUtils.deepCopyOf(v));
        }

        return ret;
    }

    /**
     * Sets the schools of those competitors placing in special honors.
     * 
     * <p>
     * The parameter is a mapping from names of special honors to arrays of the schools of those placing in them, in rank order where index 0 is first place.
     * </p>
     * 
     * @param specialSchools the specialSchools to set
     * @throws UnsupportedOperationException if this Event does not support special honors (equivalently, if {@link #getSpecialHonorees()} returns null)
     * @throws NullPointerException if the parameter is null
     * @throws IllegalArgumentException if the keys in <code>specialSchools</code> do not match the {@linkplain Event#getSpecialHonors() special honors} specified by the Event, the length of one of the arrays does not match the amount of places specified for that special honor, or one of the arrays does not properly {@linkplain ArrayUtils#checkTies(String[][]) skip places for ties}
     */
    private void setSpecialSchools (Map<String, String[][]> specialSchools) {
        if (this.specialSchools == null) {
            throw new UnsupportedOperationException("Event " + ev.getPrimaryName() + " does not support special honors");
        }
        if (specialSchools == null) {
            throw new NullPointerException("specialSchools must not be null");
        }

        // Check that the passed-in Map has same honors and same places in each
        if (!specialSchools.keySet().equals(ev.getSpecialHonors().keySet())) {
            throw new IllegalArgumentException("Keys in specialSchools must match those specified in the Event");
        }
        for (String x : specialSchools.keySet()) {
            // Check amount of places

            // How many places there should be for this special honor
            int expected = ev.getSpecialHonors().get(x);

            // How many there actually are
            int observed = specialSchools.get(x).length;

            if (expected != observed) {
                throw new IllegalArgumentException("Places passed in for honor " + x + " in event " + ev.getPrimaryName() + " must match those specified in the Event (expected: " + expected + "; observed: " + observed + ")");
            }

            // Check place skips
            if (!ArrayUtils.checkTies(specialSchools.get(x))) {
                throw new IllegalArgumentException("Places passed in for honor " + x + " do not properly skip places for ties");
            }
        }

        // Copy everything
        HashMap<String, String[][]> ret = new HashMap<String, String[][]>();
        for (Map.Entry<String, String[][]> x : specialSchools.entrySet()) {
            String k = x.getKey();
            String[][] v = x.getValue();
            ret.put(k, ArrayUtils.deepCopyOf(v));
        }

        this.specialSchools = ret;
    }

    /**
     * Sets the names and schools of those placing in special honors.
     * 
     * <p>
     * Each parameter is a mapping from names of special honors to arrays of the names or schools of those placing in them, in rank order where index 0 is first place.
     * </p>
     * 
     * @param specialHonorees the specialHonorees to set
     * @param specialSchools the specialSchools to set
     * @throws UnsupportedOperationException if this Event does not support special honors (equivalently, if {@link #getSpecialHonorees()} returns null)
     * @throws NullPointerException if the parameter is null
     * @throws IllegalArgumentException if the keys in <code>specialHonors</code> and <code>specialSchools</code> do not match each other or the {@linkplain Event#getSpecialHonors() special honors} specified by the Event, the length of one of the arrays does not match the amount of places specified for that special honor, one of the arrays does not properly {@linkplain ArrayUtils#checkTies(String[][]) skip places for ties}, or for any String <code>honor</code>, <code>specialHonorees.get(x)</code> and <code>specialSchools.get(x)</code> do not have the same {@linkplain ArrayUtils#checkStructureSame(Object[], Object[]) tied structure}
     */
    public void setSpecialResults (Map<String, String[][]> specialHonorees, Map<String, String[][]> specialSchools) {
        // Check that the keySets match to avoid problems later
        if (!specialHonorees.keySet().equals(specialSchools.keySet())) {
            throw new IllegalArgumentException("Keys in specialHonorees and specialSchools must match");
        }

        for (String x : specialHonorees.keySet()) {
            if (!ArrayUtils.checkStructureSame(specialHonorees.get(x), specialSchools.get(x))) {
                throw new IllegalArgumentException("Structure of results for honor " + x + " does not match between specialHonorees and specialSchools");
            }
        }

        // Reset sweeps
        specialSweeps = null;

        setSpecialHonorees(specialHonorees);
        setSpecialSchools(specialSchools);
    }
}

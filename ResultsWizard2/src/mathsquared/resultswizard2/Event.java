/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents one competition event (or pseudo-aggregate event).
 * 
 * @author MathSquared
 * 
 */
public class Event implements Serializable {
    private String primaryName; // The official name of the event, used for all postings
    private String[] otherNames; // Alternative names for the event
    private int indivPlaces; // The amount of individual places to be recognized
    private int teamPlaces; // The amount of team places to be recognized
    private HashMap<String, Integer> specialHonors; // Any special honors (top scorer, top speaker, etc.) that may be awarded to students in this event, mapped to the number of places to recognize
    private int[] indivSweeps; // The amount of sweepstakes points to award to each place in this event (0 being first, 1 second, etc.)
    private int[] teamSweeps; // The amount of sweepstakes points to award to each team place in this event (0 being first, 1 second, etc.)
    private HashMap<String, int[]> specialSweeps; // The amount of sweepstakes points to award to each special honor (indices correspond with specialHonors); the array is the points to award for each place
    private TiePlaceAssignment tieAssign; // How this event assigns places in the event of ties
    private SweepstakesAssignment sweepsAssign; // How this event assigns sweepstakes points in the event of ties

    /**
     * Creates an object representing a new competition event. All objects passed in have their data copied into the class; in this way, the Event object maintains no references to passed-in data.
     * 
     * <p>
     * Ordinarily, this constructor would be invoked by reading in from a file, not by client code.
     * </p>
     * 
     * <p>
     * None of the parameters to this constructor may be null; in addition, none of the array parameters may contain null elements, and none of the Map parameters may contain null keys or values. To indicate that an event does not award special honors, pass in empty maps to <code>specialHonors</code> and <code>specialSweeps</code>.
     * </p>
     * 
     * @param primaryName the official name of the event; must contain only ASCII printable characters from 0x20 to 0x7E, not including the grave character `
     * @param otherNames any other names by which this event is known
     * @param indivPlaces the amount of individual places to recognize; must be non-negative (can be zero)
     * @param teamPlaces the amount of team places to recognize; must be non-negative (can be zero)
     * @param specialHonors the names of any special honors awarded in this event (e.g. Top Scorer in Biology), mapped to the amount of places to recognize for this honor (e.g. First Top Speaker, Second Top Speaker, Third Top Speaker); all values in this map must be positive (NOT zero)
     * @param indivSweeps the amount of sweepstakes points to award for each individual placing, where 0 is for first place, 1 for second, etc.; <code>indivSweeps.length</code> must equal <code>indivPlaces</code>
     * @param teamSweeps the amount of sweepstakes points to award for each team placing, where 0 is for first place, 1 for second, etc.; <code>teamSweeps.length</code> must equal <code>teamPlaces</code>
     * @param specialSweeps the amount of sweepstakes points to award for each placing in each special honor, where the value for any key is the array of points to award for the honor indicated by that key; <code>specialSweeps.keySet().equals(specialHonors.keySet())</code> must be true (meaning <code>specialSweeps</code> and <code>specialHonors</code> must have mappings from the exact same keys)
     * @param tieAssign the {@link TiePlaceAssignment} used to award places in the event of ties
     * @param sweepsAssign the {@link SweepstakesAssignment} used to award sweepstakes points in the event of ties
     * @throws NullPointerException if any parameter is null or contains null as an element, key, or value
     * @throws IllegalArgumentException if <code>primaryName</code> contains a character outside the allowable range, <code>(indivPlaces &lt; 0)</code>, <code>(teamPlaces &lt; 0)</code>, <code>(specialHonors.get(x) &lt;= 0)</code> for any <code>x</code> present in <code>specialHonors.keySet()</code>, <code>(indivSweeps.length != indivPlaces)</code>, <code>(teamSweeps.length != teamPlaces)</code>, or <code>(!specialHonors.keySet().equals(specialSweeps.keySet()))</code>
     */
    public Event (String primaryName, String[] otherNames, int indivPlaces, int teamPlaces, Map<String, Integer> specialHonors, int[] indivSweeps, int[] teamSweeps, Map<String, int[]> specialSweeps, TiePlaceAssignment tieAssign, SweepstakesAssignment sweepsAssign) {
        // Null checks
        if (primaryName == null) {
            throw new NullPointerException("primaryName must not be null");
        }
        if (otherNames == null) {
            throw new NullPointerException("otherNames must not be null");
        }
        for (int i = 0; i < otherNames.length; i++) {
            if (otherNames[i] == null) {
                throw new NullPointerException("Element " + i + " of otherNames is null; otherNames must not contain any null elements");
            }
        }
        if (specialHonors == null) {
            throw new NullPointerException("specialHonors must not be null");
        }
        if (specialHonors.keySet().contains(null)) {
            throw new NullPointerException("specialHonors must not contain the null key");
        }
        for (Map.Entry<String, Integer> x : specialHonors.entrySet()) {
            if (x.getValue() == null) {
                throw new NullPointerException("specialHonors must not contain null values (" + x.getKey() + " mapped to null)");
            }
        }
        if (indivSweeps == null) {
            throw new NullPointerException("indivSweeps must not be null");
        }
        if (teamSweeps == null) {
            throw new NullPointerException("teamSweeps must not be null");
        }
        if (specialSweeps == null) {
            throw new NullPointerException("specialSweeps must not be null");
        }
        if (specialSweeps.keySet().contains(null)) {
            throw new NullPointerException("specialSweeps must not contain the null key");
        }
        for (Map.Entry<String, int[]> x : specialSweeps.entrySet()) {
            if (x.getValue() == null) {
                throw new NullPointerException("specialSweeps must not contain null values (" + x.getKey() + " mapped to null)");
            }
        }
        if (tieAssign == null) {
            throw new NullPointerException("tieAssign must not be null");
        }
        if (sweepsAssign == null) {
            throw new NullPointerException("sweepsAssign must not be null");
        }

        // Sanity checks
        for (char x : primaryName.toCharArray()) {
            if (x < 0x20 || x > 0x7E || x == '`') {
                throw new IllegalArgumentException(String.format("Character U+%04X must not appear in an event's primary name", x));
            }
        }
        if (indivPlaces < 0) {
            throw new IllegalArgumentException("Number of individual places must be >= 0 (" + indivPlaces + " given)");
        }
        if (teamPlaces < 0) {
            throw new IllegalArgumentException("Number of team places must be >= 0 (" + teamPlaces + " given)");
        }
        for (Map.Entry<String, Integer> x : specialHonors.entrySet()) {
            if (x.getValue() <= 0) {
                throw new IllegalArgumentException("Number of places for each special honor must be strictly > 0 (" + x.getValue() + " given for " + x.getKey() + ")");
            }
        }
        if (indivSweeps.length != indivPlaces) {
            throw new IllegalArgumentException("Number of individual places indicated is " + indivPlaces + ", but " + indivSweeps.length + " sweepstakes totals given; numbers must match");
        }
        if (teamSweeps.length != teamPlaces) {
            throw new IllegalArgumentException("Number of team places indicated is " + teamPlaces + ", but " + teamSweeps.length + " sweepstakes totals given; numbers must match");
        }

        // Make sure specialSweeps and specialHonors have identical keys
        Set<String> specialHonorsKeySet = specialHonors.keySet();
        Set<String> specialSweepsKeySet = specialSweeps.keySet();
        if (!specialHonorsKeySet.equals(specialSweepsKeySet)) { // not same mappings
            throw new IllegalArgumentException("specialHonors and specialSweeps must have identical sets of keys");
        }

        this.primaryName = primaryName;
        this.otherNames = Arrays.copyOf(otherNames, otherNames.length);
        this.indivPlaces = indivPlaces;
        this.teamPlaces = teamPlaces;
        // specialHonors handled below
        this.indivSweeps = Arrays.copyOf(indivSweeps, indivSweeps.length);
        this.teamSweeps = Arrays.copyOf(teamSweeps, teamSweeps.length);
        // specialSweeps handled below
        this.tieAssign = tieAssign;
        this.sweepsAssign = sweepsAssign;

        // Copy over the specialHonors
        this.specialHonors = new HashMap<String, Integer>();
        specialHonors.putAll(this.specialHonors); // Copy from method parameter specialHonors to instance variable specialHonors

        // Copy over the specialSweeps, making sure to copy each array--MAINTAIN NO REFERENCES to passed-in data
        this.specialSweeps = new HashMap<String, int[]>();
        for (Map.Entry<String, int[]> x : specialSweeps.entrySet()) {
            String k = x.getKey();
            int[] v = x.getValue();
            this.specialSweeps.put(k, Arrays.copyOf(v, v.length));
        }
    }

    /**
     * Returns the primary name of the event.
     * 
     * @return the primaryName
     */
    public String getPrimaryName () {
        return primaryName;
    }

    /**
     * Returns the alternative names of this event, in arbitrary order.
     * 
     * @return the otherNames
     */
    public String[] getOtherNames () {
        return Arrays.copyOf(otherNames, otherNames.length);
    }

    /**
     * Returns the amount of individual places to recognize in this event.
     * 
     * @return the indivPlaces
     */
    public int getIndivPlaces () {
        return indivPlaces;
    }

    /**
     * Returns the amount of team places to recognize in this event.
     * 
     * @return the teamPlaces
     */
    public int getTeamPlaces () {
        return teamPlaces;
    }

    /**
     * Returns a {@link Map} containing the special honors to recognize in this event, mapped to the amount of places to recognize.
     * 
     * @return the specialHonors
     */
    public Map<String, Integer> getSpecialHonors () {
        // Copy over the specialHonors into a new Map
        Map<String, Integer> ret = new HashMap<String, Integer>();
        specialHonors.putAll(ret);

        return ret;
    }

    /**
     * Returns an array where <code>getIndivSweeps()[i]</code> is the amount of points to award to the individual winner of <code>(i+1)</code>th place.
     * 
     * @return the indivSweeps
     */
    public int[] getIndivSweeps () {
        return Arrays.copyOf(indivSweeps, indivSweeps.length);
    }

    /**
     * Returns an array where <code>getTeamSweeps()[i]</code> is the amount of points to award to the team winner of <code>(i+1)</code>th place.
     * 
     * @return the teamSweeps
     */
    public int[] getTeamSweeps () {
        return Arrays.copyOf(teamSweeps, teamSweeps.length);
    }

    /**
     * Returns a mapping from names of special honors to arrays where <code>getSpecialSweeps().get(honor)[i]</code> is the amount of points to award to the winner of <code>(i+1)</code>th place in <code>honor</code>.
     * 
     * @return the specialSweeps
     */
    public Map<String, int[]> getSpecialSweeps () {
        // Copy over the specialSweeps, making sure to copy each array--MAINTAIN NO REFERENCES to returned data
        Map<String, int[]> ret = new HashMap<String, int[]>();
        for (Map.Entry<String, int[]> x : specialSweeps.entrySet()) {
            String k = x.getKey();
            int[] v = x.getValue();
            ret.put(k, Arrays.copyOf(v, v.length));
        }

        return ret;
    }

    /**
     * @return the tieAssign
     */
    public TiePlaceAssignment getTieAssign () {
        return tieAssign;
    }

    /**
     * @return the sweepsAssign
     */
    public SweepstakesAssignment getSweepsAssign () {
        return sweepsAssign;
    }
}

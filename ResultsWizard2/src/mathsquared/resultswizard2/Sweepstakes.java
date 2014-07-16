/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a set of event results, determines the amount of sweepstakes points to assign to each contestant.
 * 
 * @author MathSquared
 * 
 */
public class Sweepstakes {
    /**
     * Assigns sweepstakes points to all competitors in an event. For all array parameters, index 0 represents first place.
     * 
     * <p>
     * Note that if <code>sweepsM</code> equals {@link SweepstakesAssignment.CUSTOM}, this method returns null, since assigning sweepstakes points with a custom scheme falls to the user, who should be prompted by the application.
     * </p>
     * 
     * @param quantities the number of competitors tied for each place; if there is a tie, the other places in the tie are skipped, so a two-way tie for first followed by a competitor in third is represented by passing in <code>[2, 1]</code>
     * @param spec the number of sweepstakes points assigned to competitors in each place
     * @param tiePlaceM the {@link TiePlaceAssignment} handling assigning one place to each competitor
     * @param sweepsM the {@link SweepstakesAssignment} dictating the sweepstakes points that should be assigned to each competitor
     * @return an array where each entry is the number of points to assign to the tie in the corresponding index of <code>quantities</code>
     */
    public static Fraction[] assignPoints (int[] quantities, int[] spec, TiePlaceAssignment tiePlaceM, SweepstakesAssignment sweepsM) {
        if (sweepsM == SweepstakesAssignment.CUSTOM) {
            return null; // the app should be prompting--leave that to the GUI code, not the back-end
        }

        Fraction[] ret = new Fraction[quantities.length];

        // Start at 0 because indexing into spec is 0-based, not 1-based, and subtracting 1 all the time is too tedious
        int currPlace = 0; // tracks which place we're on, since multiple places are condensed into one entry in quantities
        for (int i = 0; i < quantities.length; i++) {
            int newPlace = currPlace + quantities[i] - 1; // fencepost; quantity of 1 is from place 1 to place 1, not 1 to 2
            Fraction sweeps = new Fraction(0);
            switch (sweepsM) {
            case TIE_PLACE:
                sweeps = new Fraction(spec[tiePlaceM.assignPlace(currPlace, newPlace)]);
                break;
            case TOP:
                sweeps = new Fraction(spec[currPlace]);
                break;
            case BOTTOM:
                sweeps = new Fraction(spec[newPlace]);
                break;
            case MID_ROUND_BETTER:
                sweeps = new Fraction(spec[currPlace + (newPlace - currPlace) / 2]); // division truncates, so this rounds toward adding less
                break;
            case MID_ROUND_WORSE:
                sweeps = new Fraction(spec[newPlace - (newPlace - currPlace) / 2]);
                break;
            case AVERAGE: // take the average of all teh things
                int total = 0;
                int count = 0;
                for (int j = currPlace; j <= newPlace; j++) {
                    total += (j < spec.length) ? spec[j] : 0; // we keep going in an overrun--we still need to add the other places
                    count++;
                }
                sweeps = new Fraction(total, count);
                break;
            case AVERAGE_IGNORE:
                int total1 = 0; // Ignore the numbers after the variables--they're duplicated in scope from case AVERAGE above, so they need to be distinguished.
                int count1 = 0;
                for (int j = currPlace; j <= newPlace && j < spec.length; j++) { // here, we stop the loop early since we ignore all nonexistent specifications
                    total1 += spec[j];
                    count1++;
                }
                sweeps = new Fraction(total1, count1);
                break;
            case AVERAGE_ADJUSTED:
                int total2 = 0;
                int count2 = 0;
                for (int j = currPlace + 1; j < newPlace; j++) { // less than in condition intentional to exclude last place
                    total2 += (j < spec.length) ? spec[j] : 0;
                    count2++;
                }
                sweeps = new Fraction(total2, count2);
                break;
            case MEDIAN:
                int placeDiff = quantities[i] - 1; // difference between currPlace and newPlace
                int halfDiff = placeDiff / 2;
                if (placeDiff % 2 != 0) { // even number of places, due to fenceposting (1 - 1 = 0, but is 1 place)
                    // average two middle ones
                    sweeps = new Fraction(spec[currPlace + halfDiff] + spec[currPlace + halfDiff + 1], 2);
                } else { // odd number of places
                    sweeps = new Fraction(spec[currPlace + halfDiff]);
                }
                break;
            default:
                sweeps = null;
            }

            ret[i] = sweeps;
            currPlace += quantities[i]; // next set of places; this time, we want to move past the range we're in
        }

        return ret;
    }

    /**
     * Indexes an array and returns the result, or 0 if the index is invalid.
     * 
     * This method returns a result equivalent to the expression <code>(index >= toInd.length || index < 0) ? 0 : toInd[index]</code>.
     * 
     * @param toInd the array to index
     * @param index the index into the array
     * @return the element at <code>toInd[index]</code>, or 0 if <code>index</code> is greater than or equal to <code>toInd.length</code> or less than 0
     */
    private int intIndexOrZero (int[] toInd, int index) {
        return (index >= toInd.length || index < 0) ? 0 : toInd[index];
    }

    /**
     * Links an array of sweepstakes points to corresponding results.
     * 
     * <p>
     * Specifically, iterates through the non-null subarrays of <code>results</code> and the entries of <code>sweeps</code>, and returns a Map from each string in <code>results</code> to the sum of corresponding entries in <code>sweeps</code>.
     * </p>
     * 
     * <p>
     * Definition of "corresponding entry":
     * </p>
     * 
     * <ol>
     * <li>Assign each sub-array of nonzero length in <code>results</code> an ID, which is equal to the number of sub-arrays before it that are not null or of length 0</li>
     * <li>For each string in each sub-array of <code>results</code>, the corresponding entry in <code>sweeps</code> is that located at the index in <code>sweeps</code> equal to the ID of the sub-array in <code>results</code> containing that string</li>
     * </ol>
     * 
     * <p>
     * If a string occurs multiple times, the corresponding entries in <code>sweeps</code> are added together. If there are multiple occurrences in the same sub-array, the corresponding entry in <code>sweeps</code> is added multiple times.
     * </p>
     * 
     * <p>
     * If an entry in <code>results</code> has no corresponding entry in <code>sweeps</code>, it is not added to the returned Map.
     * </p>
     * 
     * @param results the raw results of the event, as returned from {@link EventResults#getIndivHonorees()} or a similar method; must represent ties as entries within the same sub-array and should (but need not) correctly {@linkplain ArrayUtils#checkTies(Object[][]) skip places for ties}
     * @param sweeps the sweepstakes to assign to each place, skipping places with no actual results, as if returned from {@link #assignPoints(int[], int[], TiePlaceAssignment, SweepstakesAssignment)}
     * @return a Map linking <code>sweeps</code> from <code>results</code>, as described above
     */
    public static Map<String, Fraction> linkSweepstakes (String[][] results, Fraction[] sweeps) {
        Map<String, Fraction> ret = new HashMap<String, Fraction>();

        int r = 0; // declaring outside the loop for clarity in skipping logic
        for (int s = 0; s < sweeps.length; s++) { // r is handled below
            for (String x : results[r]) { // each string in the sub-array gets this many points
                AdditiveMapUtils.addNumber(ret, x, sweeps[s]);
            }

            // Skip places until I hit the next ones; do...while skips the current entry
            do {
                r++;
            } while (r < results.length && (results[r] == null || results[r].length == 0));

            // Check for overrunning results array
            if (r >= results.length) {
                break;
            }
        }

        return ret;
    }

    /**
     * Computes sweepstakes from start to finish.
     * 
     * <p>
     * This method behaves identically to the following code snippet:
     * </p>
     * 
     * <p>
     * <code>
     * int[] quantities = ArrayUtils.condensedLengthArray(results);<br />
     * Fraction[] sweeps = assignPoints(quantities, spec, tieAssign, sweepsAssign);<br />
     * return linkSweepstakes(results, sweeps);
     * </code>
     * </p>
     * 
     * @param results the results of the event, as if returned by {@link EventResults#getIndivHonorees()} or a similar method; must correctly {@linkplain ArrayUtils#checkTies(Object[][]) skip places for ties}
     * @param spec the specification for sweepstakes points in the event, where index 0 represents the number of points to award for first place
     * @param tieAssign the {@link TiePlaceAssignment} used to assign places in the event of a tie
     * @param sweepsAssign the {@link SweepstakesAssignment} used to assign sweepstakes points in the event of a tie
     * @return a Map from each entry in <code>results</code> to the corresponding amount of sweepstakes points they earn (entries earning 0 points may or may not be included)
     * @throws IllegalArgumentException if <code>results</code> does not properly skip places for ties
     */
    public static Map<String, Fraction> computeSweeps (String[][] results, int[] spec, TiePlaceAssignment tieAssign, SweepstakesAssignment sweepsAssign) {
        int[] quantities = ArrayUtils.condensedLengthArray(results);
        Fraction[] sweeps = assignPoints(quantities, spec, tieAssign, sweepsAssign);
        return linkSweepstakes(results, sweeps);
    }
}

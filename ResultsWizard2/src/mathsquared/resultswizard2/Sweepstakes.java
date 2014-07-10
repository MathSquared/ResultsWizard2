/**
 * 
 */
package mathsquared.resultswizard2;

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
    public Fraction[] assignPoints (int[] quantities, int[] spec, TiePlaceAssignment tiePlaceM, SweepstakesAssignment sweepsM) {
        if (sweepsM == SweepstakesAssignment.CUSTOM) {
            return null; // the app should be prompting--leave that to the GUI code, not the back-end
        }

        Fraction[] ret = new Fraction[quantities.length];

        int currPlace = 1; // tracks which place we're on, since multiple places are condensed into one entry in quantities
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
}

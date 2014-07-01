/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Represents a method for assigning sweepstakes points to multiple tied competitors. Responding to each of these instances is handled by another class.
 * 
 * @author MathSquared
 * 
 */
public enum SweepstakesAssignment {
    /**
     * Indicates the place assigned by this event's {@link TiePlaceAssignment}.
     */
    TIE_PLACE,
    /**
     * Indicates the place at the top of the range.
     */
    TOP,
    /**
     * Indicates the place at the bottom of the range.
     */
    BOTTOM,
    /**
     * Indicates the place in the middle of the range, rounding towards the better place.
     */
    MID_ROUND_BETTER,
    /**
     * Indicates the place in the middle of the range, rounding towards the worse place.
     */
    MID_ROUND_WORSE,
    /**
     * Indicates the average of all the sweepstakes point assignments within the range.
     */
    AVERAGE,
    /**
     * Indicates the average of all the sweepstakes point assignments within the range, excluding those for the highest and lowest place.
     */
    AVERAGE_ADJUSTED,
    /**
     * Indicates the point total assigned to the middle of the range, or the average of those assigned to the middle two places.
     * 
     * <p>
     * Note that this is not a true mathematical median, since it takes the middle places' sweepstakes totals, not the median of all of the sweepstakes totals in range. So, if the sweepstakes for an event are higher in second and third place than first and fourth, and there is a four-way tie for first through fourth, this method selects the average of the point totals assigned to second and third place.
     * </p>
     */
    MEDIAN,
    /**
     * Indicates that an application should prompt for point totals in case of a tie.
     */
    CUSTOM;
}

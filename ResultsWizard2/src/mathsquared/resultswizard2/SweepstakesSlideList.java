/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Map;

/**
 * Represents a list of {@link Slide}s representing the current total amount of sweepstakes points earned by each school.
 * 
 * @author MathSquared
 * 
 */
public interface SweepstakesSlideList extends SlideList {
    /**
     * Obtains the sweepstakes totals represented by this SlideList.
     * 
     * @return a mapping from school names to the displayed amount of sweepstakes points they have earned
     */
    public Map<String, Fraction> getSweeps ();
}

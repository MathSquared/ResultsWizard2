/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * An immutable object that represents a proper fraction. Can support improper fractions intermittently as well.
 * 
 * Note that any post-conditions on any methods of this class only apply after the constructor has successfully completed, as several clean-up methods are required to ensure that a Fraction is represented in a canonical form.
 * 
 * @author MathSquared
 * 
 */
public class Fraction { // TODO write unit tests
    private int unit;
    private boolean unitValid = false; // Turns true when a unit is calculated and false when it is extracted
    private int numerator;
    private int denominator;

    public Fraction (int num, int den) {
        if (den == 0) {
            throw new ArithmeticException("Denominator must not be equal to 0");
        }

        numerator = num;
        denominator = den;

        canonicalizeDenominator();
        calculateUnit();
        matchUnitNumeratorSign();
        simplify();
    }

    // Setup methods //
    /*
     * Setup methods; called in constructor Before these methods are called, the Fraction may be in a non-canonical state.
     */

    /**
     * If the denominator of a fraction is negative, switches the sign of both the numerator and denominator. This way, the denominator is always positive.
     */
    private void canonicalizeDenominator () {
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    /**
     * Calculates the unit of a fraction based on the numerator and denominator. This has the effect that the fraction represented by the numerator and denominator becomes proper.
     */
    private void calculateUnit () {
        unit += numerator / denominator; // += in case there are already any other units that we want to remain valid--5u3/2 is 6u1/2
        numerator %= denominator; // % picks sign based on numerator, so numerator keeps current sign

        unitValid = true;
    }

    /**
     * Ensures that the sign of the unit and numerator match.
     */
    private void matchUnitNumeratorSign () {
        // If signs match, or if either quantity is 0 (which has no sign), we don't need to do anything
        if ((unit > 0 && numerator > 0) || (unit < 0 && numerator < 0) || (unit == 0 || numerator == 0)) { // last || is intentionally different
            return;
        }

        // Simply convert to improper and back
        numerator = getImproperNumerator();
        unit = 0;
        unitValid = false;
        calculateUnit();
    }

    /**
     * Simplifies a fraction to its lowest terms.
     */
    private void simplify () {
        int gcd = GcdUtils.gcd(numerator, denominator);

        numerator /= gcd;
        denominator /= gcd;
    }

    // Accessors //

    /**
     * Returns the unit currently associated with this fraction. If the unit is {@linkplain #isUnitValid() invalid}, this method returns 0. If the fraction represents a negative quantity, the value returned by this method will be negative.
     * 
     * @return the unit of this fraction
     */
    public int getUnit () {
        return (unitValid ? unit : 0);
    }

    /**
     * Discards the unit of this fraction, returning the number discarded. After this method is called, {@link #getUnit()} returns 0 and {@link #isUnitValid()} returns false. If the fraction represents a negative quantity, the value returned by this method will be negative.
     * 
     * @return the unit of this fraction
     */
    public int extractUnit () {
        int temp = unit;
        unitValid = false;
        unit = 0;

        return temp;
    }

    /**
     * Returns whether the unit of this fraction will be used in future arithmetic calculations.
     * 
     * <p>
     * The {@linkplain #getUnit() unit} mechanism was created to facilitate chaining of operations, especially additions. Since units are factored into calculations as long as they are valid, the unit can be disabled by the user when it is no longer needed. This method returns whether the unit will still be used in mathematical calculations.
     * </p>
     * 
     * @return whether the current unit is valid
     */
    public boolean isUnitValid () {
        return unitValid;
    }

    /**
     * Returns the numerator of the proper portion of this fraction. If the fraction represents a negative quantity, the value returned by this method will be negative.
     * 
     * @return the numerator
     */
    public int getNumerator () {
        return numerator;
    }

    /**
     * Returns the numerator of this fraction expressed as an improper fraction. If the unit is {@linkplain #isUnitValid() valid}, this is defined to be equal to <code>(numerator + unit*denominator)</code>; if the unit is invalid, this returns the same result as {@link #getNumerator()}.
     * 
     * @return the improper numerator
     */
    public int getImproperNumerator () {
        return numerator + (unitValid ? unit : 0) * denominator;
    }

    /**
     * Returns the denominator of this fraction. This method always returns a positive quantity; the sign is unambiguously determined by the {@linkplain #getNumerator() numerator}.
     * 
     * @return the denominator
     */
    public int getDenominator () {
        return denominator;
    }
}

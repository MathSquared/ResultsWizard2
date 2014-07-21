/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * An immutable object that represents a proper or improper fraction.
 * 
 * <p>
 * Note that any post-conditions on any methods of this class only apply after the constructor has successfully completed, as several clean-up methods are required to ensure that a Fraction is represented in a canonical form.
 * </p>
 * 
 * <p>
 * Objects of this class may be marked as invalid at any time if they have zero denominators. If a Fraction is marked as invalid, subsequent operations will throw an {@link UnsupportedOperationException}. This is done on a best-effort basis; a Fraction with a zero denominator is not guaranteed to be marked invalid immediately. Note that the public constructors will disallow the creation of a Fraction with a zero denominator, so such Fractions should not be encountered in normal usage.
 * </p>
 * 
 * @author MathSquared
 * 
 */
public class Fraction { // TODO write unit tests
    private int unit;
    private int numerator;
    private int denominator;

    // If the denominator is detected as 0, this is false--prevents finalizer exploits from propagating 0 denominators
    private boolean valid = true;

    /**
     * Constructs a Fraction representing the given whole number. This is equivalent to <code>Fraction(num, 1)</code> and is provided as a convenience method.
     * 
     * @param num the integer to represent
     */
    public Fraction (int num) {
        this(num, 1);
    }

    /**
     * Constructs a Fraction representing the quotient of two integers.
     * 
     * <p>
     * The Fraction is canonicalized as follows:
     * </p>
     * 
     * <ul>
     * <li>If the denominator of the fraction is negative, both the numerator and denominator are multiplied by -1.</li>
     * <li>A whole-number unit is extracted from the fraction. Thus, {@link #getNumerator()} returns what the numerator would be if this quotient were expressed as a mixed number; {@link #getImproperNumerator()} returns what it would be as an improper fraction.</li>
     * <li>The sign of the unit and numerator are matched. If they don't match, the fraction takes on the value of (unit + (numerator / denominator)); thus, (6)u(-1/2) becomes (5)u(1/2).</li>
     * <li>The fraction is simplified to its lowest terms by dividing out the GCD of the numerator and denominator.</li>
     * </ul>
     * 
     * @param num the numerator of the fraction
     * @param den the denominator of the fraction
     * @throws ArithmeticException if <code>den == 0</code>
     */
    public Fraction (int num, int den) {
        if (den == 0) {
            valid = false;
            throw new ArithmeticException("Denominator must not be equal to 0");
        }

        numerator = num;
        denominator = den;

        canonicalize();
    }

    /**
     * Canonicalizes this fraction according to the criteria {@linkplain #Fraction(int, int) noted above}. This method may be invoked at any time.
     */
    private void canonicalize () {
        canonicalizeDenominator();
        calculateUnit();
        matchUnitNumeratorSign();
        simplify();
    }

    /**
     * Marks this fraction as invalid and throws an {@link UnsupportedOperationException} if the denominator is 0.
     */
    private void checkDenominatorNonzero () {
        if (!valid || denominator == 0) {
            valid = false;
            throw new UnsupportedOperationException("Fraction invalid; denominator is 0");
        }
    }

    /**
     * If either this fraction or another given fraction have a zero denominator, throws an UnsupportedOperationException and marks the invalid fraction(s) as such.
     * 
     * @param secondOperand the other fraction to check
     */
    private void checkDenominatorNonzero (Fraction secondOperand) {
        String exceptionMessage = null; // tracks exception messages in case both fractions are invalid
        if (!valid || denominator == 0) {
            valid = false;
            exceptionMessage = "First operand invalid; denominator is 0";
        }
        if (!secondOperand.valid || secondOperand.getDenominator() == 0) {
            secondOperand.valid = false;
            exceptionMessage = (exceptionMessage == null ? "Second operand invalid; denominator is 0" : "Both fractions invalid; denominators are 0");
        }

        if (exceptionMessage != null) {
            throw new UnsupportedOperationException(exceptionMessage);
        }
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
        calculateUnit();
    }

    /**
     * Simplifies a fraction to its lowest terms.
     */
    private void simplify () {
        // Handle zero correctly to avoid BAD THINGS HAPPENING (like division by 0)
        if (numerator == 0) {
            denominator = 1; // 0/1 == 0/2 == 0/3.14159 == 0/-37 == ...
            return;
        }

        int gcd = GcdUtils.gcd(numerator, denominator);

        numerator /= gcd;
        denominator /= gcd;
    }

    // Accessors //

    /**
     * Returns the unit currently associated with this fraction. If the fraction represents a negative quantity, the value returned by this method will be negative.
     * 
     * @return the unit of this fraction
     */
    public int getUnit () {
        return unit;
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
     * Returns the numerator of this fraction expressed as an improper fraction. Equivalent to <code>(numerator + unit*denominator)</code>.
     * 
     * @return the improper numerator
     */
    // USE THIS METHOD FOR ARITHMETIC TO SUPPORT OPERATION CHAINING; DO NOT DIRECTLY EMPLOY numerator
    public int getImproperNumerator () {
        return numerator + unit * denominator;
    }

    /**
     * Returns the denominator of this fraction. This method always returns a positive quantity; the sign is unambiguously determined by the {@linkplain #getNumerator() numerator}.
     * 
     * @return the denominator
     */
    public int getDenominator () {
        return denominator;
    }

    // Arithmetic //

    /**
     * Negates a Fraction (finds its additive inverse). This is equivalent to multiplication by -1.
     * 
     * @return a new Fraction representing the negative of this Fraction
     */
    public Fraction negative () {
        checkDenominatorNonzero();
        return new Fraction(-getImproperNumerator(), denominator);
    }

    /**
     * Multiplies a Fraction by an integer.
     * 
     * @param multiplier the number by which to multiply
     * @return a new Fraction multiplied by the multiplier
     */
    public Fraction multiply (int multiplier) {
        checkDenominatorNonzero();
        return new Fraction(getImproperNumerator() * multiplier, denominator);
    }

    /**
     * Multiplies two Fractions together.
     * 
     * @param multiplier the Fraction by which to multiply
     * @return a new Fraction multiplied by the multiplier
     */
    public Fraction multiply (Fraction multiplier) {
        checkDenominatorNonzero(multiplier);
        return new Fraction(getImproperNumerator() * multiplier.getImproperNumerator(), denominator * multiplier.getDenominator());
    }

    /**
     * Divides a Fraction by an integer.
     * 
     * @param divisor the number by which to divide
     * @return a new Fraction divided by the divisor
     * @throws ArithmeticException if the divisor is 0
     */
    public Fraction divide (int divisor) {
        checkDenominatorNonzero();
        if (divisor == 0) {
            throw new ArithmeticException("divisor must not be 0");
        }
        return new Fraction(getImproperNumerator(), denominator * divisor);
    }

    /**
     * Divides a Fraction by another. This is equivalent to multiplication by the divisor's reciprocal.
     * 
     * @param divisor the Fraction by which to divide
     * @return a new Fraction divided by the divisor
     * @throws ArithmeticException if the numerator of the divisor is 0
     */
    public Fraction divide (Fraction divisor) {
        checkDenominatorNonzero(divisor);
        if (divisor.getImproperNumerator() == 0) {
            throw new ArithmeticException("numerator of the divisor must not be 0");
        }
        return new Fraction(getImproperNumerator() * divisor.getDenominator(), denominator * divisor.getImproperNumerator());
    }

    /**
     * Raises a Fraction to a power.
     * 
     * <p>
     * This method assumes that any fraction raised to the 0 power is equal to 1. If the given exponent is negative, this method acts as if <code>this.reciprocal().pow(-exponent)</code> was called.
     * 
     * @param exponent the power to which to raise this Fraction
     * @return a new Fraction raised to the given power
     */
    public Fraction pow (int exponent) {
        checkDenominatorNonzero();
        // Math.pow will return non-integer results for negative exponents, so we take the reciprocal here first
        if (exponent < 0) {
            return reciprocal().pow(-exponent);
        }
        if (exponent == 0) {
            return new Fraction(1, 1);
        }

        // use integer power function for max precision (avoid floating-point round-off error)
        return new Fraction(GcdUtils.pow(getImproperNumerator(), exponent), GcdUtils.pow(denominator, exponent));
    }

    /**
     * Finds the reciprocal (multiplicative inverse) of a Fraction. The new Fraction is canonicalized, so it may be the case that <code>fraction.getNumerator() != fraction.reciprocal().getDenominator()</code>.
     * 
     * @return a new Fraction representing the denominator of this Fraction divided by the numerator
     * @throws ArithmeticException if the Fraction represents a zero quantity, i.e. if <code>(getImproperNumerator() == 0)</code>
     */
    public Fraction reciprocal () {
        checkDenominatorNonzero();
        if (getImproperNumerator() == 0) {
            throw new ArithmeticException("Cannot take the reciprocal of a zero fraction");
        }
        return new Fraction(denominator, getImproperNumerator());
    }

    /**
     * Adds an integer to a Fraction.
     * 
     * @param augend the integer to add to this Fraction
     * @return a new Fraction representing the result of <code>(this + augend)</code>
     */
    public Fraction add (int augend) {
        checkDenominatorNonzero();
        return new Fraction(getImproperNumerator() + augend * denominator, denominator);
    }

    /**
     * Adds two Fractions together.
     * 
     * @param augend the Fraction to which to add this one
     * @return a new Fraction representing the result of <code>(this + augend)</code>
     */
    public Fraction add (Fraction augend) {
        checkDenominatorNonzero(augend);
        int lcm = GcdUtils.lcm(denominator, augend.getDenominator());
        int multiplyA = lcm / denominator;
        int multiplyB = lcm / augend.getDenominator();

        int numA = getImproperNumerator();
        int numB = augend.getImproperNumerator();

        return new Fraction(numA * multiplyA + numB * multiplyB, lcm);
    }

    /**
     * Subtracts an integer from a Fraction.
     * 
     * @param subtrahend the integer to subtract from this Fraction
     * @return a new Fraction representing the result of <code>(this - subtrahend)</code>
     */
    public Fraction subtract (int subtrahend) {
        checkDenominatorNonzero();
        return add(-subtrahend);
    }

    /**
     * Subtracts two Fractions.
     * 
     * @param subtrahend the Fraction to subtract from this Fraction
     * @return a new Fraction representing the result of <code>(this - subtrahend)</code>
     */
    public Fraction subtract (Fraction subtrahend) {
        checkDenominatorNonzero(subtrahend);
        return add(subtrahend.negative());
    }

    /**
     * Converts a Fraction to the double it represents. This is done as if returning <code>((double) {@link #getImproperNumerator()} / denominator)</code>.
     * 
     * @return the double equivalent of this Fraction
     */
    public double toDouble () {
        return (double) getImproperNumerator() / denominator;
    }

    /**
     * Tests whether a Fraction is equivalent to another object.
     * 
     * <p>
     * A Fraction is considered equal to another object <code>other</code> if and only if:
     * </p>
     * 
     * <ul>
     * <li><code>other</code> is a <code>Byte</code>, <code>Short</code>, <code>Integer</code>, or <code>Long</code> and this fraction represents a whole number that is precisely equal to the number represented by <code>other</code></li>
     * <li><code>other</code> is a <code>Float</code> or <code>Double</code> and <code>this.toDouble()</code> precisely equals the value represented by <code>other</code></li>
     * <li><code>other</code> is another <code>Fraction</code> and both fractions' {@linkplain #getImproperNumerator() improper numerators} and {@linkplain #getDenominator() denominators} are equal to each other when both fractions are in lowest terms</li>
     * </ul>
     */
    public boolean equals (Object other) {
        canonicalize();

        // Check some special cases
        if (other instanceof Byte || other instanceof Short || other instanceof Integer || other instanceof Long) {
            // ensure number is correct && fraction is a whole number (entire denominator can be divided out)
            return getImproperNumerator() / denominator == ((Number) other).intValue() && GcdUtils.gcd(getImproperNumerator(), denominator) == denominator;
        } else if (other instanceof Float || other instanceof Double) {
            return toDouble() == ((Number) other).doubleValue();
        } else if (other instanceof Fraction) {
            Fraction fOther = (Fraction) other;
            fOther.canonicalize();
            return unit == fOther.getUnit() && numerator == fOther.getNumerator() && denominator == fOther.getDenominator();
        } else {
            return false;
        }
    }
}

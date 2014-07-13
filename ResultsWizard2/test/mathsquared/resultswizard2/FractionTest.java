/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author MathSquared
 * 
 */
public class FractionTest {

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#Fraction(int)}.
     */
    @Test
    public void testFractionInt () {
        Fraction four = new Fraction(4);
        assertEquals("Not stupid", four, new Fraction(4, 1));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#Fraction(int, int)}.
     */
    @Test
    public void testFractionIntInt () {
        Fraction twoFourths = new Fraction(2, 4);
        assertTrue("Canonical: Simplification", twoFourths.getUnit() == 0 && twoFourths.getNumerator() == 1 && twoFourths.getDenominator() == 2);

        Fraction zero = new Fraction(0, 3);
        assertTrue("Canonical: 0/n", zero.getUnit() == 0 && zero.getNumerator() == 0 && zero.getDenominator() == 1);

        Fraction sevenFifths = new Fraction(7, 5);
        assertTrue("Canonical: Unit calculation", sevenFifths.getUnit() == 1 && sevenFifths.getNumerator() == 2 && sevenFifths.getDenominator() == 5);

        Fraction twoNegativeThirds = new Fraction(2, -3);
        assertTrue("Canonical: Negative numerator", twoNegativeThirds.getUnit() == 0 && twoNegativeThirds.getNumerator() == -2 && twoNegativeThirds.getDenominator() == 3);

        Fraction negativeEightSixths = new Fraction(-8, 6);
        assertTrue("Canonical: Evil simplification", negativeEightSixths.getUnit() == -1 && negativeEightSixths.getNumerator() == -1 && negativeEightSixths.getDenominator() == 3);

        Fraction negativeThreeNegativeSevenths = new Fraction(-3, -7);
        assertTrue("Canonical: Double negative", negativeThreeNegativeSevenths.getUnit() == 0 && negativeThreeNegativeSevenths.getNumerator() == 3 && negativeThreeNegativeSevenths.getDenominator() == 7);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#getUnit()}, {@link mathsquared.resultswizard2.Fraction#extractUnit()}, and {@link mathsquared.resultswizard2.Fraction#isUnitValid()}.
     */
    @Test
    public void testExtractUnit () {
        Fraction sevenThirds = new Fraction(7, 3);
        int unit = sevenThirds.getUnit();
        assertTrue("Correctness", sevenThirds.getUnit() == 7 / 3);
        Fraction sevenNegativeThirds = new Fraction(7, -3);
        assertTrue("Negativity", sevenNegativeThirds.getUnit() == 7 / -3);
        assertTrue("Correctly flagged", sevenThirds.isUnitValid());

        int extract = sevenThirds.extractUnit();
        assertTrue("Extracted is same", unit == extract);
        assertFalse("Invalidation", sevenThirds.isUnitValid());
        assertTrue("Zeroing", sevenThirds.getUnit() == 0);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#getImproperNumerator()}.
     */
    @Test
    public void testGetImproperNumerator () {
        assertTrue("Basic", new Fraction(3, 2).getImproperNumerator() == 3);
        assertTrue("Zero", new Fraction(0).getImproperNumerator() == 0);
        assertTrue("Negative", new Fraction(4, -3).getImproperNumerator() == -4);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#negative()}.
     */
    @Test
    public void testNegative () {
        assertTrue("Proper", new Fraction(2, 3).negative().equals(new Fraction(-2, 3)));
        assertTrue("Improper", new Fraction(5, 4).negative().equals(new Fraction(-5, 4)));
        assertTrue("Zero", new Fraction(0).negative().equals(new Fraction(0)));
        assertTrue("Perfect unit", new Fraction(3).negative().equals(new Fraction(-3)));
        assertTrue("Already negative improper", new Fraction(-10, 7).negative().equals(new Fraction(10, 7)));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#multiply(int)}.
     */
    @Test
    public void testMultiplyInt () {
        assertTrue("Proper to proper", new Fraction(1, 3).multiply(2).equals(new Fraction(2, 3)));
        assertTrue("Proper to improper", new Fraction(2, 7).multiply(5).equals(new Fraction(10, 7)));
        assertTrue("Improper to improper", new Fraction(7, 4).multiply(3).equals(new Fraction(21, 4)));
        assertTrue("Perfect unit", new Fraction(7).multiply(2).equals(new Fraction(14)));
        assertTrue("Zero multiplier", new Fraction(4, 5).multiply(0).equals(new Fraction(0)));
        assertTrue("Negative multiplicand", new Fraction(-3, 5).multiply(3).equals(new Fraction(-9, 5)));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#multiply(mathsquared.resultswizard2.Fraction)}.
     */
    @Test
    public void testMultiplyFraction () {
        assertTrue("Proper to proper", new Fraction(1, 3).multiply(new Fraction(2, 5)).equals(new Fraction(2, 15)));
        assertTrue("Proper times improper", new Fraction(2, 3).multiply(new Fraction(4, 3)).equals(new Fraction(8, 9)));
        assertTrue("Improper times proper", new Fraction(4, 3).multiply(new Fraction(2, 3)).equals(new Fraction(8, 9)));
        assertTrue("Perfect unit times proper", new Fraction(6).multiply(new Fraction(2, 5)).equals(new Fraction(12, 5)));
        assertTrue("Negative handling", new Fraction(-4, 3).multiply(new Fraction(2, -5)).equals(new Fraction(8, 15)));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#divide(int)}.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivideInt () {
        assertTrue("Proper to proper", new Fraction(4, 5).divide(3).equals(new Fraction(4, 15)));
        assertTrue("Improper to proper", new Fraction(7, 3).divide(8).equals(new Fraction(7, 24)));
        assertTrue("Improper to improper", new Fraction(15, 7).divide(2).equals(new Fraction(15, 14)));
        assertTrue("Perfect unit and simplification", new Fraction(16).divide(4).equals(new Fraction(4)));
        assertTrue("Zero multiplier", new Fraction(0).divide(57).equals(0));
        assertTrue("Negative over negative", new Fraction(-3, 5).divide(-2).equals(new Fraction(3, 10)));

        // exception
        new Fraction(4, 7).divide(0);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#divide(mathsquared.resultswizard2.Fraction)}.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivideFraction () {
        assertTrue("Proper over proper", new Fraction(3, 4).divide(new Fraction(2, 5)).equals(new Fraction(15, 8)));
        assertTrue("Proper over improper and simp.", new Fraction(4, 7).divide(new Fraction(6, 5)).equals(new Fraction(10, 21)));
        assertTrue("Improper over proper", new Fraction(8, 5).divide(new Fraction(3, 4)).equals(new Fraction(32, 15)));
        assertTrue("Two perfect units", new Fraction(5).divide(new Fraction(2)).equals(new Fraction(5, 2)));
        assertTrue("Negative handling", new Fraction(9, -5).divide(new Fraction(-7, 4)).equals(new Fraction(36, 35)));

        // exception
        new Fraction(8, 5).divide(new Fraction(0, 17));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#pow(int)}.
     */
    @Test
    public void testPow () {
        Fraction negativeTwoThirds = new Fraction(-2, 3);
        Fraction fourNinths = new Fraction(4, 9);
        Fraction negativeEightTwentySevenths = new Fraction(-8, 27);
        Fraction negativeThreeHalves = new Fraction(-3, 2);
        Fraction nineFourths = new Fraction(9, 4);

        assertEquals("^2", negativeTwoThirds.pow(2), fourNinths);
        assertEquals("^3", negativeTwoThirds.pow(3), negativeEightTwentySevenths);
        assertEquals("^-1", negativeTwoThirds.pow(-1), negativeThreeHalves);
        assertEquals("^-2", negativeTwoThirds.pow(-2), nineFourths);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#reciprocal()}.
     */
    @Test(expected = ArithmeticException.class)
    public void testReciprocal () {
        Fraction threeFourths = new Fraction(3, 4);
        assertEquals("3/4", threeFourths.reciprocal(), new Fraction(4, 3));

        Fraction zero = new Fraction(0);
        zero.reciprocal(); // throws ArithmeticException
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#add(int)}. Negative arguments to <code>add(int)</code> are tested in {@link #testSubtractInt()}.
     */
    @Test
    public void testAddInt () {
        assertTrue("Proper", new Fraction(2, 3).add(1).equals(new Fraction(5, 3)));
        assertTrue("Improper", new Fraction(7, 5).add(3).equals(new Fraction(7 + 15, 5)));
        assertTrue("Perfect unit", new Fraction(3).add(1).equals(4));
        assertTrue("Proper neg+pos", new Fraction(-3, 4).add(1).equals(new Fraction(1, 4)));
        assertTrue("Improper neg+pos->neg", new Fraction(-9, 4).add(1).equals(new Fraction(-5, 4)));
        assertTrue("Improper neg+pos->pos", new Fraction(-5, 3).add(3).equals(new Fraction(4, 3)));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#add(mathsquared.resultswizard2.Fraction)}.
     */
    @Test
    public void testAddFraction () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#subtract(int)}. Negative arguments to <code>subtract(int)</code> are tested in {@link #testAddInt()}.
     */
    @Test
    public void testSubtractInt () {
        assertTrue("Proper neg+neg", new Fraction(-1, 4).subtract(3).equals(new Fraction(-13, 4)));
        assertTrue("Improper neg+neg", new Fraction(-8, 5).subtract(1).equals(new Fraction(-13, 5)));
        assertTrue("Perfect unit neg", new Fraction(-4).subtract(2).equals(new Fraction(-6)));
        assertTrue("Proper pos+neg", new Fraction(5, 7).subtract(2).equals(new Fraction(-9, 7)));
        assertTrue("Improper pos+neg->pos", new Fraction(13, 6).subtract(1).equals(new Fraction(7, 6)));
        assertTrue("Improper pos+neg->neg", new Fraction(10, 7).subtract(4).equals(new Fraction(-18, 7)));
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#subtract(mathsquared.resultswizard2.Fraction)}.
     */
    @Test
    public void testSubtractFraction () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#toDouble()}.
     */
    @Test
    public void testToDouble () {
        Fraction half = new Fraction(1, 2);
        assertTrue("1/2", half.toDouble() == 1.0 / 2);

        Fraction fiveThirds = new Fraction(5, 3);
        assertTrue("5/3", fiveThirds.toDouble() == 5.0 / 3);
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject () {
        Fraction f1 = new Fraction(2, 4);
        Fraction f2 = new Fraction(1, 2);
        assertEquals("Simplification", f1, f2);

        Fraction f3 = new Fraction(3);
        Fraction f4 = new Fraction(3, 1);
        assertEquals("Different constructors", f3, f4);

        Fraction f5 = new Fraction(7, 5);
        Fraction f6 = new Fraction(7, 5);
        Fraction f7 = new Fraction(14, 10);
        assertEquals("Improper", f5, f6);
        assertEquals("Improper uncanonical", f5, f7);

        assertEquals("Double", f2, 1.0 / 2);
        assertEquals("Int", f3, 3);
    }

}

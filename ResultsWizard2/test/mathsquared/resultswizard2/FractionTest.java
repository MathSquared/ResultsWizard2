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
    @Test
    public void testDivideInt () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#divide(mathsquared.resultswizard2.Fraction)}.
     */
    @Test
    public void testDivideFraction () {
        fail("Not yet implemented");
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
     * Test method for {@link mathsquared.resultswizard2.Fraction#add(int)}.
     */
    @Test
    public void testAddInt () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#add(mathsquared.resultswizard2.Fraction)}.
     */
    @Test
    public void testAddFraction () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#subtract(int)}.
     */
    @Test
    public void testSubtractInt () {
        fail("Not yet implemented");
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
        assertTrue("1/2", half.toDouble() == 1 / 2);

        Fraction fiveThirds = new Fraction(5, 3);
        assertTrue("5/3", fiveThirds.toDouble() == 5 / 3);
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

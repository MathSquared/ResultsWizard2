/**
 * 
 */
package mathsquared.resultswizard2;

import static org.junit.Assert.assertEquals;
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
     * Test method for {@link mathsquared.resultswizard2.Fraction#extractUnit()}.
     */
    @Test
    public void testExtractUnit () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#getImproperNumerator()}.
     */
    @Test
    public void testGetImproperNumerator () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#negative()}.
     */
    @Test
    public void testNegative () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#multiply(int)}.
     */
    @Test
    public void testMultiplyInt () {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link mathsquared.resultswizard2.Fraction#multiply(mathsquared.resultswizard2.Fraction)}.
     */
    @Test
    public void testMultiplyFraction () {
        fail("Not yet implemented");
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

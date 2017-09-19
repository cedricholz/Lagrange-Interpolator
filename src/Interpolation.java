import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Cedric Holz on 4/9/2017.
 */

public class Interpolation {

    /**
     * Constuctor
     */
    public Interpolation(){
    }

    /**
     * Multiplies two polynomials together.
     *
     * @param poly1
     * @param poly2
     * @return poly1 + poly 2
     */
    public static ArrayList<BigDecimal> multiplyPolynomials(ArrayList<BigDecimal> poly1, ArrayList<BigDecimal> poly2) {
        int totalLength = poly1.size() + poly2.size() - 1;
        ArrayList<BigDecimal> r = new ArrayList<>();
        for (int i = 0; i < totalLength; i++) {
            r.add(new BigDecimal("0.0"));
        }
        for (int i = 0; i < poly1.size(); i++) {
            for (int j = 0; j < poly2.size(); j++) {
                BigDecimal bd = r.get(i + j).add(poly1.get(i).multiply(poly2.get(j)));
                r.set(i + j, bd);
            }
        }
        return r;
    }


    /**
     * Add's two polynomials together.
     *
     * @param firstPoly
     * @param secondPoly
     * @return firstPoly + secondPoly
     */
    public static ArrayList<BigDecimal> addPolynomials(ArrayList<BigDecimal> firstPoly, ArrayList<BigDecimal> secondPoly) {
        if (firstPoly.size() == 0) {
            return secondPoly;
        }
        ArrayList<BigDecimal> r = new ArrayList<>();
        for (int i = 0; i < firstPoly.size(); i++) {
            BigDecimal added = firstPoly.get(i).add(secondPoly.get(i));
            r.add(added);
        }
        return r;
    }

    /**
     * Returns the correct unicode superscript for a power.
     *
     * @param power
     * @return tSuperscript
     */
    public static String tSuperscript(int power) {
        switch (power) {
            case 0:
                return "";
            case 1:
                return "t";
            case 2:
                return "t²";
            case 3:
                return "t³";
            case 4:
                return "t⁴";
            case 5:
                return "t⁵";
            case 6:
                return "t⁶";
            case 7:
                return "t⁷";
            case 8:
                return "t⁸";
            case 9:
                return "t⁹";
            default:
                return "t^" + power;
        }
    }

    /**
     * Takes a polynomial, in the form of an Arraylist with the 0th
     * value corresponding to the highest powered coefficient, and turns it
     * into a string in the form of p(t) = t^3 + t^2 + t + 1.
     *
     * @param poly
     * @return polynomial string
     */
    public static String polyToString(ArrayList<BigDecimal> poly) {
        int power = poly.size() - 1;
        String r = "p(t) = ";
        for (int i = 0; i < poly.size(); i++) {
            BigDecimal coefficient = poly.get(i).setScale(10, RoundingMode.HALF_UP);
            if (i == 0) {
                r += coefficient + tSuperscript(power);
            } else {
                if (coefficient.compareTo(BigDecimal.ZERO) < 0) {
                    r += " - " + coefficient.negate() + tSuperscript(power);
                } else {
                    r += " + " + coefficient + tSuperscript(power);
                }
            }
            power -= 1;
        }
        return r;
    }

    //Multiplies a polynomial, in the form of an array, by a constant.
    public static ArrayList<BigDecimal> multiplybyConstant(ArrayList<BigDecimal> p, BigDecimal constant) {
        for (int i = 0; i < p.size(); i++) {
            p.set(i, p.get(i).multiply(constant));
        }
        return p;
    }

    /**
     * Utilizes Lagrange's Interpolation method to create a polynomial from
     * two BigDecimal ArrayLists of x,y points.
     *
     * @param xVals
     * @param yVals
     * @return completePolynomial
     */
    public static ArrayList<BigDecimal> interpolate(ArrayList<BigDecimal> xVals, ArrayList<BigDecimal> yVals) {
        assert (xVals.size() == yVals.size()) : "X values and Y values are of different size.";
        ArrayList<BigDecimal> completePolynomial = new ArrayList<>();

        for (int i = 0; i < xVals.size(); i++) {
            //First Polynomial at index i + 1 i.e (x - startingX)
            ArrayList<BigDecimal> term = new ArrayList<>();
            term.add(new BigDecimal("1.0"));
            int startIndex = (i + 1) % xVals.size();
            BigDecimal startX = xVals.get(startIndex);
            term.add(startX.negate());
            BigDecimal currentX = xVals.get(i);

            //Start the denominator equation i.e (currentX - startingX)
            BigDecimal denominator = currentX.subtract(startX);

            for (int j = 0; j < xVals.size(); j++) {
                if (j != i && j != startIndex) {
                    //Piece together polynomial for term
                    ArrayList<BigDecimal> temp = new ArrayList<>();
                    temp.add(new BigDecimal("1.0"));
                    temp.add(xVals.get(j).negate());
                    term = multiplyPolynomials(term, temp);

                    //Piece together denominator for term
                    denominator = denominator.multiply(currentX.subtract(xVals.get(j)));
                }
            }

            //Find constant by calculating Yi/denominator
            BigDecimal constant;
            //If the division is irrational, round to 1000 decimal places.
            try {
                constant = yVals.get(i).divide(denominator);
            } catch (RuntimeException e) {
                constant = yVals.get(i).divide(denominator, 1000, RoundingMode.HALF_UP);
            }

            //Multiply term by constant
            term = multiplybyConstant(term, constant);

            //Add term to complete Polynmial
            completePolynomial = addPolynomials(completePolynomial, term);
        }
        return completePolynomial;
    }

    /**
     * Takes in a polynomial, evaluates it at an x values, and returns the
     * corresponding y value.
     *
     * @param poly
     * @param xVal
     * @return yVal
     */
    public static BigDecimal evaluatePolynomial(ArrayList<BigDecimal> poly, BigDecimal xVal) {
        int power = poly.size() - 1;
        BigDecimal yVal = new BigDecimal("0");
        for (int i = 0; i < poly.size(); i++) {
            BigDecimal term = xVal.pow(power).multiply(poly.get(i));
            yVal = yVal.add(term);
            power -= 1;
        }
        return yVal.setScale(5, RoundingMode.HALF_UP);
    }

}

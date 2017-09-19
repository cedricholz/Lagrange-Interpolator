import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Cedric on 4/9/2017.
 */
public class TestInterpolation {

    public void testMapping(ArrayList<BigDecimal> xVals, ArrayList<BigDecimal> yVals) {
        //Interpolation object
        Interpolation function = new Interpolation();

        //Polynomial created from xVals, and yVals.
        ArrayList p = function.interpolate(xVals, yVals);
        /*
         Checks to see if each x value maps to the correct y value when
         evaluated on polynomial.
         */
        for (int i = 0; i < xVals.size(); i++) {
            BigDecimal y = function.evaluatePolynomial(p, xVals.get(i));
            assertEquals(y.setScale(5, RoundingMode.HALF_UP), yVals.get(i).setScale(5, RoundingMode.HALF_UP));
        }

        //Print the polynomial
        System.out.println(function.polyToString(p));
    }

    @Test
    public void testValues() {
        ArrayList<BigDecimal> xVals = new ArrayList<>();
        ArrayList<BigDecimal> yVals = new ArrayList<>();

        xVals.add(new BigDecimal("4.1168"));
        xVals.add(new BigDecimal("4.19236"));
        xVals.add(new BigDecimal("4.20967"));
        xVals.add(new BigDecimal("4.46908"));


        yVals.add(new BigDecimal("0.213631"));
        yVals.add(new BigDecimal("0.214232"));
        yVals.add(new BigDecimal("0.21441"));
        yVals.add(new BigDecimal("0.218788"));
        testMapping(xVals, yVals);

        /*
          p(t) = -0.0035524462t³ + 0.0695519306t² - 0.3860077390t + 0.8718388143
         */

        xVals.clear();
        yVals.clear();

        xVals.add(new BigDecimal("1491505186.0"));
        xVals.add(new BigDecimal("1491505191.0"));
        xVals.add(new BigDecimal("1491505196.0"));
        xVals.add(new BigDecimal("1491505201.0"));
        xVals.add(new BigDecimal("1491505206.0"));
        xVals.add(new BigDecimal("1491505211.0"));
        xVals.add(new BigDecimal("1491505216.0"));
        xVals.add(new BigDecimal("1491505221.0"));
        xVals.add(new BigDecimal("1491505226.0"));

        yVals.add(new BigDecimal("5.0"));
        yVals.add(new BigDecimal("4.30"));
        yVals.add(new BigDecimal("4.12"));
        yVals.add(new BigDecimal("2.0"));
        yVals.add(new BigDecimal("1.0"));
        yVals.add(new BigDecimal("5.0"));
        yVals.add(new BigDecimal("3.21"));
        yVals.add(new BigDecimal("2.0"));
        yVals.add(new BigDecimal("0.40"));

        testMapping(xVals, yVals);

        /*
        p(t) = -1.03E-8t⁸ + 122.5477495917t⁷ - 639732122446.8708093390t⁶
        + 1908327581249361361384.9310207159t⁵ - 3557850651054612905488845263397.3015384647t⁴
        + 4245242212571484912704037532403778221280.5734603431t³
        - 3165900428896719805875908558010781757052241454215.2767457566t²
        + 1349130562613961294867755716905197397472923240007334860402.8926744986t
        - 251529407095360793945652255076574160207971603694127901193153849428.6958115789
         */

    }
}

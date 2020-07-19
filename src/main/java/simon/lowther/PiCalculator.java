
package simon.lowther;

import java.util.Scanner;
import java.lang.NumberFormatException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 
 * WARNING: Calculating Pi to large decimal places can take a long time and lock your computer up.
 * Consider running this class in a thread.
 * 
 * PiCalculator can be run from the command line where the main() will be run, or alternatively
 * instantiated using the provided constructor PiCalculator(), then running the method
 * public int Calculate(int DecimalPlaces) where DecimalPlaces is the requried number of decimal places.
 * 
 * The number of itterations used for the calculation can be read from the commandline after execution,
 * or found by using the public int getItterationsUsed() method which returns an int as the used itterations.
 * 
 * These methods return Pi as a String object as other types do not have the required accuracy to contain the 
 * number of decimal places returned.
 * 
 * Alternatively a BigDecimal copy can be obtained using getBigDecimalPi() after running the calculation() method
 * 
 */
public class PiCalculator {

    private  BigDecimal decimalNum = BigDecimal.valueOf(10);
    private  BigDecimal prevPi = BigDecimal.valueOf(3.1);
    private  int scale;
    private int IterationsUsed=0;
    private String Pi = "";
    private BigDecimal bigDecimalPi = BigDecimal.valueOf(3.1);
    
    public PiCalculator() {};
    
    public int getIterationsUsed() {
	return IterationsUsed;
    }
    
    public String getPi() {
	return Pi;
    }
    
    @Override
    public String toString() {
	return Pi;
    }
    
   public BigDecimal getBigDecimalPi() {
	return bigDecimalPi;
    }

    public static void main(String[] args) {
	PiCalculator picalc = new PiCalculator();
	picalc.getInput();
	System.out.println((picalc.arcTan()));
	System.out.println("Iterations: " + picalc.getIterationsUsed());
    }

    public void getInput() throws NumberFormatException {

	Scanner input = new Scanner(System.in);
	System.out.println("This program calculates pi to x decimal places... ");
	System.out.println("WARNING: This can take a long time, caution with the number of requested decimal places");
	System.out.println("Enter decimal places: ");
	String decimalPlaces = input.nextLine();
	try {

	    if (Integer.parseInt(decimalPlaces) == 0) {
		throw new NumberFormatException("Input or 0 is not valid");
	    } else {
		scale = Integer.parseInt(decimalPlaces) + 10;
		decimalNum = decimalNum.pow(scale);
	    }

	} catch (NumberFormatException e) {
	    System.out.println("Input error, please enter an number between 1 - 1*10^6");
	} finally {
	    input.close();
	    System.out.println("Calculating...");
	}

    }

    public String calculation(int decimalPlaces) {

	scale = (decimalPlaces) + 10;
	decimalNum = decimalNum.pow(scale);
	BigDecimal myPi = BigDecimal.valueOf(0);
	myPi = (arcTan());
	String answer = myPi.toString();
	answer = answer.substring(0,answer.length()-10);
	return answer;
    }

	
    /*
     * calculation of Pi using the formula pi/4 = 4*Arctan(1/5) - Arctan(1/239)
     * This method calculates additonal itterations of the above term by term
     * until the doCheck() method which checks for convergence returns true
     */
    private BigDecimal arcTan() {
	boolean sign = true;
	int i = 1;
	BigDecimal result = BigDecimal.valueOf(0);	
	BigDecimal term1 = BigDecimal.valueOf(0.2);	// 1/5
	BigDecimal term2 = new BigDecimal(0);
	term2 = new BigDecimal(1).divide(new BigDecimal(239),scale,RoundingMode.HALF_UP);
	BigDecimal four = BigDecimal.valueOf(4);
	BigDecimal nextTermIterration = BigDecimal.valueOf(0);
	BigDecimal term1Calc = BigDecimal.valueOf(0);
	BigDecimal term2Calc = BigDecimal.valueOf(0);
	BigDecimal index = BigDecimal.valueOf(i); 
	
	do {
	    index = BigDecimal.valueOf(i);
	    	       
	    term1Calc = ((term1.pow(i)).divide(index, scale, RoundingMode.HALF_UP)).multiply(four);
	    term2Calc = ((term2.pow(i)).divide(index, scale, RoundingMode.HALF_UP));

	    nextTermIterration = ((term1Calc.subtract(term2Calc)).multiply(four));

	    i = i + 2;

	    if (sign) {
		result = result.add(nextTermIterration);
		sign = false;
	    } else {
		result = result.subtract(nextTermIterration);
		sign = true;
	    }
	    	 
	    
	} while (doCheck(result));
	IterationsUsed = i / 2;
	bigDecimalPi = result;
	return result;
    }

    /*
     * Checks for convergence to the required number of decimal places
     * Returns false until the check passes then returns true.
     */
    public boolean doCheck(BigDecimal currentPi) {

	BigDecimal checkBig = new BigDecimal(0);
	checkBig = ((currentPi.subtract(prevPi)).multiply(decimalNum));
	if (checkBig.abs().compareTo(new BigDecimal(0)) < 1) {
	    
	    prevPi = currentPi;
	    return false;
	    
	} else {

	    prevPi = currentPi;
	    return true;
	}

    } 

}

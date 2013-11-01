/**  This is an abstract class which contains a number of utility methods for manipulating numbers
 *  
 */
package com.copyright.ccc.util;

//import com.copyright.ccc.CCRuntimeException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * NumberTools provides static methods useful for working with numbers.
 * 
 * 
 */
public abstract class NumberTools {
	/**
	 * This method takes in a double value and rounds it to two decimal places using the ROUND_HALF_EVEN algorithm.
	 * 
	 * @deprecated should use the roundMoney method.
	 * @param num unrounded double amount
	 * @return double rounded double amount
	 */
	@Deprecated
	public static double round(double num) {
		/* All numbers should be calculated to a precision of 2 decimal places. */
		DecimalFormat nf = new DecimalFormat();
		nf.setMinimumIntegerDigits(1);
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);

		return (new Double(nf.format(num))).doubleValue();
	}

	/**
	 * This method takes in a double value and rounds it to two decimal places
	 * 
	 * @param num unrounded double amount
	 * @return double rounded double amount
	 */
	public static double roundMoney(double num) {
		/* All numbers should be calculated to a precision of 2 decimal places. */
		return roundMoney(num, 2);
	}
	
	/**
	 * This method takes in a double value and rounds up
	 * 
	 * @param num unrounded double amount
	 * @return double rounded double amount
	 */
	public static double roundMoneyUp(double num) {
		/* All numbers should be calculated to a precision of 0 decimal places. */
		return roundMoney(num,0);
	}

	/**
	 * This method takes in a double value and rounds it to the specified number of decimal places
	 * 
	 * @param num unrounded double amount
	 * @param fractionalDigits number of fractional digits
	 * @return double rounded double amount
	 */
	public static double roundMoney(double num, int fractionalDigits) {
		BigDecimal bd = new BigDecimal(String.valueOf(num));
		bd = bd.setScale(fractionalDigits, BigDecimal.ROUND_HALF_EVEN);
		return bd.doubleValue();
	}

	/**
	 * This method takes in a double value and rounds it to one decimal places
	 * 
	 * @param num unrounded double amount
	 * @return double rounded double amount
	 */
	public static double roundOnePlace(double num) {
		/* All numbers should be calculated to a precision of 2 decimal places. */
		DecimalFormat nf = new DecimalFormat();
		nf.setMinimumIntegerDigits(1);
		nf.setMaximumFractionDigits(1);
		nf.setGroupingUsed(false);

		return (new Double(nf.format(num))).doubleValue();
	}
		
	public static String getDoubleAsMoneyString(double value) {
		NumberFormat _nf = NumberFormat.getInstance();
		_nf.setMinimumFractionDigits(2);
		_nf.setMaximumFractionDigits(2);
		return _nf.format(value);
	}
	
	public static double multiplyAccurately(double... dArg) {

		List<BigDecimal> bigDecimals = new ArrayList<BigDecimal>(dArg.length);
		for (int i = 0; i < dArg.length; i++) {
			if (dArg[i] == 0)
				return 0d;
			bigDecimals.add((new BigDecimal(Double.toString(dArg[i]))));
		}
		double total = 0d;
		for (int i = 0; i < bigDecimals.size(); i++) {
			BigDecimal bdTally = new BigDecimal(Double.toString(total));
			BigDecimal bdCurrent = null;
			bdCurrent = bigDecimals.get(i);
			BigDecimal bdProduct = null;
			if (i == 0)
				bdProduct = new BigDecimal(Double.toString(bdCurrent.doubleValue()));
			else
				bdProduct = new BigDecimal(Double.toString(bdTally.multiply(bdCurrent).doubleValue()));

			total = bdProduct.doubleValue();
		}
		return total;
	}

    public static double getConvertedAmount(Double amount, String currencyCode, Double currencyRate)
    {
    	if(currencyCode.equals("JPY")){
    		return roundMoneyUp(multiplyAccurately(amount, currencyRate));
    	}else{
    		return roundMoney(multiplyAccurately(amount, 
    						(currencyCode.equals("USD") ? Double.valueOf(1) : currencyRate)));
    	}

    }
	

}
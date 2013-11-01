package com.copyright.ccc.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * Represents an amount of money in a specific currency.  The internal 
 * representation is a BigDecimal to allow for precise scaling.
 * 
 * Instances of BigMoney are immutable.  
 * 
 * Our approach is to incubate this class in the Copyright.com/OMS module,
 * with the eventual goal of promoting it to the ccc-base module.
 *
 * @author dstine
 */
public class BigMoney implements Serializable, Comparable<BigMoney> {
	private static final long serialVersionUID = 1L;

	public static final String  GBP = "GBP";
	public static final String EUR ="EUR";
	public static final String USD = "USD";
	public static final String JPY ="JPY";
	public static final String CAD ="CAD";
	
	
	private Currency mCurrency;
	private BigDecimal mAmount = BigDecimal.ZERO;
	private String mSpecialAmount;
	private boolean isSpecialCurrency=false;
	private boolean displaySpecialCurrencySymbol=false;
	
	
	private static Locale mCurrentLocale=Locale.getDefault();
	private static final RoundingMode defaultRoundingMode=RoundingMode.HALF_EVEN;
	private static final Logger LOGGER = Logger.getLogger( BigMoney.class );
	
    /**
     * Gets an instance of <code>BigMoney</code> in the specified currency.
     * <p>
     * This allows you to create an instance with a specific currency and amount.
     * The scale of the money will be that of the BigDecimal.
     *
     * @param currencyCode  the currency code, not null
     * @param amount  the amount of money, not null
     * @return the new instance, never null
     * @throws IllegalArgumentException if the currency is unknown
     */

	
	public static BigMoney ofSpecialCurrency( String amount,boolean displayCurrencySymbol){
		return  new BigMoney(amount, displayCurrencySymbol, getCurrencyCode(mCurrentLocale));
	}
	public static BigMoney ofSpecialCurrency( String amount,boolean displayCurrencySymbol,String currencyCode){
		return  new BigMoney(amount, displayCurrencySymbol,  currencyCode);
	}
	
	public static BigMoney of( String amount){
		return  new BigMoney(new BigDecimal(amount),  getCurrencyCode(mCurrentLocale));
	}
	
	public static BigMoney of( String amount,String currencyCode ){
		return  new BigMoney(new BigDecimal(amount),  currencyCode );
	}
	
	public static BigMoney of( BigMoney bigMoney){
		return  new BigMoney( bigMoney.getAmount(), bigMoney.getCurrencyCode() );
	}
	
	// default USD
	public static BigMoney of( int amount ){
		return  new BigMoney( BigDecimal.valueOf( amount ),getCurrencyCode(mCurrentLocale) );
	}
	
	public  static BigMoney of( int amount, String currencyCode ){
		return  new BigMoney( BigDecimal.valueOf( amount ), currencyCode );
	}
	public  static BigMoney of( long amount ){
		return  new BigMoney( BigDecimal.valueOf( amount ), getCurrencyCode(mCurrentLocale));
	}
	public  static BigMoney of( long amount, String currencyCode ){
		return new BigMoney( BigDecimal.valueOf( amount ), currencyCode );
	}
	public  static BigMoney of( double amount ){
		return  new BigMoney( BigDecimal.valueOf( amount ),getCurrencyCode(mCurrentLocale) );
	}
	public  static BigMoney of( double amount, String currencyCode ){
		return  new BigMoney( BigDecimal.valueOf( amount ), currencyCode );
	}
	public  static BigMoney of( BigDecimal amount ){
		return  new BigMoney( amount ,getCurrencyCode(mCurrentLocale) );
	}
    //-----------------------------------------------------------------------
    /**
     * Gets an instance of <code>BigMoney</code> specifying the amount in major units.
     * <p>
     * This allows you to create an instance with a specific currency and amount.
     * The scale of the money will be zero.
     * <p>
     * The amount is a whole number only. Thus you can initialize the value
     * 'USD 20', but not the value 'USD 20.32'.
     * For example, <code>ofMajor(USD, 25)</code> creates the instance <code>USD 25</code>.
     *
     * @param currency  the currency, not null
     * @param amountMajor  the amount of money in the major division of the currency
     * @return the new instance, never null
     */
    public static BigMoney ofMajor(long amount) {
    	BigMoney bigMoney=  new BigMoney( BigDecimal.valueOf( amount ), getCurrencyCode(mCurrentLocale));
    	bigMoney.mAmount=bigMoney.mAmount.setScale(0,RoundingMode.DOWN);
    	
    	return bigMoney;
    }
    public static BigMoney ofMajor(BigDecimal amount) {
    	BigMoney bigMoney=  new BigMoney(amount , getCurrencyCode(mCurrentLocale));
    	bigMoney.mAmount=bigMoney.mAmount.setScale(0,RoundingMode.DOWN);
    	
    	return bigMoney;
    }

	/**
	 *  
	 * @param amount
	 * @param currencyCode
	 */
	private BigMoney( BigDecimal amount, String currencyCode ){
		if ( isEmpty( currencyCode ) )
		{
			throw new IllegalArgumentException( "Must pass non-empty currency code" );
		}
		mCurrency = Currency.getInstance( currencyCode );
		
		if ( amount == null )
		{
			throw new IllegalArgumentException( "Must pass non-null amount" );
		}
		
		// Create a copy to preserve immutability.  toString() guarantees one-to-one
		// mapping between Strings and BigDecimals, per its javadoc.
		// We will not depend on setScale() returning a new instance.
		mAmount = new BigDecimal( amount.toString() );
	// don't set the scale here
	//	mAmount = mAmount.setScale( mCurrency.getDefaultFractionDigits(),RoundingMode.HALF_EVEN );
		// by default set it to four decimal place
		if(mAmount.scale()>4){
		mAmount= mAmount.setScale(4,defaultRoundingMode );
		}
		
	}

	private BigMoney( String  amount, boolean displayCurrencySymbol,String currencyCode ){
		if ( isEmpty( currencyCode ) )
		{
			throw new IllegalArgumentException( "Must pass non-empty currency code" );
		}
		mCurrency = Currency.getInstance( currencyCode );
		
		if ( amount == null )
		{
			throw new IllegalArgumentException( "Must pass non-null amount" );
		}
		this.displaySpecialCurrencySymbol=displayCurrencySymbol;
		this.isSpecialCurrency=true;
		this.mSpecialAmount=amount;
		
	}

    /**
     * Gets the amount in major units as a <code>BigDecimal</code> with scale 0.
     * <p>
     * This returns the monetary amount in terms of the major units of the currency,
     * truncating the amount if necessary.
     * For example, 'EUR 2.35' will return 2, and 'BHD -1.345' will return -1.
     * <p>
     * This is returned as a <code>BigDecimal</code> rather than a <code>BigInteger</code>.
     * This is to allow further calculations to be performed on the result.
     *      * 
     * @return the major units part of the amount, never null
     */
    public BigDecimal getAmountMajor() {
    	return new BigDecimal(mAmount.setScale(0, RoundingMode.DOWN).toBigInteger());
    }
	
    /**
     * Gets the amount as  <code>BigDecimal</code> without using currency scaling.
     * <p>
     * This returns the monetary amount in terms of the major units of the currency,
     * truncating the amount if necessary.
     * For example, ' 2.3556' will return 2.3556,
     * <p>
     * 
     */
	public BigDecimal getAmount(){
		// Create a copy to preserve immutability.
		return new BigDecimal( mAmount.toString() );
	}
	
    /**
     * Gets the amount as  <code>BigDecimal</code> with using currency scaling.
     * <p>
     * This returns the monetary amount in terms of the major units of the currency,
     * truncating the amount if necessary.
     * For example, ' 2.3556' will return 2.36,
     * <p>
     * 
     */
	public BigDecimal getCurrencyAmount(){
		// Create a copy to preserve immutability.
		BigDecimal currencyAmount= mAmount.setScale(mCurrency.getDefaultFractionDigits(),defaultRoundingMode );
		return new BigDecimal(currencyAmount.toString());
	}
	
	/**
	 * Returns the monetary scale; e.g. 2 for USD.
	 */
	public int getScale(){
		return mAmount.scale();
	}
	/**
	 * Returns the monetary scale; e.g. 2 for USD.
	 */
	public int getCurrencyScale(){
		return mCurrency.getDefaultFractionDigits();
	}
	
	/**
	 * Returns the currency code; e.g. "USD" or "EUR".
	 */
	public String getCurrencyCode(){
		return mCurrency.getCurrencyCode();
	}
	
	/**
	 * Returns the currency code; e.g. "USD" or "EUR".
	 */
	public static String getCurrencyCode(Locale locale){
		return Currency.getInstance(Locale.getDefault()).getCurrencyCode();
	}

	/**
	 * Returns the currency symbol; e.g. "$" or "€".
	 * @return
	 */
	public String getSymbol(){
		return mCurrency.getSymbol();
	}
	/**
	 * Returns the currency symbol; e.g. "$" or "€".
	 * @return
	 */
	public String getSymbol(Locale locale){
		return mCurrency.getSymbol(locale);
	}
	
	/**
	 * Returns a <code>String</code> representation of  currency formated in default locale (US)
	 *   with $ sign $ 7.00 or  $ 1,200.45
	 */
	public String getDefaultFormatString(){
		DecimalFormat formatter = 
			(DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US);
         
         //formatter.applyPattern("$ ###,###.00#");
         formatter.applyPattern("$ #,##0.00;($ #,##0.00)" );
         //FOR HADNING SPECIAL CURRENCY
         if(this.isSpecialCurrency){
        	 if(this.displaySpecialCurrencySymbol){
        		 return "$ " + this.mSpecialAmount;
        	 }else{
        		 return  this.mSpecialAmount;
        	 }
         }
         return  formatter.format(getCurrencyAmount());
	}
	/**
	 * Returns a <code>String</code> representation of  currency formated in default locale (US)
	 *   with $ sign $ 7.00 or  $ 1,200.45
	 */
	public String getDefaulDisplayString(){
		return getDefaultFormatString();
	}

	/**
	 * Returns a <code>String</code> representation of  currency formated in default locale (US)
	 *   7.30 USD or   7,00.30 EUR
	 */
	public String getDisplayString(){
		Locale currentLocale= mCurrentLocale;
		DecimalFormat formatter = 
			(DecimalFormat)NumberFormat.getCurrencyInstance(currentLocale);
		//NumberFormat formatter = NumberFormat.getCurrencyInstance(currentLocale);
        //
        // Create a DecimalFormatSymbols for each locale and sets
         // its new currency symbol.
        //
         DecimalFormatSymbols symbol =
                new DecimalFormatSymbols(currentLocale);
         symbol.setCurrencySymbol(getCurrencyCode());
         
         //FOR HADNING SPECIAL CURRENCY
         if(this.isSpecialCurrency){
        	 if(this.displaySpecialCurrencySymbol){
        		 return this.mSpecialAmount + " " +symbol.getCurrencySymbol();
        	 }else{
        		 return  this.mSpecialAmount;
        	 }
         }
         
         formatter.applyPattern("#,##0.00 "+symbol.getCurrencySymbol()+";(#,##0.00) "+symbol.getCurrencySymbol());
         //formatter.setDecimalFormatSymbols(symbol);
         
         return  formatter.format(getCurrencyAmount());
	}

	/**
	 * Returns a <code>String</code> representation of  currency formated in its locale with symbol
	 * <code>Locale</code>, $ 7.30 or   7.00,30 €
	 */
	public String getDisplayString(Locale locale){
		//String customizedPattern=null;
	    //
        // Gets currency's formatted value for each locale
        // without change the currency symbol
        //
        DecimalFormat formatter = (DecimalFormat)
                NumberFormat.getCurrencyInstance(locale);
        String localizedPattern= formatter.toLocalizedPattern();
        
        // here's how we tell where the symbol goes.
        	// 	TODO changed localized pattern since euro symbol prints after the amount
        // add space
        String modifiedPattern= localizedPattern.replaceAll("\u00A4", "\u00A4 ").trim();
        
        //FOR HADNING SPECIAL CURRENCY
        if(this.isSpecialCurrency){
        	  DecimalFormatSymbols symbol =
                  new DecimalFormatSymbols(locale);
       	 if(this.displaySpecialCurrencySymbol){
       		 return symbol.getCurrencySymbol() + " " + this.mSpecialAmount;
       	 }else{
       		 return  this.mSpecialAmount;
       	 }
        }
        
        formatter.applyLocalizedPattern(modifiedPattern);
       
        return formatter.format(getCurrencyAmount());
	}
	


	
	/**
     * Returns a copy of this monetary value with the amount in the same currency added
     * retaining the scale by rounding the result.
     * <p>
     * The scale of the result will be the same as the scale of this instance.
     * For example,'25.95' plus '3.021' will '28.97 with most rounding modes.
     * <p>
     * This instance is immutable and unaffected by this method.
     * 
     *
     */
    public BigMoney plus(BigDecimal that) {
    	checkNotSpecial(this);
       	BigDecimal amount = this.getCurrencyAmount().add(that);
       	amount=amount.setScale(this.getCurrencyScale(),defaultRoundingMode);
		return new BigMoney(amount, this.getCurrencyCode() );
    }

	public BigMoney plus( BigMoney that ){
		checkInstance( that );
		checkCurrencyCode( that );
		checkNotSpecial(that);
	    checkNotSpecial(this);
		BigDecimal amount= this.getCurrencyAmount().add( that.getCurrencyAmount());
		amount=amount.setScale(this.getCurrencyScale(),defaultRoundingMode);
		return new BigMoney(amount, this.getCurrencyCode() );
	}
	 /**
     * 
     * <p>
     * No precision is lost in the result.
     * The scale of the result will be the maximum of the two scales.
     * '25.95' plus '3.021' will result '28.971.
     * <p>
     * This instance is immutable and unaffected by this method.
     */
	  public BigMoney plusRetainScale(BigDecimal that) {
		  checkNotSpecial(this);
	      BigDecimal amount = this.getAmount().add(that);
		  return new BigMoney(amount, this.getCurrencyCode());
	    }
	  
	  public BigMoney plusRetainScale(BigMoney that){
			checkInstance( that );
			checkCurrencyCode( that );
			checkNotSpecial(that);
		     checkNotSpecial(this);
		  if (that.compareTo(BigMoney.zero()) == 0) {
	          return this;
	      }
			  return new BigMoney( this.getAmount().add( that.getAmount() ), this.getCurrencyCode() );	  
	    }

	/**
	 * Returns a copy of this monetary value with the amount in the same currency added
	 * retaining the scale by rounding the result.
	 * <p>
	 * The scale of the result will be the same as the scale of this instance.
	 * '25.95' minus '3.021' will result '22.92
	 * <p>
	 * This instance is immutable and unaffected by this method.
	 * 
	 *
	 */
	public BigMoney minus(BigDecimal that) {
	    checkNotSpecial(this);
		BigDecimal amount = this.getCurrencyAmount().subtract(that);
		amount=amount.setScale(this.getCurrencyScale(),defaultRoundingMode);
		return new BigMoney(amount, this.getCurrencyCode() );
	}

	public BigMoney minus( BigMoney that ){
		checkInstance( that );
		checkCurrencyCode( that );
		checkNotSpecial(that);
		checkNotSpecial(this);
		return new BigMoney( this.getCurrencyAmount().subtract( that.getCurrencyAmount() ), this.getCurrencyCode() );
	}
	
	/**
	 * 
	 * <p>
	 * No precision is lost in the result.
	 * The scale of the result will be the maximum of the two scales.
	 *' 25.95' minus '3.021' will result '22.929.
	 * <p>
	 * This instance is immutable and unaffected by this method.
	 * 
	 */
	public BigMoney minusRetainScale(BigDecimal that) {
		checkNotSpecial(this);
		BigDecimal amount = this.getAmount().subtract(that);
		return new BigMoney(amount, this.getCurrencyCode() );
	}

	public BigMoney minusRetainScale( BigMoney that ){
		checkInstance( that );
		checkCurrencyCode( that );
		checkNotSpecial(that);
		checkNotSpecial(this);
		return new BigMoney( this.getAmount().subtract( that.getAmount() ), this.getCurrencyCode() );
	}
    

	/**
	 * 
	 * <p>
	 * No precision is lost in the result.
	 * The scale of the result will be the maximum of the two scales.
	 *For example, ' 1.13' multiplied by 2.5 yields ' 2.82'.
	 * <p>
	 * This instance is immutable and unaffected by this method.
	 * 
	 */
	public BigMoney multiplyBy(long that) {
		return multiplyBy(new BigDecimal(that));
	 }
	public BigMoney multiplyBy(BigDecimal that) {
		checkNotSpecial(this);
      	BigDecimal amount = this.getCurrencyAmount().multiply(that);
      	amount=amount.setScale(this.getCurrencyScale(),defaultRoundingMode);
		return  new BigMoney(amount, this.getCurrencyCode());
   }
   public BigMoney multiplyBy(BigMoney that) {
	   checkInstance( that );
	   checkCurrencyCode( that );
	   checkNotSpecial(this);
	   checkNotSpecial(that);
	   BigDecimal amount = this.getCurrencyAmount().multiply(that.getCurrencyAmount());
	   amount=amount.setScale(this.getCurrencyScale(),defaultRoundingMode);
	   return  new BigMoney(amount, this.getCurrencyCode());
   }
	/**
	 * 
	 * <p>
	 * No precision is lost in the result.
	 * The scale of the result will be the maximum of the two scales.
	 *For example, ' 1.13' multiplied by 2.5 yields ' 2.825'.
	 * <p>
	 * This instance is immutable and unaffected by this method.
	 * 
	 */
	public BigMoney multiplyByRetainScale(BigDecimal that) {
		checkNotSpecial(this);
		BigDecimal amount = this.getAmount().multiply(that);
		return new BigMoney(amount, this.getCurrencyCode() );
	}

	public BigMoney multiplyByRetainScale( BigMoney that ){
		checkInstance( that );
		checkCurrencyCode( that );
		checkNotSpecial(this);
		checkNotSpecial(that);
		return new BigMoney( this.getAmount().multiply( that.getAmount() ), this.getCurrencyCode() );
	}
   /**
    * Returns a copy of this monetary value divided by the specified value
    * using the specified rounding mode to adjust the scale.
    * <p>
    * The result has the same scale as this instance.
    * For example, '1.13' divided by 2.5 and rounding down yields '0.45'
    * (amount rounded down from 0.452).
    * <p>
    * This instance is immutable and unaffected by this method.
    * 
    */
	public BigMoney divideBy(long that) {
			return divideBy(new BigDecimal(that));
	  }
	
	public BigMoney divideBy(BigDecimal that) {
		BigDecimal amount = this.getCurrencyAmount().divide(that,this.getCurrencyScale(),defaultRoundingMode);
		return new BigMoney(amount, this.getCurrencyCode() );
	}
	public BigMoney divideBy(BigMoney that) {
	  checkInstance( that );
	  checkCurrencyCode( that );
	  checkNotSpecial(that);
	  BigDecimal amount = this.getCurrencyAmount().divide(that.getCurrencyAmount(),this.getCurrencyScale(),defaultRoundingMode);
	  return  new BigMoney(amount, this.getCurrencyCode());
	}
	public BigMoney divideByRetainScale(BigDecimal that) {
	  return divideByRetainScale(new BigMoney(that, this.getCurrencyCode() ));
	}
	public BigMoney divideByRetainScale( BigMoney that ){
	  checkInstance( that );
	  checkCurrencyCode( that );
	  checkNotSpecial(that);
	  return new BigMoney( this.getAmount().divide( that.getAmount() ), this.getCurrencyCode() );
	}
	/**
	 * Implements Comparable<BigMoney>.
	 */
	@Override
	public int compareTo( BigMoney that ){
		checkInstance( that );
		checkCurrencyCode( that );
		return this.getCurrencyAmount().compareTo( that.getCurrencyAmount() );
	}
	private void checkInstance( BigMoney that ){
		if ( that==null ){
			throw new IllegalArgumentException( 
					"Must pass an instance of " + BigMoney.class.getSimpleName() );
		}
	}
	private void checkNotSpecial( BigMoney that ){
		if ( that.isSpecialCurrency ){
			throw new IllegalArgumentException( 
					"Cannot perform Arthimatic operation  on Special Amount " );
		}
	}
	private void checkCurrencyCode( BigMoney that ){
		if ( this.getCurrencyCode().compareToIgnoreCase( that.getCurrencyCode() )!=0 ){
			throw new IllegalArgumentException( 
					"Mismatched currency code; expected " + this.getCurrencyCode() + 
					", received " + that.getCurrencyCode() );
		}
	}
	/**
	 * Copied from org.apache.commons.lang.StringUtils to avoid compile/runtime dependency.
	 */
    private static boolean isEmpty( String str ){
        return str == null || str.length() == 0;
    }

    /**
     * 
     * @return BigMoney with amount 0.00 set to default locale e.g. $ 0.00
     */
    public static BigMoney zero(){
    	return BigMoney.of(0);
    }
    
    /**
     * 
     * @return BigMoney with amount 0.00 set to default locale e.g. $ 0.00
     */
    public static BigMoney zero(String currencyCode){
    	return BigMoney.of(0,currencyCode);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Parses an instance of <code>BigMoney</code> from a string.
     * <p>
     * The string format is '<currencyCode> <amount>'.
     * The currency code must be three letters, and the amount must be a number.
     * This matches the output from {@link #toString()}.
     * <p>
     * For example, <code>of("$ 25")</code> creates the instance <code>25</code>
     * For example, <code>of("$ 2,350,000.00")</code> creates the instance <code>2350000.00</code>
     *
     * @param moneyStr  the money string to parse, not null
     * @return the parsed instance, never null
     * @throws IllegalArgumentException if the string is malformed
     * @throws ArithmeticException if the amount is too large
     */
    public static BigMoney parseDollar(String moneyStr) {
        if ( isEmpty( moneyStr ) ){
			throw new IllegalArgumentException( "Must pass non-empty money" );
		}
        if (moneyStr.length() < 3 || moneyStr.charAt(1) != ' ') {// not $ 3 or 2nd spot is not empty
            throw new IllegalArgumentException("Money '" + moneyStr + "' cannot be parsed");
        }
        String amountStr = moneyStr.substring(1).trim();
        return BigMoney.of(new BigDecimal(amountStr.replace(",", "")));
    }
    
}

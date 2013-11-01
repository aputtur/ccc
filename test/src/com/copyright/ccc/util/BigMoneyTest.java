package com.copyright.ccc.util;

import java.math.BigDecimal;
import java.util.Locale;

import com.copyright.ccc.CCTestCase;



public class BigMoneyTest extends CCTestCase
{
	    private static final String  GBP = "GBP";
	    private static final String EUR ="EUR";
	    private static final String USD = "USD";
	    private static final String JPY ="JPY";
	    private static final String CAD ="CAD";
		public static final String USD_SYMBOL = "\u0024";
		public static final String CAD_SYMBOL = "\u0024";
		public static final String EURO_SYMBOL = "\u20Ac";
		public static final String YEN_SYMBOL = "\u00A5";
		public static final String YEN_W_SYMBOL = "\uFFE5";
		public static final String GBP_SYMBOL = "\u00A3";
	 
		// locale default is USA
	public void testUSD()
	{
		
		
		
		
		
		
		
		// money test
		
	}
	public void testPlus(){
		//25.95' plus '3.021' will 'USD 28.97 with most rounding modes
		BigMoney bd1=BigMoney.of(new BigDecimal("25.95"));
		BigMoney bd2=BigMoney.of(new BigDecimal("3.021"));
		assertTrue( "Incorrect comparison ", bd1.plus(bd2).getAmount().compareTo(new BigDecimal("28.97"))==0 );
		assertEquals( "Wrong display string", "$ 28.97",bd1.plus(bd2).getDefaulDisplayString() );
		
	}
	public void testPlusRetainScale(){
		//25.95' plus '3.021' will 'USD 28.971 
		BigMoney bd1=BigMoney.of(new BigDecimal("25.95"));
		BigMoney bd2=BigMoney.of(new BigDecimal("3.021"));
		assertTrue( "Incorrect comparison ", bd1.plusRetainScale(bd2).getAmount().compareTo(new BigDecimal("28.971"))==0 );
		assertEquals( "Wrong display string", "$ 28.97",bd1.plusRetainScale(bd2).getDefaulDisplayString() );
	}
	public void testMinus(){
		//For example,'USD 25.95' minus 'USD 3.021' will 'USD 22.93
		BigMoney bd1=BigMoney.of(new BigDecimal("25.95"));
		BigMoney bd2=BigMoney.of(new BigDecimal("3.021"));
		assertTrue( "Incorrect comparison "+bd1.minus(bd2).getAmount(), bd1.minus(bd2).getAmount().compareTo(new BigDecimal("22.93"))==0 );
		assertEquals( "Wrong display string", "$ 22.93",bd1.minus(bd2).getDefaulDisplayString() );
		
	}
	public void testMinusRetainScale(){
		//For example,'USD 25.95' minus 'USD 3.021' will 'USD 22.929
		BigMoney bd1=BigMoney.of(new BigDecimal("25.95"));
		BigMoney bd2=BigMoney.of(new BigDecimal("3.021"));
		assertTrue( "Incorrect comparison ", bd1.minusRetainScale(bd2).getAmount().compareTo(new BigDecimal("22.929"))==0 );
		assertEquals( "Wrong display string", "$ 22.93",bd1.minusRetainScale(bd2).getDefaulDisplayString() );
	}
    
	
	public void testMultiply(){
		//For example, 'USD 1.13' multiplied by 2.5 yields 'USD 2.82'. (2.825)
		BigMoney bd1=BigMoney.of(new BigDecimal("1.13"));
		BigMoney bd2=BigMoney.of(new BigDecimal("2.5"));
		assertTrue( "Incorrect comparison ", bd1.multiplyBy(bd2).getAmount().compareTo(new BigDecimal("2.82"))==0 );
		assertEquals( "Wrong display string", "$ 2.82",bd1.multiplyBy(bd2).getDefaulDisplayString() );
		
	}
	public void testMultiplyRetainScale(){
		//For example, 'USD 1.13' multiplied by 2.5 yields 'USD 2.825'.
		BigMoney bd1=BigMoney.of(new BigDecimal("1.13"));
		BigMoney bd2=BigMoney.of(new BigDecimal("2.5"));
		assertTrue( "Incorrect comparison ", bd1.multiplyByRetainScale(bd2).getAmount().compareTo(new BigDecimal("2.825"))==0 );
		assertEquals( "Wrong display string", "$ 2.82",bd1.multiplyByRetainScale(bd2).getDefaulDisplayString() );
	}
	public void testDivide(){
		//For example, 'USD 1.13' divided by 2.5 and rounding down yields 'USD 0.45'
		BigMoney bd1=BigMoney.of(new BigDecimal("1.13"));
		BigMoney bd2=BigMoney.of(new BigDecimal("2.5"));
		assertTrue( "Incorrect comparison ", bd1.divideBy(bd2).getAmount().compareTo(new BigDecimal("0.45"))==0 );
		
		
	}
	public void testDivideRetainScale(){
		//For example, 'USD 1.13' divided by 2.5 and rounding down yields 'USD 0.452'
		BigMoney bd1=BigMoney.of(new BigDecimal("1.13"));
		BigMoney bd2=BigMoney.of(new BigDecimal("2.5"));
		assertTrue( "Incorrect comparison ", bd1.divideByRetainScale(bd2).getAmount().compareTo(new BigDecimal("0.452"))==0 );
	}

	public void testSpecial()
	{
		
		
		BigMoney sp1 =BigMoney.ofSpecialCurrency("TBD",true);
		verify( sp1,  USD, USD_SYMBOL, 2, "$ TBD","TBD USD" );
		
		
		BigMoney sp2 =BigMoney.ofSpecialCurrency("TBD",true, "GBP");
		//default  locale USD TXT symbol
		
		// us locale $ symbol 
		verify(Locale.UK, sp2, GBP, GBP_SYMBOL, 2, GBP_SYMBOL+ " TBD" );
		
		
	}
	
	public void testCAD()
	{
		
		
		BigMoney bm =BigMoney.of( 5, "CAD" );
		//default  locale USD TXT symbol
		verify( bm,  CAD, CAD, 2, "$ 5.00","5.00 CAD" );
		
		// us locale $ symbol 
		verify(Locale.CANADA, bm, CAD, CAD_SYMBOL, 2, "$ 5.00" );
		
		
	}
	
	
	public void testEUR()
	{
		BigMoney bm = BigMoney.of(6, EUR);
		// default EUR symbol
		verify( bm, "EUR",EUR, 2, "$ 6.00", "6.00 EUR" );
		// FRANCE Locale € symbol
		verify(Locale.FRANCE, bm, EUR,EURO_SYMBOL, 2,   "6,00 "+EURO_SYMBOL    );
	}
	
	public void testGBP()
	{
		BigMoney bm = BigMoney.of( 7,GBP );
		// defualt locale GBP symbol
		verify( bm, GBP, GBP, 2, "$ 7.00", "7.00 GBP" );
		// uk locale £ symbol
		verify(Locale.UK, bm, GBP, GBP_SYMBOL, 2, GBP_SYMBOL+" 7.00" );
	}
	
	public void testJPY()
	{
		BigMoney bm = BigMoney.of( "1800.39", JPY );
		
		// default locale JPY symbol
		verify( bm, JPY,JPY, 0,  "$ 1,800.00", "1,800.00 JPY"  );
		
		//japan locale Full width ¥ symbol
		verify(Locale.JAPAN, bm, JPY, YEN_W_SYMBOL, 0,   YEN_W_SYMBOL+" 1,800"  );
	}
	
	public void testAdd()
	{
		BigMoney bm1 = BigMoney.of( 3, USD );
		BigMoney bm2 = BigMoney.of( 4, USD );
		BigMoney result = bm1.plus( bm2 );
		verify( result, new BigDecimal( "7.00" ), USD, "$", 2, "$ 7.00", "7.00 USD" );
		verify(Locale.US, result, new BigDecimal( "7.00" ), USD, USD_SYMBOL, 2, "$ 7.00" );

		bm1 = BigMoney.of( "3.13", USD );
		bm2 = BigMoney.of( "4.14", USD );
		result = bm1.plus( bm2 );
		verify( result, new BigDecimal( "7.27" ),USD, USD_SYMBOL, 2, "$ 7.27", "7.27 USD" );
		verify(Locale.US, result, new BigDecimal( "7.27" ), USD, USD_SYMBOL, 2,  "$ 7.27" );
	}
	
	public void testCompareTo()
	{
		BigMoney low =BigMoney.of( "3.13",USD);
		BigMoney mid = BigMoney.of( "4.14", USD );
		BigMoney high = BigMoney.of( "5.15",USD );
		assertTrue( "Incorrect comparison", low.compareTo( mid ) < 0 );
		assertTrue( "Incorrect comparison", high.compareTo( mid ) > 0 );
	}
	
	private void verify( BigMoney bm, BigDecimal amount, String currencyCode, String symbol, int scale, String defaultDisplayString,String displayString )
	{
		assertEquals( "Wrong amount", amount, bm.getCurrencyAmount() );
		
		verify( bm, currencyCode, symbol, scale, defaultDisplayString,displayString );
	}
	
	private void verify(Locale locale, BigMoney bm, BigDecimal amount, String currencyCode, String symbol, int scale,  String displayString )
	{
		assertEquals( "Wrong amount", amount, bm.getCurrencyAmount() );
		
		verify(locale, bm, currencyCode, symbol, scale, displayString );
	}
	

	private void verify( BigMoney bm, String currencyCode, String symbol, int scale, String defaultDisplayString,String displayString )
	{
		

		assertEquals( "Wrong currency code", currencyCode, bm.getCurrencyCode() );
		assertEquals( "Wrong scale", scale, bm.getCurrencyScale() );
     	assertEquals( "Wrong symbol", symbol, bm.getSymbol());
		assertEquals( "Wrong display string", displayString, bm.getDisplayString() );
		assertEquals( "Wrong default display string", defaultDisplayString, bm.getDefaultFormatString() );
		
		
	}

	private void verify(Locale locale, BigMoney bm, String currencyCode, String symbol, int scale, String displayString )
	{
		assertEquals( "Wrong currency code", currencyCode, bm.getCurrencyCode() );
		assertEquals( "Wrong scale", scale, bm.getCurrencyScale() );
		assertEquals( "Wrong symbol", symbol, bm.getSymbol(locale));
		assertEquals( "Wrong display string", displayString, bm.getDisplayString(locale) );	
		
	}
}

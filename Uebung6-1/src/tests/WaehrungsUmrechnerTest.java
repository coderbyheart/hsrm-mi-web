package tests;

import java.math.BigDecimal;

import org.junit.Test;

import beans.WaehrungsUmrechner;

public class WaehrungsUmrechnerTest extends junit.framework.TestCase {
	
	@Test
	public void testBean()
	{
		WaehrungsUmrechner rechner = new WaehrungsUmrechner();
		rechner.setSourceCurrency("EUR");
		assertEquals("EUR", rechner.getSourceCurrency());
		rechner.setTargetCurrency("EUR2");
		assertEquals("EUR2", rechner.getTargetCurrency());
		rechner.setRate(1);
		assertTrue(rechner.getRate().compareTo(new BigDecimal(1)) == 0);
		rechner.setSourceValue(2);
	}
	
	@Test
	public void testUmrechnungSourceToTarget()
	{
		WaehrungsUmrechner rechner = new WaehrungsUmrechner();
		rechner.setSourceCurrency("EUR");
		rechner.setTargetCurrency("EUR2");
		rechner.setRate(2);
		rechner.setSourceValue(1);
		assertTrue(rechner.getTargetValue().compareTo(new BigDecimal(2)) == 0);
	}
	
	@Test
	public void testUmrechnungTargetToSource()
	{
		WaehrungsUmrechner rechner = new WaehrungsUmrechner();
		rechner.setSourceCurrency("EUR");
		rechner.setTargetCurrency("EUR2");
		rechner.setRate(2);
		rechner.setTargetValue(2);
		assertTrue(rechner.getSourceValue().compareTo(new BigDecimal(1)) == 0);
	}
}

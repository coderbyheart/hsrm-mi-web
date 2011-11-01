package uebung.ueb06;

import java.math.BigDecimal;
import java.math.MathContext;

public class WaehrungsUmrechnerBean {
	private String sourceCurrency;
	private String targetCurrency;
	private BigDecimal sourceValue;
	private BigDecimal rate;
	
	public WaehrungsUmrechnerBean()
	{
		sourceCurrency = "€";
		targetCurrency = "$";
		rate = new BigDecimal(1, MathContext.DECIMAL128).divide(new BigDecimal(1.418, MathContext.DECIMAL128), BigDecimal.ROUND_HALF_UP);
		sourceValue = new BigDecimal(1, MathContext.DECIMAL128);
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public String getSourceCurrency() {
		return sourceCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setSourceValue(BigDecimal sourceValue) {
		this.sourceValue = sourceValue;
	}

	public BigDecimal getSourceValue() {
		return sourceValue;
	}

	public void setTargetValue(BigDecimal targetValue) {
		this.sourceValue = targetValue.divide(getRate(), BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getTargetValue() {
		return getSourceValue().multiply(getRate());
	}

	public void setRate(double d) {
		setRate(new BigDecimal(d, MathContext.DECIMAL128));
	}

	public void setSourceValue(double d) {
		setSourceValue(new BigDecimal(d, MathContext.DECIMAL128));
	}

	public void setTargetValue(double d) {
		setTargetValue(new BigDecimal(d, MathContext.DECIMAL128));
	}

	public void setSourceValue(String sourceValue) {
		setSourceValue(new BigDecimal(sourceValue, MathContext.DECIMAL128));
	}

	public void setTargetValue(String targetValue) {
		setTargetValue(new BigDecimal(targetValue, MathContext.DECIMAL128));
	}
}

package de.medieninf.webanw.belegung.gruppe05.listing.filtering;

import de.medieninf.webanw.belegung.gruppe05.listing.BeanListing;

/**
 * Eine Bedinung zum Filtern eines {@link BeanListing BeanListings}.
 * 
 * @author Markus Tacker <m@tacker.org>
 */
public class Condition implements IBeanListingCondition, IConditionQueryString {
	private String param;
	private String key;
	private Object value;
	private String type = "=";

	/**
	 * Erzeugt eine neue Bedingung.
	 * 
	 * @param param
	 *            Der Name des Platzhalters im Query
	 * @param key
	 *            Der Name des Feldes
	 * @param value
	 *            Der Wert der Bedingung
	 */
	public Condition(String param, String key, Object value) {
		this.setParam(param);
		this.setKey(key);
		this.setValue(value);

	}

	public Condition() {

	}

	/**
	 * Setzt den Namen des Feldes
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gibt den Namen des Feldes zurück
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Setzt den Wert der Bedingung
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Gibt den Wert der Bedingung zurück
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setzt den Namen des Platzhalters
	 * 
	 * @param param
	 */
	public void setParam(String param) {
		this.param = param;
	}

	/**
	 * Gibt den Namen des Platzhalters zurück
	 */
	public String getParam() {
		return param;
	}

	/**
	 * Setzt den Typen der Bedingung. Standard ist "="
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gibt den Typen der Bedingung zurück
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gibt einen String zur Verwendung in einem Query zurück
	 */
	@Override
	public String toQueryCondition(String prefix) {
		return prefix + "." + this.getKey() + " " + this.getType() + " :"
				+ this.getParam();
	}
}
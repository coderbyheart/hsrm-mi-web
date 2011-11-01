package de.medieninf.webanw.belegung.gruppe05.listing.searching;

import de.medieninf.webanw.belegung.gruppe05.listing.filtering.Condition;

/**
 * Definiert ein durchsuchbares Feld.
 * 
 * @author Markus Tacker <m@tacker.org>
 */
public class SearchField {
	private String name;
	private String label;
	private String field;
	private SearchFieldType type = SearchFieldType.STRING;

	public enum SearchFieldType {
		STRING, INT
	}

	/**
	 * @param name
	 *            Setzt den Name (identifier) des Feldes
	 * @param field
	 *            Setzt das Datenbank-Feld, in dem gesucht wird
	 * @param label
	 *            Setzt den Bezeichner des Feldes
	 */
	public SearchField(String name, String field, String label) {
		setName(name);
		setField(field);
		setLabel(label);
	}

	/**
	 * @param name
	 *            Setzt den Name (identifier) des Feldes
	 * @param label
	 *            Setzt den Bezeichner des Feldes
	 */
	public SearchField(String name, String label) {
		setName(name);
		setLabel(label);
	}

	/**
	 * Setzt den Name (identifier) des Feldes
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gibt den Name (identifier) des Feldes zurück
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Bezeichner des Feldes
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gibt den Bezeichner des Feldes zurück
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Setzt das Datenbank-Feld, in dem gesucht wird
	 * 
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * Gibt das Datenbank-Feld zurück
	 */
	public String getField() {
		return field;
	}

	/**
	 * Returns a condition for this field
	 * 
	 * @param search
	 */
	public Condition getCondition(String search) {
		Condition c;
		if (getType().equals(SearchFieldType.STRING)) {
			c = new SearchCondition("search" + getField(), getField(), "%"
					+ search + "%");
			c.setType("LIKE");
		} else {
			String clean = search.replaceAll("[^0-9]", "");
			Integer n = clean.length() > 0 ? Integer.parseInt(clean) : 0;
			c = new Condition("search" + getField(), getField(), n);
		}
		return c;
	}

	/**
	 * Setzt den Typen des Feldes
	 * 
	 * @param type
	 */
	public void setType(SearchFieldType type) {
		this.type = type;
	}

	/**
	 * Gibt den Typen des Feldes zurück
	 */
	public SearchFieldType getType() {
		return type;
	}
}
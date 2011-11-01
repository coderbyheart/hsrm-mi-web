package de.medieninf.webanw.belegung.gruppe05.listing;

import java.util.List;

/**
 * Interface für Beans, die aufgelistet werden können.
 * 
 * @author Markus Tacker <m@tacker.org>
 */
public interface IListableBean {

	/**
	 * Gibt die Richtung zurück, in der Sortiert wird.
	 */
	String getSortDir();
	
	/**
	 * Gibt die Anzahl der Zeilen zurück, die pro Seite angezeigt werden sollen.
	 */
	int getRowsPerPage();
	
	/**
	 * Gibt den Start-Index (1-basiert) der ersten Zeile der aktuellen Auflistung zurück.
	 */
	int getStart();
	
	/**
	 * Setzt die Anzahl der Zeilen im gesamten Ergebnis. 
	 * 
	 * Dieser Wert wird durch ein SELECT COUNT(...) ermittelt.
	 * 
	 * @param l
	 */
	void setNumRows(long l);
	
	/**
	 * Gibt das Feld zurück, nachdem standardmäßig sortiert wird
	 */
	String getDefaultSortBy();

	/**
	 * Gibt die Liste der Feldbezeichnungen zurück, die zum Sortieren verwendet
	 * werden können.
	 */
	List<String> getSortFields();

}

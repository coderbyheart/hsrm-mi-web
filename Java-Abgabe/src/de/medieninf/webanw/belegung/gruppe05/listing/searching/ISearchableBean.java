package de.medieninf.webanw.belegung.gruppe05.listing.searching;

import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;


/**
 * Interface für Beans, in denen gesucht werden kann.
 * 
 * @author Markus Tacker <m@tacker.org>
 */
public interface ISearchableBean {
	
	/**
	 * Gibt das Feld zurück, in dem standardmäßig gesucht wird.
	 */
	String getDefaultSearchType();

	/**
	 * Gibt eine Liste mit Feldern zurück, in denen gesucht werden kann.
	 */
	List<SelectItem> getSearchTypeList();
	
	/**
	 * Gibt eine Map mit Feldern zurück, in denen gesucht werden kann.
	 */
	HashMap<String, SearchField> getSearchFields();
}

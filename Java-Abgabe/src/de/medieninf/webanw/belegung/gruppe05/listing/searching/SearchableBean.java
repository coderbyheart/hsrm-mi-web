package de.medieninf.webanw.belegung.gruppe05.listing.searching;

import de.medieninf.webanw.belegung.gruppe05.listing.ListingBean;


/**
 * Basisklasse für Beans, in denen gesucht werden kann.
 * 
 * @author Markus Tacker <m@tacker.org>
 */
abstract public class SearchableBean extends ListingBean implements
		ISearchableBean {

	private String search;
	private String searchType;

	public SearchableBean() {
		searchType = getDefaultSearchType();
	}

	/**
	 * Setzt den Suchbegriff
	 * 
	 * @param search
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * Gibt den Suchbegriff zurück
	 */
	public String getSearch() {
		return search;
	}

	/**
	 * Setzt das Feld nachdem sortiert wird
	 * 
	 * @param searchType
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * Gibt das Feld zurück, nachdem sortiert wird
	 */
	public String getSearchType() {
		return searchType;
	}

}

package de.medieninf.webanw.belegung.gruppe05.listing.filtering;

public interface IConditionQueryString {
	/**
	 * Gibt einen String zur Verwendung in einem Query zurück
	 */
	String toQueryCondition(String prefix);
}

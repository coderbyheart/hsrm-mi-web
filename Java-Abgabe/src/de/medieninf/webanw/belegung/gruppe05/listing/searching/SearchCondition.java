package de.medieninf.webanw.belegung.gruppe05.listing.searching;

import de.medieninf.webanw.belegung.gruppe05.listing.filtering.Condition;


public class SearchCondition extends Condition {

	public SearchCondition(String param, String key, Object value) {
		super(param, key, value);
	}
	
	/**
	 * Gibt einen String zur Verwendung in einem Query zurück
	 */
	@Override
	public String toQueryCondition(String prefix) {
		return "LOWER(" + prefix + "." + this.getKey() + ") " + this.getType() + " :" + this.getParam();
	}

}

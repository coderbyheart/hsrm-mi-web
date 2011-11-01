package de.medieninf.webanw.belegung.gruppe05.listing.filtering;

public interface IBeanListingCondition {

	String toQueryCondition(String prefix);
	String getParam();
	Object getValue();
}

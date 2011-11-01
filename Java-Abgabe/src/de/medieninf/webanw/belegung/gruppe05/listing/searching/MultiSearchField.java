package de.medieninf.webanw.belegung.gruppe05.listing.searching;

import java.util.ArrayList;

import de.medieninf.webanw.belegung.gruppe05.listing.filtering.Condition;


public class MultiSearchField extends SearchField {
	private ArrayList<SearchField> fields = new ArrayList<SearchField>();

	/**
	 * @see SearchField#SearchField(String, String)
	 */
	public MultiSearchField(String name, String label) {
		super(name, label);
	}

	/**
	 * Fügt ein Feld hinzu, in dem gesucht wird
	 * @param field
	 */
	public void addField(SearchField field) {
		getFields().add(field);
	}

	/**
	 * Gibt ein Liste mit {@link Condition Conditions} für dieses Multi-Search-Field zurück
	 * 
	 * @param search
	 */
	public ConditionGroup getConditionGroup(String search) {
		ConditionGroup cgroup = new ConditionGroup();
		for(SearchField sf: getFields()) {
			if (sf instanceof MultiSearchField) {
				MultiSearchField msf = (MultiSearchField)sf;
				ConditionGroup subGroup = msf.getConditionGroup(search);
				cgroup.addCondition(subGroup);
			} else {
				cgroup.addCondition(sf.getCondition(search));
			}
		}
		return cgroup;
	}

	public ArrayList<SearchField> getFields() {
		return fields;
	}
}

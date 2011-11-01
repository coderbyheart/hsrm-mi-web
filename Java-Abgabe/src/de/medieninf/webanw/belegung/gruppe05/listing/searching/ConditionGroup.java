package de.medieninf.webanw.belegung.gruppe05.listing.searching;

import java.util.ArrayList;

import de.medieninf.webanw.belegung.gruppe05.listing.filtering.Condition;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.IConditionQueryString;

public class ConditionGroup extends Condition {

	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private String type = "OR";
	
	public void addCondition(Condition c)
	{
		this.getConditions().add(c);
	}

	@Override
	public String toQueryCondition(String prefix) {
		String ret = "(";
		boolean first = true; 
		for(IConditionQueryString c: getConditions()) {
			if (first) {
				first = false;
			} else {
				ret += " " + getType() + " ";
			}
			ret += c.toQueryCondition(prefix);
		}
		ret += ")";
		return ret;
	}

	@Override
	public String getParam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public ArrayList<Condition> getConditions() {
		return conditions;
	}
}

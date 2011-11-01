package de.medieninf.webanw.belegung.gruppe05.aufg0102;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Studierende;
import de.medieninf.webanw.belegung.gruppe05.listing.BeanListing;
import de.medieninf.webanw.belegung.gruppe05.listing.ListingBean;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.Condition;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.IBeanListingCondition;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.IFilterableBean;
import de.medieninf.webanw.belegung.gruppe05.listing.searching.ISearchableBean;
import de.medieninf.webanw.belegung.gruppe05.listing.searching.MultiSearchField;
import de.medieninf.webanw.belegung.gruppe05.listing.searching.SearchField;
import de.medieninf.webanw.belegung.gruppe05.listing.searching.SearchableBean;
import de.medieninf.webanw.belegung.gruppe05.listing.searching.SearchField.SearchFieldType;

/**
 * Listet die Studenten auf
 * 
 * @author Markus Tacker <m@tacker.org>
 */
@ManagedBean(name = "studlist")
@SessionScoped
public class StudentenListBean extends SearchableBean implements
		IFilterableBean {

	private String fachSemesterFilter = "";
	private String studiengangFilter = "";

	/**
	 * Gibt die Liste der Studenten zurück, verteilt auf Seiten.
	 */
	public DataModel<Studierende> getList() {
		cEm();

		ArrayList<IBeanListingCondition> conditions = new ArrayList<IBeanListingCondition>();
		// Fachsemester
		if (fachSemesterFilter != null && fachSemesterFilter.length() > 0
				&& Integer.parseInt(fachSemesterFilter) > 0) {
			conditions.add(new Condition("fachsem", "fachsem", Integer
					.parseInt(fachSemesterFilter)));
		}

		// Studiengang
		BelegungBean bb = new BelegungBean();
		if (studiengangFilter != null && studiengangFilter.length() > 0
				&& Integer.parseInt(studiengangFilter) > 0) {
			conditions.add(new Condition("sg", "studiengang", bb
					.getStudiengang(Integer.parseInt(studiengangFilter))));
		}

		// Freitextsuche
		if (getSearch() != null && getSearch().length() > 0) {
			SearchField sf = getSearchFields().get(getSearchType());
			if (sf instanceof MultiSearchField) {
				MultiSearchField msf = (MultiSearchField) sf;
				conditions
						.add(msf.getConditionGroup(getSearch().toLowerCase()));
			} else {
				conditions.add(sf.getCondition(getSearch().toLowerCase()));
			}
		}

		return new BeanListing<Studierende>(this).getListingData(conditions);
	}

	/**
	 * Gibt die Liste der möglichen Fachsemester zurück
	 */
	public List<SelectItem> getFachSemesterList() {
		cEm();
		List<SelectItem> ret = new ArrayList<SelectItem>();
		ret.add(new SelectItem("", "Alle"));
		String queryString = "SELECT DISTINCT s.fachsem FROM Studierende s ORDER BY s.fachsem ASC";
		Query query = em.get().createQuery(queryString);
		for (Object entry : query.getResultList()) {
			ret.add(new SelectItem(entry.toString()));
		}
		return ret;
	}

	/**
	 * @see ListingBean#getSortFields()
	 */
	@Override
	public ArrayList<String> getSortFields() {
		ArrayList<String> al = new ArrayList<String>();
		for (String f : "matnr,vorname,nachname,fachsem,studiengang,adresse1,adresse2,plz,stadt,land,email,tel"
				.split(","))
			al.add(f);
		return al;
	}

	/**
	 * @see ListingBean#getDefaultSortBy()
	 */
	@Override
	public String getDefaultSortBy() {
		return "nachname";
	}

	/**
	 * Setzt den Wert des Fachsemester-Filters
	 * 
	 * @param fachSemesterFilter
	 */
	public void setFachSemesterFilter(String fachSemesterFilter) {
		this.fachSemesterFilter = fachSemesterFilter;
	}

	/**
	 * Gibt den Wert des Fachsemester-Filters zurück
	 */
	public String getFachSemesterFilter() {
		return fachSemesterFilter;
	}

	/**
	 * Setzt den Wert des Studiengang-Filters
	 * 
	 * @param studiengangFilter
	 */
	public void setStudiengangFilter(String studiengangFilter) {
		this.studiengangFilter = studiengangFilter;
	}

	/**
	 * Gibt den Wert des Studiengang-Filters zurück
	 */
	public String getStudiengangFilter() {
		return studiengangFilter;
	}

	/**
	 * @see IFilterableBean#getSortField()
	 */
	@Override
	public String getSortField() {
		String sortField = getSortBy().equals("studiengang") ? "studiengang.bezeichnung"
				: getSortBy();
		return sortField;
	}

	/**
	 * @see IFilterableBean#getDomain()
	 */
	@Override
	public String getDomain() {
		return "Studierende";
	}

	/**
	 * @see ISearchableBean#getDefaultSearchType()
	 */
	@Override
	public String getDefaultSearchType() {
		return "all";
	}

	/**
	 * @see ISearchableBean#getSearchTypeList()
	 */
	@Override
	public List<SelectItem> getSearchTypeList() {
		List<SelectItem> ret = new ArrayList<SelectItem>();
		HashMap<String, SearchField> sf = getSearchFields();
		for (String key : sf.keySet()) {
			SearchField f = sf.get(key);
			ret.add(new SelectItem(f.getName(), f.getLabel()));
		}
		return ret;
	}

	/**
	 * Um die HashMap mit den Suchefeldern auf zu bauen, wird ein Konfigurations-String zerlegt.
	 * <p>
	 * Eine Felddefinition darin sieht so aus:
	 * <p>
	 * <code>{id}[@{type}]:{label}[:{dbfeld}[|{dbfeld}[|<...>]]]</code>
	 * <ul>
	 * <li>Id ist der Identifier des Feldes, der auch im Formular verwendet wird.
	 * <li>Type (optional) ist der Typ des Feldes, ATM wird nur zwischen String und Integer unterschieden
	 * <li>Label ist die Bezeichnung, wird im Formular angezeigt
	 * <li>Die Liste mit Datenbankfelder in den gesucht wird ist ebenfalls optional und mit "|" getrennt
	 * </ul>
	 * <p>
	 * Die Felddefinitionen sind dann mit Komma getrennt.
	 * 
	 * @see SearchableBean#getSearchFields()
	 */
	@Override
	public HashMap<String, SearchField> getSearchFields() {
		HashMap<String, SearchField> ret = new HashMap<String, SearchField>();
		MultiSearchField allSearch = new MultiSearchField("all", "Alle Felder");
		ret.put("all", allSearch);
		for (String p : "matnr@int:Matrikelnummer,vorname:Vorname,nachname:Nachname,adresse:Adresse:adresse1|adresse2,plz@int:PLZ,stadt:Stadt,email:E-Mail,tel:Telefon"
				.split(",")) {
			String[] parts = p.split(":");
			String[] nameParts = parts[0].split("@");

			SearchField sf = null;
			if (parts.length > 2) {
				String[] fieldDef = parts[2].split("\\|");
				if (fieldDef.length > 1) {
					sf = new MultiSearchField(nameParts[0], parts[1]);
					for (String field : fieldDef) {
						((MultiSearchField) sf).addField(new SearchField(
								nameParts[0], field, parts[1]));
					}
				} else {
					sf = new SearchField(nameParts[0], fieldDef[0], parts[1]);
				}
			} else {
				sf = new SearchField(nameParts[0], nameParts[0], parts[1]);
			}
			if (nameParts.length > 1 && nameParts[1].equals("int"))
				sf.setType(SearchFieldType.INT);
			ret.put(nameParts[0], sf);
			allSearch.addField(sf);
		}
		return ret;
	}
}

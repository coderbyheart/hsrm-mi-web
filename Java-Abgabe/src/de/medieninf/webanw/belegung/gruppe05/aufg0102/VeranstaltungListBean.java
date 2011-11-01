package de.medieninf.webanw.belegung.gruppe05.aufg0102;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Modul;
import de.medieninf.webanw.belegung.Studiengang;
import de.medieninf.webanw.belegung.Veranstaltung;
import de.medieninf.webanw.belegung.gruppe05.listing.BeanListing;
import de.medieninf.webanw.belegung.gruppe05.listing.ListingBean;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.Condition;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.IBeanListingCondition;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.IFilterableBean;

/**
 * Listet die Veranstaltungen auf
 * 
 * @author Markus Tacker <m@tacker.org>
 */
@ManagedBean(name = "veranlist")
@SessionScoped
public class VeranstaltungListBean extends ListingBean implements IFilterableBean {
	private String studiengangFilter = "";
	private String search;
	private String searchType;
	
	/**
	 * Gibt die Liste der Studenten zurück, verteilt auf Seiten.
	 */
	public DataModel<Veranstaltung> getList() {
		cEm();
		
		ArrayList<IBeanListingCondition> conditions = new ArrayList<IBeanListingCondition>();
		BelegungBean bb = new BelegungBean();
		if (studiengangFilter != null && studiengangFilter.length() > 0
				&& Integer.parseInt(studiengangFilter) > 0) {
			Studiengang s = bb.getStudiengang(Integer.parseInt(studiengangFilter));
			ArrayList<Long> modulIds = new ArrayList<Long>();
			for (Modul m: s.getModul()) {
				modulIds.add(m.getModulId());
			}
			Condition c = new Condition("sg", "modul.modulId", modulIds);
			c.setType("IN");
			conditions.add(c);
		}
		
		return new BeanListing<Veranstaltung>(this).getListingData(conditions);
	}

	/**
	 * @see ListingBean#getSortFields()
	 */
	@Override
	public List<String> getSortFields() {
		ArrayList<String> al = new ArrayList<String>();
		for(String f: "veranstaltungId,modul,veranstaltungsTyp,dauer,maxTeilnehmer".split(",")) al.add(f);
		return al;
	}

	/**
	 * @see ListingBean#getDefaultSortBy()
	 */
	@Override
	public String getDefaultSortBy() {
		return "veranstaltungId";
	}

	/**
	 * Setzt den Wert des Studiengang-Filters
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
		String sortField = getSortBy();
		if (sortField.equals("modul")) sortField = "modul.bezeichnung";
		if (sortField.equals("veranstaltungsTyp")) sortField = "veranstaltungsTyp.bezeichnung";
		return sortField;
	}

	/**
	 * @see IFilterableBean#getDomain()
	 */
	@Override
	public String getDomain() {
		return "Veranstaltung";
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

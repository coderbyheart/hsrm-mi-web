package de.medieninf.webanw.belegung.gruppe05.listing;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Basis-Klasse für die Beans in einem Listing, dass mit Hilfe eines Pagers
 * angezeigt wird
 * 
 * @author Markus Tacker <m@tacker.org>
 */
abstract public class ListingBean extends BaseBean implements IListableBean {

	private int page = 1;
	private int rowsPerPage = 20;
	private long numRows = 0;

	private String sortBy;
	private String sortDir = "ASC";
	private HtmlDataTable dataTable;

	public ListingBean() {
		sortBy = getDefaultSortBy();
	}

	/**
	 * Setzt die Nummer der Seite, die angezeigt werden soll.
	 * 
	 * @param page
	 */
	public void setPage(int page) {
		if (page < 1)
			return;
		if (page > getNumPages())
			return;
		this.page = page;
	}

	/**
	 * Gibt die Nummer der aktuelle Seite zurück.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Setzt die Anzahl der Zeilen, die pro Seite angezeigt werden sollen.
	 * 
	 * @param rowsPerPage
	 */
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * @see IListableBean#getRowsPerPage()
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	/**
	 * @see IListableBean#getStart()
	 */
	public int getStart() {
		return (getPage() - 1) * getRowsPerPage();
	}

	/**
	 * @see IListableBean#setNumRows(long)
	 */
	public void setNumRows(long l) {
		this.numRows = l;
	}

	/**
	 * Gibt die Anzahl der Zeilen im gesamten Ergebnis zurück.
	 */
	public long getNumRows() {
		return numRows;
	}

	/**
	 * Gibt die Anzahl der Seiten zurück, die zur Darstellung des Ergebnisses
	 * nötig sind.
	 */
	public int getNumPages() {
		return (int) Math.ceil(getNumRows() / getRowsPerPage());
	}

	/**
	 * Wird aufgerufen, wenn im GUI ein Feld zum Sortieren ausgewählt wird.
	 * 
	 * @param ae
	 */
	public void setSortByAction(ActionEvent ae) {
		String f = ae.getComponent().getAttributes().get("field").toString();
		setSortBy(f);
	}

	/**
	 * Gibt das Feld zurück, nach dem sortiert werden soll.
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * Setzt das Feld, nach dem sortiert werden soll.
	 * 
	 * @param sortBy
	 */
	public void setSortBy(String sortBy) {
		if (getSortFields().contains(sortBy)) {
			if (sortBy.equals(this.sortBy)) {
				// Switch sort direction
				setSortDir(getSortDir().equals("ASC") ? "DESC" : "ASC");
			}
			this.sortBy = sortBy;
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage("setSortBy", new FacesMessage("Unknown field: "
					+ sortBy));
		}
	}

	/**
	 * Setzt die Richtung in der Sortiert wird.
	 * 
	 * @param sortDir
	 */
	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	/**
	 * @see IListableBean#getSortDir()
	 */
	public String getSortDir() {
		return sortDir;
	}

	/**
	 * Geht zur ersten Seite.
	 */
	public void gotoFirstPage() {
		setPage(1);
	}

	/**
	 * Get eine Seite zurück.
	 */
	public void gotoPrevPage() {
		setPage(getPage() - 1);
	}

	/**
	 * Geht eine Seite vor.
	 */
	public void gotoNextPage() {
		setPage(getPage() + 1);
	}

	/**
	 * Geht zur letzten Seite.
	 */
	public void gotoLastPage() {
		setPage(getNumPages());
	}

	/**
	 * Setzt das {@link HtmlDataTable HtmlDataTable-Object}, das zur Anzeige der
	 * Liste verwendet wird.
	 * 
	 * @param dataTable
	 */
	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	/**
	 * Gibt das {@link HtmlDataTable HtmlDataTable-Object} zurück, das zur
	 * Anzeige der Liste verwendet wird.
	 */
	public HtmlDataTable getDataTable() {
		return dataTable;
	}
}
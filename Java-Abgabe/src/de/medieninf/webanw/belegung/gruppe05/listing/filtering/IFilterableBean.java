package de.medieninf.webanw.belegung.gruppe05.listing.filtering;

import de.medieninf.webanw.belegung.gruppe05.listing.BeanListing;
import de.medieninf.webanw.belegung.gruppe05.listing.IListableBean;

/**
 * Interface für Beans, die gefiltert werden können.
 * 
 * @author Markus Tacker <m@tacker.org>
 */
public interface IFilterableBean extends IListableBean {

	/**
	 * Gibt den Namen des Feldes zurück, dass zum Sortieren im Query verwendet
	 * wird.
	 */
	String getSortField();

	/**
	 * Gibt den Namen der Domain (Entity, Tabelle) zurück, die zum erzeugen
	 * eines Listings mit {@link BeanListing} verwendet wird.
	 */
	String getDomain();
}

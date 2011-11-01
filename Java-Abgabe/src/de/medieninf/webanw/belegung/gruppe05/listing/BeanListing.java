package de.medieninf.webanw.belegung.gruppe05.listing;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import de.medieninf.webanw.belegung.gruppe05.listing.filtering.Condition;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.IBeanListingCondition;
import de.medieninf.webanw.belegung.gruppe05.listing.filtering.IFilterableBean;
import de.medieninf.webanw.belegung.gruppe05.listing.searching.ConditionGroup;

/**
 * Diese Klasse enthält die Logik zum erzeugen eines filterbaren Listings.
 * 
 * @author Markus Tacker <m@tacker.org>
 * 
 * FIXME: Suche in allen Feldern
 * 
 * @param <T>
 */
public class BeanListing<T> {

	private IFilterableBean listingBean;

	protected EntityManagerFactory emf;
	protected ThreadLocal<EntityManager> em;
	protected ThreadLocal<EntityTransaction> tx;

	/**
	 * Erzeugt ein neues {@link BeanListing BeanListing-Objekt} für das
	 * übergebene {@link ListingBean Bean}.
	 * 
	 * @param listingBean
	 */
	public BeanListing(IFilterableBean listingBean) {
		this.listingBean = listingBean;
		emf = Persistence.createEntityManagerFactory(BaseBean.persistenceUnit);
		em = new ThreadLocal<EntityManager>();
		tx = new ThreadLocal<EntityTransaction>();
	}

	protected void cEm() {
		if (em.get() != null && em.get().isOpen())
			em.get().close();
		em.set(emf.createEntityManager());
		tx.set(em.get().getTransaction());
	}

	/**
	 * Gibt das befüllte {@link DataModel} zurück, dass entsprechen den
	 * {@link Condition Konditionen} gefilter ist.
	 * 
	 * @param conditions
	 */
	public DataModel<T> getListingData(
			ArrayList<IBeanListingCondition> conditions) {
		String queryString = "SELECT d FROM " + getListingBean().getDomain()
				+ " d ";
		String countQueryString = "SELECT COUNT(d) FROM "
				+ getListingBean().getDomain() + " d ";

		// Filtern
		if (conditions.size() > 0) {
			queryString += "WHERE ";
			countQueryString += "WHERE ";
			Boolean first = true;
			for (IBeanListingCondition cond : conditions) {
				if (first) {
					first = false;
				} else {
					queryString += " AND ";
					countQueryString += " AND ";
				}
				queryString += " " + cond.toQueryCondition("d");
				countQueryString += " " + cond.toQueryCondition("d");
			}
		}

		// Sortieren
		queryString += " ORDER BY d." + getListingBean().getSortField() + " "
				+ getListingBean().getSortDir();

		// Parameter binden
		cEm();
		Query countQuery = em.get().createQuery(countQueryString);
		Query query = em.get().createQuery(queryString);
		query.setMaxResults(getListingBean().getRowsPerPage());
		query.setFirstResult(getListingBean().getStart());
		if (conditions.size() > 0) {
			for (IBeanListingCondition cond : conditions) {
				if (cond instanceof ConditionGroup) {
					for (IBeanListingCondition c : ((ConditionGroup) cond)
							.getConditions()) {
						if (c instanceof ConditionGroup) {
							for (IBeanListingCondition c2 : ((ConditionGroup) c)
									.getConditions()) {
								query.setParameter(c2.getParam(), c2.getValue());
								countQuery.setParameter(c2.getParam(),
										c2.getValue());
							}
						} else {
							query.setParameter(c.getParam(), c.getValue());
							countQuery.setParameter(c.getParam(), c.getValue());
						}
					}
				} else {
					query.setParameter(cond.getParam(), cond.getValue());
					countQuery.setParameter(cond.getParam(), cond.getValue());
				}

			}
		}

		// Ausführen: COUNT
		getListingBean().setNumRows((Long) countQuery.getSingleResult());

		// Ausführen: SELECT
		@SuppressWarnings("unchecked")
		List<T> list = query.getResultList();

		ListDataModel<T> listData = new ListDataModel<T>();
		listData.setWrappedData(list);
		return listData;
	}

	/**
	 * Gibt das {@link ListingBean Bean} zurück, mit dem dieses Listing
	 * arbeitet.
	 */
	public IFilterableBean getListingBean() {
		return listingBean;
	}

}

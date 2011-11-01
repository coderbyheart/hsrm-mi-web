package de.medieninf.webanw.belegung.gruppe05.listing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Basis-Bean für alle Klassen im Package.
 * 
 * Stellt die Datenbank bereit.
 * 
 * @author Markus Tacker <m@tacker.org>
 */
public class BaseBean {
	protected static final String persistenceUnit = "belegung";
	protected EntityManagerFactory emf;
	protected ThreadLocal<EntityManager> em;
	protected ThreadLocal<EntityTransaction> tx;

	public BaseBean() {
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
		em = new ThreadLocal<EntityManager>();
		tx = new ThreadLocal<EntityTransaction>();
	}

	protected void cEm() {
		if (em.get() != null && em.get().isOpen())
			em.get().close();
		em.set(emf.createEntityManager());
		tx.set(em.get().getTransaction());
	}
}

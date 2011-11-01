package de.medieninf.webanw.belegung;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

public class BelegungBean implements Belegung {
	private final static String persistenceUnit = "belegung";
	
    private EntityManagerFactory emf;
    private ThreadLocal<EntityManager> em;
    private ThreadLocal<EntityTransaction> tx;

    public BelegungBean() {
        emf = Persistence.createEntityManagerFactory(persistenceUnit);
        em = new ThreadLocal<EntityManager>();
        tx = new ThreadLocal<EntityTransaction>();
    }

    private void cEm() {
    	if (em.get() != null && em.get().isOpen()) 
    		em.get().close();
    	em.set(emf.createEntityManager());
    	tx.set(em.get().getTransaction());
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public List<Studiengang> getStudiengaenge() {
		cEm();
        String queryString = "SELECT s FROM Studiengang s";
        Query query = em.get().createQuery(queryString);
        List<Studiengang> studiengaenge = query.getResultList();
        return studiengaenge;
	}
        
	@Override
	public Studiengang getStudiengang(long studiengangId) {
		cEm();
		String queryString = "SELECT s FROM Studiengang s WHERE s.studiengangId=:studiengangId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("studiengangId", studiengangId);
		try {
			Studiengang sg = (Studiengang) query.getSingleResult();
			return sg;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Studiengang update(Studiengang studiengang) {
		cEm();
        tx.get().begin();
        Studiengang ret = em.get().merge(studiengang);
        tx.get().commit();
        return ret;
	}
	
	private Query genQuerySearchStudierende(String vorname, String nachname,
			Integer fachsem, Studiengang studiengang, String adresse,
			Integer plz, String stadt, String land, String email, String tel) {
		StringBuffer queryString = new StringBuffer("SELECT s FROM Studierende s ");
		String joinString = " WHERE ";
		if (vorname != null) {
			queryString.append(joinString + "s.vorname LIKE :vorname");
			joinString = " AND ";
		}
		if (nachname != null) {
			queryString.append(joinString + "s.nachname LIKE :nachname");
			joinString = " AND ";
		}
		if (fachsem != null) {
			queryString.append(joinString + "s.fachsem = :fachsem");
			joinString = " AND ";
		}
		if (studiengang != null){
			queryString.append(joinString + "s.studiengang = :studiengang");
			joinString = " AND ";
		}
		if (adresse != null){
			queryString.append(joinString + "(s.adresse1 LIKE :adresse OR s.adresse2 LIKE :adresse)");
			joinString = " AND ";
		}
		if (plz != null){
			queryString.append(joinString + "s.plz = :plz");
			joinString = " AND ";
		}
		if (stadt != null) {
			queryString.append(joinString + "s.stadt LIKE :stadt");
			joinString = " AND ";
		}
		if (land != null) {
			queryString.append(joinString + "s.land LIKE :land");
			joinString = " AND ";
		}
		if (email != null) {
			queryString.append(joinString + "s.email LIKE :email");
			joinString = " AND ";
		}
		if (tel != null) {
			queryString.append(joinString + "s.tel LIKE :tel");
			joinString = " AND ";
		}
		Query query = em.get().createQuery(queryString.toString());
        if (vorname != null)
            query.setParameter("vorname", "%" + vorname + "%");
        if (nachname != null)
            query.setParameter("nachname", "%" + nachname + "%");
        if (fachsem != null)
            query.setParameter("fachsem", fachsem);
        if (studiengang != null)
            query.setParameter("studiengang", studiengang);
        if (adresse != null)
            query.setParameter("adresse", "%" + adresse + "%");
        if (plz != null)
            query.setParameter("plz", plz);
        if (stadt != null)
            query.setParameter("stadt", "%" + stadt + "%");
        if (land != null)
            query.setParameter("land", "%" + land + "%");
        if (email != null)
            query.setParameter("email", "%" + email + "%");
        if (tel != null)
            query.setParameter("tel", "%" + tel + "%");
		return query;
	}

	@Override
	public Studierende getStudierende(long matnr) {
		cEm();
		return getStudierende(matnr, em.get());
	}
	
	private Studierende getStudierende(long matnr, EntityManager em) {
		String queryString = "SELECT s FROM Studierende s WHERE s.matnr=:matnr";
        Query query = em.createQuery(queryString);
		query.setParameter("matnr", matnr);
		try {
			Studierende s = (Studierende) query.getSingleResult();
			return s;
		} catch (NoResultException e) {
			return null;
		}		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Studierende> searchStudierende(String vorname, String nachname,
			Integer fachsem, Studiengang studiengang, String adresse,
			Integer plz, String stadt, String land, String email, String tel,
			int howmany, int start) {
		cEm();
        Query query = genQuerySearchStudierende(vorname, nachname, fachsem, studiengang,
        		adresse, plz, stadt, land, email, tel);
        query.setMaxResults(howmany);
        query.setFirstResult(start);
        List<Studierende> studierende = query.getResultList();
        return studierende;
	}
	
	@Override
	public Studierende update(Studierende studierende) {
		cEm();
        tx.get().begin();
        Studierende ret = em.get().merge(studierende);
        tx.get().commit();
        return ret;
	}
	
	private Studierende refresh(Studierende studierende) {
		return this.getStudierende(studierende.getMatnr());
	}
	
	@Override
	public boolean remove(Studierende studierende) {
		cEm();
		studierende = this.refresh(studierende);
        ArrayList<VeranstaltungGruppe> copy = new ArrayList<VeranstaltungGruppe>();
        copy.addAll(studierende.getBelegungen()); // avoid concurrentModificationException
        tx.get().begin();
        for (VeranstaltungGruppe vg : copy) {
        	boolean ok = removeBelegung(studierende, vg, em.get()); 
        	if (!ok) {
        		tx.get().rollback();
        		return false;
        	}
        }
        em.get().remove(studierende);
        tx.get().commit();
        return true;
	}
	
	@Override
	public Modul getModul(long modulId) {
		cEm();
		String queryString = "SELECT m FROM Modul m WHERE m.modulId=:modulId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("modulId", modulId);
		try {
			Modul m = (Modul) query.getSingleResult();
			return m;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Modul update(Modul modul) {
		cEm();
        tx.get().begin();
        Modul ret = em.get().merge(modul);
        tx.get().commit();
        return ret;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Modul> getModule(Studiengang studiengang) {
		cEm();
		String queryString = "SELECT m FROM Modul m WHERE m.studiengaenge=:studiengaenge";
        Query query = em.get().createQuery(queryString);
		query.setParameter("studiengaenge", studiengang); 
		List<Modul> veranstaltungen = (List<Modul>) query.getResultList();
		return veranstaltungen;				
	}
	
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<VeranstaltungsTyp> getVeranstaltungsTypen() {
		cEm();
        String queryString = "SELECT vt FROM VeranstaltungsTyp vt";
        Query query = em.get().createQuery(queryString);
        List<VeranstaltungsTyp> veranstaltungsTypen = query.getResultList();
        return veranstaltungsTypen;
	}
	
	@Override
	public VeranstaltungsTyp getVeranstaltungsTyp(long id) {
		cEm();
		String queryString = "SELECT vt FROM VeranstaltungsTyp vt WHERE vt.veranstaltungsTypId=:veranstaltungsTypId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("veranstaltungsTypId", id);
		try {
			VeranstaltungsTyp vt = (VeranstaltungsTyp) query.getSingleResult();
			return vt;
		} catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	public VeranstaltungsTyp update(VeranstaltungsTyp veranstaltungsTyp) {
		cEm();
        tx.get().begin();
		VeranstaltungsTyp ret = em.get().merge(veranstaltungsTyp);
        tx.get().commit();
        return ret;
	}
	
	@Override
	public boolean remove(VeranstaltungGruppe veranstaltungGruppe) {
		cEm();
		veranstaltungGruppe = this.refresh(veranstaltungGruppe, em.get());
		EntityTransaction tx = this.tx.get();
		tx.begin();
		boolean ok = remove(veranstaltungGruppe, em.get());
		if (!ok) {
			tx.rollback();
		} else {
			tx.commit();
		}
		return ok;
	}
	
	private boolean remove(VeranstaltungGruppe veranstaltungGruppe, 
			EntityManager em) {
        ArrayList<Studierende> copy = new ArrayList<Studierende>();
        copy.addAll(veranstaltungGruppe.getStudierende()); // avoid concurrentModificationException
        boolean ok = true;
        for (Studierende s : copy) {
        	ok = removeBelegung(s, veranstaltungGruppe, em);
        	if (!ok) {
        		return false;
        	}
        }
        em.remove(veranstaltungGruppe);
        return true;
	}
	
	@Override
	public Veranstaltung getVeranstaltung(long veranstaltungId) {
		cEm();
		return this.getVeranstaltung(veranstaltungId, em.get());
	}
	
	private Veranstaltung getVeranstaltung(long veranstaltungId, EntityManager em) {
		String queryString = "SELECT v FROM Veranstaltung v WHERE v.veranstaltungId=:veranstaltungId";
        Query query = em.createQuery(queryString);
		query.setParameter("veranstaltungId", veranstaltungId);
		try {
			Veranstaltung v = (Veranstaltung) query.getSingleResult();
			return v;
		} catch (NoResultException e) {
			return null;
		}		
	}
	
	private Veranstaltung refresh(Veranstaltung veranstaltung, EntityManager em) {
		return this.getVeranstaltung(veranstaltung.getVeranstaltungId(), em);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Veranstaltung> getVeranstaltungen(Modul modul) {
		cEm();
		String queryString = "SELECT v FROM Veranstaltung v WHERE v.modul=:modul";
        Query query = em.get().createQuery(queryString);
		query.setParameter("modul", modul);
		List<Veranstaltung> veranstaltungen = (List<Veranstaltung>) query.getResultList();
		return veranstaltungen;		
	}

	@Override
	public Veranstaltung update(Veranstaltung veranstaltung) {
		cEm();
        tx.get().begin();
        Veranstaltung ret = em.get().merge(veranstaltung);
        tx.get().commit();
        return ret;
	}

	@Override
	public boolean remove(Veranstaltung veranstaltung) {
		cEm();
		veranstaltung = refresh(veranstaltung, em.get());
        tx.get().begin();
		for (VeranstaltungGruppe vg : this.getVeranstaltungGruppe(veranstaltung, em.get())) {
			boolean ok = this.remove(vg, em.get());
			if (!ok) {
				tx.get().rollback();
				return false;
			}
		}
        em.get().remove(veranstaltung);
        tx.get().commit();
        return true;
	}
	
	@Override
	public VeranstaltungGruppe getVeranstaltungGruppe(Veranstaltung veranstaltung, char gruppe) {
		cEm();
		return getVeranstaltungGruppe(veranstaltung, gruppe, em.get());
	}
	
	private VeranstaltungGruppe getVeranstaltungGruppe(
			Veranstaltung veranstaltung, char gruppe, EntityManager em) {
		String queryString = "SELECT vg FROM VeranstaltungGruppe vg" + 
			" WHERE vg.veranstaltungGruppePk.id_veranstaltung=:id_veranstaltung" + 
			" AND   vg.veranstaltungGruppePk.gid=:gid";
        Query query = em.createQuery(queryString);
		query.setParameter("id_veranstaltung", veranstaltung.getVeranstaltungId());
		query.setParameter("gid", gruppe);
		try {
			VeranstaltungGruppe vg = (VeranstaltungGruppe) query.getSingleResult();
			return vg;
		} catch (NoResultException e) {
			return null;
		}		

	}

	@Override
	public VeranstaltungGruppe createVeranstaltungGruppe(Veranstaltung veranstaltung, char gruppe) {
		cEm();
		veranstaltung = refresh(veranstaltung, em.get());
		if (veranstaltung == null)
			return null;
		VeranstaltungGruppe veranstaltungGruppe = new VeranstaltungGruppe();
		VeranstaltungGruppePK vgpk = new VeranstaltungGruppePK(); 
		vgpk.setId_veranstaltung(veranstaltung.getVeranstaltungId());
		vgpk.setGid(gruppe);
		veranstaltungGruppe.setVeranstaltungGruppePk(vgpk);
		veranstaltungGruppe.setVeranstaltung(veranstaltung);
		veranstaltungGruppe = this.update(veranstaltungGruppe);  // erstellen
		return veranstaltungGruppe;
	}

	
	@Override
	public List<VeranstaltungGruppe> getVeranstaltungGruppe(Veranstaltung veranstaltung) {
		cEm();
		return getVeranstaltungGruppe(veranstaltung, em.get());
	}
	
	@SuppressWarnings("unchecked")
	private List<VeranstaltungGruppe> getVeranstaltungGruppe(Veranstaltung veranstaltung, EntityManager em) {	
		String queryString = "SELECT vg FROM VeranstaltungGruppe vg" + 
			" WHERE vg.veranstaltungGruppePk.id_veranstaltung=:id_veranstaltung";
        Query query = em.createQuery(queryString);
		query.setParameter("id_veranstaltung", veranstaltung.getVeranstaltungId());
		List<VeranstaltungGruppe> gruppen = (List<VeranstaltungGruppe>) query.getResultList();
		return gruppen;	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<VeranstaltungGruppe> searchVeranstaltungGruppe(Studiengang studiengang, String modul, int howMany, int start) {
		cEm();
		String queryString = "SELECT vg FROM VeranstaltungGruppe vg" + 
			" WHERE vg.veranstaltung.modul.bezeichnung LIKE :modulBezeichnung ";
        Query query = em.get().createQuery(queryString);
		query.setParameter("modulBezeichnung", "%"+modul+"%");
        query.setMaxResults(howMany);
        query.setFirstResult(start);
		List<VeranstaltungGruppe> allgruppen = (List<VeranstaltungGruppe>) query.getResultList();
		// IN-clause geht nicht? StackOverflow toplink -> manuell
		List<VeranstaltungGruppe> gruppen;
		if (studiengang != null) {
			gruppen = new ArrayList<VeranstaltungGruppe>();
			for (VeranstaltungGruppe vg : allgruppen) {
				if (vg.getVeranstaltung().getModul().getStudiengaenge().contains(studiengang)) {
					gruppen.add(vg);
				}
			}
		} else {
			gruppen = allgruppen;
		}
		return gruppen;	
	}
	
	@Override
	public VeranstaltungGruppe update(VeranstaltungGruppe veranstaltungGruppe) {
		cEm();
        tx.get().begin();
        VeranstaltungGruppe ret = em.get().merge(veranstaltungGruppe);
        tx.get().commit();
        return ret;
	}
	
	private VeranstaltungGruppe refresh(VeranstaltungGruppe vg, EntityManager em) {
		return this.getVeranstaltungGruppe(
				vg.getVeranstaltung(), 
				vg.getVeranstaltungGruppePk().getGid(), em);
	}
	
	@Override
	public boolean addBelegung(Studierende studierende, VeranstaltungGruppe veranstaltungGruppe) {
		cEm();
		// veranstaltungGruppe = em.get().merge(veranstaltungGruppe);
		// studierende = em.get().merge(studierende);
		List<VeranstaltungGruppe> lvg = studierende.getBelegungen();
		if (lvg.contains(veranstaltungGruppe))
			return false;
        tx.get().begin();
        lvg = studierende.getBelegungen();
		if (lvg.contains(veranstaltungGruppe)) { // check ob auch in der Transaktion ok
			tx.get().rollback();
			return false;
		}
		// es muss an *beiden* Seiten hinzugefügt werden!
		studierende.getBelegungen().add(veranstaltungGruppe);
        veranstaltungGruppe.getStudierende().add(studierende);
        veranstaltungGruppe = em.get().merge(veranstaltungGruppe);
        studierende = em.get().merge(studierende);
        tx.get().commit();
		return true;
	}
	
	@Override
	public boolean removeBelegung(Studierende studierende, VeranstaltungGruppe veranstaltungGruppe) {
		cEm();
		EntityTransaction tx = this.tx.get();
		tx.begin();
		boolean ok = removeBelegung(studierende, veranstaltungGruppe, em.get());
		if (!ok) {
			tx.rollback();
		} else {
			tx.commit();
		}
		return ok;
	}
	
	private boolean removeBelegung(Studierende studierende, VeranstaltungGruppe veranstaltungGruppe, 
			EntityManager em) {
		List<VeranstaltungGruppe> lvg = studierende.getBelegungen();
		if (!lvg.contains(veranstaltungGruppe))
			return false;
        lvg = studierende.getBelegungen();
		if (!lvg.contains(veranstaltungGruppe)) { // check ob auch in der Transaktion ok
			return false;
		}
		// es muss an *beiden* Seiten gelöscht werden!
		// erst an der mappedBy
        veranstaltungGruppe.getStudierende().remove(studierende);
        // dann an der owning
		studierende.getBelegungen().remove(veranstaltungGruppe);
		// dann machen
		veranstaltungGruppe = em.merge(veranstaltungGruppe);
		studierende = em.merge(studierende);
		return true;
	}

}

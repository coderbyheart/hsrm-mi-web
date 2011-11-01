package de.medieninf.webanw.belegung.gruppe05.aufg04;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Modul;
import de.medieninf.webanw.belegung.Studiengang;
import de.medieninf.webanw.belegung.Veranstaltung;
import de.medieninf.webanw.belegung.VeranstaltungsTyp;
import de.medieninf.webanw.belegung.gruppe05.listing.BaseBean;

/**
 * Fügt {@link Veranstaltung Veranstaltungen} hinzu
 * 
 * @author Markus Tacker <m@tacker.org>
 */
@RequestScoped
@ManagedBean(name = "veranadd")
public class VeranstaltungAddBean extends BaseBean {
	private long studiengangId = -1;
	private long modulId = -1;
	private long veranstaltungsTypId = -1;
	private int dauer;
	private int maxTeilnehmer;
	
	/**
	 * Fügt eine neue Veranstaltung hinzu
	 * 
	 * Modul wird hier gesetzt, aber beim Persistieren nicht gespeichert.
	 */
	public void addAction()	{
		
		BelegungBean bb = new BelegungBean();
		
		cEm();
		
		// Das ist ein Workaround dafür, dass Veranstaltung nicht ohne ID erzeugt werden kann
		Query query = em.get().createQuery("SELECT MAX(v.veranstaltungId) FROM Veranstaltung v");
		Long vId = (Long) query.getSingleResult();
			
		Veranstaltung v = new Veranstaltung(vId);
		v.setDauer(dauer);
		v.setMaxTeilnehmer(maxTeilnehmer);
		v.setVeranstaltungsTyp(bb.getVeranstaltungsTyp(veranstaltungsTypId));
		v.setModul(bb.getModul(modulId));
		bb.update(v);
		
		resetAction();
		FacesContext.getCurrentInstance().addMessage("veranadd", new FacesMessage("Eintrag hinzugefügt."));
	}

	private void resetAction() {
		studiengangId = -1;
		modulId = -1;
		veranstaltungsTypId = -1;
		dauer = 0;
		maxTeilnehmer = 0;
	}

	/**
	 * Gibt eine Liste mit {@link Studiengang Studiengängen} zurück
	 */
	public List<SelectItem> getStudiengangList() {
		BelegungBean bb = new BelegungBean();

		List<SelectItem> ret = new ArrayList<SelectItem>();
		ret.add(new SelectItem(-1, "Bitte wählen"));
		for (Studiengang s : bb.getStudiengaenge()) {
			ret.add(new SelectItem(s.getStudiengangId(), s.getBezeichnung()));
		}
		return ret;
	}

	/**
	 * Gibt eine Liste mit {@link Modul Modulen} zurück
	 */
	public List<SelectItem> getModulList() {
		List<SelectItem> ret = new ArrayList<SelectItem>();
		if (getStudiengangId() <= 0)
			return ret;
		BelegungBean bb = new BelegungBean();
		Studiengang s = bb.getStudiengang(getStudiengangId());
		for (Modul m : s.getModul()) {
			ret.add(new SelectItem(m.getModulId(), m.getBezeichnung()));
		}
		return ret;
	}

	/**
	 * Gibt eine Liste mit {@link VeranstaltungsTyp VeranstaltungsTypen} zurück
	 */
	public List<SelectItem> getVeranstaltungsTypList() {
		BelegungBean bb = new BelegungBean();

		List<SelectItem> ret = new ArrayList<SelectItem>();
		for (VeranstaltungsTyp v : bb.getVeranstaltungsTypen()) {
			ret.add(new SelectItem(v.getVeranstaltungsTypId(), v
					.getBezeichnung()));
		}
		return ret;
	}

	public void setStudiengangId(long studiengangId) {
		this.studiengangId = studiengangId;
	}

	public long getStudiengangId() {
		return studiengangId;
	}

	public void setModulId(long modulId) {
		this.modulId = modulId;
	}

	public long getModulId() {
		return modulId;
	}

	public void setVeranstaltungsTypId(long veranstaltungsTypId) {
		this.veranstaltungsTypId = veranstaltungsTypId;
	}

	public long getVeranstaltungsTypId() {
		return veranstaltungsTypId;
	}

	public void setDauer(int dauer) {
		this.dauer = dauer;
	}

	public int getDauer() {
		return dauer;
	}

	public void setMaxTeilnehmer(int maxTeilnehmer) {
		this.maxTeilnehmer = maxTeilnehmer;
	}

	public int getMaxTeilnehmer() {
		return maxTeilnehmer;
	}


}

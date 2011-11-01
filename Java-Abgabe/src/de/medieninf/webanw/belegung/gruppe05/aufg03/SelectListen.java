package de.medieninf.webanw.belegung.gruppe05.aufg03;

import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Studiengang;

/**
 * 
 * Liefert SelectListen für den Einsatz in den jsf Seiten
 * 
 * @author Jan Lietz
 * 
 */
@ManagedBean(name = "selectListen")
@ApplicationScoped
public class SelectListen {

	private BelegungBean bb = null;

	public SelectListen() {
		bb = new BelegungBean();
	}

	/**
	 * Saemtliche Studiengaenge werden aus der Datenbank geholt und in einer
	 * SelectListe abgelegt
	 * 
	 * @author Jan Lietz
	 * @return SelectListe aller Studiengaenge
	 */
	public List<SelectItem> getStudiengangSelectList() {
		List<Studiengang> gaenge = bb.getStudiengaenge();
		List<SelectItem> rek = new LinkedList<SelectItem>();
		for (Studiengang gang : gaenge) {
			rek.add(new SelectItem(gang, gang.getBezeichnung()));
		}
		return rek;

	}

	/**
	 * Legt eine SelectLsite der Wochentage an.
	 * 
	 * @author Jan Lietz
	 * @return SelectListe der Wochentage mit dem WochentagNr als select.
	 */
	public List<SelectItem> getWochenTagSelectList() {
		String name[] = new String[] { "Montag", "Dienstag", "Mitwoch",
				"Donnerstag", "Freitag", "Samstag", "Sonntag" };
		List<SelectItem> rek = new LinkedList<SelectItem>();
		for (int i = 0; i < name.length; i++) {
			rek.add(new SelectItem(i, name[i]));
		}
		return rek;
	}

}

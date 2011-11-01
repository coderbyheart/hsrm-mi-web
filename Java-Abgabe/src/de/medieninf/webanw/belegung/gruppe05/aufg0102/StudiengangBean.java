package de.medieninf.webanw.belegung.gruppe05.aufg0102;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Studiengang;

/**
 * Listet {@link Studiengang Studiengänge}
 * 
 * @author Markus Tacker <m@tacker.org>
 */
@ManagedBean(name = "studiengang")
@RequestScoped
public class StudiengangBean {

	/**
	 * Gibt eine Liste mit {@link Studiengang Studiengängen} zurück
	 */
	public List<SelectItem> getStudiengangList() {

		BelegungBean bb = new BelegungBean();

		List<SelectItem> ret = new ArrayList<SelectItem>();
		ret.add(new SelectItem("", "Alle"));
		for (Studiengang s : bb.getStudiengaenge()) {
			ret.add(new SelectItem(s.getStudiengangId(), s.getBezeichnung()));
		}
		return ret;
	}
}

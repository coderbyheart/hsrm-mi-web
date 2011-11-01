package de.medieninf.webanw.belegung.gruppe05.aufg04;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Modul;
import de.medieninf.webanw.belegung.Studiengang;

/**
 *  Listet {@link Modul Module}
 * 
 * @author Markus Tacker <m@tacker.org>
 */
@ManagedBean(name = "modul")
@RequestScoped
public class ModulBean {

	/**
	 * Gibt eine Liste mit {@link Modul Modulen} zurück
	 */
	public List<SelectItem> getModulList() {

		BelegungBean bb = new BelegungBean();
		
		List<SelectItem> ret = new ArrayList<SelectItem>();
		for(Studiengang s: bb.getStudiengaenge()) {
			SelectItemGroup sg = new SelectItemGroup(s.getBezeichnung());
			List<SelectItem> ms = new ArrayList<SelectItem>();
			for (Modul m : s.getModul()) {
				ms.add(new SelectItem(m.getModulId(), m.getBezeichnung()));
			}
			sg.setSelectItems(ms.toArray(new SelectItem[ms.size()]));
			ret.add(sg);
		}
		return ret;
	}
}


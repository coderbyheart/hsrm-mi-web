package de.medieninf.webanw.belegung.gruppe05.aufg03;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Modul;
import de.medieninf.webanw.belegung.Studierende;
import de.medieninf.webanw.belegung.Veranstaltung;
import de.medieninf.webanw.belegung.VeranstaltungGruppe;

@ManagedBean(name = "studentDetailB")
@SessionScoped
public class StudentDetailBean implements Serializable {

	private static final long serialVersionUID = -5236056935243736613L;
	private Studierende student = null;
	private transient BelegungBean bb;

	private transient List<VeransatltungBelegung> gruppenSelect = null;
	private transient Map<Veranstaltung, List<VeranstaltungGruppe>> gruppenMap = null;

	public StudentDetailBean() {
		bb = new BelegungBean();
	}

	/**
	 * Speichert die geaenderten Belegungen von der SelectListe in die Datenbank
	 * 
	 * @return "studentDetail" die zu oeffnende JSF-Seite
	 */
	public String saveBelegung() {
		try {
			List<VeranstaltungGruppe> vb = new LinkedList<VeranstaltungGruppe>(
					student.getBelegungen());
			for (VeransatltungBelegung g : gruppenSelect) {
				for (VeranstaltungGruppe gr : g.getGruppen()) {
					if (!vb.contains(gr))
						bb.addBelegung(student, gr);
					vb.remove(gr);
				}
			}
			for (VeranstaltungGruppe gr : vb) {
				bb.removeBelegung(student, gr);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getLocalizedMessage()));
		}
		init(student.getMatnr());
		loadVeranList();
		return "studentDetail";
	}

	/**
	 * Speichert die geaenderten StudentenDaten in der Datenbank
	 * 
	 * @return "studentDetail" die zu oeffnende JSF-Seite
	 * @throws Exception
	 */
	public String save() throws Exception {
		try {
			bb.update(student);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getLocalizedMessage()));
		}
		init(student.getMatnr());
		loadVeranList();
		return "studentDetail";

	}

	/**
	 * ActionListener um den zu bearbeitenden Studenten von der JSF-Seite aus zu
	 * laden.
	 * 
	 */
	public void select(ActionEvent ae) {
		Long matnr = (Long) ae.getComponent().getAttributes().get("stud_matnr");
		init(matnr);
		loadVeranList();
	}

	/**
	 * Holt den Studenten aus der Datenbank
	 * 
	 * @param matnr
	 *            Matrikelnummer, welcher der schluessel ist um den Studenten
	 *            aus der Datenbank zu holen
	 */
	private void init(long matnr) {
		student = bb.getStudierende(matnr);
	}

	/**
	 * Klasse welche die Gruppen einer veranstaltung und deren SelectItemListe
	 * verknuepft.
	 * 
	 * @author Jan Lietz
	 * 
	 */
	public class VeransatltungBelegung {
		List<SelectItem> rek = new LinkedList<SelectItem>();
		Veranstaltung v;
		List<VeranstaltungGruppe> gruppen;

		public VeransatltungBelegung(Veranstaltung v) {
			this.v = v;
			this.gruppen = gruppenMap.get(v);
			List<VeranstaltungGruppe> g2 = bb.getVeranstaltungGruppe(v);
			for (VeranstaltungGruppe vg : g2) {
				rek.add(new SelectItem(vg, vg.getVeranstaltungGruppePk()
						.getGid() + ""));
			}
		}

		public List<VeranstaltungGruppe> getGruppen() {
			return gruppen;
		}

		public void setGruppen(List<VeranstaltungGruppe> gruppen) {
			this.gruppen = gruppen;
		}

		public List<SelectItem> getRek() {
			return rek;
		}

		public Veranstaltung getV() {
			return v;
		}

		public void setRek(List<SelectItem> rek) {
			this.rek = rek;
		}

		public void setV(Veranstaltung v) {
			this.v = v;
		}

	}

	/**
	 * Laedt die Liste aller Veranstaltungen im Studiengang des Studenten und
	 * generiert daraus eine Liste der gg
	 */
	private void loadVeranList() {
		gruppenSelect = new LinkedList<VeransatltungBelegung>();
		gruppenMap = new HashMap<Veranstaltung, List<VeranstaltungGruppe>>();
		for (VeranstaltungGruppe g : student.getBelegungen()) {
			Veranstaltung v = g.getVeranstaltung();
			if (!gruppenMap.containsKey(v))
				gruppenMap.put(v, new LinkedList<VeranstaltungGruppe>());
			gruppenMap.get(v).add(g);
			gruppenSelect.add(new VeransatltungBelegung(v));
		}

		List<Modul> module = bb.getModule(student.getStudiengang());
		for (Modul m : module) {
			List<Veranstaltung> vv = bb.getVeranstaltungen(m);
			for (Veranstaltung v : vv) {
				if (!gruppenMap.containsKey(v)) {
					gruppenMap.put(v, new LinkedList<VeranstaltungGruppe>());
					gruppenSelect.add(new VeransatltungBelegung(v));
				}
			}
		}

	}

	public void setGruppenSelect(List<VeransatltungBelegung> gruppenSelect) {
		this.gruppenSelect = gruppenSelect;
	}

	public List<VeransatltungBelegung> getGruppenSelect() {
		return gruppenSelect;
	}

	public Studierende getStudent() {
		return student;
	}

	public void setStudent(Studierende student) {
		this.student = student;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeLong(student.getMatnr());
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		bb = new BelegungBean();
		init(in.readLong());
		loadVeranList();
	}

}

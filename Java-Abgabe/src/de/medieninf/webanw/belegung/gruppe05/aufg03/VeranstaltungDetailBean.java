package de.medieninf.webanw.belegung.gruppe05.aufg03;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Veranstaltung;
import de.medieninf.webanw.belegung.VeranstaltungGruppe;

@ManagedBean(name = "veranDetailB")
@SessionScoped
public class VeranstaltungDetailBean implements Serializable {

	private static final long serialVersionUID = 5882605092958545565L;
	private Veranstaltung veranstaltung = null;
	private List<VeranstaltungGruppe> gruppen = null;
	private transient BelegungBean bb = null;

	public VeranstaltungDetailBean() {
		bb = new BelegungBean();
	}

	/**
	 * Speichert die geaenderten VeranstaltungsDaten in der Datenbank
	 * 
	 * @return "veranDetail" die zu oeffnende JSF-Seite
	 */
	public String save() {
		try {

			bb.update(veranstaltung);
			bb.update(veranstaltung.getModul());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getLocalizedMessage()));

		}
		init(veranstaltung.getVeranstaltungId());
		return "veranDetail";
	}

	/**
	 * ActionListener um die zu bearbeitende Veranstaltung von der JSF-Seite aus
	 * zu laden.
	 * 
	 */
	public void select(ActionEvent ae) {

		Long veranID = (Long) ae.getComponent().getAttributes().get("veran_id");
		init(veranID);
	}

	/**
	 * Holt die Veranstaltung aus der Datenbank
	 * 
	 * @param ID
	 *            der Veranstaltung, welcher der schluessel ist um die
	 *            Veranstaltung aus der Datenbank zu holen
	 */
	private void init(Long id) {
		veranstaltung = bb.getVeranstaltung(id);
		gruppen = bb.getVeranstaltungGruppe(veranstaltung);
	}

	public Veranstaltung getVeranstaltung() {
		return veranstaltung;
	}

	public void setVeranstaltung(Veranstaltung veranstaltung) {
		this.veranstaltung = veranstaltung;
	}

	public List<VeranstaltungGruppe> getGruppen() {
		return gruppen;
	}

	public void setGruppen(List<VeranstaltungGruppe> gruppen) {
		this.gruppen = gruppen;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeLong(veranstaltung.getVeranstaltungId());
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		bb = new BelegungBean();
		veranstaltung = bb.getVeranstaltung(in.readLong());
		gruppen = bb.getVeranstaltungGruppe(veranstaltung);
	}

}

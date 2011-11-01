package de.medieninf.webanw.belegung.gruppe05.aufg03;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.VeranstaltungGruppe;
import de.medieninf.webanw.belegung.VeranstaltungGruppePK;

@ManagedBean(name = "gruppeDetailB")
@SessionScoped
public class GruppeDetailBean implements Serializable {

	private static final long serialVersionUID = -5236056935243736613L;
	private VeranstaltungGruppe gruppe = null;
	private transient BelegungBean bb;

	public GruppeDetailBean() {
		bb = new BelegungBean();
	}

	/**
	 * Speichert die änderungen der Veranstaltungsgruppe in der Datenbank
	 * 
	 * @return "gruppeDetail" die zu oeffnende JSF-Seite
	 */
	public String save() {
		try {
			bb.update(gruppe);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getLocalizedMessage()));
		}
		init(gruppe.getVeranstaltungGruppePk());
		return "gruppeDetail";
	}

	/**
	 * ActionListener um die zu bearbeitende VeranstaltungsGruppe von der
	 * JSF-Seite aus zu laden.
	 * 
	 */
	public void select(ActionEvent ae) {
		VeranstaltungGruppe gruppe2 = (VeranstaltungGruppe) ae.getComponent()
				.getAttributes().get("gruppe");
		init(gruppe2.getVeranstaltungGruppePk());

	}

	/**
	 * Holt die Veranstaltungsgruppe aus der Datenbank
	 * 
	 * @param pk
	 *            VeranstaltungsGruppePK, welcher der schluessel ist um die
	 *            Veranstaltungsgruppe aus der Datenbank zu holen
	 */
	private void init(VeranstaltungGruppePK pk) {
		gruppe = bb.getVeranstaltungGruppe(
				bb.getVeranstaltung(pk.getId_veranstaltung()), pk.getGid());
	}

	/**
	 * * @return Veranstaltungsgruppe welche hier bearbeitet wird
	 */
	public VeranstaltungGruppe getGruppe() {
		return gruppe;
	}

	/**
	 * 
	 * @param gruppe
	 *            die Aktuel zu berarbeitende Veranstaltungsgruppe
	 */
	public void setGruppe(VeranstaltungGruppe gruppe) {
		this.gruppe = gruppe;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeLong(gruppe.getVeranstaltungGruppePk().getId_veranstaltung());
		out.writeChar(gruppe.getVeranstaltungGruppePk().getGid());
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		bb = new BelegungBean();
		VeranstaltungGruppePK pk = new VeranstaltungGruppePK();
		pk.setId_veranstaltung(in.readLong());
		pk.setGid(in.readChar());
		init(pk);
	}
	
	

}

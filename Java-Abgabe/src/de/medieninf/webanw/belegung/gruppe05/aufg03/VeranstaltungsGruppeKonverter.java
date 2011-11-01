package de.medieninf.webanw.belegung.gruppe05.aufg03;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Veranstaltung;
import de.medieninf.webanw.belegung.VeranstaltungGruppe;

/**
 * FaceKonverter, welcher VeranstaltungGruppen von und in String convertiert
 * 
 * @author Jan Lietz
 * 
 */
@FacesConverter(value = "veranstaltungsGruppeConverter")
public class VeranstaltungsGruppeKonverter implements Converter {
	private BelegungBean bb = null;

	public VeranstaltungsGruppeKonverter() {
		bb = new BelegungBean();
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		String[] d = arg2.split(":");
		Veranstaltung v = bb.getVeranstaltung(Long.parseLong(d[0]));
		return bb.getVeranstaltungGruppe(v, d[1].charAt(0));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		VeranstaltungGruppe v = (VeranstaltungGruppe) arg2;
		Long vID = v.getVeranstaltungGruppePk().getId_veranstaltung();
		char c = v.getVeranstaltungGruppePk().getGid();
		return vID + ":" + c;
	}

}

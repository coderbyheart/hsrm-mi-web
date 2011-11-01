package de.medieninf.webanw.belegung.gruppe05.aufg03;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Studiengang;

/**
 * FaceKonverter, welcher ein Studiengang von und in String convertiert
 * 
 * @author Jan Lietz
 * 
 */
@FacesConverter(value = "studiengangConverter")
public class StudiengangKonverter implements Converter {
	private BelegungBean bb = null;

	public StudiengangKonverter() {
		bb = new BelegungBean();
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return bb.getStudiengang(Long.parseLong(arg2));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return ((Studiengang) arg2).getStudiengangId() + "";
	}

}

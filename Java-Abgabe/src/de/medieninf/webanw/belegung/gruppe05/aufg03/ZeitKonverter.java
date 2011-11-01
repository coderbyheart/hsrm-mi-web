package de.medieninf.webanw.belegung.gruppe05.aufg03;

import java.sql.Time;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * FaceKonverter, welcher ein eine Zeiteingabe als String in eine Time-Instance
 * convertiert und umgekehrt
 * 
 * @author Jan Lietz
 * 
 */
@FacesConverter(value = "zeitKonverter")
public class ZeitKonverter implements Converter {

	protected Time uhrzeit;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try {
			return Time.valueOf(arg2);
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage("DIe Zeitangabe ist nicht valide: "+ e.getLocalizedMessage()));
		}
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return ((Time) arg2).toString();
	}

}

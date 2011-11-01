package de.medieninf.webanw.belegung;

import java.io.Serializable;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class TestBackingBean implements Serializable {
	private static final long serialVersionUID = 1041867216445528065L;
	
	long matnr;
	Studierende studi;

	public long getMatnr() { return matnr; }
	public void setMatnr(long matnr) {
		this.matnr = matnr;
		FacesContext fc = FacesContext.getCurrentInstance();
		BelegungBean belegung = (BelegungBean) fc.getApplication().evaluateExpressionGet(fc, "#{belegung}", BelegungBean.class);
		if (belegung == null) {
			fc.addMessage(null, new FacesMessage("cannot get belegung"));
			return;
		}		
		studi = belegung.getStudierende(matnr);
	}

	public Studierende getStudierende() {
		return studi;
	}

	public List<VeranstaltungGruppe> getBelegungFromStudierende() {
		if (studi == null) {
			return null;
		}
		return studi.getBelegungen();
	}
	
	public int getAnzahlBelegungen() {
		if (studi == null) {
			return 0;
		}
		return studi.getBelegungen().size();
	}
	
}

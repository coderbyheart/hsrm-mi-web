package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="namebean")
@RequestScoped
public class NameBean {
	String vorname;
	String nachname;
	String mittelname;
	int alter;

	public NameBean() {
		vorname = "Maxi";
		mittelname = "M.";
		nachname = "Mustermann";
		alter = 0;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vn) {
		this.vorname = vn;
	}

	public String getMittelname() {
		return mittelname;
	}

	public void setMittelname(String mn) {
		this.mittelname = mn;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		if (alter < 0)
			alter = 0;
		this.alter = alter;
	}

	public String getName() {
		return vorname + " " + mittelname + " " + nachname;
	}

	public String gueltig() {
		if (alter > 0
				&& (vorname + " " + mittelname + " " + nachname).length() >= 6) {
			return "ok";
		} else {
			return "fehler";
		}
	}

	public String getStatus() {
		return gueltig();
	}
}
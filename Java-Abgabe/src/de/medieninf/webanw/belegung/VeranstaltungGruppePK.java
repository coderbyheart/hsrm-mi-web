package de.medieninf.webanw.belegung;

import java.io.Serializable;

import javax.persistence.*;

@Embeddable
public class VeranstaltungGruppePK implements Serializable {
	private static final long serialVersionUID = -7420257846579585329L;

	long id_veranstaltung;
	char gid;
	
	public VeranstaltungGruppePK() {
	}
	
	// @Column(name="id_veranstaltung")
	public long getId_veranstaltung() {
		return id_veranstaltung;
	}
	public void setId_veranstaltung(long id_veranstaltung) {
		this.id_veranstaltung = id_veranstaltung;
	}
	
	// @Column(name="gid")
	public char getGid() {
		return gid;
	}
	public void setGid(char gid) {
		this.gid = gid;
	}	
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof VeranstaltungGruppePK)) 
			return false;
		VeranstaltungGruppePK vgpk = (VeranstaltungGruppePK) o;
		if (vgpk.id_veranstaltung != id_veranstaltung)
			return false;
		if (vgpk.gid != gid)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) (id_veranstaltung*Character.MAX_CODE_POINT+gid);
	}

	@Override
	public String toString() {
		return "VGPK[" + id_veranstaltung + " " + gid + "]";
	}
	
}

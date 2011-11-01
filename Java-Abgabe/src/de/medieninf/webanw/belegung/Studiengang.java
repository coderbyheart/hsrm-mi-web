package de.medieninf.webanw.belegung;

import java.io.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="studiengang")
public class Studiengang implements Serializable {
	private static final long serialVersionUID = -3839456453547604154L;

	protected long studiengangId;
	protected int version;
	protected String bezeichnung;
	protected Set<Modul> modul;
	
	protected Studiengang() {		
	}
	
	public Studiengang(long studiengangId) {
		this.studiengangId = studiengangId;
	}
	
	@Id
	@Column(name="id")
	public long getStudiengangId() {
		return studiengangId;
	}
	public void setStudiengangId(long studiengangId) {
		this.studiengangId = studiengangId;
	}

	@Version
	@Column(name="version")
	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name="bezeichnung", length=80)
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	@OneToMany
	@JoinTable(name="modul_studiengang")
	public Set<Modul> getModul() {
		return modul;
	}
	public void setModul(Set<Modul> modul) {
		this.modul = modul;
	}

	@Override
	public String toString() {
		return "[" + studiengangId + "]-" + bezeichnung;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Studiengang)) 
			return false;
		Studiengang sg = (Studiengang) o;
		if (sg.studiengangId != studiengangId)
			return false;
		if (!sg.bezeichnung.equals(bezeichnung))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) studiengangId;
	}
}

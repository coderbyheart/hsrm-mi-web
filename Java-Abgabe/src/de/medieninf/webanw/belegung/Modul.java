package de.medieninf.webanw.belegung;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="modul")
public class Modul implements Serializable {
	private static final long serialVersionUID = 5899021176140917455L;

	protected long modulId;
	protected int version;
	protected String bezeichnung;
	protected Set<Studiengang> studiengaenge;
	
	protected Modul() {
	}
	
	public Modul(long modulId) {
		this.modulId = modulId;
	}
		
	@Id	
	@Column(name="id")
	public long getModulId() {
		return modulId;
	}
	public void setModulId(long modulId) {
		this.modulId = modulId;
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
	@JoinTable(name="modul_studiengang", inverseJoinColumns=@JoinColumn(name="studiengang_id"))
	public Set<Studiengang> getStudiengaenge() { // don't use, there is only one
		return studiengaenge;
	}
	public void setStudiengaenge(Set<Studiengang> studiengaenge) {
		this.studiengaenge = studiengaenge;
	}
	
	public Studiengang getStudiengang() { // use that one (and i start hating JPA)
		return getStudiengaenge().iterator().next();
	}
	
	@Override
	public String toString() {
		return "[" + modulId + "]-" + bezeichnung;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Modul)) 
			return false;
		Modul modul = (Modul) o;
		if (modul.modulId != modulId)
			return false;
		if (!modul.bezeichnung.equals(bezeichnung))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) this.modulId;
	}
	
}

package de.medieninf.webanw.belegung;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="veranstaltung")
public class Veranstaltung implements Serializable {
	private static final long serialVersionUID = 231737709093857221L;
	
	protected long veranstaltungId;
	protected int version;
	protected Modul modul;
	protected VeranstaltungsTyp veranstaltungsTyp;
	protected int dauer;
	protected int maxTeilnehmer;
	
	protected Veranstaltung() {
	}
	
	public Veranstaltung(long veranstaltungId) {
		this.veranstaltungId = veranstaltungId;
	}
	
	@Id
	@Column(name="id")
	public long getVeranstaltungId() {
		return veranstaltungId;
	}
	public void setVeranstaltungId(long veranstaltungId) {
		this.veranstaltungId = veranstaltungId;
	}

	@Version
	@Column(name="version")
	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
		this.version = version;
	}	
	
	@ManyToOne
    @JoinColumn(name="modul_id", nullable=false, referencedColumnName="id")
	public Modul getModul() {
		return modul;
	}
	public void setModul(Modul modul) {
		this.modul = modul;
	}
	
	@ManyToOne
    @JoinColumn(name="typ_id", nullable=false, referencedColumnName="id")	
	public VeranstaltungsTyp getVeranstaltungsTyp() {
		return veranstaltungsTyp;
	}
	public void setVeranstaltungsTyp(VeranstaltungsTyp veranstaltungsTyp) {
		this.veranstaltungsTyp = veranstaltungsTyp;
	}
	
	@Column(name="dauer")
	public int getDauer() {
		return dauer;
	}
	public void setDauer(int dauer) {
		this.dauer = dauer;
	}
	
	@Column(name="max_teiln")
	public int getMaxTeilnehmer() {
		return maxTeilnehmer;
	}
	public void setMaxTeilnehmer(int maxTeilnehmer) {
		this.maxTeilnehmer = maxTeilnehmer;
	}
	
	@Override
	public String toString() {
		return "[" + getVeranstaltungId() + ", " + getModul().getModulId() + getVeranstaltungsTyp() + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;		
		if (!(o instanceof Veranstaltung))
			return false;
		Veranstaltung v = (Veranstaltung) o;
		if (v.veranstaltungId != veranstaltungId)
			return false;
		if (!v.modul.equals(modul))
			return false;
		if (!v.veranstaltungsTyp.equals(veranstaltungsTyp))
			return false;
		if (v.dauer != dauer)
			return false;
		if (v.maxTeilnehmer != maxTeilnehmer)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) veranstaltungId;
	}
	
}

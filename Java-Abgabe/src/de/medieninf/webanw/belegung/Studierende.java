package de.medieninf.webanw.belegung;

import java.io.*;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="studierende")
public class Studierende implements Serializable {
	private static final long serialVersionUID = 6712980602633035459L;
	
	protected long matnr;
	protected int version;
	protected String vorname;
	protected String nachname;
	protected int fachsem;
	protected Studiengang studiengang;
	protected String adresse1;
	protected String adresse2;
	protected int plz;
	protected String stadt;
	protected String land;
	protected String email;
	protected String tel;
	protected List<VeranstaltungGruppe> belegungen;

	protected Studierende() {
	}
	
	public Studierende(long matnr) {
		this.matnr = matnr;
	}
	
	@Id
	@Column(name="matnr")
	public long getMatnr() {
		return matnr;
	}
	public void setMatnr(long matnr) {
		this.matnr = matnr;
	}

	@Version
	@Column(name="version")
	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
		this.version = version;
	}	
	
	@Column(name="vorname", nullable=false, length=80)
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	
	@Column(name="nachname", nullable=false, length=80)
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	
	@Column(name="fachsem", nullable=false)
	public int getFachsem() {
		return fachsem;
	}
	public void setFachsem(int fachsem) {
		this.fachsem = fachsem;
	}

	@ManyToOne
    @JoinColumn(name="studiengang_id", nullable=false)
	public Studiengang getStudiengang() {
		return studiengang;
	}
	public void setStudiengang(Studiengang studiengang) {
		this.studiengang = studiengang;
	}
	
	@Column(name="adresse1", length=80)
	public String getAdresse1() {
		return adresse1;
	}
	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}
	
	@Column(name="adresse2", length=80)
	public String getAdresse2() {
		return adresse2;
	}
	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}
	
	@Column(name="plz", precision=5, scale=0)
	public int getPlz() {
		return plz;		
	}
	public void setPlz(int plz) {
		this.plz = plz;
	}
	
	@Column(name="stadt", length=80)
	public String getStadt() {
		return stadt;
	}
	public void setStadt(String stadt) {
		this.stadt = stadt;
	}
	
	@Column(name="land", length=80)
	public String getLand() {
		return land;
	}
	public void setLand(String land) {
		this.land = land;
	}
	
	@Column(name="email", length=80)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="tel", length=80)
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@ManyToMany(mappedBy="studierende")
	public List<VeranstaltungGruppe> getBelegungen() {
		return belegungen;
	}
	public void setBelegungen(List<VeranstaltungGruppe> belegungen) {
		this.belegungen = belegungen;
	}
	
	@Override
	public String toString() {
		return vorname + " " + nachname + " [" + matnr + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Studierende)) 
			return false;
		Studierende s = (Studierende) o;
		if (s.matnr != matnr)
			return false;
		if (!s.adresse1.equals(adresse1))
			return false;
		if (!s.adresse2.equals(adresse2))
			return false;
		if (!s.email.equals(email))
			return false;
		if (s.fachsem != fachsem)
			return false;
		if (!s.land.equals(land))
			return false;
		if (!s.nachname.equals(nachname))
			return false;
		if (s.plz != plz)
			return false;
		if (!s.stadt.equals(stadt))
			return false;
		if (!s.studiengang.equals(studiengang))
			return false;
		if (!s.tel.equals(tel))
			return false;
		if (!s.vorname.equals(vorname))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) matnr;
	}
	
}

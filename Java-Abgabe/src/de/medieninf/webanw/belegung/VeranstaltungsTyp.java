package de.medieninf.webanw.belegung;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="veranstaltungstyp")
public class VeranstaltungsTyp implements Serializable {
	private static final long serialVersionUID = 7118813350307196916L;
	
	protected long veranstaltungsTypId;
	int version;
	protected String bezeichnung;
	protected int maxTeilnehmerDefault;
	protected int maxDuplikateDefault;
	protected int dauerDefault;
	
	public VeranstaltungsTyp() {		
	}
	
	@Id
    @SequenceGenerator(name="VeranstaltungstypIdGen", sequenceName="veranstaltungstyp_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VeranstaltungstypIdGen")
	@Column(name="id")
	public long getVeranstaltungsTypId() {
		return veranstaltungsTypId;
	}
	public void setVeranstaltungsTypId(long veranstaltungsTypId) {
		this.veranstaltungsTypId = veranstaltungsTypId;
	}

	@Version
	@Column(name="version")
	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
		this.version = version;
	}	
	
	@Column(name="bezeichnung")
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	@Column(name="max_teiln_def")
	public int getMaxTeilnehmerDefault() {
		return maxTeilnehmerDefault;
	}
	public void setMaxTeilnehmerDefault(int maxTeilnehmerDefault) {
		this.maxTeilnehmerDefault = maxTeilnehmerDefault;
	}
	
	@Column(name="max_dupl_def")
	public int getMaxDuplikateDefault() {
		return maxDuplikateDefault;
	}
	public void setMaxDuplikateDefault(int maxDuplikateDefault) {
		this.maxDuplikateDefault = maxDuplikateDefault;
	}
	
	@Column(name="dauer_def")
	public int getDauerDefault() {
		return dauerDefault;
	}
	public void setDauerDefault(int dauerDefault) {
		this.dauerDefault = dauerDefault;
	}
	
	@Override
	public String toString() {
		return getBezeichnung();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;		
		if (!(o instanceof VeranstaltungsTyp))
			return false;
		VeranstaltungsTyp vt = (VeranstaltungsTyp) o;
		if (vt.veranstaltungsTypId != veranstaltungsTypId)
			return false;
		if (!vt.bezeichnung.equals(bezeichnung))
			return false;
		if (vt.dauerDefault != dauerDefault)
			return false;
		if (vt.maxDuplikateDefault != maxDuplikateDefault)
			return false;
		if (vt.maxTeilnehmerDefault != maxTeilnehmerDefault)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) veranstaltungsTypId;
	}
	
}

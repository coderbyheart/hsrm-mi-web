package de.medieninf.webanw.belegung;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="veranstaltung_gruppe")
public class VeranstaltungGruppe implements Serializable{
	private static final long serialVersionUID = -4809278666830943393L;
	
	protected VeranstaltungGruppePK primaryKey;	
	protected int version;
	protected Veranstaltung veranstaltung;
	protected int wochentag;
	protected java.sql.Time uhrzeit;
	protected List<Studierende> studierende;
	
	public VeranstaltungGruppe() {
		primaryKey = new VeranstaltungGruppePK();
	}
	
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name="id_veranstaltung", column=@Column(name="id_veranstaltung")),
		@AttributeOverride(name="gid", column=@Column(name="gid"))
	})
	public VeranstaltungGruppePK getVeranstaltungGruppePk() {
		return primaryKey;
	}
	public void setVeranstaltungGruppePk(VeranstaltungGruppePK primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Version
	@Column(name="version")
	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
		this.version = version;
	}	
	
	// das insertable=false und updatable=false ist wichtig, da ansonsten 
	// zweimal veranstaltung gemappt wäre
	@ManyToOne
    @JoinColumn(name="id_veranstaltung", referencedColumnName="id", insertable=false, updatable=false)
    public Veranstaltung getVeranstaltung() {
        return veranstaltung;
    }
    public void setVeranstaltung(Veranstaltung veranstaltung) {
        this.veranstaltung = veranstaltung;
    }
    
	@Column(name="wochentag")
	public int getWochentag() {
		return wochentag;
	}
	public void setWochentag(int wochentag) {
		this.wochentag = wochentag;
	}
		
	@Column(name="uhrzeit")
	public java.sql.Time getUhrzeit() {
		return uhrzeit;
	}
	public void setUhrzeit(java.sql.Time uhrzeit) {
		this.uhrzeit = uhrzeit;
	}
	
	// Das funktionierte erst überhaupt nicht, da angeblich gid nicht
	// existiert - Meiner Ansicht nach ein Bug. Er muss das selbst aus
	// der EmbeddedId rauslesen.
	// Man muss aktiv Column gid als Attribut definieren damit er die Selection 
	// richtig umsetzen kann. Aber dann muss man auch für gid updatable und insertable 
	// auf false setzen, damit er nicht zweimal mapped (also weiss er schon, 
	// dass er ein gid hat). Rotz!
	// Jetzt geht nur das Hinzufügen, aber nicht das löschen, obwohl CascadeType.ALL
	// gesetzt ist. Laut Datenbank-Log setzer er zwar die Matrikelnummer beim Löschen,
	// aber nicht v_id und v_gid, die bleiben leider NULL. Riesenrotz!  
	// 2011: EclipseLink, besser?
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="studierende_veranstaltung_gruppe",
			inverseJoinColumns={@JoinColumn(name="matnr")},
			joinColumns={
				@JoinColumn(name="v_id", referencedColumnName="id_veranstaltung"), 
				@JoinColumn(name="v_gid", referencedColumnName="gid")
			}
	)
	public List<Studierende>  getStudierende() {
		return studierende;
	}
	public void setStudierende(List<Studierende> studierende) {
		this.studierende = studierende;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof VeranstaltungGruppe))
			return false;
		VeranstaltungGruppe vg = (VeranstaltungGruppe) o;
		if (!vg.primaryKey.equals(primaryKey))
			return false;
		if (vg.wochentag != wochentag)
			return false;
		if (!vg.uhrzeit.equals(uhrzeit))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return primaryKey.hashCode() + wochentag + uhrzeit.hashCode();
	}
	
	@Override
	public String toString() {
		return "VG_PK[" + primaryKey.getId_veranstaltung() + primaryKey.getGid() + "]";
	}
	
}


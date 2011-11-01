package de.medieninf.webanw.belegung;

import javax.persistence.*;

@Entity
@Table(name="modul_studiengang")
@IdClass(ModulStudiengangId.class)
public class ModulStudiengang {
	@Id
	@Column(name="modul_id")
	private long modulId;

	@Id
	@Column(name="studiengang_id")
	private long studiengangId;

	@Column(name="fachsem")
	private int fachsemester;

	@ManyToOne
	@PrimaryKeyJoinColumn(name="modul_id", referencedColumnName="id")
	private Modul modul;

	@ManyToOne
	@PrimaryKeyJoinColumn(name="studiengang_id", referencedColumnName="id")
	private Studiengang studiengang;
	
	@Override
	public String toString() {
		return "[m= " + modulId + ", sg="+studiengangId + "]-" + fachsemester;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof ModulStudiengang)) 
			return false;
		ModulStudiengang ms = (ModulStudiengang) o;
		if (ms.modulId != modulId)
			return false;
		if (ms.studiengangId != studiengangId)
			return false;
		if (ms.fachsemester != fachsemester)
			return false;
		if (!ms.modul.equals(modul))
			return false;
		if (!ms.studiengang.equals(studiengang))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) (modulId*1024 + studiengangId);
	}
	
}

class ModulStudiengangId {

	@Id
	@Column(name="modul_id")
	private long modulId;

	@Id
	@Column(name="studiengang_id")
	private long studiengangId;

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof ModulStudiengangId))
			return false;
		ModulStudiengangId mid = (ModulStudiengangId) o;
		if (mid.modulId != modulId)
			return false;
		if (mid.studiengangId != studiengangId)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) (modulId+studiengangId*10000);
	}
}

package de.medieninf.webanw.belegung.gruppe05.aufg04;

import java.util.LinkedList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Query;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Studierende;
import de.medieninf.webanw.belegung.VeranstaltungGruppe;

import de.medieninf.webanw.belegung.gruppe05.listing.BaseBean;

/**
 * Fügt {@link Studierende Studenten} hinzu
 * 
 * @Jan Lietz
 */
@RequestScoped
@ManagedBean(name = "studAddB")
public class StudentAddBean extends BaseBean {
	private BelegungBean bb;
	private Studierende student;

	public StudentAddBean() {
		bb = new BelegungBean();
		newStudent();

	}
	
	
	/**
	 * Speichert neuen Studenten in die Datenbank
	 * 
	 * 
	 */
	public void addAction() {
		try {
			bb.update(student);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getLocalizedMessage()));
			return;
		}
		newStudent();
		FacesContext.getCurrentInstance().addMessage("studentadd",
				new FacesMessage("Eintrag hinzugefügt."));

	}
	
public Studierende getStudent() {
	return student;
}
public void setStudent(Studierende student) {
	this.student = student;
}

	/**
	 * Erzeugt neue Studenten-Instance
	 */
	private void newStudent() {
		cEm();

		Query query = em.get().createQuery(
				"SELECT MAX(s.matnr) FROM Studierende s");
		Long matnr = (Long) query.getSingleResult();

		student = new Studierende(matnr + 1);
		student.setBelegungen(new LinkedList<VeranstaltungGruppe>());

	}

	

}

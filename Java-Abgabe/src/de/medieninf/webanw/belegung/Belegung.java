package de.medieninf.webanw.belegung;

import java.util.*;

public interface Belegung {
	
	/**
	 * Gebe alle verfügbaren Studiengänge zurück
	 * @return Liste aller Studiengänge
	 */
	List<Studiengang> getStudiengaenge();
	
	/**
	 * Gibt Studiengang anhand seiner ID zurück wenn vorhanden, null sonst.
	 * @param id int StudiengangId
	 * @return Studiengang mit id wenn vorhanden, null sonst
	 */
	Studiengang getStudiengang(long studiengangId);

	/**
	 * Aktualisiert oder erstellt Studiengang.
	 * @param studiengang Studiengang
	 * @return geänderter oder neuer Studiengang
	 */
	Studiengang update(Studiengang studiengang);
	
	/**
	 * Gibt Studierenden anhand Matrikelnummer zurück, wenn vorhanden, null sonst.
	 * @param matnr int Matrikelnummer
	 * @return Studierende, wenn mit matnr vorhanden, null sonst.
	 */
	Studierende getStudierende(long matnr);
	
	/**
	 * Sucht Studierende anhand von Eigenschaften des Studierenden. 
	 * Eigenschaft wird beim Filtern ignoriert, wenn die Eigenschaft null ist.
	 * Für Eigenschaften mit Typ String wird der String als Teilstring 
	 * gesucht (LIKE). Eigenschaften mit anderen Typen müssen genau passen.  
	 * @param vorname String 
	 * @param nachname String
	 * @param fachsem Integer Fachsemester  
	 * @param studiengang Studiengang
	 * @param adresse String Adresse1 und Adresse2
	 * @param plz Integer Postleitzahl
	 * @param stadt String 
	 * @param land String
	 * @param email String
	 * @param tel String Telefonnummer
     * @param howMany Maximale Anzahl von Studierenden, die zurückgegeben werden
     * @param start Der Index des ersten Studierenden, der zurückgegeben wird
     * @return Liste der Studierenden, die auf die Kriterien passen
	 */
	public List<Studierende> searchStudierende(
		String vorname,
		String nachname,
		Integer fachsem,
		Studiengang studiengang,
		String adresse,
		Integer plz,
		String stadt,
		String land,
		String email,
		String tel,
		int howmany,
		int start
	);

	/**
	 * Aktualisiert Studierende oder legt neuen an.
	 * @param studierende Studierende
	 * @return neuer oder geänderter Studiengang
	 */
	Studierende update(Studierende studierende);
	
	/**
	 * Löscht Studierende. Belegungen des Studierenden werden mit gelöscht.
	 * @param studierende Studierende
	 * @return true gdw Löschen war erfolgreich
	 */
	boolean remove(Studierende studierende);
	
	
	/**
	 * Gibt Modul anhand seiner ID zurück wenn vorhanden, null sonst.
	 * @param id int modulId
	 * @return Modul mit id wenn vorhanden, null sonst
	 */
	Modul getModul(long modulId);

	/**
	 * Aktualisiert oder erstellt Modul.
	 * @param modul Modul
	 * @return geändertes oder neues Modul
	 */
	Modul update(Modul modul);

	/**
	 * Gebe alle verfügbaren Module eines Studiengangs zurück.
	 * @param studiengang Studiengang
	 * @return List<Modul> verfügbare Module des Studiengangs
	 */
	List<Modul> getModule(Studiengang studiengang);
	
	/**
	 * Gebe alle verfügbaren Veranstaltungstypen zurück
	 * @return Liste aller Veranstaltungstypen
	 */
	List<VeranstaltungsTyp> getVeranstaltungsTypen();

	/**
	 * Gibt VeranstaltungsTyp anhand seiner ID zurück wenn vorhanden, null sonst.
	 * @param id int ID des Veranstaltungstyps
	 * @return VeranstaltungsTyp mit ID veranstaltungsTypId wenn vorhanden, null sonst
	 */
	VeranstaltungsTyp getVeranstaltungsTyp(long veranstaltungsTypId);

	/**
	 * Aktualisiert oder erzeugt neuen Veranstaltungstyp.
	 * @param veranstaltungsTyp VeranstaltungsTyp
	 * @return neuer oder erzeugter Veranstaltungstyp
	 */
	VeranstaltungsTyp update(VeranstaltungsTyp veranstaltungsTyp);

	/**
	 * Gibt Veranstaltung anhand Ihrer ID zurück
	 * @param veranstaltungId long ID der Veranstaltung
	 * @return Veranstaltung mit ID veranstaltungId wenn vorhanden, null sonst
	 */
	Veranstaltung getVeranstaltung(long veranstaltungId);
	
	/**
	 * Veranstaltung Gruppe einer Veranstaltung und einer GruppenId
	 * Achtung: Nicht versuchen manuell die Belegungen zu aktualisieren, das geht leider 
	 * leider nur teilweise ....
	 * Verwenden Sie statt dessen addBelegung bzw. removeBelegung.
	 * @param veranstaltung Veranstaltung
	 * @param gruppe char GruppenId
	 * @return VeranstaltungGruppe einer Veranstaltung mit passender Gruppenid wenn vorhanden, null sonst
	 */
	VeranstaltungGruppe getVeranstaltungGruppe(Veranstaltung veranstaltung, char gruppe);

	/**
	 * Erzeugt neue VeranstaltungGruppe.
	 * @param veranstaltung
	 * @param gruppe
	 * @return neue VeranstaltungGruppe oder Ausnahme falls schon vorhanden
	 */
	VeranstaltungGruppe createVeranstaltungGruppe(Veranstaltung veranstaltung, char gruppe);
	
	/**
	 * Gibt alle verfügbaren Veranstaltungen eines Moduls zurück.
	 * @param modul Modul 
	 * @return Liste der Veranstaltungen eines Moduls
	 */
	List<Veranstaltung> getVeranstaltungen(Modul modul);
	
	/**
	 * Aktualisiert oder erzeugt Veranstaltung.
	 * @param veranstaltung Veranstaltung
	 * @return neue oder geänderte Veranstaltung
	 */
	Veranstaltung update(Veranstaltung veranstaltung);
	
	/**
	 * Löscht Veranstaltung. Gruppen dieser Veranstaltung werden mit
	 * gelöscht.
	 * @param veranstaltung Veranstaltung
	 * @return true gdw Löschen war erfolgreich
	 */
	boolean remove(Veranstaltung veranstaltung);
	
	/**
	 * Gibt alle Gruppen einer Veranstaltung zurück
	 * @param veranstaltung Veranstaltung
	 * @return Liste aller Gruppen einer Veranstaltung
	 */
	List<VeranstaltungGruppe> getVeranstaltungGruppe(Veranstaltung veranstaltung);
	
	/**
	 * Aktualisiert oder erzeugt neue VeranstaltungGruppe.
	 * @param veranstaltungGruppe VeranstaltungGruppe
	 * @return neue oder geänderte Veranstaltungsgruppe
	 */
	VeranstaltungGruppe update(VeranstaltungGruppe veranstaltungGruppe);

	/**
	 * Entfernt VeranstaltungGruppe
	 * @param veranstaltungGruppe VeranstaltungGruppe
	 * @return true gdw Löschen war erfolgreich
	 */
	boolean remove(VeranstaltungGruppe veranstaltungGruppe);

	/**
	 * Sucht Veranstaltunggruppen eines Moduls anhand der Bezeichnung
	 * ggf. mit Einschränkung auf den Studiengang
	 * @param studiengang Studiengang, null wenn jeder Studiengang
	 * @param modul String Teilstring in Modulnbezeichnung nach der gesucht wird
     * @param howMany Maximale Anzahl von VeranstaltungGruppen, die zurückgegeben werden
     * @param start Der Index der ersten VeranstaltungGruppe, die zurückgegeben wird
 	 * @return
	 */
	List<VeranstaltungGruppe> searchVeranstaltungGruppe(
			Studiengang studiengang, 
			String modul,
			int howMany,
			int start);

	/**
	 * Hinzufügen einer Belegung eines Studierenden zu einer Veranstaltung einer spezifischen Gruppe.
	 * @param studierende Studierende
	 * @param veranstaltungGruppe VeranstaltungGruppe
	 * @return true bei Strukturänderung, also wenn Belegung noch nicht existierte.
	 */
	boolean addBelegung(Studierende studierende, VeranstaltungGruppe veranstaltungGruppe);

	/**
	 * Entfernen einer Belegung eines Studierenden in einer Veranstaltung mit spezifischer Gruppe.
	 * @param studierende Studierende
	 * @param veranstaltungGruppe VeranstaltungGruppe
	 * @return true bei Strukturänderung, also wenn Belegung vorher existierte.
	 */
	boolean removeBelegung(Studierende studierende, VeranstaltungGruppe veranstaltungGruppe);
}

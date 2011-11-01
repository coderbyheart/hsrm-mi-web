package de.medieninf.webanw.belegung.gruppe05.aufg05;

import java.util.List;

/**
 * Klasse VeranstaltungListeBean repräsentiert eine Veranstaltung, 
 * wie sie zur Anzeige und Verarbeitung bei der Wahl gebraucht wird.
 * @author Simon Franzen
 *
 */
public class VeranstaltungListBean {
	
	private long modulid;
	private String modulbez;
	private long veranstaltungsid;
	private String veranstaltungstyp;
	private int maximalTeilnehmer;
	private List <GruppeBean> gruppen; 
	
	/**
	 * Konstruktor von VeranstaltungListBean.
	 */
	public VeranstaltungListBean(){
			
	}
	
	/**
	 * Getter für die Veranstaltungs ID.
	 * @return veranstaltungsId
	 */
	public long getVeranstaltungsid() {
		return veranstaltungsid;
	}
	/**
	 * Setter für die VEranstaltungs ID.
	 * @param veranstaltungsid
	 */
	public void setVeranstaltungsid(long veranstaltungsid) {
		this.veranstaltungsid = veranstaltungsid;
	}
	/**
	 * Getter für den Veranstaltungstyp.
	 * @return veranstaltungstyp
	 */
	public String getVeranstaltungstyp() {
		return veranstaltungstyp;
	}
	
	/**
	 * Setter für den Veranstaltungstyp.
	 * @param veranstaltungstyp
	 */
	public void setVeranstaltungstyp(String veranstaltungstyp) {
		this.veranstaltungstyp = veranstaltungstyp;
	}
	
	/**
	 * Setter für die ModulID.
	 * @param modulid
	 */
	public void setModulid(long modulid) {
		this.modulid = modulid;
	}
	
	/**
	 * Getter für die Modul ID
	 * @return modulid
	 */
	public long getModulid() {
		return modulid;
	}
	
	/**
	 * Setter für die Modulbezeichnung.
	 * @param modulbez
	 */
	public void setModulbez(String modulbez) {
		this.modulbez = modulbez;
	}
	
	/**
	 * Getter für die Modulbezeichnung.
	 * @returnmodulbez
	 */
	public String getModulbez() {
		return modulbez;
	}
	
	/**
	 * Setter für die Liste aus Gruppenbeans.
	 * @param gruppen
	 */
	public void setGruppen(List <GruppeBean> gruppen) {
		this.gruppen = gruppen;
	}
	
	/**
	 * Getter für die Liste aus Gruppenbeans.
	 * @return gruppen
	 */
	public List <GruppeBean> getGruppen() {
		return gruppen;
	}
	
	/**
	 * Setter für Maximalteilnehmer.
	 * @param maximalTeilnehmer
	 */
	public void setMaximalTeilnehmer(int maximalTeilnehmer) {
		this.maximalTeilnehmer = maximalTeilnehmer;
	}
	
	/**
	 * Getter für Maximalteilnehmer.
	 * @return
	 */
	public int getMaximalTeilnehmer() {
		return maximalTeilnehmer;
	}
	
	/**
	 * Getter für die erste Gruppe.
	 * Wird gebraucht um Belegungen anzuzeigen.
	 */
	public GruppeBean getFirstGruppe(){
		return this.gruppen.get(0);
	}
}

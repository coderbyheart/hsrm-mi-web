package de.medieninf.webanw.belegung.gruppe05.aufg05;

/**
 * Klasse GruppenBean repräsentiert die Daten einer Gruppe einer VeranstaltunListBean.
 * @author Simon Franzen
 *
 */
public class GruppeBean {

	private int teilnehmer;
	private String wochentag;
	private String uhrzeit;
	private char gruppe;
	private boolean checked;
	
	/**
	 * Konstruktor von GruppeBean.
	 */
	public GruppeBean(){
		
	}

	/**
	 * Getter für die Teilnehmeranzahl.
	 * @return teilnehmer
	 */
	public int getTeilnehmer() {
		return teilnehmer;
	}

	/**
	 * Setter für die Teilnehmeranzahl.
	 * @param teilnehmer
	 */
	public void setTeilnehmer(int teilnehmer) {
		this.teilnehmer = teilnehmer;
	}
	
	/**
	 * Getter für den Wochentag.
	 * @return wochentag
	 */
	public String getWochentag() {
		return wochentag;
	}

	/**
	 * Setter für den Wochentag.
	 * @param wochentag
	 */
	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}

	/**
	 * Getter für die Uhrzeit.
	 * @return uhrzeit
	 */
	public String getUhrzeit() {
		return uhrzeit;
	}

	/**
	 * Setter für die Uhrzeit.
	 * @param uhrzeit
	 */
	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	/**
	 * Getter für die Gruppenbuchstaben.
	 * @return gruppe
	 */
	public char getGruppe() {
		return gruppe;
	}

	/**
	 * Setter für den Gruppenbuchstaben.
	 * @param gruppe
	 */
	public void setGruppe(char gruppe) {
		this.gruppe = gruppe;
	}

	/**
	 * Getter für die Angabe, ob Gruppe gewählt wurde.
	 * @return checked
	 */
	public boolean getChecked() {
		return checked; 
	}

	/**
	 * Setter für die Angabe, ob Gruppe gewählt wurde.
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}

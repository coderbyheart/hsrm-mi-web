package de.medieninf.webanw.belegung.gruppe05.aufg05;

import java.util.LinkedList;
import java.util.List;

import de.medieninf.webanw.belegung.BelegungBean;
import de.medieninf.webanw.belegung.Modul;
import de.medieninf.webanw.belegung.Studierende;
import de.medieninf.webanw.belegung.Veranstaltung;
import de.medieninf.webanw.belegung.VeranstaltungGruppe;
import de.medieninf.webanw.belegung.Wochentag;

/**
 * Klasse VerWahlBean kümmert sich um die Daten, die zur Anzeige und Wahl von Veranstaltungen wichtig sind.
 * @author Simon Franzen
 *
 */
public class VerWahlBean {
	
	//BelegungBean für Zugriffe auf die DB
	private BelegungBean bb;
	
	//Eingeloggter Student
	private Studierende loggedInUser;

	//Aktuelle Wahl des Studenten
	private List <VeranstaltungListBean> belegungen;
	
	//Alle Veranstaltungen die der Student waehlen kann
	private List <VeranstaltungListBean> veranstaltungen;
	
	/**
	 * Konstruktor von VerWahlBean.
	 */
	public VerWahlBean(){
		
	}
	
	/**
	 * Setter für den eingeloggten Nutzer.
	 * @param loggedInUser
	 */
	public void setLoggedInUser(Studierende loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	/**
	 * Getter für den eingeloggten Nutzer.
	 * @return loggedInUser 
	 */
	public Studierende getLoggedInUser() {
		return loggedInUser;
	}
	
	/**
	 * Getter für die BelegungsBean.
	 * @return bb
	 */
	public BelegungBean getBb() {
		//Falls nicht gesetzt
		if(bb == null){
			bb = new BelegungBean();
		}
		return bb;
	}
	
	/**
	 * Setter für die Belegungsbean.
	 * @param bb
	 */
	public void setBb(BelegungBean bb) {
		this.bb = bb;
	}
	
	/**
	 * Setter für Veranstaltungen.
	 */
	public void setVeranstaltungen(List <VeranstaltungListBean> ver) {
		this.veranstaltungen = ver;
	}

		
	/**
	 * Getter für Liste aus VeranstaltungListBean.
	 * 
	 * @return Liste mit VeranstaltungListBean
	 */
	public List <VeranstaltungListBean> getVeranstaltungen() {
		//Falls nicht gesetzt, Daten aus der Datenbank holen
		if(veranstaltungen==null){
			//Alle Veranstaltungen durchgehen
			veranstaltungen = new LinkedList<VeranstaltungListBean>();
			// alle möglichen Module des Studiengangs
			for(Modul m : getBb().getModule(getLoggedInUser().getStudiengang())){
				//Für Alle Veranstaltungen dieser Module
				VeranstaltungListBean b;
				for(Veranstaltung v : getBb().getVeranstaltungen(m)){			
					b = new VeranstaltungListBean();
					b.setModulid(v.getModul().getModulId());
					b.setModulbez((v.getModul().getBezeichnung()));
					b.setVeranstaltungsid(v.getVeranstaltungId());
					b.setVeranstaltungstyp(v.getVeranstaltungsTyp().getBezeichnung());
					b.setMaximalTeilnehmer(v.getMaxTeilnehmer());
					//Gruppenbeans setzen
					List <GruppeBean> gruppen = new LinkedList <GruppeBean>();
					GruppeBean gb;
					for(VeranstaltungGruppe vg : getBb().getVeranstaltungGruppe(v)){		
						gb = new GruppeBean();
						gb.setTeilnehmer(vg.getStudierende().size());
						gb.setUhrzeit(vg.getUhrzeit().toString());
						gb.setWochentag(Wochentag.genWochentag(vg.getWochentag()).toString());
						gb.setGruppe(vg.getVeranstaltungGruppePk().getGid());
						//Schauen ob veranstaltung schon gewaehlt
						for(VeranstaltungGruppe belegung : getLoggedInUser().getBelegungen()){
							if(belegung.getVeranstaltungGruppePk().getId_veranstaltung() ==
									vg.getVeranstaltungGruppePk().getId_veranstaltung() &&
								belegung.getVeranstaltungGruppePk().getGid() ==
									vg.getVeranstaltungGruppePk().getGid()){
								gb.setChecked(true);
							}
						}
						gruppen.add(gb);
					}
					b.setGruppen(gruppen);	
					veranstaltungen.add(b);
				
				}
			}
		}
		return veranstaltungen;
	}

	/**
	 * Entfernt eine Belegung aus der Datenbank.
	 * @param vg VeranstaltungGruppe
	 */
	public boolean removeBelegung(long veranstaltungsid, char gruppe) throws Exception{
		boolean ok;
		ok = getBb().removeBelegung(getLoggedInUser(),
				getBb().getVeranstaltungGruppe(
						getBb().getVeranstaltung(veranstaltungsid), gruppe));
		if(ok){
			//Gruppe als nicht mehr gewählt setzen
			for(GruppeBean g : getVeranstaltung(veranstaltungsid).getGruppen()){
				if(g.getGruppe()==gruppe){
					g.setChecked(false);
					g.setTeilnehmer(g.getTeilnehmer()-1);
				}
			}
		}
		return ok;
	}
	
	/**
	 * Belegt eine Veranstaltung in der Datenbank.
	 * @param vg VeranstaltungGruppe
	 */
	public boolean addBelegung(long veranstaltungsid, char gruppe) throws Exception{
		boolean ok;
		ok = getBb().addBelegung(getLoggedInUser(),
				getBb().getVeranstaltungGruppe(
						getBb().getVeranstaltung(veranstaltungsid), gruppe));
		if(ok){
			//Gruppe als gewählt setzen in Liste aller Veranstaltungen
			for(GruppeBean g : getVeranstaltung(veranstaltungsid).getGruppen()){
				if(g.getGruppe()==gruppe){
					g.setChecked(true);
					g.setTeilnehmer(g.getTeilnehmer()+1);
				}
			}
		}
		return ok;
	}
	/**
	 * Getter für eine VeranstaltungListBean aus der Liste der VeranstaltungListBean.
	 * @param veranstaltungs Id
	 * @return Veranstaltung
	 */
	public VeranstaltungListBean getVeranstaltung(long verId) {
		for(VeranstaltungListBean v : getVeranstaltungen()){
			if(verId == v.getVeranstaltungsid()){
				return v;
			}
		}
		return null;
	}
	
	/**
	 * Macht einen Datenbank update des aktuell eingeloggten Nutzers.
	 */
	public void updateLoggedInUser() {
		setLoggedInUser(getBb().getStudierende(getLoggedInUser().getMatnr()));
		setBelegungen(null);
	}
	
	/**
	 * Setter für die Belegungen.
	 * @param belegungen
	 */
	public void setBelegungen(List <VeranstaltungListBean> belegungen) {
		this.belegungen = belegungen;
	}

	/**
	 * Getter für die Belegungen.
	 * @return belegungen
	 */
	public List <VeranstaltungListBean> getBelegungen() {
		//Falls nicht gesetzt, Daten aus der Datenbank holen
		if(belegungen==null){
			//Alle VeranstaltungenGruppe des Studenten durchgehen
			belegungen = new LinkedList<VeranstaltungListBean>();
			//Für Alle Veranstaltungen dieser Module
			VeranstaltungListBean b;
			for(VeranstaltungGruppe vg : getLoggedInUser().getBelegungen()){			
				b = new VeranstaltungListBean();
				b.setModulid(vg.getVeranstaltung().getModul().getModulId());
				b.setModulbez((vg.getVeranstaltung().getModul().getBezeichnung()));
				b.setVeranstaltungsid(vg.getVeranstaltung().getVeranstaltungId());
				b.setVeranstaltungstyp(vg.getVeranstaltung().getVeranstaltungsTyp().getBezeichnung());
				b.setMaximalTeilnehmer(vg.getVeranstaltung().getMaxTeilnehmer());
				//Gruppenbean setzen
				List <GruppeBean> gruppen = new LinkedList <GruppeBean>();
				GruppeBean gb = new GruppeBean();
				gb.setTeilnehmer(vg.getStudierende().size());
				gb.setUhrzeit(vg.getUhrzeit().toString());
				gb.setWochentag(Wochentag.genWochentag(vg.getWochentag()).toString());
				gb.setGruppe(vg.getVeranstaltungGruppePk().getGid());
				gruppen.add(gb);
				b.setGruppen(gruppen);	
				belegungen.add(b);
			
			}
		}
		return belegungen;
	}
}

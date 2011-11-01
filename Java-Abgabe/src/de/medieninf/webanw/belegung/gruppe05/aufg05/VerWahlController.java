package de.medieninf.webanw.belegung.gruppe05.aufg05;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.medieninf.webanw.belegung.Studierende;

/**
 * VerWahlController ist der zentrale Servlet Controller für die Wahl 
 * von Veranstaltungen für die Studenten.
 * 
 * @author Simon Franzen
 *
 */
public class VerWahlController extends HttpServlet {
	
	private static String root = "/aufg5";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
							throws ServletException, IOException { 
		
		//Session holen
		HttpSession session = request.getSession();
		//Bean zur Verwaltung der Wahl von Veranstaltungen
		VerWahlBean vwb = null;
		//Liste sammelt Fehlermeldung zur späteren Ausgabe
		LinkedList <String> errMessages = new LinkedList <String>();
		
		//VerWahlBean setzen, falls noch nicht gesetzt
		if(session.getAttribute("vwb")!=null){
			vwb = (VerWahlBean) session.getAttribute("vwb");
		}else{
			vwb = new VerWahlBean();
		}
		
		//Login Überprüfung
		if(request.getParameter("username")!=null &&
				request.getParameter("password")!=null&&
					vwb.getLoggedInUser()==null){	
			try{
				long usrname = Long.parseLong(request.getParameter("username"));
				long pw = Long.parseLong(request.getParameter("password"));
				//Studierenden holen
				Studierende s = vwb.getBb().getStudierende(usrname);
				if(s!=null){
					//Erfolgreicher Login
					if(s.getMatnr() == pw){						
						vwb.setLoggedInUser(s);
					}else{
						errMessages.add("Passwort falsch.");
					}
				}else{
					errMessages.add("Benutzer existiert nicht.");
				}
			}catch(NumberFormatException e){
				errMessages.add("Benutzername oder Passwort haben falsches Format.");
			}
		}
		
		//Logout Überprüfung
		if(request.getParameter("logout")!=null){	
			vwb.setLoggedInUser(null);
		}
	
		//Wahl der Veranstaltungen überprüfen
		if(request.getParameter("waehlen")!=null && vwb.getLoggedInUser() != null){
			if(request.getParameterValues("gruppen")!=null){
				//Wahl reinholen
				String [] wahl = request.getParameterValues("gruppen");
				String [] currw;
				for(int i = 0;i< wahl.length;i++){
					currw = wahl[i].split(":");
					//Wahl ID und Gruppe setzen
					long wahlid = Long.parseLong(currw[0]);
					String wahlgruppe = currw[1];
					//Aktuelle Liste mit Belegungen des Studenten
					List <VeranstaltungListBean> belegung = vwb.getBelegungen();
					//Veranstaltung reinholen die gewählt oder abgewählt werden soll
					VeranstaltungListBean v = vwb.getVeranstaltung(wahlid);				
					//Alle Gruppen der Veranstaltung durchgehen
					for(GruppeBean gruppe : v.getGruppen()){					
						//Abwahl
						if(v.getVeranstaltungsid()==wahlid && wahlgruppe.equals("none")){
							for(VeranstaltungListBean bel : belegung){
								if(bel.getVeranstaltungsid()==wahlid){
									//Falls Gruppe aus Veranstaltung schon gewaehlt erst alte entfernen
									try{
										vwb.removeBelegung(bel.getVeranstaltungsid(),gruppe.getGruppe());
									} catch(Exception e){
										errMessages.add("Fehler beim Verarbeiten der Daten. Ihre Daten waren nicht mehr aktuell. Full Exception:");
										errMessages.add(e.getLocalizedMessage());
										vwb.setVeranstaltungen(null);
									}
								}
							}
						}
						//Für Wahl die richtige Veranstaltung mit id und Buchstabe heraussuchen
						if(v.getVeranstaltungsid()== wahlid && gruppe.getGruppe() == wahlgruppe.charAt(0)){
							boolean bereitsbelegt = false;
							boolean isId = false;
							boolean isGruppe = false;
							//Alle Belegungen durchgehen
							for(VeranstaltungListBean bel : belegung){
								isId = bel.getVeranstaltungsid() == wahlid;
								for(GruppeBean g : bel.getGruppen()){
									isGruppe = g.getGruppe() == wahlgruppe.charAt(0);
									//Falls schonmal in Liste
									if(isId && isGruppe){
										bereitsbelegt = true;
									}else if(isId && !isGruppe){
										//Falls Gruppe aus Veranstaltung schon gewaehlt erst alte Gruppen entfernen
										try {
											vwb.removeBelegung(bel.getVeranstaltungsid(),g.getGruppe());
										} catch (Exception e) {
											errMessages.add("Fehler beim Verarbeiten der Daten. Ihre Daten waren nicht mehr aktuell. Full Exception:");
											errMessages.add(e.getLocalizedMessage());
											vwb.setVeranstaltungen(null);
										}
									}
								}
							}
							//Falls nicht belegt
							if(!bereitsbelegt){
								//Maximale Teilnehmerzahl ueberpruefen
								int size = gruppe.getTeilnehmer();
								int maxsize = v.getMaximalTeilnehmer();
								if(size>=maxsize){
									errMessages.add("Fehler Wahl Nr: "+wahlid+"! Es sind bereits " + size + " Studierende angemeldet."
											+" Maximalteilnehmerzahl von " + maxsize + " bereits erreicht.");
								}else{
									try {
										vwb.addBelegung(wahlid,wahlgruppe.charAt(0));
									} catch (Exception e) {
										errMessages.add("Fehler beim Verarbeiten der Daten. Ihre Daten waren nicht mehr aktuell. Full Exception:");
										errMessages.add(e.getLocalizedMessage());
										vwb.setVeranstaltungen(null);
									}
								}
							}
						}
					}	
				}				
			}
			//Eingeloggten Nutzer updaten
			vwb.updateLoggedInUser();
		}
	
		session.setAttribute("vwb", vwb);
		request.setAttribute("errmessages", errMessages);
		
		//Falls kein erfolgreicher Login oder ausgeloogt wieder zur Login Seite schicken
		if(vwb.getLoggedInUser()==null){
			RequestDispatcher rd = request.getRequestDispatcher(root+"/login.jsp"); 
			rd.forward(request, response);
			return;
		}
		
		//Falls User eingeloggt
		if(vwb.getLoggedInUser()!=null){
			RequestDispatcher rd = request.getRequestDispatcher(root+"/wahl.jsp"); 
			rd.forward(request, response);
			return;
			}
		
	}
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
				doGet(request, response);
	}


}

package de.medieninf.webanw.belegung;

import java.util.*;
import org.junit.*;

public class TestBelegung {

	private static BelegungBean bb; // eine Bean-Instanz für alle Tests
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bb = new BelegungBean();
	}
	
	@Test
	public void testWarmup() {
		// Persistenzprovider aufwärmen um später auch etwas 
		// sinnvollere Ausführungszeiten zu erhalten
		List<Studiengang> ls = bb.getStudiengaenge();
		Assert.assertEquals(36, ls.size());		
	}
	
	@Test
	public void testStudiengang() {
		int anzStdg = 36;
		int stdgId1 = 1;
		String stdgBez1 = "Bachelor Medieninformatik";
		int stdgId2 = 42;
		String stdgBez2 = "Bachelor Zauberinformatik";
		String stdgBez2Neu = "Bachelor WeichwarenIngenieurinformatik";
		
		List<Studiengang> ls = bb.getStudiengaenge();
		Assert.assertEquals(anzStdg, ls.size());
		for (Studiengang sg : ls) {
			Studiengang sg2 = bb.getStudiengang(sg.getStudiengangId());
			Assert.assertEquals(sg, sg2);
		}
		Studiengang sg = bb.getStudiengang(1);
		Assert.assertEquals(stdgId1, sg.getStudiengangId());
		Assert.assertEquals(stdgBez1, sg.getBezeichnung());
		sg = bb.getStudiengang(stdgId2);
		Assert.assertEquals(stdgId2, sg.getStudiengangId());
		Assert.assertEquals(stdgBez2, sg.getBezeichnung());
		sg.setBezeichnung(stdgBez2Neu);
		sg = bb.update(sg);
		Studiengang sg2 = bb.getStudiengang(sg.getStudiengangId());
		Assert.assertEquals(stdgBez2Neu, sg2.getBezeichnung());
		sg2 = bb.update(sg2);
		sg2.setBezeichnung(stdgBez2);
		sg2 = bb.update(sg2);
		sg = bb.getStudiengang(sg.getStudiengangId());		
		Assert.assertEquals(stdgBez2, sg.getBezeichnung());
		Assert.assertEquals(sg, sg2);
	}
	
	@Test
	public void testStudierende() {
		long matnr1 = 0;
		long matnr2 = 123457;
		String vorname2 = "Hartwell";
		String vornameNeu2 = "Suserich";
		
		Studierende s = bb.getStudierende(matnr1);
		Assert.assertEquals(null, s);
		s = bb.getStudierende(matnr2);
		Assert.assertEquals(matnr2, s.getMatnr());
		Assert.assertEquals(vorname2, s.getVorname());
		s.setVorname(vornameNeu2);
		s = bb.update(s);
		Assert.assertEquals(vornameNeu2, s.getVorname());
		Studierende s2 = bb.getStudierende(matnr2);
		Assert.assertEquals(vornameNeu2, s2.getVorname());
		s2.setVorname(vorname2);
		s2 = bb.update(s2);
		s = bb.getStudierende(matnr2);
		Assert.assertEquals(vorname2, s.getVorname());
		Assert.assertEquals(vorname2, s2.getVorname());
		Assert.assertEquals(s, s2);
	}
	
	@Test
	public void testSearchStudierende() {
		String vorname = "Rudi";
		List<Studierende> ls = bb.searchStudierende(vorname, null, null, null, null, null, null, null, null, null, 100, 0);
		Assert.assertEquals(8, ls.size());
		String nachname = "Barth";
		ls = bb.searchStudierende(null, nachname, null, null, null, null, null, null, null, null, 100, 0);
		Assert.assertEquals(9, ls.size());
		nachname = "Zelosko";
		ls = bb.searchStudierende(null, nachname, null, null, null, null, null, null, null, null, 100, 0);
		Assert.assertEquals(0, ls.size());
		int fachsem = 1;
		ls = bb.searchStudierende(null, null, fachsem, null, null, null, null, null, null, null, 4000, 0);
		Assert.assertEquals(3319, ls.size());
		fachsem = 8;
		ls = bb.searchStudierende(null, null, fachsem, null, null, null, null, null, null, null, 4000, 0);
		Assert.assertEquals(0, ls.size());
		Studiengang sg = bb.getStudiengang(1);
		ls = bb.searchStudierende(null, null, null, sg, null, null, null, null, null, null, 1000, 0);
		Assert.assertEquals(574, ls.size());
		String adresse = "Feldstraße";
		ls = bb.searchStudierende(null, null, null, null, adresse, null, null, null, null, null, 1000, 0);
		Assert.assertEquals(280, ls.size());
		int plz = 66636;
		ls = bb.searchStudierende(null, null, null, null, null, plz, null, null, null, null, 100, 0);
		Assert.assertEquals(2, ls.size());
		String stadt = "Wiesbaden";
		ls = bb.searchStudierende(null, null, null, null, null, null, stadt, null, null, null, 100, 0);
		Assert.assertEquals(18, ls.size());
		// land ist alle Deutschland
		String email = "Weitz";
		ls = bb.searchStudierende(null, null, null, null, null, null, null, null, email, null, 100, 0);
		Assert.assertEquals(6, ls.size());
		String tel = "0815";
		ls = bb.searchStudierende(null, null, null, null, null, null, null, null, null, tel, 100, 0);
		Assert.assertEquals(9, ls.size());
		// combsearch
		sg = bb.getStudiengang(42);
		ls = bb.searchStudierende("Pe", null, 3, sg, null, null, null, null, null, null, 100, 0);
		Assert.assertEquals(2, ls.size());
	}
	
	@Test
	public void testModul() {
		int mnr = 1055;
		int sgId = 29;
		String modulBezeichnung = "Notizen zu Mathematik für Wiederholer";
		String modulBezeichnungNeu = "Humpty Dumpty for Dummies";
		
		Modul modul = bb.getModul(mnr);
		Assert.assertNotNull(modul);
		Assert.assertEquals(modulBezeichnung, modul.getBezeichnung());
		Studiengang studiengang = bb.getStudiengang(sgId);
		Assert.assertEquals(studiengang, modul.getStudiengang());
		Set<Modul> sm = studiengang.getModul();
		long [] ai = {  1055, 1080, 1085, 1122, 1148, 1166, 1186, 1205, 1211, 1282, 1322, 
			1379, 1381, 1448, 1466, 1475, 1493, 1522, 1584, 1615, 1619, 1812, 1823, 1867, 
			1887, 1897, 1902, 1920, 1931, 1946};
		Assert.assertEquals(ai.length, sm.size());
		Set<Long> si1 = new TreeSet<Long>();
		for (long l : ai)
			si1.add(l);
		Set<Long> si2 = new TreeSet<Long>();
		for (Modul m : sm)
			si2.add(m.getModulId());
		Assert.assertEquals(si1, si2);
		List<Modul> lm = bb.getModule(studiengang);
		Assert.assertEquals(ai.length, lm.size());	
		
		modul.setBezeichnung(modulBezeichnungNeu);
		modul = bb.update(modul);
		Assert.assertEquals(modulBezeichnungNeu, modul.getBezeichnung());
		Modul modul2 = bb.getModul(modul.getModulId());
		Assert.assertEquals(modul, modul2);
		modul2.setBezeichnung(modulBezeichnung);
		modul2 = bb.update(modul2);
		modul = bb.getModul(modul2.getModulId());		
		Assert.assertEquals(modulBezeichnung, modul.getBezeichnung());
		Assert.assertEquals(modul, modul2);
		
		//TODO neue Veranstaltung in Modul
	}
	
	@Test
	public void testVeranstaltungstyp() {
		List<VeranstaltungsTyp> lvt = bb.getVeranstaltungsTypen();
		Assert.assertEquals(4, lvt.size());
		VeranstaltungsTyp vt = bb.getVeranstaltungsTyp(1);
		Assert.assertEquals(1, vt.getVeranstaltungsTypId());
		Assert.assertEquals("Vorlesung", vt.getBezeichnung());
		vt = bb.getVeranstaltungsTyp(2);
		Assert.assertEquals(2, vt.getVeranstaltungsTypId());
		Assert.assertEquals("Übung", vt.getBezeichnung());
		String vt_bez_new = "Da wo keiner sich vorbereitet, nicht zuhört und nix macht";
		String vt_bez_old = vt.getBezeichnung();
		vt.setBezeichnung(vt_bez_new);
		vt = bb.update(vt);
		VeranstaltungsTyp vt2 = bb.getVeranstaltungsTyp(vt.getVeranstaltungsTypId());
		Assert.assertEquals(vt_bez_new, vt2.getBezeichnung());
		vt = bb.update(vt);
		Assert.assertEquals(vt_bez_new, vt.getBezeichnung());
		vt.setBezeichnung(vt_bez_old);
		vt = bb.update(vt);
		Assert.assertEquals(vt_bez_old, vt.getBezeichnung());
		vt2 =  bb.getVeranstaltungsTyp(vt.getVeranstaltungsTypId());
		Assert.assertEquals(vt_bez_old, vt.getBezeichnung());
	}

	@Test 
	public void testVeranstaltung() {
		int mnr = 1055;
		int lvnr1 = 10105;
		int lvnr2 = 10106;
		Modul modul = bb.getModul(mnr);
		
		List<Veranstaltung> lv = bb.getVeranstaltungen(modul);
		Assert.assertEquals(2, lv.size());
		for (Veranstaltung v : lv) {
			if (v.getVeranstaltungId() == lvnr1) {
				Assert.assertEquals(bb.getVeranstaltungsTyp(1), v.getVeranstaltungsTyp());
				Assert.assertEquals(60, v.getMaxTeilnehmer());
				Veranstaltung vv = bb.getVeranstaltung(lvnr1);
				Assert.assertEquals(vv, v);
			} else {
				Assert.assertEquals(lvnr2, v.getVeranstaltungId());
				Assert.assertEquals(bb.getVeranstaltungsTyp(3), v.getVeranstaltungsTyp());
				Assert.assertEquals(17, v.getMaxTeilnehmer());
				Veranstaltung vv = bb.getVeranstaltung(lvnr2);
				Assert.assertEquals(vv, v);
			}
			Assert.assertEquals(2, v.getDauer());
		}
		List<Modul> lm = bb.getModule(bb.getStudiengang(1));
		lv = new ArrayList<Veranstaltung>();
		for (Modul mlm: lm) {
			lv.addAll(bb.getVeranstaltungen(mlm));
		}
		Assert.assertEquals(57, lv.size());		
	}
	
	@Test
	public void testVeranstaltungGruppe() {
		// System.out.println("TEST -- Getting one 10101");
		Veranstaltung v1 = bb.getVeranstaltung(10101);
		VeranstaltungGruppe vg1 = bb.getVeranstaltungGruppe(v1, 'A');
		Assert.assertEquals('A', vg1.getVeranstaltungGruppePk().getGid());
		List<VeranstaltungGruppe> lvg1 = bb.getVeranstaltungGruppe(v1);
		Assert.assertEquals(1, lvg1.size());
		// System.out.println("TEST -- Getting one 10107");
		v1 = bb.getVeranstaltung(10107);
		vg1 = bb.getVeranstaltungGruppe(v1, 'A');
		Assert.assertEquals(v1, vg1.getVeranstaltung());
		// System.out.println("TEST -- Getting all 10107");
		Veranstaltung v2 = bb.getVeranstaltung(10107);
		List<VeranstaltungGruppe> lvg2 = bb.getVeranstaltungGruppe(v2);
		Assert.assertEquals(1, lvg2.size());
		for (VeranstaltungGruppe vg : lvg2) {
			// System.out.println("Testing " + vg.getVeranstaltungGruppePk());
			Assert.assertEquals(v2, vg.getVeranstaltung());
			switch (vg.getVeranstaltungGruppePk().getGid()) {
			case 'A':
				Assert.assertEquals(java.sql.Time.valueOf("14:15:00"), vg.getUhrzeit());
				Assert.assertEquals(4, vg.getWochentag());
				break;
			case 'B':
				Assert.assertEquals(java.sql.Time.valueOf("14:15:00"), vg.getUhrzeit());
				Assert.assertEquals(4, vg.getWochentag());
				break;
			case 'C':
				Assert.assertEquals(java.sql.Time.valueOf("08:15:00"), vg.getUhrzeit());
				Assert.assertEquals(2, vg.getWochentag());
				break;
			case 'D':
				Assert.assertEquals(java.sql.Time.valueOf("08:15:00"), vg.getUhrzeit());
				Assert.assertEquals(0, vg.getWochentag());
				break;
			default:
				Assert.fail("Gruppe nicht erwartet" + vg.getVeranstaltungGruppePk().getGid());
			}

		}
		Veranstaltung cv = bb.getVeranstaltung(11111);
		VeranstaltungGruppe changeVg = bb.getVeranstaltungGruppe(cv, 'A');
		java.sql.Time old_t = changeVg.getUhrzeit();
		java.sql.Time new_t = java.sql.Time.valueOf("22:22:22");
		changeVg.setUhrzeit(new_t);
		changeVg = bb.update(changeVg);
		Assert.assertEquals(new_t, changeVg.getUhrzeit());
		changeVg = bb.getVeranstaltungGruppe(cv, 'A');
		Assert.assertEquals(new_t, changeVg.getUhrzeit());
		changeVg.setUhrzeit(old_t);
		changeVg = bb.update(changeVg);
		Assert.assertEquals(old_t, changeVg.getUhrzeit());
		changeVg = bb.getVeranstaltungGruppe(cv, 'A');
		Assert.assertEquals(old_t, changeVg.getUhrzeit());
	}
	
	@Test
	public void testBelegung() {
		int matnr = 123457;
		int vnr = 10101;
		char gruppe = 'A';
		int anzahlStud = 92;
		int anzahlBel = 8;
		
		Studierende studi = bb.getStudierende(matnr);
		Assert.assertEquals(matnr, studi.getMatnr());
		Assert.assertEquals(anzahlBel, studi.getBelegungen().size());
		Veranstaltung veranstaltung = bb.getVeranstaltung(vnr);
		VeranstaltungGruppe veranstaltungGruppe = bb.getVeranstaltungGruppe(veranstaltung, gruppe);
		List<Studierende> ls = veranstaltungGruppe.getStudierende();
		Assert.assertEquals(anzahlStud, ls.size());
		Assert.assertEquals(anzahlBel, studi.getBelegungen().size());
		boolean res = bb.addBelegung(studi, veranstaltungGruppe);
		Assert.assertEquals(true, res); // sollte hinzugefügt worden sein
		veranstaltungGruppe = bb.getVeranstaltungGruppe(veranstaltung, gruppe);
		studi = bb.getStudierende(matnr);
		ls = veranstaltungGruppe.getStudierende();
		Assert.assertEquals(anzahlStud+1, ls.size());
		Assert.assertEquals(anzahlBel+1, studi.getBelegungen().size());
		res = bb.removeBelegung(studi, veranstaltungGruppe);
		Assert.assertEquals(true, res); // sollte gelöscht worden sein
		veranstaltung = bb.getVeranstaltung(veranstaltung.getVeranstaltungId());
		veranstaltungGruppe = bb.getVeranstaltungGruppe(veranstaltung, gruppe);
		studi = bb.getStudierende(studi.getMatnr());
		ls = veranstaltungGruppe.getStudierende();
		Assert.assertEquals(anzahlStud, ls.size());
		Assert.assertEquals(anzahlBel, studi.getBelegungen().size());
	}

	private Studierende genStudi(int matnr) {
		Studierende studi = new Studierende(matnr);
		studi.setAdresse1("nix");
		studi.setAdresse2("nixer");
		studi.setEmail("nix@no.de");
		studi.setFachsem(1);
		studi.setLand("Schweiz");
		studi.setNachname("Nixli");
		studi.setPlz(15832);
		studi.setStadt("KeinHausen");
		studi.setStudiengang(bb.getStudiengang(1));
		studi.setTel("gebbds ned");
		studi.setVorname("Nil");
		return studi;
	}
	
	@Test
	public void testNeuStudi() {
		int matnr = 1234;
		int vnr = 10101;
		char gruppe = 'A';
		
		Studierende studi = genStudi(matnr);
		studi = bb.update(studi);
		Studierende getstudi = bb.getStudierende(matnr);
		Assert.assertEquals(studi, getstudi);

		VeranstaltungGruppe veranstaltungGruppe = bb.getVeranstaltungGruppe(bb.getVeranstaltung(vnr), gruppe);
		int anzStudis = veranstaltungGruppe.getStudierende().size();
		Assert.assertEquals(0, studi.getBelegungen().size());
		boolean res = bb.addBelegung(studi, veranstaltungGruppe);
		Assert.assertEquals(true, res); // sollte hinzugefügt worden sein
		studi = bb.getStudierende(matnr);
		Assert.assertEquals(1, studi.getBelegungen().size());		
		veranstaltungGruppe = bb.getVeranstaltungGruppe(bb.getVeranstaltung(vnr), gruppe);
		Assert.assertEquals(anzStudis+1, veranstaltungGruppe.getStudierende().size());
		bb.remove(studi);		
		getstudi = bb.getStudierende(matnr);
		Assert.assertTrue(null == getstudi);
		veranstaltungGruppe = bb.getVeranstaltungGruppe(bb.getVeranstaltung(vnr), gruppe);
		Assert.assertEquals(anzStudis, veranstaltungGruppe.getStudierende().size());
	}
	
	@Test
	public void testChangeStudi() {
		int matnr = 123457;
		Studierende s = bb.getStudierende(matnr);
		Assert.assertEquals(matnr, s.getMatnr());	
		String origStadt = s.getStadt();
		String neuStadt = origStadt + " anders";
		s.setStadt(neuStadt);
		bb.update(s);
		s = bb.getStudierende(matnr);
		Assert.assertEquals(neuStadt, s.getStadt());
		s.setStadt(origStadt);
		bb.update(s);
		s = bb.getStudierende(matnr);
		Assert.assertEquals(origStadt, s.getStadt());
	}
	
	@Test
	public void testNeuVeranstaltungGruppe() {
		int vnr = 10101;
		char gruppe = 'B';
		int matnr1 = 123457;
		int matnr2 = 123459;
		Veranstaltung veranstaltung = bb.getVeranstaltung(vnr);
		
		VeranstaltungGruppe veranstaltungGruppe;
		veranstaltungGruppe = bb.createVeranstaltungGruppe(veranstaltung, gruppe);  
		veranstaltungGruppe.setWochentag(5);
		veranstaltungGruppe.setUhrzeit(java.sql.Time.valueOf("06:30:00"));
		veranstaltungGruppe = bb.update(veranstaltungGruppe);
		
		VeranstaltungGruppe vg = bb.getVeranstaltungGruppe(veranstaltung, gruppe);
		Assert.assertEquals(veranstaltungGruppe, vg);
		Studierende studi1 = bb.getStudierende(matnr1);
		Studierende studi2 = bb.getStudierende(matnr2);
		
		Assert.assertEquals(0, veranstaltungGruppe.getStudierende().size());
		int anzBel1 = studi1.getBelegungen().size();
		int anzBel2 = studi2.getBelegungen().size();
		veranstaltungGruppe = bb.getVeranstaltungGruppe(veranstaltung, gruppe); //refresh
		boolean res = bb.addBelegung(studi1, veranstaltungGruppe);
		Assert.assertEquals(true, res); // sollte hinzugefügt worden sein
		veranstaltungGruppe = bb.getVeranstaltungGruppe(veranstaltung, gruppe); //refresh
		res = bb.addBelegung(studi2, veranstaltungGruppe);
		Assert.assertEquals(true, res); // sollte hinzugefügt worden sein

		veranstaltungGruppe = bb.getVeranstaltungGruppe(veranstaltung, gruppe);
		Assert.assertEquals(2, veranstaltungGruppe.getStudierende().size());
		Assert.assertEquals(anzBel1+1, studi1.getBelegungen().size());		
		Assert.assertEquals(anzBel2+1, studi2.getBelegungen().size());		

		bb.remove(veranstaltungGruppe); // entfernen
		vg = bb.getVeranstaltungGruppe(bb.getVeranstaltung(vnr), gruppe);
		Assert.assertTrue(null == vg);
		
		studi1 = bb.getStudierende(matnr1);
		studi2 = bb.getStudierende(matnr2);
		Assert.assertEquals(anzBel1, studi1.getBelegungen().size());		
		Assert.assertEquals(anzBel2, studi2.getBelegungen().size());
	}
	
	@Test
	public void testNeuVeranstaltung() {
		int veranstaltungId = 4711;
		Veranstaltung veranstaltung = new Veranstaltung(veranstaltungId);
		veranstaltung.setVeranstaltungsTyp(bb.getVeranstaltungsTyp(1)); // Vorlesung
		veranstaltung.setModul(bb.getModul(1001));
		veranstaltung.setMaxTeilnehmer(17);
		veranstaltung.setDauer(2);
		veranstaltung = bb.update(veranstaltung);
		boolean doBeleg = true;
		char gruppe = 'A';
		if (doBeleg) { // mache Gruppe und Belegungen
			VeranstaltungGruppe veranstaltungGruppe = bb.createVeranstaltungGruppe(veranstaltung, gruppe);
			// Veranstaltung existiert, Gruppe ist neu
			veranstaltungGruppe.setWochentag(5);
			veranstaltungGruppe.setUhrzeit(java.sql.Time.valueOf("06:30:00"));
			veranstaltungGruppe = bb.update(veranstaltungGruppe);
			Studierende studi = bb.getStudierende(123457);
			boolean res = bb.addBelegung(studi, veranstaltungGruppe);
			Assert.assertEquals(true, res); // sollte hinzugefügt worden sein
		}
		Veranstaltung veranstaltung2 = bb.getVeranstaltung(veranstaltungId);
		Assert.assertNotNull(veranstaltung2);
		Assert.assertEquals(veranstaltungId, veranstaltung2.getVeranstaltungId());
		if (doBeleg) {
			List<VeranstaltungGruppe> lvg = bb.getVeranstaltungGruppe(veranstaltung2);
			for (VeranstaltungGruppe vg : lvg) {
				if (vg.getVeranstaltungGruppePk().getGid() == gruppe) {
					Assert.assertEquals(1, vg.getStudierende().size());
				}
			}
		}
		bb.remove(veranstaltung2);
		Veranstaltung veranstaltung3 = bb.getVeranstaltung(veranstaltungId);		
		Assert.assertTrue(null == veranstaltung3);
	}
	
	@Test
	public void testSearchVeranstaltungGruppe() {
		int matnr = 123461;
		Studierende s = bb.getStudierende(matnr);
		String modul = "Python";
		List<VeranstaltungGruppe> lvg = bb.searchVeranstaltungGruppe(s.getStudiengang(), modul, 200, 0);
		Assert.assertEquals(3, lvg.size());
		Studiengang sg = bb.getStudiengang(2);
		lvg = bb.searchVeranstaltungGruppe(sg, modul, 2000, 0);
		Assert.assertEquals(1, lvg.size());
		lvg = bb.searchVeranstaltungGruppe(null, modul, 2000, 0);
		Assert.assertEquals(70, lvg.size());
	}
	
}

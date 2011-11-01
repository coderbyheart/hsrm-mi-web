<?php

/**
 * Ueberprueft eingegebene Nutzerdaten.
 * @param $usr, $pw
 * @return $s 	Nutzer, falls erfolgreich, sonst FALSE
 */
function checkLogin($usr,$pw){
	global $pdo;
    $result = $pdo->prepare('SELECT * FROM studierende WHERE matnr=? AND tel=?');
    $result->bindValue(1, $usr, PDO::PARAM_INT);
    $result->bindValue(2, $pw, PDO::PARAM_STR);
    $result->execute();
	$s = $result->fetch();
	if($s!=FALSE){
		if($s->matnr == $usr && $s->tel == $pw) return $s;
	}
	return FALSE;
}

/**
 * Holt alle Veranstaltungen, die der Student bereits gewaehlt hat
 * @param $student
 * @return $stud_ver 	Gewaehlte Veranstaltungen falls erfolgreich, sonst FALSE
 */
function getStudentVeranstaltungen($student){
	global $pdo;
	$stud_ver = array();
    $result = $pdo->prepare('SELECT M.id AS modul_id, M.bezeichnung AS titel, V.id as ver_id, T.bezeichnung AS typ, SVG.v_gid AS gruppe FROM modul M, veranstaltung V, studierende_veranstaltung_gruppe SVG, veranstaltungstyp T WHERE SVG.matnr=? AND SVG.v_id=V.id AND V.modul_id=M.id AND V.typ_id=T.id');
    $result->bindValue(1, $student->matnr, PDO::PARAM_INT);
    $result->execute();
	while($v = $result->fetch()) {
    	$stud_ver [] = $v;
	}
	return $stud_ver;
}

/**
 * Ueberprueft ob der Student die Veranstaltung bereits belegt hat.
 * @param $student_veranstaltungen	bereits gewaehlte Veranstaltungen
 * @param $ver_nr
 * @param $gruppe
 * 
 * @return True / False
 */
function checkVeranstaltung($ver_nr,$gruppe){
	global $student_veranstaltungen;
	foreach($student_veranstaltungen as $ver) {
		if($ver->ver_id==$ver_nr&&$ver->gruppe==$gruppe) return TRUE;
	}	
	return FALSE;
}
/**
 * Ueberprueft ob der Student schon eine Gruppe aus dieser Veranstaltung gewählt hat
 * @param $ver_nr
 */
function checkVeranstaltungsGruppe($ver_nr){
	global $student_veranstaltungen;
	foreach($student_veranstaltungen as $ver) {
		if($ver->ver_id==$ver_nr) return TRUE;
	}	
	return FALSE;
}

/**
 * Überprüft ob mehr als eine Gruppe von einer Veranstaltung gewählt wurde
 * @param $ver_nr
 * @param $akt_wahl
 */
function checkAktuelleAuswahl($ver_nr,$akt_wahl){
	$cnt = 0;
	foreach($akt_wahl as $ver) {
		if($ver[0]==$ver_nr) $cnt++;
	}	
	return ($cnt>1)?TRUE:FALSE;
}

/**
 * Sendet Auswahl an die Datenbank

 * @param $matnr
 * @param $ver_wahl
 */
function sendChooseToDatabase($matnr,$ver_wahl){
	global $pdo;
	$errors = array();
	//In Liste hinzufügen
	if(array_key_exists('wahl', $ver_wahl)){
		foreach($ver_wahl['wahl'] as $ver){
			$tmp = TRUE;
			//check, falls schon gewählt
			if(checkVeranstaltung($ver[0],$ver[1])){
				$errors[] = ('<p class="err">Veranstaltung mit der ID '.$ver[0].' ist bereits gewählt.</p>');
				$tmp = FALSE;
			}
			if(checkVeranstaltungsGruppe($ver[0])){
				//check falls mehrere Gruppen aus einer Veranstaltung gewählt sind
				$errors[] = ('<p class="err">Es wurde bereits eine Gruppe aus der Veranstaltung '.$ver[0].' gewählt.</p>');
				$tmp = FALSE;
			}
			if(checkAktuelleAuswahl($ver[0],$ver_wahl['wahl'])){
				//check ob mehrere Gruppen in der aktuellen Auswahl gewählt sind
			    $errors[] = ('<p class="err">Man darf nicht mehrere Gruppen aus der Veranstaltung '.$ver[0].' wählen.</p>');	
				$tmp = FALSE;
			}
			if($tmp){
				// query
				$sql = "INSERT INTO studierende_veranstaltung_gruppe VALUES (:matnr,:ver_id,:gruppe)";
				$q = $pdo->prepare($sql);
				$q->execute(array(':matnr'=>$matnr,':ver_id'=>$ver[0],':gruppe'=>$ver[1]));
			}
		}
	}
	//Aus Liste löschen
	if(array_key_exists('abwahl', $ver_wahl)){
		foreach($ver_wahl['abwahl'] as $ver){
			if(checkVeranstaltung($ver[0],$ver[1])){
                $del = $pdo->prepare('DELETE FROM studierende_veranstaltung_gruppe WHERE matnr=? AND v_id=? AND v_gid=?');
                $del->bindValue(1, $matnr, PDO::PARAM_INT);
                $del->bindValue(2, $ver[0], PDO::PARAM_INT);
                $del->bindValue(3, $ver[1], PDO::PARAM_INT);
                $del->execute();
			}else{
				$errors[] = ('<p class="err">Veranstaltung mit der ID '.$ver[0].' ist bereits abgewählt.</p>');
			}
		}
	}
	
	return $errors;
}

/**
 * Gibt alle Studiengänge aus der DB zurück
 */
function getStudiengaenge(){
	global $pdo;
	$studiengaenge = array();
	$result = $pdo->query('SELECT * FROM studiengang');
	while($s = $result->fetch()) {
    	$studiengaenge [] = $s;
	}
	return $studiengaenge;
}
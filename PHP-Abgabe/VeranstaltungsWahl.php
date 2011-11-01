<?php

/**
 * @author Simon Franzen <simon.franzen@natelio.com>
 * @version $Id$
 */

require_once __DIR__ . DIRECTORY_SEPARATOR . 'lib' . DIRECTORY_SEPARATOR . 'bootstrap.php';
require_once LIB . 'veranstaltung_modul.php';

// Ausgabe vorbereiten
$Page = new Page_Default();
//Aktuelle Seite
$p = 1;
//Eingeloggter Student
$student = NULL;
//Seine Veranstaltungen
$student_veranstaltungen = NULL;
//Aktuelle Wahl
$aktuelleWahl = NULL;
//Studiengaenge
$studiengaenge = NULL;
//Aktueller STudiengang
$akt_studiengang = NULL;
// Anzahl der Einträge pro Seite
$rowspp = 20;

//Falls Login-Formular abgeschickt wurde
if(isset($_POST['user'])&&isset($_POST['password'])){	
	$s = checkLogin((int) $_POST['user'],$_POST['password']);
	if($s==FALSE){
		$Page->append('<p class="err">Falschen Benutzername und/oder Passwort eingegeben.</p>');
	}else{
		$_SESSION['student'] = $s;
	}    
}

//Falls Student gesetzt wurde
if(isset($_SESSION['student'])){
	$student = $_SESSION['student'];
	//sicher gehen das eigene veranstaltungen auch noch synchron sind
	$_SESSION['student_veranstaltungen'] = getStudentVeranstaltungen($student);
	$student_veranstaltungen = $_SESSION['student_veranstaltungen'];
	$akt_studiengang = $student->studiengang_id;
}


//Alle Studiengaenge für Auswahl
if(isset($_SESSION['studiengaenge'])){
 $studiengaenge = $_SESSION['studiengaenge'];
}else{
 $studiengaenge = getStudiengaenge();
 $_SESSION['studiengaenge'] = $studiengaenge;
}

//Falls Seite gesetzt wurde
if(isset($_POST['page'])){
	$p = $_POST['page'];
}

//Falls neuer Studiengang ausgewaehlt wurde
if(isset($_POST['studiengang'])){
 	$akt_studiengang = $_POST['studiengang'];
}

//Falls Studiengang gewechselt wurde, soll Seite 1 angezeigt werden
if(isset($_POST['studiengang_waehlen'])){
	$p=1;
}

//Aktuelle Wahl der Veranstaltungen
if(isset($_SESSION['aktuelleWahl'])){
	$aktuelleWahl = $_SESSION['aktuelleWahl'];
}else{
	$aktuelleWahl = array();
	$aktuelleWahl['wahl'] = array();
	$aktuelleWahl['abwahl'] = array();
	$_SESSION['aktuelleWahl'] = $aktuelleWahl;
}

//Falls Veranstaltungen gewählt wurden
if(isset($_POST['wahl'])){
	foreach($_POST['wahl'] as $ver){
		$v = explode(":", $ver);
		$aktuelleWahl['wahl'][$ver]=$v;
	}
	$_SESSION['aktuelleWahl'] = $aktuelleWahl;
}
//Falls Veranstaltungen abgewählt wurden
if(isset($_POST['abwahl'])){
	foreach($_POST['abwahl'] as $ver){
		$v = explode(":", $ver);
		$aktuelleWahl['abwahl'][$ver]=$v;
	}
		$_SESSION['aktuelleWahl'] = $aktuelleWahl;
}

//Falls die Wahl abgesendet werden soll
if(isset($_POST['wahlabsenden'])&&isset($_SESSION['student'])){
	//Daten an Datenbank senden
	$err = sendChooseToDatabase($student->matnr,$aktuelleWahl);
	foreach ($err as $e) $Page->append($e);
	
	//Aktualisierte Veranstaltungen auf Datenbank holen
	$_SESSION['student_veranstaltungen'] = getStudentVeranstaltungen($student);
	$student_veranstaltungen = $_SESSION['student_veranstaltungen'];

	//Aktuelle Wahl zurücksetzen
	$aktuelleWahl = array();
	$aktuelleWahl['wahl'] = array();
	$aktuelleWahl['abwahl'] = array();
	$_SESSION['aktuelleWahl'] = $aktuelleWahl;
}
	
//Falls Logout-Formular abgeschickt wurde
if(isset($_POST['logout'])){
	$_SESSION['student']=NULL;
	$_SESSION['student_veranstaltungen']=NULL;
	$_SESSION['studiengaenge']=NULL;
	$_SESSION['studiengang']=NULL;
	$_SESSION['aktuelleWahl']=NULL;
}

//Falls Student eingeloggt ist
if(isset($_SESSION['student'])){
	
	//Loginname anzeigen
	$Page->append('Eingeloggt als '.$student->vorname.' '.$student->nachname.'.');
	//Logout Form
	$Page->append('<form method="POST" name="logout_form" action="%s">', $_SERVER['PHP_SELF']);
	$Page->append('<input type="submit" name="logout" value="Logout">');
	$Page->append('</form><br/>');
	//Tabelle mit gewaehlten Veranstaltung
	$Page->append('<h2>Ihre gewählten Veranstaltungen:</h2>');
	if($student_veranstaltungen == FALSE){
		$Page->append('Keine Veranstaltungen gewählt.<br/>');
	}else{
		$Page->append('<table>
		    <thead>
		        <tr>
		        	<th>Modul-ID</th>
		            <th>Titel</th>
		            <th>Veranstaltung-ID</th>
		            <th>Veranstaltungs-Typ</th>
		            <th>Gruppe</th>
		        </tr>
		    </thead>
		 ');
		foreach($student_veranstaltungen as $ver) {
		    $Page->append('<tr>
		    				<td>%s</td>
		    				<td>%s</td>
		    				<td>%s</td>
		    				<td>%s</td>
		    				<td>%s</td>
		    			  </tr>', array($ver->modul_id, 
		    							$ver->titel, 
		    							$ver->ver_id, 
		    							$ver->typ, 
		    							$ver->gruppe));
			}
		$Page->append('</tbody></table>');
	}
	
	
	// Query das die Treffer der Veranstaltungen zu einem Studiengang zählt
	$countsql = sprintf('SELECT COUNT(*) as count FROM modul M, veranstaltung V, veranstaltungstyp T,modul_studiengang MS, veranstaltung_gruppe VG WHERE MS.studiengang_id=%d AND MS.modul_id = M.id AND M.id=V.modul_id AND V.typ_id=T.id AND V.id=VG.id_veranstaltung', $akt_studiengang);
	$countStmnt = $pdo->prepare($countsql);
	
	// Anzahl der Einträge zählen
	$countStmnt->execute();
	$numRows = $countStmnt->fetch()->count;
	$countStmnt->closeCursor();
	$numPages = ceil($numRows / $rowspp);
	
	// Welche Seite ist angefordert
	if(isset($_POST['seite_weiter'])){
		$p++;
	}
	if(isset($_POST['seite_zurueck'])){
		$p--;
	}
	if(isset($_POST['seite_anfang'])){
		$p = 1;
	}
	if(isset($_POST['seite_ende'])){
		$p = $numPages;
	}
	
	//query was die Veranstaltungen des aktuell gewählten Studiengangs holt
	$sql = sprintf('SELECT M.id AS modul_id, M.bezeichnung AS titel, V.id as ver_id, T.bezeichnung AS typ, VG.gid AS gruppe FROM modul M, veranstaltung V, veranstaltungstyp T,modul_studiengang MS, veranstaltung_gruppe VG WHERE MS.studiengang_id=%d AND MS.modul_id = M.id AND M.id=V.modul_id AND V.typ_id=T.id AND V.id=VG.id_veranstaltung', $akt_studiengang);
	$sql .= sprintf(' LIMIT %d OFFSET %d', $rowspp, ($p - 1) * $rowspp);
	$selectStmnt = $pdo->prepare($sql);
	
	
	// Tabelle mit Veranstaltung zum Waehlen/Abwaehlen
	$Page->append('<h2>Veranstaltungen wählen/abwählen</h2>');
	//Veranstaltung From
	$Page->append('<form method="POST" name="veranstaltung_form" action="%s">', $_SERVER['PHP_SELF']);
    $Page->append('<p><input type="submit" name="wahlabsenden" value="Belegung absenden"></p>');
    //Studiengang auswählen
	$Page->append('<b>Studiengang auswählen</b>');
	$Page->append('<p>');
    $Page->append('<select name="studiengang" size="1">');
    foreach($studiengaenge as $s){
    	if($s->id!=$akt_studiengang){
    		$Page->append('<option value="'.$s->id.'">'.$s->bezeichnung.'</option>');
    	}else{
    		$Page->append('<option value="'.$s->id.'" selected="selected">'.$s->bezeichnung.'</option>');
    	}
    }
    $Page->append('</select>');
    $Page->append('<input type="submit" name="studiengang_waehlen" value="Studiengang wählen">');
    $Page->append('</p>');
	//Tabelle mit Veranstaltungen bauen
    if ($numRows > 0) {
		$Page->append('<table>
		    <thead>
		        <tr>
		        	<th>Modul-ID</th>
		            <th>Titel</th>
		            <th>Veranstaltung-ID</th>
		            <th>Veranstaltungs-Typ</th>
		            <th>Gruppe</th>
		            <th>Belegung</th>
		        </tr>
		    </thead>
		 ');	
		// Daten holen
	    $selectStmnt->execute();
	    while($vers = $selectStmnt->fetch()) {
	    	$gew = checkVeranstaltung($vers->ver_id,$vers->gruppe);
	    	$akt = FALSE;
	    	if(isset($aktuelleWahl['wahl'])){
		    	foreach ($aktuelleWahl['wahl'] as $a){
		    		if($a[0]==$vers->ver_id&&$a[1]==$vers->gruppe){
		    			$akt = TRUE;
		    		}
		    	}
	    	}
	    	if(isset($aktuelleWahl['abwahl'])){
		    	foreach ($aktuelleWahl['abwahl'] as $a){
		    		if($a[0]==$vers->ver_id&&$a[1]==$vers->gruppe){
		    			$akt = TRUE;
		    		}
		    	}
	    	}
	    	$Page->append('<tr>
		                    <td>%s</td>
		                    <td>%s</td>
		                    <td>%s</td>
		                    <td>%s</td>
		                    <td>%s</td>	                    
		                    <td><input type="checkbox" name="%s" value="%s:%s" %s> %s</td>
		                   </tr>', 
	    			array($vers->modul_id, 
	    				  $vers->titel, 
	    				  $vers->ver_id, 
	    				  $vers->typ, 
	    				  $vers->gruppe,
	    				  ($gew) ? 'abwahl[]':'wahl[]',
	    				  $vers->ver_id, 
	    				  $vers->gruppe,
	    				  ($akt) ? 'checked="checked"':'',
	    				  ($gew) ? 'Abwählen':'Wählen'));
	    }
	    
	    $selectStmnt->closeCursor();
	    $Page->append('</tbody></table>');
	} else {
		$Page->append('<p>Keine passenden Einträge gefunden.</p>');
	}
	$Page->append('<input type="hidden" name="page" value="%d">',$p);
	// Pager
    $Page->append('<tr>');
    $Page->append('<td colspan="8" class="center">');
    $Page->append('<span class="numrows">%d Einträge gefunden.</span><br />', $numRows);
    if ($p > 1) {
   		$Page->append('<input type="submit" name="seite_anfang" value="&laquo;">');
		$Page->append('<input type="submit" name="seite_zurueck" value="&lt; zurück">');
    }
    $Page->append('<span class="status">Seite %d von %d</span>', array($p, $numPages));
    if ($p < $numPages) {
    	$Page->append('<input type="submit" name="seite_weiter" value="weiter &gt;">');
       // $Page->append('<a href="?page=%d" title="Springe zur Seite %d" accesskey="n"></a>', array($p + 1, $p + 1));
        $Page->append('<input type="submit" name="seite_ende" value="&raquo;">');
       // $Page->append('<a href="?page=%d" title="Springe zur letzten Seite">&raquo;</a>', $numPages);
    }
    $Page->append('</td>');
    $Page->append('        </tr>');

	$Page->append('</tbody></table>');
	$Page->append('</form><br/>');
	}else{
		//Loginbereich anzeigen
		$Page->append('
		<h2>Login für Studenten</h2>
		<form method="POST" action="%s">
			<table>
				<tr><td>Benutzer (Matrikel-Nr):</td><td><input type="text" name="user" /></td></tr>
			   	<tr><td>Passwort (Telefonnummer):</td><td><input type="password" name="password" /></td></tr>
			</table>
			<input type="submit" name="ok" value="Login">
		</form>
		', $_SERVER['PHP_SELF']);
	}

// Fertig. Ausgabe
$Page->show();
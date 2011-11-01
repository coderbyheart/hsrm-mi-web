<?php
require_once __DIR__ . DIRECTORY_SEPARATOR . 'lib' . DIRECTORY_SEPARATOR . 'bootstrap.php';

//neue Page erzeugen
$Page = new Page_Default();
$Page->append('<h2>Detailansicht</h2>');

//matnr holen aus dem GET
$matnr='';
if (array_key_exists('matnr', $_GET)) { $matnr=$_GET['matnr']; };
if (array_key_exists('matnr', $_POST)) { $matnr=$_POST['matnr']; };
$matnr = (int)$matnr;

// prüfen ob ein edit Befehl im Post mitgeliefert wurde
$edit="";
if (array_key_exists('edit', $_POST)) { $edit=$_POST['edit']; };

// array zum vereinfachten holen der werte aus dem POST
$werte=array( "vorname", "nachname", "fachsem", "studiengang_id", "adresse1", "adresse2", "plz", "stadt", "land", "email", "tel" );




//button zum edit/save
$button="edit";

// falls man gerade editiert muss der button save anzeigen
if($edit=="edit"){
	$button="save";
}

//Prüfen ob eine matrikelnummer mitgelierfert wurde, falls ja dann...
if($matnr!=''){
	
	// holen der studenten daten
    $res = $pdo->prepare('SELECT * FROM studierende WHERE matnr=?');
    $res->bindValue(1, $matnr, PDO::PARAM_INT);
    $res->execute();
	$student=$res->fetch();
	$res->closeCursor();
    
	// falls man vorher editierte und auf save klickte dann updat der Werte
	if($edit=="save"){
		// array für editierte werte
		$editvalues=array();
		// prüfen ob vergleichsdaten in der session vorhanden sind
		if (array_key_exists('old'.$matnr, $_SESSION) ) { 
			//vergelichsdaten holen
			$old=$_SESSION['old'.$matnr]; 
			// vergelichsdaten in der session entfernen
			unset($_SESSION['old'.$matnr]);
			
			
			// editierte Werte aus dem Post holen
			foreach ($werte as $dbkey){
				if (array_key_exists($dbkey, $_POST) ) 	$editvalues[$dbkey]=htmlspecialchars($_POST[$dbkey]); 	
			}

			// vergleich der alten daten mit den aktuellen studentendaten
			$identisch= $student==$old ;
			
			// falls sie identisch waren, also niemand anders sie verändert hat dann:
			if($identisch){
				// array für query inhalt
				$updatevalues=array();
                $updatefields=array();
				//editierte adten als query inhalt zusammenbasteln
				foreach($editvalues as $updatekeys => $updatevalue){
                    $updatefields[] = $updatekeys . '= ?';
					$updatevalues[$updatekeys] = $updatevalue;
				}
                $sql = 'UPDATE studierende SET ' . join(', ', $updatefields) . ' WHERE matnr = ?';
				//query zusammenbauen
                $u = $pdo->prepare($sql);
                $i = 0;
                foreach($updatevalues as $col => $v) {
                    $i++;
                    switch($col) {
                    case 'matnr':
                    case 'fachsem':
                    case 'studiengang_id':
                    case 'plz':
                        $u->bindValue($i, $v, PDO::PARAM_INT);
                        break;
                    default:
                            $u->bindValue($i, $v, PDO::PARAM_STR);
                    }
                }
                $u->bindValue($i + 1, $matnr, PDO::PARAM_INT);

				//versuch daten zu ändern
				try {
					$u->execute();
					// holen der studenten daten nach dem update
					$res = $pdo->query(sprintf('SELECT * FROM studierende WHERE matnr=%s', $matnr));
					$student=$res->fetch();
				} catch (PDOException $e) {
					//  falls gescheitert Meldung ausgeben
					$Page->append('<p style="color: #FF0000">Ein Fehler ist aufgetreten, bitte versuchen sie es erneut und überprüfen sie ihre eingaben.</p>');
				}


			}// falls die vergleichdaten nicht identisch sind dann Fehlermeldung
			else  $Page->append('<p style="color: #FF0000"> Die Daten wurden von einem anderen User geändert, versuchen sie es nochmal.</p>');	
			
		}// falls keine vergleichsdaten vorhanden sind dann Fehlermeldung:
	    else  $Page->append('<p style="color: #FF0000">Keine Vergleichsdaten! Bitte ändern sie nur Daten über den edit-Button und save-Button.</p>');	
	}

	//details des users
	
	// prüfen ob es einen studenten mit der bekommenen matnr gibt
	if($student!=Null){	

		// falls man gerade editiert:
		if($edit=="edit"){
			// und speichert die vergleichsdaten
			$_SESSION['old'.$matnr]=$student;
		}
		
		// studiengänge holen um sie später einzufügen
		$studiengang = $pdo->query('SELECT * FROM studiengang');
		
		// tabelle anfangen
        $Page->append('<form method="post" action="%s">', $_SERVER['PHP_SELF']);
        $Page->append('<p>');
        $Page->append('<input type="submit" name="edit" value="%s" />  <br/> ', $button);
        $Page->append('<input type="hidden" name="matnr" value="%s" />  <br/> ', $matnr);
        $Page->append('</p>');
		$Page->append('<table>');
        $Page->append('<tbody>');
		
		// daten des studenten durchlaufen
		foreach($student as $key=>$value ){	
			// falls es die matrikelnummer ist dann kein formuar	
			if($key=="matnr"){
				// matrikelnummer in die tabelle einfügen
				$Page->append('<tr><td>%s</td><td >%s </td></tr>   ', array($key,  $value ));
			}
			else
			// falls es die studiengang_id ist dann:
			if ($key=='studiengang_id'){
				// wenn man gerade beim editieren ist dann:
				if($edit=='edit'){		
					//dropdown menü mit allen möglichen studiengängen		
					$Page->append('<tr><td>Studiengang</td><td > <select name="%s">', $key);
					while($gang = $studiengang->fetch()){
						$selected="";
						//damit der aktuelle studiengang selektiert ist
						if($gang->id==$value)$selected="selected";
						$Page->append('<option '.$selected.' value="%s">%s</option>',array(  $gang->id, $gang->bezeichnung));
					}
					$Page->append('</select></td></tr>');
				
				} //falss nicht editiert wird
				else{// studiengang id mit studiengang-bezeichnung ersetzen
					$bezeichnung=$studiengang->fetchAll( PDO::FETCH_COLUMN | PDO::FETCH_GROUP ); 
					$Page->append('<tr><td>%s</td><td >%s</td></tr>  ', array($key, $bezeichnung[$value][0] ));
			
				}
			}else
				//alle anderen werte im falls editiert wird werden als input-box dargestellt
				if($edit=='edit'){					
					$Page->append('<tr><td>%s</td><td ><input type="text" name="%s" value="%s" /></td></tr>  ', array($key,$key, $value ));
				}else
					// falls nicht editiert wird nur als text anzeigen
					$Page->append('<tr><td>%s</td><td > %s</td></tr>  ', array($key,  $value ));
			
				}
		// button für edit/save
		$Page->append('</tbody></table>');
		$Page->append('</form>');

		// belegungsquery
		$belegungskey=array('v_id' ,  'dauer' , 'bezeichnung');
        $belegung = $pdo->prepare(sprintf("Select %s from
		studierende_veranstaltung_gruppe as svg  
		join veranstaltung as v on svg.v_id = v.id 
		join modul as m on v.modul_id = m.id where 
		matnr=?",implode(' , ', $belegungskey)));
        $belegung->bindValue(1, $matnr, PDO::PARAM_INT);
		// belegung holen
		$belegung->execute();
		
		
		$Page->append('<h2>Belegung</h2>');
		//belegungstabelle anfangen
		$Page->append('<table>');
		//spaltennamen
		$Page->append('<tr>');
		foreach($belegungskey as $key){
			$Page->append(sprintf('<td> %s </td>', $key));
		}
		$Page->append('</tr>');

		//spalteninhalt
		while($vorlesung = $belegung->fetch()) {
			// cellen befühlen
		    $Page->append('<tr>');
			foreach($vorlesung as $value){
				$Page->append(sprintf('<td> %s </td>', $value));
			}
			$Page->append('</tr>');
		}
		$Page->append('</table>');
	
		
	}//Fehlermeldung falls es keinen studenten mit der angebenen matrikelnummer gibt dann:
	else {
		//Fehlermeldung
		$Page->append('<p style="color: #FF0000"> Es wurde eine nicht vorhandene matnr angegeben</p>');
		// formualr für eingabe der Matrikelnummer
		$Page->append('<form method="post" action="%s">matnr: <input type="text" name="matnr" /><input type="submit" value="senden" /></form>', $_SERVER['PHP_SELF']);
		$Page->append('<p>Oder <a href="index.php">zurück zur Index-Seite</a>.</p>');
	}
} // falls keine Matrikelnummer mitgeliefert wurde dann:
else {
	// Fehlermeldung 
	$Page->append('<p style="color: #FF0000"> Es wurde keine matnr angegeben</p>');
	// formualr für eingabe der Matrikelnummer	
	$Page->append('<form method="post" action="%s">matnr: <input type="text" name="matnr" /><input type="submit" value="senden"></form>', $_SERVER['PHP_SELF']);
	$Page->append('<p>Oder <a href="index.php">zurück zur Index-Seite</a>.</p>');
}
$Page->show();
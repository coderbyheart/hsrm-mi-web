<?php

/**
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

/**
 * Listet die Studenten auf
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

require_once __DIR__ . DIRECTORY_SEPARATOR . 'lib' . DIRECTORY_SEPARATOR . 'bootstrap.php';
require_once LIB . 'studsearch.php';

// Definiert die Filter, die mittels DropDown ausgewählt werden
$selectFilters = array(
    'studiengang_id' => array(
        'sql' => 'SELECT id, bezeichnung as label FROM studiengang ORDER BY bezeichnung',
        'label' => 'Studiengang',
        'alias' => 'studiengang'
    ),
    'fachsem' => array(
        'sql' => 'SELECT fachsem as id, fachsem as label FROM studierende GROUP BY fachsem ORDER BY fachsem',
        'label' => 'Fachseminar',
    ),
);

// Definiert die Felder die durchsucht werden können
$searchableFields = array(
	# Feld => Bezeichnung
	'matnr' => array('label' => 'Matrikelnummer', 'type' => PDO::PARAM_INT),
	'vorname' => array('label' => 'Vorname'),
	'nachname' => array('label' => 'Nachname'),
	'adresse1' => array('label' => '1. Adresse'),
	'adresse2' => array('label' => '2. Adresse'),
	'plz' => array('label' => 'PLZ', 'type' => PDO::PARAM_INT),
	'stadt' => array('label' => 'Stadt'),
	'land' => array('label' => 'Land'),
	'email' => array('label' => 'E-Mail'),
	'tel' => array('label' => 'Telefon', 'type' => PDO::PARAM_INT)
);
$searchFilters = studlistingCreateSearchFilters($searchableFields);

// Einstellungen aus POST lesen oder aus Session verwenden
$settings = &studlistingGetSettings($selectFilters, $searchFilters, 'nachname');

// Definiert die Felder, die in der Tabelle dargestellt werden
$showableFields = array_merge($selectFilters, $searchableFields);
$showedFields = studlistingGetShowedFields($showableFields);

// Anzahl der Einträge pro Seite
$rowspp = 20;

// ++ Studenten ausgeben
// Query das die Daten holt
$sql = 'SELECT s.*, sg.bezeichnung as studiengang FROM studierende s LEFT JOIN studiengang sg ON sg.id = s.studiengang_id';
// Query das die Treffer zählt
$countsql = 'SELECT COUNT(s.*) as count FROM studierende s';
// Filter anwenden
$where = array();
// Einschränkung über Dropdowns
foreach($selectFilters as $filterId => $info) {
	if ($settings[$filterId]) {
	    $where[] = sprintf('s.%s = ?', $filterId, $filterId);
	}
}
// Einschränkung über Text
foreach($searchFilters as $filterId => $info) {
	if ($settings[$filterId]) {
		$conds = array();
		foreach($info['columns'] as $col) {
			# Alle Felder auf TEXT casten, damit darin gesucht werden kann
			$conds[] = sprintf('LOWER(CAST(s.%s AS TEXT)) LIKE LOWER(?)', $col);
		}
		$where[] = '(' . join(' OR ', $conds) . ')';
	}
}
if ($where) {
	$sql .= ' WHERE ' . join(' AND ', $where);
	$countsql .= ' WHERE ' . join(' AND ', $where);
}
if ($settings['order']) {
    $col = isset($showableFields[$settings['order']]['alias']) ? $showableFields[$settings['order']]['alias'] : $settings['order'];
    $sql .= ' ORDER BY ' . $col . ' ' . $settings['order_dir'];
}
// Download?
$download = filter_input(INPUT_GET, 'download', FILTER_VALIDATE_BOOLEAN);
// Pager anwenden
// Welche Seite ist angefordert
$p = getPage();
if (!$download) $sql .= sprintf(' LIMIT %d OFFSET %d', $rowspp, ($p - 1) * $rowspp);

// Werte für die Suche binden
$countStmnt = $pdo->prepare($countsql);
$selectStmnt = $pdo->prepare($sql);
if ($where) {
	foreach(array($countStmnt, $selectStmnt) as $stmnt) {
		$k = 1;
		foreach($selectFilters as $filterId => $info) {
			if ($settings[$filterId]) {
				$stmnt->bindValue($k, $settings[$filterId], PDO::PARAM_INT);
				$k++;
			}
		}
		foreach($searchFilters as $filterId => $info) {
			if ($settings[$filterId]) {
				foreach($info['columns'] as $col) {
					$stmnt->bindValue($k, '%' . $settings[$filterId] . '%', PDO::PARAM_STR);
					$k++;
				}
			}
		}
	}
}

// Anzahl der Einträge zählen
$countStmnt->execute();
$numRows = $countStmnt->fetch()->count;
$countStmnt->closeCursor();

// Optional, Download des Suchergebnisses als CSV-Datei
if ($download) {
    header('Content-Type: text/csv; charset=utf8');
    header('Content-Disposition: attachment; filename=studenten.csv');
    // Write UTF-8 BOM
    $fp = fopen('php://output', 'w');
    fputs($fp, "\xEF\xBB\xBF");
    $selectStmnt->execute();
    $first = true;
    while($student = $selectStmnt->fetch(PDO::FETCH_ASSOC)) {
        if ($first) {
            $fields = array();
            foreach($showableFields as $col => $info) {
                $fields[$col] = $info['label'];
            }
            $header = array();
            foreach($student as $col => $value) {
                if (!isset($fields[$col])) continue;
                $header[] = $fields[$col];
            }
            fputcsv($fp, $header);
            $first = false;
        }
        $row = array();
        foreach($student as $col => $value) {
            if (!isset($showableFields[$col])) continue;
            $row[] = isset($showableFields[$col]['alias']) ? $student[$showableFields[$col]['alias']] : $value;
        }
        fputcsv($fp, $row);
    }
    $selectStmnt->closeCursor();
    // Fertig
    exit;
}

// Ausgabe vorbereiten
$Page = new Page_Default();
$Page->append('<h2>Liste der Studenten</h2>');

// Tabelle bauen
if ($numRows > 0) {
    $Page->append('<table>
        <thead>
            <tr>');
    $Page->append('<th>#</th>');
    foreach($showedFields as $col => $info) {
        $Page->append('<th>');
        $Page->append('<a href="?order=%s" title="Tabelle nach %s sortieren">%s</a>', array($col, $info['label'], $info['label']));
        if ($col == $settings['order']) {
            $Page->append($settings['order_dir'] == 'ASC' ? '↓' : '↑');
        }
        $Page->append('</th>');
    }
    $Page->append('<th><a href="%s?download=1" title="Tabelle als CSV herunterladen">Download</a></th>', $_SERVER['PHP_SELF']);
    $Page->append('        </tr>
        </thead>
        <tfoot>
            <tr>');

    // Pager
    $numPages = ceil($numRows / $rowspp);
    $Page->append('<td colspan="%d" class="center">', count($showedFields) + 2);
    $Page->append('<span class="numrows">%d Einträge gefunden.</span><br />', $numRows);
    if ($p > 1) {
        $Page->append('<a href="?page=1" title="Springe zur ersten Seite">&laquo;</a>');
        $Page->append('<a href="?page=%d" title="Springe zur Seite %d" accesskey="b">&lt; zurück</a>', array($p - 1, $p - 1));
    }
    $Page->append('<span class="status">Seite %d von %d</span>', array($p, $numPages));
    if ($p < $numPages) {
        $Page->append('<a href="?page=%d" title="Springe zur Seite %d" accesskey="n">weiter &gt;</a>', array($p + 1, $p + 1));
        $Page->append('<a href="?page=%d" title="Springe zur letzten Seite">&raquo;</a>', $numPages);
    }

    $Page->append('</td>');
    $Page->append('        </tr>
        </tfoot>
        <tbody>');
    // Daten holen
    $selectStmnt->execute();
    $i = $rowspp * ($p - 1) + 1;
    while($student = $selectStmnt->fetch()) {
        $Page->append('<tr>');
        $Page->append('<td>%d.</td>', $i++);
        foreach($showedFields as $col => $info) {
            $Page->append('<td>%s</td>', isset($info['alias']) ? $student->{$info['alias']} : $student->$col);
        }
        $Page->append('<td><a href="DetailAnsicht.php?matnr=%d" title="Datensatz bearbeiten">bearbeiten</a></td>', $student->matnr);
        $Page->append('</tr>');
    }
    $selectStmnt->closeCursor();
    $Page->append('</tbody></table>');
} else {
    $Page->append('<p>Keine passenden Einträge gefunden.</p>');
}

// Forumular
$Page->append('<form method="post" action="%s" class="studsearch">', $_SERVER['PHP_SELF']);
// Suche
$Page->append('    <fieldset>');
$Page->append('        <legend>Studenten filtern</legend>');
// Input-Elemente für Select-Filter bauen
foreach($selectFilters as $filterId => $info) {
    $result = $pdo->query($info['sql']);
    $Page->append('        <p><label>%s: <select name="%s">', array($info['label'], $filterId));
    $Page->append('        <option value="" %s>alle</option>', empty($settings[$filterId]) ? 'selected' : '');
    while($row = $result->fetch()) {
        $Page->append('        <option value="%d" %s>%s</option>', array($row->id, $settings[$filterId] == $row->id ? 'selected' : '', $row->label));
    }
    $result->closeCursor();
    $Page->append('        </select></label></p>');
}
// Freitextsuche
foreach($searchFilters as $filterId => $info) {
    $Page->append('<p><label>%s: <input name="%s" value="%s" class="%s" /></label></p>', array($info['label'], $filterId, $settings[$filterId], $info['type'] === PDO::PARAM_INT ? 'number' : 'text'));
}
// Abschluss des Forms
$Page->append('        <p><button type="submit" name="submit" value="1">filtern</button><button type="submit" name="reset" value="1">zurücksetzen</button></p>');
$Page->append('    </fieldset>');
$Page->append('</form>');

// Welche Felder sollen im Ergebnis angezeigt werden
$Page->append('<form method="post" action="%s" class="studsearch">', $_SERVER['PHP_SELF']);
$Page->append('    <fieldset>');
$Page->append('        <legend>Angezeigte Felder</legend>');
$Page->append('        <p><select name="showfields[]" multiple size="%d">', count($showableFields) + 2);
$Page->append('        <optgroup label="Angezeigte Felder">');
foreach($showedFields as $fieldId => $info) {
    $Page->append('            <option value="%s" selected="selected">%s</option>', array($fieldId, $info['label']));
}
$Page->append('        </optgroup>');
if (count($showedFields) < count($showableFields)) {
    $Page->append('        <optgroup label="Verborgene Felder">');
    foreach($showableFields as $fieldId => $info) {
        if (isset($showedFields[$fieldId])) continue;
        $Page->append('            <option value="%s">%s</option>', array($fieldId, $info['label']));
    }
    $Page->append('        </optgroup>');
}
$Page->append('        </select></p>');
$Page->append('        <p class="clear"><button type="submit" name="fieldsubmit" value="1">setzen</button><button type="submit" name="fieldreset" value="1">zurücksetzen</button></p>');
$Page->append('    </fieldset>');
$Page->append('</form>');


// Fertig. Ausgabe
$Page->show();

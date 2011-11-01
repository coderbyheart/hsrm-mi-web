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

// Ausgabe vorbereiten
$Page = new Page_Default();
$Page->setTitle('Toller Titel');
$Page->append('<h2>Liste der Studenten</h2>');
$Page->append('<p>Einfacher Absatz</p>');
$Page->append('<p>Einfacher Absatz: %d</p>', 2);
$Page->append('<p>Einfacher Absatz: %s %s %s</p>', array('Wert1', 'Wert2', '<strong>'));

// Fertig. Ausgabe
$Page->show();
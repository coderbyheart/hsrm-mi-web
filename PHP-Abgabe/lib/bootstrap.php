<?php

/**
 * Konfiguriert die Anwendung
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

/**
 * @global string Shorthand für DIRECTORY_SEPARATOR
 */
define('DIR_SEP', DIRECTORY_SEPARATOR);

/**
 * @global string Konstante für das Home-Verzeichnis
 */
define('HOME', __DIR__ . DIR_SEP . '..' . DIR_SEP);

/**
 * @global string Konstante für das lib-Verzeichnis
 */
define('LIB', __DIR__ . DIR_SEP);

// Autoloader initialisieren
require_once LIB . 'Autoloader.php';
spl_autoload_register(array('Autoloader', 'autoload'));

$config = Config::getInstance();
// Fehlermeldungen  anzeigen
if ($config->getDebug()) {
    error_reporting(-1);
    ini_set('display_errors', 1);
    if (!isset($_SERVER['PWD'])) ini_set('html_errors', 1);
}

// Datenbankverbindung aufbauen
$dsn = sprintf('pgsql:host=%s;port=%d;dbname=%s;user=%s;password=%s', $config->getDBHost(), $config->getDBPort(), $config->getDatabase(), $config->getDBUser(), $config->getDBPassword());
try {
    $pdo = new PDO($dsn);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_OBJ);
} catch(PDOException $E) {
    $Page = new Page_Error($E->getMessage());
    $Page->show();
    exit();
}

session_start();

$killsession = filter_input(INPUT_GET, 'killsession', FILTER_VALIDATE_BOOLEAN);
if ($killsession) {
    $_SESSION = array();
}

require_once LIB . 'functions.php';

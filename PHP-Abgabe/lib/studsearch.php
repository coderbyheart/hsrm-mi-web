<?php

/**
 * Funktionen für die Studentensuche
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

/**
 * @global string Session Prefix
 */
define('STUDLISTING', 'studlisting');

/**
 * Erzeugt ein Array mit Suchfeldern aus einer Definition von Feldern
 *
  * @param array $searchableFields Definiert durchsuchbare Felder
 * @return array
 */
function studlistingCreateSearchFilters(array &$searchableFields)
{
    // Beide zusammen bauen
    $searchFilters = array(
        # Durchsucht alle Felder
        'fulltext' => array(
            'label' => 'Freitextsuche',
            'columns' => array_keys($searchableFields),
        'type' => PDO::PARAM_STR
        )
    );
    foreach($searchableFields as $col => $info) {
        $searchFilters[$col] = array(
            'label' => $info['label'],
            'columns' => array($col),
            'type' => isset($info['type']) ? $info['type'] : PDO::PARAM_STR,
        );
    }
    return $searchFilters;
}

/**
 * Erzeugt ein Array mit Feldern, die in der Tabelle angezeigt werden sollen
 *
 * @param array Array mit allen anzeigbaren Feldern
 * @return array Array mit Feldern die tatsächlich angezeigt werden sollen
 */
function studlistingGetShowedFields(array $showableFields)
{
    $return = array();
    foreach($showableFields as $col => $info) {
        if (!in_array($col, $_SESSION[STUDLISTING]['showfields'])) continue;
        $return[$col] = $info;
    }
    return $return;
}

/**
 * Erzeugt ein Array mit Einstellungen für das Listing
 * Liest Einstellungen aus POST und verwendet alternativ Werte aus der Session
 *
 * @param array Liste mit DropDown-Filtern
 * @param array Liste mit Freitext-Filtern
 * @param string Standard-Sortierfeld
 * @return array Referenz auf das Array mit den Einstellungen
 */
function &studlistingGetSettings(array &$selectFilters, array &$searchFilters, $defaultOrder)
{
    if (!isset($_SESSION[STUDLISTING])) {
        $default = array();
        $default['order'] = isset($selectFilters[$defaultOrder]) || isset($selectFilters[$defaultOrder]) ? $defaultOrder : false;
        $_SESSION[STUDLISTING] = $default;
        studlistingResetSearch($selectFilters, $searchFilters);
        studlistingResetFields($selectFilters, $searchFilters);
    }

    $order = filter_input(INPUT_GET, 'order', FILTER_SANITIZE_STRING, FILTER_FLAG_STRIP_HIGH | FILTER_FLAG_STRIP_LOW);
    $dirs = array('ASC', 'DESC');

    if ($order && (isset($selectFilters[$order]) || isset($searchFilters[$order]))) {
        if ($_SESSION[STUDLISTING]['order'] == $order) {
            // Sortierung umdrehen
            $currentDir = $_SESSION[STUDLISTING]['order_dir'];
            $dir = array_filter($dirs, function($el) use($currentDir) { return $el !== $currentDir; });
            $_SESSION[STUDLISTING]['order_dir'] = array_shift($dir);
        } else {
            $_SESSION[STUDLISTING]['order'] = $order;
            $_SESSION[STUDLISTING]['order_dir'] = 'ASC';
        }
    }

    studlistingHandleSearchSubmit($selectFilters, $searchFilters);
    studlistingHandleFieldsSubmit($selectFilters, $searchFilters);

    return $_SESSION[STUDLISTING];
}

/**
 * Resettet die Sucheinstellungen
 *
 * @param array Liste mit DropDown-Filtern
 * @param array Liste mit Freitext-Filtern
 */
function studlistingResetSearch(array &$selectFilters, array &$searchFilters)
{
    foreach($selectFilters as $filterId => $info) {
            $_SESSION[STUDLISTING][$filterId] = null;
    }
    foreach($searchFilters as $filterId => $info) {
        $_SESSION[STUDLISTING][$filterId] = null;
    }
}

/**
 * Kümmert sich um das Such-Formular
 *
 * @param array Liste mit DropDown-Filtern
 * @param array Liste mit Freitext-Filtern
 */
function studlistingHandleSearchSubmit(array &$selectFilters, array &$searchFilters)
{
    $submit = filter_input(INPUT_POST, 'submit', FILTER_VALIDATE_BOOLEAN);
    $reset = filter_input(INPUT_POST, 'reset', FILTER_VALIDATE_BOOLEAN);
    if ($submit) {
        // Werte für die Select-Filter auslesen
        foreach($selectFilters as $filterId => $info) {
            $value = filter_input(INPUT_POST, $filterId,  FILTER_VALIDATE_INT, array('options' => (array('min_range' => 1))));
            $_SESSION[STUDLISTING][$filterId] = $value ? $value : null;
        }
        // Werte für die Text-FIlter auslesen
        foreach($searchFilters as $filterId => $info) {
            if ($info['type'] === PDO::PARAM_INT) {
                    $value = filter_input(INPUT_POST, $filterId,  FILTER_VALIDATE_INT);
            } else {
                $value = filter_input(INPUT_POST, $filterId, FILTER_SANITIZE_STRING, FILTER_FLAG_STRIP_LOW);
            }
            $_SESSION[STUDLISTING][$filterId] = $value ? $value : null;
        }
    }
    if($reset) {
        // Wert zurücksetzen
        studlistingResetSearch($selectFilters, $searchFilters);
    }
}

/**
 * Kümmert sich um das Formular mit der Feld-Auswahl
 *
 * @param array Liste mit DropDown-Filtern
 * @param array Liste mit Freitext-Filtern
 */
function studlistingHandleFieldsSubmit(array &$selectFilters, array &$searchFilters)
{
    $submit = filter_input(INPUT_POST, 'fieldsubmit', FILTER_VALIDATE_BOOLEAN);
    $reset = filter_input(INPUT_POST, 'fieldreset', FILTER_VALIDATE_BOOLEAN);
    if ($submit) {
        $fields = filter_input(INPUT_POST, 'showfields', FILTER_SANITIZE_STRING, FILTER_FLAG_STRIP_HIGH | FILTER_FLAG_STRIP_LOW | FILTER_FORCE_ARRAY);
        if ($fields) {
            $_SESSION[STUDLISTING]['showfields'] = array();
            foreach($fields as $field) {
                if (isset($selectFilters[$field]) || isset($searchFilters[$field])) {
                    $_SESSION[STUDLISTING]['showfields'][] = $field;
                }
            }
        }
    }
    if ($reset) {
        // Felder zurücksetzen
        studlistingResetFields($selectFilters, $searchFilters);
    }
}

/**
 * Resettet die Feldeinstellungen
 *
 * @param array Liste mit DropDown-Filtern
 * @param array Liste mit Freitext-Filtern
 */
function studlistingResetFields(array &$selectFilters, array &$searchFilters)
{
    $_SESSION[STUDLISTING]['showfields'] = array_merge(array_keys($selectFilters), array_keys($searchFilters));
}

<?php

/**
 * Enthält gemeinsam verwendete Funktionen
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

/**
 * List die angeforderte Seite aus
 *
 * @return int
 */
function getPage()
{
    $p = filter_input(INPUT_GET, 'page', FILTER_VALIDATE_INT, array('options' => (array('min_range' => 1))));
    if (!$p) $p = 1;
    return $p;
}
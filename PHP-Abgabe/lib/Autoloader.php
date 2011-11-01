<?php

/**
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */


/**
 * Autoloader für die Anwendung
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */
class Autoloader
{
    /**
     * Implementiert einen Autoloader
     *
     * @static
     * @param string $class
     * @return void
     */
    public static function autoload($class)
    {
        require_once __DIR__ . DIRECTORY_SEPARATOR . str_replace('_', DIRECTORY_SEPARATOR, $class) . '.php';
    }
}

<?php

/**
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

/**
 * Zeigt eine Fehlermeldung an
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */
class Page_Error extends Page_Default
{
    /**
     * Erzeugt eine neue Fehlerseite mit der Fehlermeldung $message
     *
     * @param  $message
     */
    public function __construct($message)
    {
        $this->setTitle('Oops! Ein Fehler ist aufgetreten.');
        $this->append('<p>%s</p>', $message);
    }

    /**
     * Zeigt die Seite an, gibt dabei eine HTTP-Fehlercode aus
     */
    public function show()
    {
        header('HTTP/1.0 500 Internal Server Error');
        parent::show();
    }
}

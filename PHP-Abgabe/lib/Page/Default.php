<?php

/**
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

/**
 * Einfache implementierung einer Template-Engine
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */
class Page_Default
{
    /**
     * @var string
     */
    private $title = 'Webanwendungen Gruppe 05';

    /**
     * @var string[]
     */
    private $body = array();

    /**
     * Gibt die Seite aus
     *
     * @return void
     */
    public function show()
    {
        header('Content-Type: text/html; charset=utf-8');
        echo $this->getHeader();
        echo $this->getBody();
        echo $this->getFooter();
    }

    /**
     * Gibt den Header der Seite zurück
     *
     * @return string
     */
    protected function getHeader()
    {
        $title = htmlspecialchars($this->getTitle());
        return <<<END
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>$title</title>
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssreset/reset-min.css" />
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssbase/base-min.css" />
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts-min.css" />
		<link rel="stylesheet" type="text/css" href="styles.css" />
	</head>
	<body>
		<h1>$title</h1>
END;
    }

    /**
     * Gibt den Footer der Seite zurück
     */
    protected function getFooter()
    {
        return <<<END
	</body>
</html>
END;
    }

    /**
     * @param string $title
     * @return void
     */
    public function setTitle($title)
    {
        $this->title = $title;
    }

    /**
     * @return string
     */
    public function getTitle()
    {
        return $this->title;
    }

    /**
     * Setzt den Inhalt des Bodys
     * @param string $body
     * @return void
     */
    public function setBody($body)
    {
        $this->body = array($body);
    }

    /**
     * Gibt den Inhalt des Bodys zurück
     *
     * @return string
     */
    public function getBody()
    {
        return join("\n", $this->body);
    }

    /**
     * Hängt etwas an den Body an
     *
     * @param string
     */
    public function append($str, $replace = null)
    {
        if (is_array($replace)) {
            $str = call_user_func_array('sprintf', array_merge(array($str), array_map(array($this, 'escape'), $replace)));
        } elseif ($replace !== null) {
            $str = sprintf($str, $this->escape($replace));
        }
        $this->body[] = $str;
    }

    /**
     * Convertiert einen Wert zur Ausgabe in HTML
     *
     * @param string
     * @return string
     */
    protected function escape($str)
    {
        return htmlspecialchars($str);
    }

    /**
     * Hilfsmethode, die einen Pager für ein Listing erzeugt
     *
     * @param Listing_Base
     * @return string
     */
    public function getPager(Listing_Base $listing)
    {
        $str = '<span class="pager">';
        // Zurück
        if ($listing->getPage() > 2) {
            $str .= '<a href="?page=1" title="Springe zur ersten Seite">&laquo;</a>';
        }
        if ($listing->getPage() > 1) {
            $prev = $listing->getPage() - 1;
            $str .= sprintf('<a href="?page=%d" title="Springe zu Seite %d">&lt;</a>', $prev, $prev);
        }
        // Aktuelle Postion
        $str .= sprintf('<span class="position">Seite %d von %d</span>', $listing->getPage(), $listing->getNumPages());
        // Vorwärts
        if ($listing->getPage() < $listing->getNumPages()) {
            $next = $listing->getPage() + 1;
            $str .= sprintf('<a href="?page=%d" title="Springe zu Seite %d">&gt;</a>', $next, $next);
        }
        if ($listing->getPage() < $listing->getNumPages() - 1) {
            $str .= sprintf('<a href="?page=%d" title="Springe zur letzten Seite">&raquo;</a>', $listing->getNumPages());
        }
        $str .= '</span>';
        return $str;
    }
}

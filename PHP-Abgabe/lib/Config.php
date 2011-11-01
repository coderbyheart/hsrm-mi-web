<?php

/**
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */

/**
 * Stellt Anwendngseinstellungen zur Verfügung
 *
 * @author Markus Tacker <m@tacker.org>
 * @version $Id$
 */
class Config {

    /**
     * @var Config
     */
    private static $instance;

    /**
     * @var string Datenbankname
     */
    private $database = null;

    /**
     * @var string Nutzername zur Verbindung mit der Datenbank
     */
    private $dbuser = null;

    /**
     * @var string Passwort zur Verbindung mit der Datenbank
     */
    private $dbpassword = null;

    /**
     * @var string Datenbankhost
     */
    private $dbhost = 'localhost';

    /**
     * @var int Database Port
     */
    private $dbport = 5432;

    /**
     * @var Boolean Switch der Angibt, ob Debug-Informationen ausgegeben werden sollen
     */
    private $debug = false;

    /**
     * @return Config
     */
    private function __construct()
    {
        foreach(parse_ini_file(HOME . 'config.ini') as $k => $v) {
            $this->configValue($k, $v);
        }
    }

    /**
     * Ist $value nicht null, wird der Wert der Konfigurationseinstellung
     * mit dem Namen $name auf den Wert von $value gesetzt.
     *
     * Gibt den aktuellen Wert der Konfigurationseinstellung
     * mit dem Namen $name zurück
     *
     * @param string $name
     * @param string $value
     * @return string
     */
    private function configValue($name, $value = null)
    {
        if (!property_exists($this, $name)) {
            throw new ConfigException(sprintf('Unknown config value: %s', $name));
        }
        if ($value !== null) {
            $this->$name = $value;
        }
        return $this->$name;
    }

    /**
     * Getter
     *
     * Alias für {@link configValue}
     *
     * @param string Name des Wertes
     * @return mixed Wert
     */
    public function __get($key)
    {
        return $this->configValue($key);
    }

    /**
     * Setter
     *
     * Alias für {@link configValue}
     * @param string Name des Wertes
     * @param mixed Wert
     */
    public function __set($key, $value)
    {
        $this->configValue($key, $value);
    }

    /**
     * Erzeugt eine neue Datenbankverbindung, falls diese noch nicht besteht und gibt diese zurück
     *
     * @static
     * @return Config
     */
    public static function singleton()
    {
        if (self::$instance === null) {
            $class = __CLASS__;
            self::$instance = new $class();
        }
        return self::$instance;
    }

    /**
     * Gibt eine Instanz der Datenbankverbindung zrück
     *
     * @return Config
     */
    public static function getInstance()
    {
        return self::singleton();
    }

    /**
     * @param string $database
     * @return void
     */
    public function setDatabase($database)
    {
        $this->database = $database;
    }

    /**
     * @return string
     */
    public function getDatabase()
    {
        return $this->database;
    }

    /**
     * @param string $dbhost
     * @return void
     */
    public function setDBHost($dbhost)
    {
        $this->dbhost = $dbhost;
    }

    /**
     * @return string
     */
    public function getDBHost()
    {
        return $this->dbhost;
    }

    /**
     * @param string $dbpassword
     * @return void
     */
    public function setDBPassword($dbpassword)
    {
        $this->dbpassword = $dbpassword;
    }

    /**
     * @return string
     */
    public function getDBPassword()
    {
        return $this->dbpassword;
    }

    /**
     * @param string $dbuser
     * @return void
     */
    public function setDBUser($dbuser)
    {
        $this->dbuser = $dbuser;
    }

    /**
     * @return string
     */
    public function getDBUser()
    {
        return $this->dbuser;
    }

    /**
     * @param boolean $debug
     * @return void
     */
    public function setDebug($debug)
    {
        $this->debug = $debug;
    }

    /**
     * @return boolean
     */
    public function getDebug()
    {
        return $this->debug;
    }

    /**
     * @param int $dbport
     * @return void
     */
    public function setDBPort($dbport)
    {
        $this->dbport = $dbport;
    }

    /**
     * @return int
     */
    public function getDBPort()
    {
        return $this->dbport;
    }
}
<?php

/**
 * konek db dulu
 */
class DB_CONNECT {
	//konstruktor
    function __construct() {
        // konek database
        $this->connect();
    }

    // destructor
    function __destruct() {
        // putuskan koneksi
        $this->close();
    }
    
    function connect() {
        // import properti database untuk koneksi
        require_once __DIR__ . '/db_config.php';

        // konek ke db mysql
        $con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());

        // select database
        $db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());

        // return
        return $con;
    }

    function close() {
        // putus koneksi
        mysql_close();
    }

}

?>

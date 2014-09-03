<?php

/*
 * kode untuk tampilak semua produk, pada halaman home
 */

$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// ckonekin ke db
$db = new DB_CONNECT();

//  get by pendaftaran
$result = mysql_query("SELECT *FROM pendaftaran") or die(mysql_error());

// cek
if (mysql_num_rows($result) > 0) {
    // looping hasil
    // pendaftaran node
    $response["pendaftaran"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        $pendaftaran = array();
        $pendaftaran["pid"] = $row["pid"];
        $pendaftaran["name"] = $row["name"];
        $pendaftaran["email"] = $row["email"];
        $pendaftaran["description"] = $row["description"];
        $pendaftaran["created_at"] = $row["created_at"];
        $pendaftaran["updated_at"] = $row["updated_at"];



        // masukan pendaftaran pada $response
        array_push($response["pendaftaran"], $pendaftaran);
    }
    // sukses
    $response["success"] = 1;

    // echo JSON response
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "Tidak ada data yang ditemukan";

    echo json_encode($response);
}
?>
